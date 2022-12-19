package cn.origin.cube.guis.statistics;

import cn.origin.cube.Cube;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.Objects;

public class StatScreen extends GuiScreen {

    public StatScreen() {
        super();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //box
        Render2DUtil.drawBorderedRect(200, 200, 500, 400, 2, new Color(50,50,50,150).getRGB(), new Color(0,0,0,255).getRGB());
        Render2DUtil.drawRect1(200, 200, 500, 400, new Color(0,0,0,170).getRGB());
        StatUtils.drawHead(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getLocationSkin(), 210, 210);
        //text
        Cube.fontManager.CustomFont.drawString(mc.player.getName().toString(), 250, 210, new Color(255, 255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Kills: " + StatUtils.getPlayerKills(), 360, 383, new Color(255, 255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Deaths: " + StatUtils.getPlayerDeaths(), 410, 383, new Color(255, 255, 255, 255).getRGB());
        //text up
        Cube.fontManager.CustomFont.drawString("K/D: " + StatUtils.getPlayerKD(), 210, 250, new Color(255, 255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Health: " + mc.player.getHealth(), 210, 260, new Color(255, 255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Cue.Client", 210, 390, new Color(255, 255, 255, 255).getRGB());
        //bars
        Render2DUtil.drawRect1(410, 375 - (StatUtils.getPlayerDeaths() * 4), 450, 380,new Color(253, 0, 0,255).getRGB());
        Render2DUtil.drawRect1(360, 375 - (StatUtils.getPlayerKills() * 4), 400, 380,new Color(55, 253, 0,255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
