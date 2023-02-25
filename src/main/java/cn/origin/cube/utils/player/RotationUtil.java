package cn.origin.cube.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + (double) RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
    }
    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = RotationUtil.getLegitRotations(vec);
        RotationUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float) MathHelper.normalizeAngle((int) rotations[1], 360) : rotations[1], RotationUtil.mc.player.onGround));
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - RotationUtil.mc.player.rotationYaw), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - RotationUtil.mc.player.rotationPitch)};
    }

    public static double normalizeAngle(double angle) {
        angle %= 360.0;

        if (angle >= 180.0) angle -= 360.0;
        if (angle < -180.0) angle += 360.0;

        return angle;
    }

    public static float[] getRotationToPos(BlockPos pos) {
        double lengthXZ = Math.hypot(pos.getX(), pos.getZ());
        double yaw = normalizeAngle(Math.toDegrees(Math.atan2(pos.getZ(), pos.getX())) - 90.0);
        double pitch = normalizeAngle(Math.toDegrees(-Math.atan2(pos.getY(), lengthXZ)));
        return new float[] {(float) yaw, (float) pitch};
    }

    public static int getDirection4D() {
        return MathHelper.floor(RotationUtil.mc.player.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
    }


    public static String getDirection4D(final boolean b) {
        final int direction4D = getDirection4D();
        if (direction4D == 0) {
            return "South (+Z)";
        }
        if (direction4D == 1) {
            return "West (-X)";
        }
        if (direction4D == 2) {
            return String.valueOf(new StringBuilder().append(b ? "§c" : "").append("North (-Z)"));
        }
        if (direction4D == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }


}
