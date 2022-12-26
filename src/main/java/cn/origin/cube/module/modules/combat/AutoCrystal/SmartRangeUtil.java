package cn.origin.cube.module.modules.combat.AutoCrystal;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import static cn.origin.cube.core.module.AbstractModule.mc;

public class SmartRangeUtil {
    public static boolean isInSmartRange(BlockPos pos, Entity entity,
                                         double rangeSq, int smartTicks) {
        return isInSmartRange(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ()
                + 0.5f, entity, rangeSq, smartTicks);
    }

    public static boolean isInSmartRange(
            double crystalX, double crystalY, double crystalZ,
            Entity entity, double rangeSq, int smartTicks) {
        double x = entity.posX + entity.motionX * smartTicks;
        double y = entity.posY + entity.motionY * smartTicks;
        double z = entity.posZ + entity.motionZ * smartTicks;

        return Utils.distanceSq(crystalX, crystalY, crystalZ, x, y, z)
                < rangeSq;
    }

    public static boolean isInStrictBreakRange(
            double crystalX, double crystalY, double crystalZ, double rangeSq,
            double entityX, double entityY, double entityZ) {
        final double height = 2.0f;
        final double pY = entityY + mc.player
                .getEyeHeight();
        final double dY = crystalY;
        if (pY <= dY);
        else
            if (pY >= dY + height) {
                crystalY = dY + height;
            }
            else {
                crystalY = pY;
            }

        double x = crystalX - entityX;
        double y = crystalY - pY;
        double z = crystalZ - entityZ;

        return x * x + y * y + z * z <= rangeSq;
    }
}
