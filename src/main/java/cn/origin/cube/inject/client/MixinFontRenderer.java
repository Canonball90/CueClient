package cn.origin.cube.inject.client;

import cn.origin.cube.Cube;
import cn.origin.cube.module.modules.client.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    final Minecraft mc = Minecraft.getMinecraft();

    @Inject(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "HEAD"), cancellable = true)
    public void renderString(String text, float x, float y, int color, boolean shadow, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        if (mc.player == null || mc.world == null) return;

        if (ClickGui.INSTANCE.override.getValue()) {
            callbackInfoReturnable.setReturnValue(getShadowString(text, x, y, color));
        }
    }

    private int getShadowString(String text, float x, float y, int colour) {
        Cube.fontManager.CustomFont.drawString(StringUtils.stripControlCodes(text), x + 0.5f, y + 0.5f, 0x000000);
        return Cube.fontManager.CustomFont.drawString(text, x, y, colour);
    }

}
