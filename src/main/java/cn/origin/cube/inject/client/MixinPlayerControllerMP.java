package cn.origin.cube.inject.client;

import cn.origin.cube.core.events.player.ProcessRightClickBlockEvent;
import cn.origin.cube.core.events.world.PlayerDamageBlockEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    private void onBlockDamage(BlockPos p_180512_1_, EnumFacing p_180512_2_, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerDamageBlockEvent(p_180512_1_, p_180512_2_))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method={"processRightClickBlock"}, at={@At(value="HEAD")}, cancellable=true)
    public void processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
        ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(pos, hand, Minecraft.getMinecraft().player.getHeldItem(hand));
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            cir.cancel();
        }
    }

//    @Inject(method={"processRightClick"}, at={@At(value="HEAD")}, cancellable=true)
//    private void processClickHook(EntityPlayer player, World worldIn, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
//        RightClickItemEvent e = new RightClickItemEvent(player, worldIn, hand);
//        MinecraftForge.EVENT_BUS.post(e);
//        if (e.isCanceled()) {
//            cir.setReturnValue(EnumActionResult.PASS);
//        }
//    }

}
