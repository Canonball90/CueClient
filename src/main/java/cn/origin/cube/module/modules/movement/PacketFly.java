package cn.origin.cube.module.modules.movement;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.player.MotionEvent;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.inject.client.INetworkManager;
import cn.origin.cube.inject.client.ISPacketPlayerPosLook;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.DoubleSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
@Constant(constant = false)
@ModuleInfo(name = "PacketFly",
    descriptions = "Fly",
    category = Category.MOVEMENT)
public class PacketFly extends Module {

    private static final double CONCEAL = 0.0624;
    private static final double MOVE_FACTOR = 1.0 / StrictMath.sqrt(2.0);
    ModeSetting mode = registerSetting("Mode", Mode.FACTOR);
    DoubleSetting factor = registerSetting("Factor", 1.2, 0.1, 5.0);
    ModeSetting bounds = registerSetting("Bounds", Bounds.DOWN);
    ModeSetting phase = registerSetting("Phase", Phase.NCP);
    BooleanSetting conceal = registerSetting("Conceal", false);
    BooleanSetting antiKick = registerSetting("AntiKick", false);
    private final Map<Integer, Vec3d> predictions = new HashMap<>();
    private int tpId = 0;
    private int lagTime = 0;

    @Override
    public void onDisable() {
        super.onDisable();

        predictions.clear();
        tpId = 0;
        lagTime = 0;
        mc.player.noClip = false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMove(MotionEvent event) {
        int loops = (int) Math.floor(factor.getValue());
        if (mode.getValue().equals(Mode.FACTOR)) {
            if (mc.player.ticksExisted % 10.0 < 10.0 * (factor.getValue() - Math.floor(factor.getValue()))) {
                loops++;
            }
        }

        else {
            loops = 1;
        }
        double moveSpeed = (conceal.getValue() || --lagTime > 0 || isPhased()) ? CONCEAL : 0.2873;

        double motionY = 0.0;

        boolean doAntiKick = false;

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            motionY = CONCEAL;

            if (MovementUtils.isMoving(mc.player)) {
                moveSpeed *= MOVE_FACTOR;
                motionY *= MOVE_FACTOR;
            }
        }

        else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            motionY = -CONCEAL;

            if (MovementUtils.isMoving(mc.player)) {
                moveSpeed *= MOVE_FACTOR;
                motionY *= MOVE_FACTOR;
            }
        }

        else {
            doAntiKick = antiKick.getValue()
                    && mc.player.ticksExisted % 40 == 0
                    && !isPhased()
                    && !mc.world.collidesWithAnyBlock(mc.player.getEntityBoundingBox())
                    && !MovementUtils.isMoving(mc.player);

            if (doAntiKick) {
                loops = 1;
                motionY = -0.04;
            }
        }

        send(loops, moveSpeed, motionY, doAntiKick);

        event.setX(mc.player.motionX);
        event.setY(mc.player.motionY);
        event.setZ(mc.player.motionZ);

        if (!phase.getValue().equals(Phase.NONE)) {
            mc.player.noClip = true;
        }

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive event) {

        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();

            Vec3d prediction = predictions.get(packet.getTeleportId());
            if (prediction != null) {

                if (prediction.x == packet.getX() && prediction.y == packet.getY() && prediction.z == packet.getZ()) {

                    if (!mode.getValue().equals(Mode.SETBACK)) {
                        event.setCanceled(true);
                    }

                    mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
                    return;
                }
            }

            ((ISPacketPlayerPosLook) packet).setYaw(mc.player.rotationYaw);
            ((ISPacketPlayerPosLook) packet).setPitch(mc.player.rotationPitch);

            mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));

            lagTime = 10;
            tpId = packet.getTeleportId();
        }
    }

    private void send(int factor, double moveSpeed, double motionY, boolean antiKick) {

        if (factor == 0) {
            mc.player.setVelocity(0, 0, 0);
            return;
        }

        double[] strafe = MovementUtils.getMoveSpeed(moveSpeed);

        for (int i = 1; i < factor + 1; ++i) {

            double motionX = strafe[0] * i;
            double motionZ = strafe[1] * i;

            double velY = motionY;

            if (!antiKick) {
                velY *= i;
            }

            mc.player.motionX = motionX;
            mc.player.motionY = velY;
            mc.player.motionZ = motionZ;

            Vec3d posVec = mc.player.getPositionVector();

            Vec3d moveVec = posVec.add(motionX, velY, motionZ);

            send(moveVec);
            if (!mode.getValue().equals(Mode.SETBACK)) {

                predictions.put(++tpId, moveVec);

                mc.player.connection.sendPacket(new CPacketConfirmTeleport(tpId));
            }
        }
    }

    private void send(Vec3d vec) {
        ((INetworkManager) mc.player.connection.getNetworkManager()).hookDispatchPacket(new CPacketPlayer.Position(vec.x, vec.y, vec.z, true), null);
    }

    private boolean isPhased() {
        return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    public enum Mode {
        FACTOR,
        FAST,
        SETBACK
    }

    public enum Bounds {
        UP(1337.0),
        DOWN(-1337.0),
        MIN(512.0);

        private final double yOffset;

        Bounds(double yOffset) {
            this.yOffset = yOffset;
        }

        public Vec3d modify(Vec3d in) {
            return in.add(0, yOffset, 0);
        }
    }

    public enum Phase {
        VANILLA,
        NCP,
        NONE
    }
}
