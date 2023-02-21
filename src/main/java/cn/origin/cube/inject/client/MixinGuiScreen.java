package cn.origin.cube.inject.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow public void drawScreen(int mouseX, int mouseY, float partialTicks) {}

    @Shadow protected abstract void handleComponentHover(ITextComponent component, int x, int y);

    @Shadow public Minecraft mc;
    @Shadow public int width;
    @Shadow public int height;
    @Shadow
    protected List<GuiButton> buttonList;

}
