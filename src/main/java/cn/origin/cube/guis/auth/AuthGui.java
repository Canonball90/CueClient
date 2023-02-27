package cn.origin.cube.guis.auth;

import cn.origin.cube.Cube;
import cn.origin.cube.core.security.keyauth.KeyAuthApp;
import cn.origin.cube.guis.mainmenu.MainMenu;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class AuthGui extends GuiScreen {
    private GuiTextField keyField;
    private String key = "";
    private int statusTime;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect1(0, 0, width, height, Color.ORANGE.getRGB());

        drawCenteredStringWithShadow("Auth with license key", width / 2, height / 4 + 6, -1);

        keyField.drawTextBox();

        drawCenteredStringWithShadow("If you have access but havent key, you can dm CanonBalls90#0001 for get help", width / 2, 10, -1);
        drawCenteredStringWithShadow("(C) All rights reserved", width / 2, height - 14, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredStringWithShadow(String text, double x, double y, int color) {
        fontRenderer.drawStringWithShadow(text,  (int) x - fontRenderer.getStringWidth(text) / 2, (int) y, color);
    }

    @Override
    public void updateScreen() {
        if (statusTime > 0) statusTime--;
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char chr, int keyCode) {
        keyField.textboxKeyTyped(chr, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        try {super.mouseClicked(mouseX, mouseY, button);} catch (IOException e) {e.printStackTrace();}
        keyField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void initGui() {
        super.initGui();
        KeyAuthApp.keyAuth.init();
        Keyboard.enableRepeatEvents(true);
        keyField = new GuiTextField(2, Minecraft.getMinecraft().fontRenderer, width / 2 - 70, height / 4 + 50, 140, 22);
        if(key != null && !key.isEmpty()) keyField.setText(key);
        buttonList.add(new GuiButton(0, width - 25, 5, 20, 20, "X"));
        buttonList.add(new GuiButton(1, width / 2 - 50, height / 4 + 100, 100, 21, "Login"));

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.shutdown();
                break;
            case 1:
                if(KeyAuthApp.keyAuth.license(keyField.getText())) {
                    System.out.println("success");
                    Cube.isOpenAuthGui = false;
                    mc.displayGuiScreen(new MainMenu());
                }
                statusTime = 50;
                break;
        }
    }
}
