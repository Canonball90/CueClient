package cn.origin.cube.guis.alt.screen;

import cn.origin.cube.Cube;
import cn.origin.cube.guis.alt.Alt;
import cn.origin.cube.guis.alt.AuthenticationThread;
import cn.origin.cube.guis.alt.component.AltSlot;
import cn.origin.cube.utils.render.Render2DUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import org.apache.commons.lang3.tuple.Pair;
import java.io.IOException;
import java.util.*;

public class GuiAltManager extends GuiScreen {

    private final GuiScreen parentScreen;
    private GuiCreateAlt guiCreateAlt;
    private AltSlot alts;

    private AuthenticationThread authThread;
    private final Random random = new Random();
    private final Gson gson = new Gson();

    public GuiAltManager(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.guiCreateAlt = new GuiCreateAlt(this);

        this.alts = new AltSlot(this, mc, width, height, 25, height - 98, 36);

        this.buttonList.add(new GuiButton(0, width / 2 - 60, height - 90, 50, 20, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 + 10, height - 90, 50, 20, "Add"));
        this.buttonList.add(new GuiButton(2, width / 2 + 10, height - 60, 50, 20, "Remove"));
        this.buttonList.add(new GuiButton(3, width / 2 - 60, height - 60, 50, 20, "Random"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.enabled) {
            if (button.id == 0) {
                login(alts.getAlt());
            }
            if (button.id == 1) {
                mc.displayGuiScreen(guiCreateAlt);
            }
            if (button.id == 2) {
                Cube.altManager.remove(alts.getAlt().getEmail());
            }
            if (button.id == 3) {
                login(alts.getAlt(random.nextInt(alts.size())));
            }
        }
    }

    @Override
    public void drawDefaultBackground() {
        Render2DUtil.drawRect1(0, 0, width, height, 0xFF000000);
    }

    @Override
    public void drawBackground(int tint) {
        Render2DUtil.drawRect1(0, 0, width, height, 0xFF000000);
    }

    @Override
    public void drawWorldBackground(int tint) {
        Render2DUtil.drawRect1(0, 0, width, height, 0xFF000000);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect1(0, 0, width, height, 0xFF000000);
        alts.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(mc.fontRenderer, "\247a" + mc.getSession().getUsername(), width / 2, 5, 0xFFFFFFFF);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        alts.handleMouseInput();
    }

    public void login(Alt alt) {
        authThread = new AuthenticationThread(alt);
        new Thread(authThread, "Account Authentication Thread").start();
    }

    public List<Alt> getAccounts() {
        return Cube.altManager.getContents();
    }

}
