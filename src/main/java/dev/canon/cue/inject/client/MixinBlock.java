package dev.canon.cue.inject.client;

import dev.canon.cue.module.modules.visual.XRay;
import dev.canon.cue.module.modules.visual.XRay;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void renderBlockPatch(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (XRay.INSTANCE.isEnabled()) {
            if (XRay.xrayBlocks.contains(blockState.getBlock())) {
                callbackInfoReturnable.setReturnValue(true);
            } else {
                callbackInfoReturnable.setReturnValue(false);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(method = "isFullCube", at = @At("HEAD"), cancellable = true)
    public void isFullCubePatch(IBlockState state, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (XRay.INSTANCE.isEnabled()) {
            callbackInfoReturnable.setReturnValue(XRay.xrayBlocks.contains(state.getBlock()));
        }
    }

}
