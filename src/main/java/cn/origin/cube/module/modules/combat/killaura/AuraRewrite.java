package cn.origin.cube.module.modules.combat.killaura;

import cn.origin.cube.Cube;
import cn.origin.cube.core.settings.*;
import cn.origin.cube.core.events.player.UpdateWalkingPlayerEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.module.interfaces.Para;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.module.modules.combat.AutoCrystal.AutoCrystal;
import cn.origin.cube.utils.client.event.event.ParallelListener;
import cn.origin.cube.utils.client.event.event.Priority;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.InventoryUtil;
import cn.origin.cube.utils.player.RotationUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import cn.origin.cube.utils.render.RenderBuilder;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Date;

@Para(para = Para.ParaMode.Light)
@ParallelListener(priority = Priority.HIGH)
@ModuleInfo(name = "AuraRewrite", descriptions = "Auto attack entity", category = Category.COMBAT)
public class AuraRewrite extends Module {

    public EntityLivingBase target = null;
    long killLast = new Date().getTime();
    public float pitch;
    public float yaw;

    public enum Page{Target,AntiCheat,Render}

    ModeSetting<Page> page = registerSetting("Page", Page.Target);
    //Target
    DoubleSetting range = registerSetting("Range", 4.0, 1, 6).modeVisible(page, Page.Target);
    BooleanSetting players = registerSetting("Players", true).modeVisible(page, Page.Target);
    BooleanSetting animals = registerSetting("Animals", true).modeVisible(page, Page.Target);
    BooleanSetting mobs = registerSetting("Mobs", true).modeVisible(page, Page.Target);
    //AntiCheat
    BooleanSetting hitDelay = registerSetting("HitDelay", true).modeVisible(page, Page.AntiCheat);
    BooleanSetting packet = registerSetting("PacketHit", false).modeVisible(page, Page.AntiCheat);
    BooleanSetting swimArm = registerSetting("SwimArm", true).booleanVisible(packet).modeVisible(page, Page.AntiCheat);
    ModeSetting<Mode> breakHand = registerSetting("SwingHand", Mode.Main).modeVisible(page, Page.AntiCheat);
    IntegerSetting delay = registerSetting("Delay", 4, 0, 70).modeVisible(page, Page.AntiCheat);
    BooleanSetting randomD = registerSetting("RandomDelay", false).modeVisible(page, Page.AntiCheat);
    IntegerSetting randomDelay = registerSetting("Random Delay", 4, 0, 40).booleanVisible(randomD).modeVisible(page, Page.AntiCheat);
    IntegerSetting iterations = registerSetting("Iterations", 1, 1, 10).modeVisible(page, Page.AntiCheat);
    BooleanSetting walls = registerSetting("Walls", true).modeVisible(page, Page.AntiCheat);
    DoubleSetting wallsRange = registerSetting("Wall Range", 3.5, 1, 10).booleanVisible(walls).modeVisible(page, Page.AntiCheat);
    BooleanSetting rotate = registerSetting("Rotate", true).modeVisible(page, Page.AntiCheat);
    BooleanSetting rotateStrict = registerSetting("StrictRotate", true).modeVisible(page, Page.AntiCheat);
    BooleanSetting betterCrit = registerSetting("BetterCrit", false).modeVisible(page, Page.AntiCheat);
    BooleanSetting projectiles = registerSetting("Projectiles", true).modeVisible(page, Page.AntiCheat);
    BooleanSetting silent = registerSetting("Silent", true).modeVisible(page, Page.AntiCheat);

    //Render
    BooleanSetting render = registerSetting("Render", true).modeVisible(page, Page.Render);
    ModeSetting<RenderMode> rMode = registerSetting("RenderMode", RenderMode.Circle).modeVisible(page, Page.Render);
    FloatSetting lineWidth = registerSetting("LineWidth", 1.5f, 0f, 5f).booleanVisible(render).modeVisible(page, Page.Render);
    FloatSetting CWidth = registerSetting("CWidth", 1f, -5f, 5f).booleanVisible(render).modeVisible(page, Page.Render);
    IntegerSetting Height = registerSetting("Height", 200, 0, 500).modeVisible(page, Page.Render);
    IntegerSetting Width = registerSetting("Width", 200, 0, 500).modeVisible(page, Page.Render);

    @SubscribeEvent
    public void onUpdate(UpdateWalkingPlayerEvent event) {
        if(fullNullCheck()) return;
        if(target != null) {
            if (target.getDistance(mc.player) >= range.getValue() || target.isDead || !target.isEntityAlive())
                target = null;
        }
        if(silent.getValue()){
            SwitchUtils.silent();
            if(target == null){
                SwitchUtils.oldSwitch();
            }
        }
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity == mc.player) continue;
            if(entity.isDead || !entity.isEntityAlive()) continue;
            if(entity.getDistance(mc.player) <= range.getValue()) {
                if(mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                    if (entity instanceof EntityPlayer && players.getValue()) {
                        target = (EntityLivingBase) entity;
                        final int delay = (int)(this.delay.getValue() * 10 + (randomD.getValue() ? this.randomDelay.getValue() * 10 * Math.random() : 0));
                        if (new Date().getTime() >= this.killLast + delay) {
                            this.killLast = new Date().getTime();
                            if (rotate.getValue()) {
                                if(rotateStrict.getValue()) {
                                    Rotate.rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                                }else{
                                    Rotate.rotateTo(target);
                                }
                            }
                            for (int i = 0; i < this.iterations.getValue(); ++i) {
                                attack(target);
                            }
                        }
                    }
                    if (entity instanceof EntityAnimal && animals.getValue()) {
                        target = (EntityLivingBase) entity;
                        final int delay = (int)(this.delay.getValue() * 10 + (randomD.getValue() ? this.randomDelay.getValue() * 10 * Math.random() : 0));
                        if (new Date().getTime() >= this.killLast + delay) {
                            this.killLast = new Date().getTime();
                            if (rotate.getValue()) {
                                if(rotateStrict.getValue()) {
                                    Rotate.rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                                }else{
                                    Rotate.rotateTo(target);
                                }
                            }
                            for (int i = 0; i < this.iterations.getValue(); ++i) {
                                attack(target);
                            }
                        }
                    }
                    if(utils.isProjectile(entity) && projectiles.getValue()){
                        target = (EntityLivingBase)entity;
                        final int delay = (int)(this.delay.getValue() * 10 + (randomD.getValue() ? this.randomDelay.getValue() * 10 * Math.random() : 0));
                        if (new Date().getTime() >= this.killLast + delay) {
                            this.killLast = new Date().getTime();
                            if (rotate.getValue()) {
                                if(rotateStrict.getValue()) {
                                    Rotate.rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                                }else{
                                    Rotate.rotateTo(target);
                                }
                            }
                            for (int i = 0; i < this.iterations.getValue(); ++i) {
                                attack(target);
                            }
                        }
                    }
                    if ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue()) {
                        target = (EntityLivingBase) entity;
                        final int delay = (int)(this.delay.getValue() * 10 + (randomD.getValue() ? this.randomDelay.getValue() * 10 * Math.random() : 0));
                        if (new Date().getTime() >= this.killLast + delay) {
                            this.killLast = new Date().getTime();
                            if (rotate.getValue()) {
                                if(rotateStrict.getValue()) {
                                    Rotate.rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                                }else{
                                    Rotate.rotateTo(target);
                                }
                            }
                            for (int i = 0; i < this.iterations.getValue(); ++i) {
                                attack(target);
                            }
                        }
                    }
                }
            }
        }
    }

    static double healthBarTarget = 0, healthBar = 0;

    @Override
    public void onRender3D(Render3DEvent event) {
        if(target != null && render.getValue()) {
            if(rMode.getValue().equals(RenderMode.Circle)) {
                Render3DUtil.drawCircle(new RenderBuilder()
                        .setup()
                        .line(lineWidth.getValue())
                        .depth(true)
                        .blend()
                        .texture(), EntityUtil.getInterpolatedPosition(target, 1), target.width - CWidth.getValue(), target.height * (0.5 * (Math.sin((mc.player.ticksExisted * 3.5) * (Math.PI / 180)) + 1)), new Color(255, 50, 50));
            }
            if(rMode.getValue().equals(RenderMode.Box)) {
                AxisAlignedBB box = target.getRenderBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
                Render3DUtil.drawBox(new RenderBuilder()
                        .position(box)
                        .color(new Color(Colors.getGlobalColor().getRGB()))
                        .box(RenderBuilder.Box.FILL)
                        .setup()
                        .depth(true)
                        .height(lineWidth.getValue())
                        .blend()
                        .texture());
            }
        }
    }

    @Override
    public void onRender2D() {
        if (target != null) {

            if(packet.getValue()){
                Cube.fontManager.CustomFont.drawString("Attacking", 500, 500, -1);
            }

            DecimalFormat dec = new DecimalFormat("#");

            healthBarTarget = Width.getValue() / 2 - 41 + (((140) / (target.getMaxHealth())) * (target.getHealth()));
            double HealthBarSpeed = 5;

            if (healthBar > healthBarTarget) {
                healthBar = ((healthBar) - ((healthBar - healthBarTarget) / HealthBarSpeed));
            } else if (healthBar < healthBarTarget) {
                healthBar = ((healthBar) + ((healthBarTarget - healthBar) / HealthBarSpeed));
            }
            int color = (target.getHealth() / target.getMaxHealth() > 0.66f) ? 0xff00ff00 : (target.getHealth() / target.getMaxHealth() > 0.33f) ? 0xffff9900 : 0xffff0000;

            color = 0xff00ff00;
            float[] hsb = Color.RGBtoHSB(((int) 255), ((int) 255), ((int) 255), null);
            float hue = hsb[0];
            float saturation = hsb[1];
            color = Color.HSBtoRGB(hue, saturation, 1);

            float hue1 = System.currentTimeMillis() % (int) ((100.5f - 50) * 1000) / (float) ((100.5f - 50) * 1000);
            color = Color.HSBtoRGB(hue1, 0.65f, 1);

            Gui.drawRect(Width.getValue() / 2 - 110, Height.getValue() / 2 + 100, Width.getValue() / 2 + 110, Height.getValue() / 2 + 170, 0xff36393f);
            Gui.drawRect(Width.getValue() / 2 - 41, Height.getValue() / 2 + 100 + 54, Width.getValue() / 2 + 100, Height.getValue() / 2 + 96 + 45, 0xff202225);
            Gui.drawRect(Width.getValue() / 2 - 41, Height.getValue() / 2 + 100 + 54, (int) healthBar, Height.getValue() / 2 + 96 + 45, color);
            GlStateManager.color(1, 1, 1);
            GuiInventory.drawEntityOnScreen(Width.getValue() / 2 - 75, Height.getValue() / 2 + 165, 25, 1f, 1f, target);
            Cube.fontManager.CustomFont.drawString(target.getName(), Width.getValue() / 2 - 40, Height.getValue() / 2 + 110, -1);
            Cube.fontManager.CustomFont.drawString("HP: ", Width.getValue() / 2 - 40, Height.getValue() / 2 + 125, -1);
            Cube.fontManager.CustomFont.drawString("搂c鉂�: 搂f" + dec.format(target.getHealth()), Width.getValue() / 2 - 40 + Cube.fontManager.CustomFont.getStringWidth("HP: "), Height.getValue() / 2 + 125, color);
        }
    }
    public void attack(Entity entity) {
        Rotate.rotateTo(entity.posX, entity.posY, entity.posZ, mc.player, false);
        if (hitDelay.getValue()) {
            if(betterCrit.getValue()){
                mc.gameSettings.keyBindSneak.pressed = true;
            }
            if(!packet.getValue()) {
                if (mc.player.getCooledAttackStrength(0) >= 1) {
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(getHandToBreak());
                }
            }else{
                mc.playerController.connection.sendPacket(new CPacketUseEntity(entity));
                if (swimArm.getValue()) mc.player.swingArm(getHandToBreak());
            }
            if(betterCrit.getValue()){
                mc.gameSettings.keyBindSneak.pressed = true;
            }
        }
        if(betterCrit.getValue()){
            mc.gameSettings.keyBindSneak.pressed = true;
        }
        if (!hitDelay.getValue()) {
            mc.playerController.attackEntity(mc.player, entity);
            mc.player.swingArm(getHandToBreak());
        }
        if(betterCrit.getValue()){
            mc.gameSettings.keyBindSneak.pressed = true;
        }
    }

    @Override
    public void onDisable() {
        if(target != null)
            target = null;
        Rotate.resetRotation();
    }

    @Override
    public void onEnable(){
        SwitchUtils.oldSwitch();
    }

    private EnumHand getHandToBreak() {
        if (breakHand.getValue().equals(Mode.Offhand)) {
            return EnumHand.OFF_HAND;
        }else if(breakHand.getValue().equals(Mode.Both)){
            return EnumHand.OFF_HAND;
        }
        return EnumHand.MAIN_HAND;
    }

    public enum RenderMode{
        Circle,Box
    }

    public enum Mode{
        Offhand,Main,Both
    }
}
