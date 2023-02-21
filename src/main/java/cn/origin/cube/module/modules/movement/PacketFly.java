package cn.origin.cube.module.modules.movement;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.player.MotionEvent;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.*;
import cn.origin.cube.inject.client.INetworkManager;
import cn.origin.cube.inject.client.ISPacketPlayerPosLook;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

@Constant(constant = false)
@ModuleInfo(name = "PacketFly",
    descriptions = "Fly",
    category = Category.MOVEMENT)
public class PacketFly extends Module {

    BooleanSetting bound = registerSetting("Bound", true);
    BooleanSetting cancelPacket = registerSetting("CancelPacket", true);
    BooleanSetting tpAccept = registerSetting("Bound", true);
    BooleanSetting edgeEnable = registerSetting("Bound", true);
    BooleanSetting noclip = registerSetting("Bound", true);
    FloatSetting slide = registerSetting("Slide", 0.5f, 0f, 1f);
    IntegerSetting jitterAmount = registerSetting("JitterAmount", 1,0,3);
    IntegerSetting boundAmount = registerSetting("BoundAmount", 555,-1337,1337);
    DoubleSetting speed = registerSetting("Speed", 0.01, 0.01, 0.1);


    boolean cancelling = true;
    int teleportId;
    ArrayList<Position> packets = new ArrayList<>();

    @Override
    public void onDisable() {
        if (noclip.getValue()) {
            PacketFly.mc.player.noClip = false;
        }
    }

    @Override
    public void onEnable() {
        if (noclip.getValue()) {
            PacketFly.mc.player.noClip = true;
        }
        teleportId = 0;
    }

    public static double[] forward(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0.0F) {
            if (side > 0.0F) {
                yaw += (float) (forward > 0.0F ? -45 : 45);
            } else if (side < 0.0F) {
                yaw += (float) (forward > 0.0F ? 45 : -45);
            }

            side = 0.0F;
            if (forward > 0.0F) {
                forward = 1.0F;
            } else if (forward < 0.0F) {
                forward = -1.0F;
            }
        }

        double sin = Math.sin(Math.toRadians(yaw + 90.0F));
        double cos = Math.cos(Math.toRadians(yaw + 90.0F));
        double posX = (double) forward * speed * cos + (double) side * speed * sin;
        double posZ = (double) forward * speed * sin - (double) side * speed * cos;

        return new double[] { posX, posZ};
    }

    @Override
    public void onUpdate() {
        if (shouldFly()) {
            cancelling = false;
            double[] forward = forward(getSpeed() * getJitter());
            double up = PacketFly.mc.gameSettings.keyBindJump.isKeyDown() ? 0.0233D : (PacketFly.mc.gameSettings.keyBindSneak.isKeyDown() ? -0.0233D : 1.0E-6D);
            double[] playerPos = toPlayerPos(forward[0], up, forward[1]);

            PacketFly.mc.player.setVelocity(forward[0], up, forward[1]);
            Position packetPlayer = new Position(playerPos[0], playerPos[1], playerPos[2], PacketFly.mc.player.onGround);

            PacketFly.mc.player.connection.sendPacket(packetPlayer);
            packets.add(packetPlayer);
            PacketFly.mc.player.setPosition(playerPos[0], playerPos[1], playerPos[2]);
            if (bound.getValue()) {
                Position bounds = new Position(playerPos[0], boundAmount.getValue(), playerPos[2], PacketFly.mc.player.onGround);

                PacketFly.mc.player.connection.sendPacket(bounds);
                packets.add(bounds);
            }

            ++teleportId;
            if (tpAccept.getValue()) {
                PacketFly.mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId - 1));
                PacketFly.mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId));
                PacketFly.mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId + 1));
            }

            cancelling = true;
        }
    }


    @SubscribeEvent
    public boolean onPacketSend(PacketEvent.Send packet2) {
        if (packet2.packet instanceof Position && cancelPacket.getValue()) {
            return cancelling;
        }
        return false;
    }

    @SubscribeEvent
    public boolean onPacketReceive(PacketEvent.Receive packet5) {
        SPacketPlayerPosLook packet2 = packet5.getPacket();
        ISPacketPlayerPosLook inter = (ISPacketPlayerPosLook) packet2;

        if (PacketFly.mc.player.isEntityAlive() && PacketFly.mc.world.isBlockLoaded(new BlockPos(PacketFly.mc.player.posX, PacketFly.mc.player.posY, PacketFly.mc.player.posZ)) && !(PacketFly.mc.currentScreen instanceof GuiDownloadTerrain)) {
            if (teleportId <= 0) {
                teleportId = packet2.getTeleportId();
            } else {
                inter.setX(MathHelper.clampedLerp(Math.min(PacketFly.mc.player.posX, packet2.getX()), Math.max(PacketFly.mc.player.posX, packet2.getX()), slide.getValue()));
                inter.setY(MathHelper.clampedLerp(Math.min(PacketFly.mc.player.getEntityBoundingBox().minY, packet2.getY()), Math.max(PacketFly.mc.player.getEntityBoundingBox().minY, packet2.getY()), slide.getValue()));
                inter.setZ(MathHelper.clampedLerp(Math.min(PacketFly.mc.player.posZ, packet2.getZ()), Math.max(PacketFly.mc.player.posZ, packet2.getZ()), slide.getValue()));
            }
        }

        return false;
    }




    double getSpeed() {
        return !PacketFly.mc.gameSettings.keyBindJump.isKeyDown() && !PacketFly.mc.gameSettings.keyBindSneak.isKeyDown() ? speed.getValue() : 0.0D;
    }

    double[] toPlayerPos(double x, double y, double z) {
        return new double[] { PacketFly.mc.player.posX + x, PacketFly.mc.player.posY + y, PacketFly.mc.player.posZ + z};
    }

    double getJitter() {
        return Math.floor(Math.random() * jitterAmount.getValue() - ((Number) 0).doubleValue() + 1.0D) + ((Number) 0).doubleValue();
    }

    boolean isOnEdge() {
        boolean verticalFlying = PacketFly.mc.gameSettings.keyBindJump.isKeyDown() || PacketFly.mc.gameSettings.keyBindSneak.isKeyDown();

        return PacketFly.mc.player.collidedHorizontally || verticalFlying;
    }

    boolean shouldFly() {
        return !edgeEnable.getValue() || isOnEdge();
    }

}
