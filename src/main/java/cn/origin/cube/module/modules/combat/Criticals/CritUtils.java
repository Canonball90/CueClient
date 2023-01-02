package cn.origin.cube.module.modules.combat.Criticals;

import cn.origin.cube.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import java.util.Objects;

public class CritUtils {
    public static Timer timer = new Timer();
    static Minecraft mc = Minecraft.getMinecraft();
    static Criticals crit = new Criticals();

    public static void doCrit(){
        CPacketUseEntity packet = null;
            if (!timer.passedMs(0L)) {
                return;
            }
            switch (crit.getPackets().getValue()) {
                case 1: {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + (double) 0.1f, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    break;
                }
                case 2: {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625101, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1E-5, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    break;
                }
                case 3: {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625101, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0125, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    break;
                }
                case 4: {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1625, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 4.0E-6, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0E-6, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer());
                    mc.player.onCriticalHit(Objects.requireNonNull(packet.getEntityFromWorld(mc.world)));
                }
            }
            timer.reset();
    }

    public static boolean canCrit(Boolean onlyWeapon) {
        return (!mc.player.isInWeb && !mc.player.isOnLadder() && !mc.player.isRiding() &&
                !mc.player.isPotionActive(MobEffects.BLINDNESS) && !mc.player.isInWater() && !mc.player.isInLava()
                && (!onlyWeapon || (isHoldingWeapon())));
    }

    public static boolean isHoldingWeapon() {
        return mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD
                || mc.player.getHeldItemMainhand().getItem() == Items.IRON_SWORD ||
                mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_SWORD ||
                mc.player.getHeldItemMainhand().getItem() == Items.STONE_SWORD ||
                mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_SWORD ||
                mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_AXE ||
                mc.player.getHeldItemMainhand().getItem() == Items.IRON_SWORD ||
                mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_AXE ||
                mc.player.getHeldItemMainhand().getItem() == Items.STONE_AXE ||
                mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_AXE;
    }
}