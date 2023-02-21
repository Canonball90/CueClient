package cn.origin.cube.guis.alt.component;

import cn.origin.cube.Cube;
import cn.origin.cube.guis.alt.Alt;
import cn.origin.cube.guis.alt.screen.GuiAltManager;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class AltSlot extends GuiSlot {

    private GuiAltManager screen;
    private int selected;

    public AltSlot(GuiAltManager screen, Minecraft mcIn, int width, int height, int p_i1052_4_, int p_i1052_5_, int p_i1052_6_) {
        super(mcIn, width, height, p_i1052_4_, p_i1052_5_, p_i1052_6_);

        this.screen = screen;
    }

    public int size() {
        return getSize();
    }

    @Override
    protected int getSize() {
        return screen.getAccounts().size();
    }

    @Override
    public int getSlotHeight() {
        return super.getSlotHeight();
    }

    @Override
    protected void elementClicked(int slot, boolean var2, int var3, int var4) {
        this.selected = slot;

        if (var2) {
            screen.login(getAlt());
        }
    }

    @Override
    protected boolean isSelected(int slot) {
        return selected == slot;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int slot) {
        this.selected = slot;
    }

    public Alt getAlt() {
        return getAlt(getSelected());
    }

    @Override
    protected void drawBackground() {
        Render2DUtil.drawRect1(0, 0, width, height, 0xFF000000);
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {
        Render2DUtil.drawRect1(0, 0, width, height, 0xFF191919);
    }

    @Override
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
        Render2DUtil.drawRect1(left, startY, right, endY, 0xFF101010);
    }

    @Override
    protected void drawSlot(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
        Alt alt = getAlt(p_192637_1_);

        if (alt != null) {
            int x = p_192637_2_;
            int y = p_192637_3_;
            int width = this.getListWidth() - 4;
            int height = slotHeight - 4;

            Render2DUtil.drawOutlineRect(x, y, x + width, y + height, 2f, new Color(0xFF000000));
            Render2DUtil.drawVGradientRect(x, y, x + width, y + height, 0xFF101010, 0xFF191919);

            Cube.fontManager.CustomFont.drawString(alt.getUsername() != null ? alt.getUsername() : alt.getEmail(), p_192637_2_ + this.getListWidth() - 6 - Cube.fontManager.CustomFont.getStringWidth(alt.getUsername() != null ? alt.getUsername() : alt.getEmail()), p_192637_3_ + ((slotHeight - 4) / 2f), 0xFFFFFFFF, true);
        }
    }

    public Alt getAlt(int slot) {
        int count = 0;

        for (Alt alt : screen.getAccounts()) {
            if (count == slot) {
                return alt;
            }
            count++;
        }

        return null;
    }

    public void setBounds(int width, int height, int top, int bottom, int right) {
        this.width = width;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
    }

}
