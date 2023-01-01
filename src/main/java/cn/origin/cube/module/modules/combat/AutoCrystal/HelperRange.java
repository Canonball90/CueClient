package cn.origin.cube.module.modules.combat.AutoCrystal;

import cn.origin.cube.Cube;
import cn.origin.cube.utils.client.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static cn.origin.cube.module.modules.combat.AutoCrystal.Utils.distanceSq;


public class HelperRange{
    private final AutoCrystal module;
    public Minecraft mc = Minecraft.getMinecraft();

    public HelperRange(AutoCrystal module) {
        this.module = module;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCrystalInRange(Entity crystal) {
        return isCrystalInRange(crystal.posX, crystal.posY, crystal.posZ, 0);
    }

    public boolean isCrystalInRangeOfLastPosition(Entity crystal) {
        return isCrystalInRange(crystal.posX, crystal.posY, crystal.posZ,
                Cube.positionManager.getX(),
                Cube.positionManager.getY(),
                Cube.positionManager.getZ());
    }

    public boolean isCrystalInRange(
            double crystalX, double crystalY, double crystalZ, int ticks) {
        if (module.smartBreakTrace.getValue()
                && isOutsideBreakTrace(crystalX, crystalY, crystalZ, ticks)) {
            return false;
        }

        if (module.ncpRange.getValue()) {
            Entity breaker = mc.player;
            double breakerX = breaker.posX + breaker.motionX * ticks;
            double breakerY = breaker.posY + breaker.motionY * ticks;
            double breakerZ = breaker.posZ + breaker.motionZ * ticks;
            return SmartRangeUtil.isInStrictBreakRange(
                    crystalX, crystalY, crystalZ,
                    MathUtil.square(module.breakRange.getValue()),
                    breakerX, breakerY, breakerZ);
        }

        return SmartRangeUtil.isInSmartRange(
                crystalX, crystalY, crystalZ, mc.player,
                MathUtil.square(module.breakRange.getValue()), ticks);
    }

    public boolean isCrystalInRange(
            double crystalX, double crystalY, double crystalZ,
            double breakerX, double breakerY, double breakerZ) {
        if (module.smartBreakTrace.getValue()
                && !isCrystalInBreakTrace(crystalX, crystalY, crystalZ,
                breakerX, breakerY, breakerZ)) {
            return false;
        }

        if (module.ncpRange.getValue()) {
            return SmartRangeUtil.isInStrictBreakRange(
                    crystalX, crystalY, crystalZ,
                    MathUtil.square(module.breakRange.getValue()),
                    breakerX, breakerY, breakerZ);
        }

        return distanceSq(crystalX, crystalY, crystalZ,
                breakerX, breakerY, breakerZ)
                < MathUtil.square(module.breakRange.getValue());
    }

    public boolean isCrystalOutsideNegativeRange(BlockPos pos) {
        int negativeTicks = 6;
        if (negativeTicks == 0) {
            return false;
        }

        if (module.negativeBreakTrace.getValue()
                && isOutsideBreakTrace(pos, negativeTicks)) {
            return true;
        }

        if (module.ncpRange.getValue()) {
            Entity breaker = mc.player;
            double breakerX = breaker.posX + breaker.motionX * negativeTicks;
            double breakerY = breaker.posY + breaker.motionY * negativeTicks;
            double breakerZ = breaker.posZ + breaker.motionZ * negativeTicks;
            return !SmartRangeUtil.isInStrictBreakRange(
                    pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5,
                    MathUtil.square(module.breakRange.getValue()),
                    breakerX, breakerY, breakerZ);
        }

        return !SmartRangeUtil.isInSmartRange(
                pos, mc.player,
                MathUtil.square(module.breakRange.getValue()),
                negativeTicks);
    }

    public boolean isOutsideBreakTrace(BlockPos pos, int ticks) {
        return isOutsideBreakTrace(
                pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f, ticks);
    }

    public boolean isOutsideBreakTrace(double x, double y, double z, int ticks)
    {
        Entity breaker = mc.player;
        double breakerX = breaker.posX + breaker.motionX * ticks;
        double breakerY = breaker.posY + breaker.motionY * ticks;
        double breakerZ = breaker.posZ + breaker.motionZ * ticks;
        return !isCrystalInBreakTrace(x, y, z, breakerX, breakerY, breakerZ);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCrystalInBreakTrace(
            double crystalX, double crystalY, double crystalZ,
            double breakerX, double breakerY, double breakerZ) {
        return distanceSq(crystalX, crystalY, crystalZ,
                breakerX, breakerY, breakerZ)
                < MathUtil.square(7)
                || mc.world.rayTraceBlocks(
                new Vec3d(breakerX, breakerY
                        + mc.player.getEyeHeight(), breakerZ),
                new Vec3d(crystalX, crystalY + 1.7, crystalZ),
                false,
                true,
                false) == null;
    }

}
