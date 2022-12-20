package cn.origin.cube.inject.client;

import java.io.File;
import java.util.List;

import cn.origin.cube.core.viaforge.gui.GuiProtocolSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMultiplayer.class})
public abstract class MixinGuiMultiplayer
extends GuiScreen {
    @Inject(method={"createButtons"}, at=@At(value="HEAD"), cancellable=true)
    public void Method279(CallbackInfo ci) {
        AccessorGuiScreen screen = (AccessorGuiScreen) this;
        List<GuiButton> buttonList = screen.getButtonList();
        if (!new File(Minecraft.getMinecraft().gameDir, "novia").exists()) {
            buttonList.add(new GuiProtocolSlider(1200, this.width / 2 + 4 + 76 + 76, this.height - 28, 105, 20));
            screen.setButtonList(buttonList);
        }
    }

}