package cn.origin.cube.module.modules.combat;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.client.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
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

import java.util.Random;

@Constant(constant = false)
@ModuleInfo(name = "BowBomb", descriptions = "BowBomb", category = Category.COMBAT)
public class BowBomb extends Module {

    ModeSetting<Page> page = registerSetting("Exploit", Page.AntiCheat);

    ModeSetting<ModeEn> Mode = registerSetting("Exploit", ModeEn.Maximum).modeVisible(page, Page.AntiCheat);
    BooleanSetting rotation = registerSetting("Rotation", false).modeVisible(page, Page.AntiCheat);
    FloatSetting factor = registerSetting("Factor", 1f,1f,20f).modeVisible(page, Page.AntiCheat);
    BooleanSetting minimize = registerSetting("Minimize", false).modeVisible(page, Page.AntiCheat);
    FloatSetting scale = registerSetting("Scale", 0.01f,0.01f,0.4f).modeVisible(page, Page.AntiCheat);
    FloatSetting delay = registerSetting("Scale", 5f,0f,10f).modeVisible(page, Page.AntiCheat);
    ModeSetting<exploitEn> exploit = registerSetting("Exploit", exploitEn.Strict).modeVisible(page, Page.AntiCheat);
    //ItemSelect
    BooleanSetting bow = registerSetting("Bow", true).modeVisible(page, Page.Items);
    BooleanSetting pearls = registerSetting("Pearl", true).modeVisible(page, Page.Items);
    BooleanSetting xp = registerSetting("Xp", true).modeVisible(page, Page.Items);
    BooleanSetting eggs = registerSetting("Eggs", true).modeVisible(page, Page.Items);
    BooleanSetting potions = registerSetting("Potion", true).modeVisible(page, Page.Items);
    BooleanSetting snowballs = registerSetting("SnowBalls", true).modeVisible(page, Page.Items);


    private Random rnd = new Random();
    public static Timer delayTimer = new Timer();


    @SubscribeEvent
    protected void onPacketSend(PacketEvent.Send event) {
        if(fullNullCheck() || !delayTimer.passedMs((long) (delay.getValue() * 1000))) return;
        if (event.getPacket() instanceof CPacketPlayerDigging && ((CPacketPlayerDigging) event.getPacket()).getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && (mc.player.getActiveItemStack().getItem() == Items.BOW && bow.getValue())
                || event.getPacket() instanceof CPacketPlayerTryUseItem && ((CPacketPlayerTryUseItem)event.getPacket()).getHand() == EnumHand.MAIN_HAND &&  ((mc.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL && pearls.getValue()) || (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && xp.getValue()) || (mc.player.getHeldItemMainhand().getItem() == Items.EGG && eggs.getValue()) || (mc.player.getHeldItemMainhand().getItem() == Items.SPLASH_POTION && potions.getValue()) || (mc.player.getHeldItemMainhand().getItem() == Items.SNOWBALL && snowballs.getValue()))) {

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));

            double[] strict_direction = new double[]{ 100f * -Math.sin(Math.toRadians(mc.player.rotationYaw)),100f * Math.cos(Math.toRadians(mc.player.rotationYaw))};

            if(exploit.getValue().equals(exploitEn.Fast)){
                for (int i = 0; i < getRuns(); i++) {
                    spoof(mc.player.posX,  minimize.getValue() ? mc.player.posY : mc.player.posY - 1e-10, mc.player.posZ, true);
                    spoof(mc.player.posX, mc.player.posY + 1e-10, mc.player.posZ, false);
                }
            }
            if(exploit.getValue().equals(exploitEn.Strong)){
                for (int i = 0; i < getRuns(); i++) {
                    spoof(mc.player.posX, mc.player.posY + 1e-10, mc.player.posZ, false);
                    spoof(mc.player.posX, minimize.getValue() ? mc.player.posY : mc.player.posY - 1e-10, mc.player.posZ, true);
                }
            }
            if(exploit.getValue().equals(exploitEn.Phobos)){
                for (int i = 0; i < getRuns(); i++) {
                    spoof(mc.player.posX, mc.player.posY + 0.00000000000013, mc.player.posZ, true);
                    spoof(mc.player.posX, mc.player.posY + 0.00000000000027, mc.player.posZ,  false);
                }
            }
            if(exploit.getValue().equals(exploitEn.Strict)){
                for (int i = 0; i < getRuns(); i++) {
                    if(rnd.nextBoolean()){
                        spoof(mc.player.posX - strict_direction[0], mc.player.posY, mc.player.posZ - strict_direction[1], false);
                    } else {
                        spoof(mc.player.posX + strict_direction[0], mc.player.posY, mc.player.posZ + strict_direction[1], true);
                    }
                }
            }

            delayTimer.reset();
        }
    }

    private void spoof(double x, double y , double z, boolean ground){
        if(rotation.getValue()){
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(x, y, z, mc.player.rotationYaw, mc.player.rotationPitch, ground));
        } else {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, ground));
        }
    }

    private int getRuns(){
        if(Mode.getValue().equals(ModeEn.Factorised)){
            return 10 + (int)((factor.getValue() - 1));
        }
        if(Mode.getValue().equals(ModeEn.Normal)){
            return (int) Math.floor(factor.getValue());
        }
        if(Mode.getValue().equals(ModeEn.Maximum) ){
            return (int) (30f * factor.getValue());
        }
        return  1;
    }

    private enum exploitEn {
        Strong, Fast, Strict, Phobos
    }

    private enum ModeEn {
        Normal, Maximum, Factorised
    }

    private enum Page {
        Items, AntiCheat
    }
}

