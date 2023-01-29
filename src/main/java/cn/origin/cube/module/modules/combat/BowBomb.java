package cn.origin.cube.module.modules.combat;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.utils.client.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@Constant(constant = false)
@ModuleInfo(name = "BowBomb", descriptions = "BowBomb", category = Category.COMBAT)
public class BowBomb extends Module {

    private long lastShootTime;
    BooleanSetting Bows = registerSetting("Bows", true);
    BooleanSetting pearls = registerSetting("Pearls", true);
    BooleanSetting eggs = registerSetting("Eggs", true);
    BooleanSetting snowballs = registerSetting("SnowBalls", true);
    IntegerSetting Timeout = registerSetting("Timeout", 5000, 100, 20000);
    IntegerSetting spoofs = registerSetting("Spoofs", 10, 1, 300);
    BooleanSetting bypass = registerSetting("Bypass", true);
    BooleanSetting debug = registerSetting("Debug", true);

    @SubscribeEvent
    public void onSend(PacketEvent.Send event) {
        ItemStack handStack;
        CPacketPlayerTryUseItem packet2;
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            ItemStack handStack2;
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !(handStack2 = BowBomb.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty()) {
                handStack2.getItem();
                if (handStack2.getItem() instanceof ItemBow && this.Bows.getValue()) {
                    this.doSpoofs();
                    if (this.debug.getValue()) {
                        ChatUtil.sendMessage("trying to spoof");
                    }
                }
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet2 = (CPacketPlayerTryUseItem)event.getPacket()).getHand() == EnumHand.MAIN_HAND && !(handStack = BowBomb.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty()) {
            handStack.getItem();
            if (handStack.getItem() instanceof ItemEgg && this.eggs.getValue()) {
                this.doSpoofs();
            } else if (handStack.getItem() instanceof ItemEnderPearl && this.pearls.getValue()) {
                this.doSpoofs();
            } else if (handStack.getItem() instanceof ItemSnowball && this.snowballs.getValue()) {
                this.doSpoofs();
            }
        }
    }

    @Override
    public void onEnable() {
        this.lastShootTime = System.currentTimeMillis();
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - this.lastShootTime >= (long)this.Timeout.getValue()) {
            this.lastShootTime = System.currentTimeMillis();
            BowBomb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BowBomb.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int index = 0; index < this.spoofs.getValue(); ++index) {
                if (this.bypass.getValue()) {
                    BowBomb.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowBomb.mc.player.posX, BowBomb.mc.player.posY + 1.0E-10, BowBomb.mc.player.posZ, false));
                    BowBomb.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowBomb.mc.player.posX, BowBomb.mc.player.posY - 1.0E-10, BowBomb.mc.player.posZ, true));
                    continue;
                }
                BowBomb.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowBomb.mc.player.posX, BowBomb.mc.player.posY - 1.0E-10, BowBomb.mc.player.posZ, true));
                BowBomb.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowBomb.mc.player.posX, BowBomb.mc.player.posY + 1.0E-10, BowBomb.mc.player.posZ, false));
            }
            if (this.debug.getValue()) {
                ChatUtil.sendMessage("spoofed");
            }
        }
    }
}
