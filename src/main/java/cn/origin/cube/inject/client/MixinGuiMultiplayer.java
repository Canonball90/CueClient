package cn.origin.cube.inject.client;

import cn.origin.cube.viaforge.ViaForge;
import cn.origin.cube.viaforge.gui.GuiProtocolSelector;
import cn.origin.cube.viaforge.protocol.ProtocolCollection;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(value = GuiMultiplayer.class, priority = 10000)
public class MixinGuiMultiplayer extends GuiScreen {
    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        buttonList.add(new GuiButton(1337, 7, 7 * 2 + 20, 98, 20, ProtocolCollection.getProtocolById(ViaForge.getInstance().getVersion()).getName()));
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 1337) mc.displayGuiScreen(new GuiProtocolSelector(this));
    }
}
