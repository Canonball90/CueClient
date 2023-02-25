package cn.origin.cube.core.managers;

import cn.origin.cube.core.module.AbstractModule;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RotationManager extends AbstractModule {
    private float yaw;
    private float pitch;

    public void lookAtEntity(final Entity entity) {
        final float[] calcAngle = MathUtil.calcAngle(RotationManager.mc.player.getPositionEyes(RotationManager.mc.getRenderPartialTicks()), entity.getPositionEyes(RotationManager.mc.getRenderPartialTicks()));
        this.setPlayerRotations(calcAngle[0], calcAngle[1]);
    }

    public void restoreRotations() {
        RotationManager.mc.player.rotationYaw = this.yaw;
        RotationManager.mc.player.rotationYawHead = this.yaw;
        RotationManager.mc.player.rotationPitch = this.pitch;
    }

    public void lookAtPos(final BlockPos blockPos) {
        final float[] calcAngle = MathUtil.calcAngle(RotationManager.mc.player.getPositionEyes(RotationManager.mc.getRenderPartialTicks()), new Vec3d((double)(blockPos.getX() + 0.5f), (double)(blockPos.getY() + 0.5f), (double)(blockPos.getZ() + 0.5f)));
        this.setPlayerRotations(calcAngle[0], calcAngle[1]);
    }

    public float getPitch() {
        return this.pitch;
    }

    public void updateRotations() {
        this.yaw = RotationManager.mc.player.rotationYaw;
        this.pitch = RotationManager.mc.player.rotationPitch;
    }

    public int getDirection4D() {
        return RotationUtil.getDirection4D();
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void lookAtVec3d(final double n, final double n2, final double n3) {
        this.lookAtVec3d(new Vec3d(n, n2, n3));
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public void lookAtVec3d(final Vec3d vec3d) {
        final float[] calcAngle = MathUtil.calcAngle(RotationManager.mc.player.getPositionEyes(RotationManager.mc.getRenderPartialTicks()), new Vec3d(vec3d.x, vec3d.y, vec3d.z));
        this.setPlayerRotations(calcAngle[0], calcAngle[1]);
    }

    public String getDirection4D(final boolean b) {
        return RotationUtil.getDirection4D(b);
    }

    public void setPlayerPitch(final float rotationPitch) {
        RotationManager.mc.player.rotationPitch = rotationPitch;
    }

    public void setPlayerRotations(final float n, final float rotationPitch) {
        RotationManager.mc.player.rotationYaw = n;
        RotationManager.mc.player.rotationYawHead = n;
        RotationManager.mc.player.rotationPitch = rotationPitch;
    }

    public void setPlayerYaw(final float n) {
        RotationManager.mc.player.rotationYaw = n;
        RotationManager.mc.player.rotationYawHead = n;
    }
}
