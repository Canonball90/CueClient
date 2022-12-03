package cn.origin.cube.module.modules.function.scaffold;

import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;
import cn.origin.cube.settings.BooleanSetting;
import cn.origin.cube.settings.DoubleSetting;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.BlockUtil;
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
    DoubleSetting speed = registerSetting("Speed", 0.7, 0.0, 1.0);

    private List<ScaffoldBlock> blocksToRender = new ArrayList<ScaffoldBlock>();
    private BlockPos pos;
    private boolean packet = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.pos = new BlockPos(this.mc.player.posX, this.mc.player.posY - 1.0, this.mc.player.posZ);
        if (this.isAir(this.pos)) {//ToDo add , this.mc.player.isSneaking() later
            BlockUtil.placeBlock(this.pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet);
            this.blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(this.pos)));
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
        if (this.Tower.getValue().booleanValue() && this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.player.moveForward == 0.0f && this.mc.player.moveStrafing == 0.0f && !this.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.mc.player.motionY = 0.2444441;
            this.mc.player.motionZ = 0.0;
            this.mc.player.motionX = 0.0;
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
