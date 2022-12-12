package dev.canon.cue.module.modules.function.scaffold;

import dev.canon.cue.module.*;
import dev.canon.cue.settings.BooleanSetting;
import dev.canon.cue.settings.DoubleSetting;
import dev.canon.cue.utils.Timer;
import dev.canon.cue.utils.client.MathUtil;
import dev.canon.cue.utils.player.BlockUtil;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Scaffold", descriptions = "Automatically places blocks under you", category = Category.FUNCTION)
public class Scaffold extends Module {

    BooleanSetting rotate = registerSetting("Rotate", true);
    BooleanSetting swing = registerSetting("Swing", true);
    BooleanSetting Switch = registerSetting("Switch", true);
    BooleanSetting Tower = registerSetting("Tower", true);
    BooleanSetting center = registerSetting("Center", true);
    DoubleSetting speed = registerSetting("Speed", 0.7, 0.0, 1.0);

    private final List<ScaffoldBlock> blocksToRender = new ArrayList<ScaffoldBlock>();
    private boolean teleported;
    private BlockPos pos;
    private final boolean packet = false;
    Timer time = new Timer();

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (this.isAir(this.pos)) {//ToDo add , this.mc.player.isSneaking() later
            BlockUtil.placeBlock(this.pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet);
            this.blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(this.pos)));
        }
        if (this.swing.getValue().booleanValue()) {
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }
        if (mc.player == null || mc.world == null) {
            return;
        }
        double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        mc.player.motionX = calc[0];
        mc.player.motionZ = calc[1];
        if (this.Switch.getValue().booleanValue() && (mc.player.getHeldItemMainhand().getItem() == null || !(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock))) {
            for (int j = 0; j < 9; ++j) {
                if (mc.player.inventory.getStackInSlot(j) == null || mc.player.inventory.getStackInSlot(j).getCount() == 0 || !(mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock))
                    continue;
                mc.player.inventory.currentItem = j;
                break;
            }
        }
        if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F && Tower.getValue() && !mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            if (!this.teleported && center.getValue()) {
                this.teleported = true;
                BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                mc.player.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            }
            if (center.getValue() && !this.teleported)
                return;
            mc.player.motionY = 0.41999998688697815D;
            mc.player.motionZ = 0.0D;
            mc.player.motionX = 0.0D;
            if (this.time.passed(1500L)) {
                //ToDo add timer manager
                time.reset();
                mc.player.motionY = -0.28D;
            }
        }
    }

    private boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.blocksToRender.clear();
    }
}
