package cn.origin.cube.module.modules.combat;

import cn.origin.cube.Cube;
import cn.origin.cube.core.settings.*;
import cn.origin.cube.event.events.player.UpdateWalkingPlayerEvent;
import cn.origin.cube.event.events.world.Render3DEvent;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.interfaces.Para;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.RotationUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import cn.origin.cube.utils.render.RenderBuilder;
import cn.origin.cube.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Date;

@Para(para = Para.ParaMode.Light)
@ModuleInfo(name = "AuraRewrite", descriptions = "Auto attack entity", category = Category.COMBAT)
public class AuraRewrite extends Module {
    DoubleSetting range = registerSetting("Range", 4.0, 1, 6);
    BooleanSetting players = registerSetting("Players", true);
    BooleanSetting animals = registerSetting("Animals", true);
    BooleanSetting mobs = registerSetting("Mobs", true);
    BooleanSetting hitDelay = registerSetting("HitDelay", true);
    BooleanSetting packet = registerSetting("PacketHit", false);
    BooleanSetting swimArm = registerSetting("SwimArm", true).booleanVisible(packet);
    IntegerSetting delay = registerSetting("Delay", 4, 0, 70).booleanVisible(packet);
    BooleanSetting randomD = registerSetting("RandomDelay", false);
    IntegerSetting randomDelay = registerSetting("Random Delay", 4, 0, 40).booleanVisible(randomD);
    IntegerSetting iterations = registerSetting("Iterations", 1, 1, 10);
    DoubleSetting wallsRange = registerSetting("Wall Range", 3.5, 1, 10);
    BooleanSetting rotate = registerSetting("Rotate", true);
    BooleanSetting rotateStrict = registerSetting("StrictRotate", true);

    BooleanSetting render = registerSetting("Render", true);
    ModeSetting<RenderMode> rMode = registerSetting("RenderMode", RenderMode.Circle);
    FloatSetting lineWidth = registerSetting("LineWidth", 1.5f, 0f, 5f).booleanVisible(render);
    FloatSetting CWidth = registerSetting("Width", 1.5f, -5f, 5f).booleanVisible(render);
    IntegerSetting Height = registerSetting("Height", 200, 0, 500);
    IntegerSetting Width = registerSetting("Width", 200, 0, 500);
    public boolean shouldRotate;
    public float yaw;
    public float pitch;
    public boolean shouldReset;
    public float playerPitch;
    public float lastPlayerPitch;
    public float playerYaw;
    public float renderYaw;
    public float YawOffset;
    float rotationYaw;
    boolean smoothRotatePitch;
    boolean smoothRotated;
    boolean smoothRotateYaw;
    int addedInputYaw;
    float smoothYaw;
    float rotationPitch;
    int addedOriginYaw;
    float smoothPitch;

    long killLast = new Date().getTime();
    public EntityLivingBase target = null;

    @SubscribeEvent
    public void onUpdate(UpdateWalkingPlayerEvent event) {
        if(fullNullCheck()) return;
        if(target != null) {
            if (target.getDistance(mc.player) >= range.getValue() || target.isDead || !target.isEntityAlive())
                target = null;
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
                                    rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                                }else{
                                    rotateTo(target);
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
                                    rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                                }else{
                                    rotateTo(target);
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
                                    rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                                }else{
                                    rotateTo(target);
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
    float lastHealth = 0;

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
                Render3DUtil.prepareGL();
                GL11.glLineWidth(lineWidth.getValue());
                int r = (int) ((target.hurtTime > 0 ? 255 : ClickGui.getCurrentColor().getRed())  / 255f);
                int g = (int) ((target.hurtTime > 0 ? 0 : ClickGui.getCurrentColor().getGreen())  / 255f);
                int b = (int) ((target.hurtTime > 0 ? 0 : ClickGui.getCurrentColor().getBlue())  / 255f);
                Render3DUtil.drawBoundingBox(box, lineWidth.getValue(), r, g,b,100);
                Render3DUtil.drawBBBox(box, ClickGui.getCurrentColor(), 170, lineWidth.getValue(), false);
                RenderUtil.release();
            }
        }
    }

    @Override
    public void onRender2D() {
        if (target != null) {

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
        rotateTo(entity.posX, entity.posY, entity.posZ, mc.player, false);
        if (hitDelay.getValue()) {
            if(!packet.getValue()) {
                if (mc.player.getCooledAttackStrength(0) >= 1) {
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }else{
                mc.playerController.connection.sendPacket(new CPacketUseEntity(entity));
                if (swimArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
        if (!hitDelay.getValue()) {
            mc.playerController.attackEntity(mc.player, entity);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public void rotateTo(Entity target) {
        RotationUtil.faceVector(new Vec3d(target.posX, target.posY + 1, target.posZ), true);
    }

    public boolean rotateTo(final double n, final double n2, final double n3, final EntityPlayer entityPlayer, final boolean b){
        final double[] calculateLook = EntityUtil.calculateLookAt(n, n2, n3, entityPlayer);
        return setRotation((float) calculateLook[0], (float) calculateLook[1], b);
    }

    public boolean setRotation(float setSmoothRotationYaw, float n, final boolean b) {
        final boolean b2 = false;
        smoothRotatePitch = b2;
        smoothRotateYaw = b2;
        smoothRotated = true;
        if (b) {
            if (!shouldRotate) {
                yaw = mc.player.prevRotationYaw;
                pitch = mc.player.prevRotationPitch;
            }
            if (calculateDirectionDifference(setSmoothRotationYaw + 180.0f, yaw + 180.0f) > 90.0) {
                setSmoothRotationYaw = setSmoothRotationYaw(setSmoothRotationYaw, yaw);
                smoothRotated = false;
            }
            if (Math.abs(n - pitch) > 90.0f) {
                smoothRotatePitch = true;
                smoothRotated = false;
                smoothPitch = n;
                if (n > pitch) {
                    n -= (n - pitch) / 2.0f;
                } else {
                    n += (pitch - n) / 2.0f;
                }
            }
        }
        yaw = setSmoothRotationYaw;
        pitch = n;
        shouldRotate = true;
        return !smoothRotatePitch && !smoothRotateYaw;
    }

    public float setSmoothRotationYaw(float smoothYaw, float n) {
        smoothRotateYaw = true;
        final int n2 = 0;
        addedOriginYaw = n2;
        addedInputYaw = n2;
        while (smoothYaw + 180.0f < 0.0f) {
            smoothYaw += 360.0f;
            ++addedInputYaw;
        }
        while (smoothYaw + 180.0f > 360.0f) {
            smoothYaw -= 360.0f;
            --addedInputYaw;
        }
        while (n + 180.0f < 0.0f) {
            n += 360.0f;
            ++addedOriginYaw;
        }
        while (n + 180.0f > 360.0f) {
            n -= 360.0f;
            --addedOriginYaw;
        }
        smoothYaw += 180.0f;
        n += 180.0f;
        final double n3 = n - smoothYaw;
        if (n3 >= -180.0 && n3 >= 180.0) {
            smoothYaw -= (float) (n3 / 2.0);
        } else {
            smoothYaw += (float) (n3 / 2.0);
        }
        smoothYaw -= 180.0f;
        if (addedInputYaw > 0) {
            for (int i = 0; i < addedInputYaw; ++i) {
                smoothYaw -= 360.0f;
            }
        } else if (addedInputYaw < 0) {
            for (int j = 0; j > addedInputYaw; --j) {
                smoothYaw += 360.0f;
            }
        }
        return smoothYaw;
    }

    public static double calculateDirectionDifference(double n, double n2) {
        while (n < 0.0) {
            n += 360.0;
        }
        while (n > 360.0) {
            n -= 360.0;
        }
        while (n2 < 0.0) {
            n2 += 360.0;
        }
        while (n2 > 360.0) {
            n2 -= 360.0;
        }
        final double n3 = Math.abs(n2 - n) % 360.0;
        return (n3 > 180.0) ? (360.0 - n3) : n3;
    }

    private void resetRotation() {
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    @Override
    public void onDisable() {
        if(target != null)
            target = null;
        resetRotation();
    }

    public enum RenderMode{
        Circle,Box
    }
}
