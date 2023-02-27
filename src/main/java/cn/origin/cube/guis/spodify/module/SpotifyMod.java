package cn.origin.cube.guis.spodify.module;

import cn.origin.cube.Cube;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.guis.spodify.SpotifyAPI;
import cn.origin.cube.guis.spodify.util.ColorUtil;
import cn.origin.cube.guis.spodify.util.ScissorUtil;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@Constant
@ModuleInfo(name = "Spodify", descriptions = "", category = Category.CLIENT)
public class SpotifyMod extends Module {
    private int posX = 50, posY = 50;

    private boolean downloadedCover;
    private int imageColor = -1;
    private ResourceLocation currentAlbumCover;

    private Track currentTrack;
    private CurrentlyPlayingContext currentPlayingContext;

    @Override
    public void onRender2D() {
        if (mc.player == null || Cube.spotifyAPI.currentTrack == null || Cube.spotifyAPI.currentPlayingContext == null) {
            return;
        }
        if (currentTrack != Cube.spotifyAPI.currentTrack || currentPlayingContext != Cube.spotifyAPI.currentPlayingContext) {
            this.currentTrack = Cube.spotifyAPI.currentTrack;
            this.currentPlayingContext = Cube.spotifyAPI.currentPlayingContext;
        }

        final int albumCoverSize = 55;
        final int playerWidth = 150;
        final int diff = currentTrack.getDurationMs() - currentPlayingContext.getProgress_ms();
        final long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
        final long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        final String trackRemaining = String.format("-%s:%s", diffMinutes < 10 ? "0" + diffMinutes : diffMinutes, diffSeconds < 10 ? "0" + diffSeconds : diffSeconds);

        try {
            Gui.drawRect(posX + albumCoverSize, posY, playerWidth, albumCoverSize, imageColor);
            ScissorUtil.scissor(posX + albumCoverSize, posY, playerWidth, albumCoverSize);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            mc.fontRenderer.drawString("§l" + currentTrack.getName(), posX + albumCoverSize + 4, posY + 6, -1);
            final StringBuilder artistsDisplay = new StringBuilder();
            for (int artistIndex = 0; artistIndex < currentTrack.getArtists().length; artistIndex++) {
                final ArtistSimplified artist = currentTrack.getArtists()[artistIndex];
                artistsDisplay.append(artist.getName()).append(artistIndex + 1 == currentTrack.getArtists().length ? '.' : ", ");
            }

            mc.fontRenderer.drawString(artistsDisplay.toString(), posX + albumCoverSize + 4, posY + 17, -1);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            mc.fontRenderer.drawString(trackRemaining, posX + playerWidth + 8, posY + albumCoverSize - 11, -1);

            final int progressBarWidth = ((playerWidth - albumCoverSize) * currentPlayingContext.getProgress_ms()) / currentTrack.getDurationMs();
            Gui.drawRect(posX + albumCoverSize + 5, posY + albumCoverSize - 9, playerWidth - albumCoverSize, 4, new Color(50, 50, 50).getRGB());
            Gui.drawRect(posX + albumCoverSize + 5, posY + albumCoverSize - 9, progressBarWidth, 4, new Color(20, 200, 10).getRGB());

            if (currentAlbumCover != null && downloadedCover) {
                mc.getTextureManager().bindTexture(currentAlbumCover);
                GlStateManager.color(1,1,1);
                Gui.drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, albumCoverSize, albumCoverSize, albumCoverSize, albumCoverSize);
            }
            if ((currentAlbumCover == null || !currentAlbumCover.getNamespace().contains(currentTrack.getAlbum().getId()))) {
                downloadedCover = false;
                final ThreadDownloadImageData albumCover = new ThreadDownloadImageData(null, currentTrack.getAlbum().getImages()[1].getUrl(), null, new IImageBuffer() {

                    @Override
                    public BufferedImage parseUserSkin(BufferedImage image) {
                        imageColor = ColorUtil.averageColor(image, image.getWidth(), image.getHeight(), 1).getRGB();
                        downloadedCover = true;
                        return image;
                    }
                    @Override
                    public void skinAvailable() {
                    }
                });
                GlStateManager.color(1, 1, 1);
                mc.getTextureManager().loadTexture(currentAlbumCover = new ResourceLocation("spotifyAlbums/" + currentTrack.getAlbum().getId()), albumCover);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onRender2D();
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            return;
        }
        Cube.spotifyAPI.init();
        super.onEnable();
    }
}
