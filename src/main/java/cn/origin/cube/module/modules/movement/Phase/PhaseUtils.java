package cn.origin.cube.module.modules.movement.Phase;

import net.minecraft.client.Minecraft;

import static cn.origin.cube.core.module.AbstractModule.mc;

public class PhaseUtils {

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
        mc.player.onGround = true;
        mc.player.motionY = 0;
        if (PhaseWalk.c > 40) {
            mc.player.posY -= 0.032;
            PhaseWalk.c = 0;
        } else PhaseWalk.c ++;
        if (mc.player.ticksExisted % 3 != 0)
            mc.player.setPosition(mc.player.posX, mc.player.posY += 1.0e-9, mc.player.posZ);
    }
}
