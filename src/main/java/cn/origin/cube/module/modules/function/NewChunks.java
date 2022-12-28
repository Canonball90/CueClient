package cn.origin.cube.module.modules.function;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.settings.DoubleSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.utils.render.Render3DUtil;
import cn.origin.cube.utils.render.RenderBuilder;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Set;

@ModuleInfo(name = "NewChunks", descriptions = "", category = Category.VISUAL)
public class NewChunks extends Module {

    ModeSetting<RenderBuilder.Box> render = registerSetting("Render", RenderBuilder.Box.GLOW);
    DoubleSetting height = registerSetting("Height", 0.0,0.0,10.0);
    private final Set<Vec2f> chunks = new ConcurrentSet<>();

    @Override
    public void onRender3D(Render3DEvent event) {
        chunks.forEach((chunk) -> {

            Render3DUtil.drawBox(new RenderBuilder()
                    .position(new AxisAlignedBB(chunk.x, 0, chunk.y, chunk.x + 16, height.getValue(), chunk.y + 16))
                    .color(getPrimaryAlphaColor(50))
                    .box(render.getValue())
                    .setup()
                    .line(3)
                    .depth(true)
                    .blend()
                    .texture()
            );
        });
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChunkData) {
            if (!((SPacketChunkData) event.getPacket()).isFullChunk()) {
                chunks.add(new Vec2f(((SPacketChunkData) event.getPacket()).getChunkX() * 16, ((SPacketChunkData) event.getPacket()).getChunkZ() * 16));
            }
        }
    }

    private Color getPrimaryAlphaColor(int alpha) {
        return new Color(ClickGui.getCurrentColor().getRed(), ClickGui.getCurrentColor().getGreen(), ClickGui.getCurrentColor().getBlue(), alpha);
    }
}
