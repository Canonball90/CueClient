package cn.origin.cube.module.modules.function.scaffold;

import cn.origin.cube.core.events.player.TravelEvent;
import cn.origin.cube.core.events.player.UpdateWalkingPlayerEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.interfaces.Para;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.DoubleSetting;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.BlockUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Para(para = Para.ParaMode.Light)
@ModuleInfo(name = "Scaffold", descriptions = "Automatically places blocks under you", category = Category.FUNCTION)
public class Scaffold extends Module {

    BooleanSetting rotate = registerSetting("Rotate", true);
    BooleanSetting render = registerSetting("Render", true);
    BooleanSetting swing = registerSetting("Swing", true);
    BooleanSetting Switch = registerSetting("Switch", true);
    BooleanSetting Tower = registerSetting("Tower", true);
    BooleanSetting NCP = registerSetting("NCP", true).booleanVisible(Tower);
    BooleanSetting NCPJumo = registerSetting("NCPJump", true).booleanVisible(Tower);
    BooleanSetting center = registerSetting("Center", true);
    DoubleSetting speed = registerSetting("Speed", 0.7, 0.0, 1.0);
    DoubleSetting upSpeed = registerSetting("Up-Speed", 0.41999998688697815D, 0.0, 1.0).booleanVisible(Tower);

    private List<ScaffoldBlock> blocksToRender = new ArrayList<ScaffoldBlock>();
    private boolean teleported;
    private BlockPos pos;
    private boolean packet = false;
    Timer time = new Timer();
    private final Timer towerTimer = new Timer();

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.pos = new BlockPos(this.mc.player.posX, this.mc.player.posY - 1.0, this.mc.player.posZ);
        if(isAir(this.pos)){
            mc.player.setSneaking(true);
        }else{
            mc.player.setSneaking(false);
        }
        if (this.isAir(this.pos)) {//ToDo add , this.mc.player.isSneaking() later
            BlockUtil.placeBlock(this.pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet);
            this.blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(this.pos)));
        }
        if(render.getValue()){
            Render3DUtil.drawBoxOutline(pos, Colors.getGlobalColor(), 2);
            Render3DUtil.drawBox(pos, Colors.getGlobalColor(), 0, false, false, 255);
        }
        if (this.swing.getValue().booleanValue()) {
            this.mc.player.isSwingInProgress = false;
            this.mc.player.swingProgressInt = 0;
            this.mc.player.swingProgress = 0.0f;
            this.mc.player.prevSwingProgress = 0.0f;
        }
        if (this.mc.player == null || this.mc.world == null) {
            return;
        }
        double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        this.mc.player.motionX = calc[0];
        this.mc.player.motionZ = calc[1];
        if (this.Switch.getValue().booleanValue() && (this.mc.player.getHeldItemMainhand().getItem() == null || !(this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock))) {
            for (int j = 0; j < 9; ++j) {
                if (this.mc.player.inventory.getStackInSlot(j) == null || this.mc.player.inventory.getStackInSlot(j).getCount() == 0 || !(this.mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock)) continue;
                this.mc.player.inventory.currentItem = j;
                break;
            }
        }
        if (NCP.getValue()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                if (NCPJumo.getValue()) {
                    mc.player.jump();
                } else {
                    mc.player.motionY = 0.2;
                }

                mc.player.motionX *= 0.3;
                mc.player.motionZ *= 0.3;

                if (towerTimer.passed(1200L)) {
                    towerTimer.reset();
                    mc.player.motionY = -0.28;
                }
            }
        }else {
            if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F && Tower.getValue() && !mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                if (!this.teleported && center.getValue()) {
                    this.teleported = true;
                    BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                    mc.player.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                }
                if (center.getValue() && !this.teleported)
                    return;
                mc.player.motionY = upSpeed.getValue();
                mc.player.motionZ = 0.0D;
                mc.player.motionX = 0.0D;
                if (this.time.passed(1500L)) {
                    //ToDo add timer manager
                    time.reset();
                    mc.player.motionY = -0.28D;
                }
            }
        }
    }

    private boolean isAir(BlockPos pos) {
        return this.mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.blocksToRender.clear();
    }
}
