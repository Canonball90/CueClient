package cn.origin.cube.module.modules.movement.Phase;

import cn.origin.cube.inject.client.INetworkManager;
import cn.origin.cube.utils.player.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static cn.origin.cube.core.module.AbstractModule.mc;

public class PhaseUtils {

    @NotNull
    @Contract("_ -> new")
    public static double[] getMotion(double speed) {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * Minecraft.getMinecraft().getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    public static boolean isPhased() {
        return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    public static boolean eChestCheck() {
        String loc = String.valueOf(mc.player.posY);
        String deciaml = loc.split("\\.")[1];
        return deciaml.equals("875");
    }

    public static void doFly(){

        double[] strafe = MovementUtils.getMoveSpeed(0.2873);
        int i = 1;

        mc.player.onGround = true;
        mc.player.motionY = 0;

        double motionX = strafe[0] * i;
        double motionZ = strafe[1] * i;

        double velY = 0.0;

        Vec3d posVec = mc.player.getPositionVector();
        Vec3d moveVec = posVec.add(motionX, velY, motionZ);

        send(moveVec);
        if (PhaseWalk.c > 40) {
            mc.player.posY -= 0.032;
            PhaseWalk.c = 0;
        } else PhaseWalk.c ++;
        if (mc.player.ticksExisted % 3 != 0)
            mc.player.setPosition(mc.player.posX, mc.player.posY += 1.0e-9, mc.player.posZ);
    }

    private static void send(Vec3d vec) {
        ((INetworkManager) mc.player.connection.getNetworkManager()).hookDispatchPacket(new CPacketPlayer.PositionRotation(vec.x, vec.y, vec.z,PhaseWalk.mc.player.rotationYaw * -5.0f, PhaseWalk.mc.player.rotationPitch * -5.0f, true), null);
    }
}
