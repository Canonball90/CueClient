package cn.origin.cube.utils.ai;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AI {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static AI instance = new AI();

    public static class HalqPos {
        BlockPos blockPos;
        float targetDamage;

        public HalqPos(BlockPos blockPos, float targetDamage) {
            this.blockPos = blockPos;
            this.targetDamage = targetDamage;
        }

        public float getTargetDamage() {
            return targetDamage;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }
    }

    public static EnumFacing getEnumFacing(boolean rayTrace, BlockPos placePosition) {
        RayTraceResult result = Minecraft.getMinecraft().world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(placePosition.getX() + 0.5, placePosition.getY() - 0.5, placePosition.getZ() + 0.5));

        if (placePosition.getY() == 255)
            return EnumFacing.DOWN;

        if (rayTrace) {
            return (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        }

        return EnumFacing.UP;
    }

}
