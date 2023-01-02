package cn.origin.cube.module.modules.world;

import cn.origin.cube.core.events.world.PlayerDamageBlockEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.module.interfaces.Para;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Para(para = Para.ParaMode.Full)
@ModuleInfo(name = "PacketMine", descriptions = "PacketMine", category = Category.WORLD)
public class PacketMine extends Module {

    public static long packetMineStartTime = 0L;

    ModeSetting<Page> page = registerSetting("Page", Page.Mine);

    ModeSetting<Swap> swap = registerSetting("Swap", Swap.Normal);
    ModeSetting<Mode> mode = registerSetting("Mode", Mode.InstantMine);
    BooleanSetting oppositeFaceHit = registerSetting("OppositeFaceHit", false);
    BooleanSetting spamPackets = registerSetting("SpamPackets", false);
    BooleanSetting antiNeededBlocks = registerSetting("NoNeededBlocks", false);
    IntegerSetting instantMineDelay = registerSetting("InstantMineDelay",70, 0, 1000);
    FloatSetting range = registerSetting("Range", 8.0f, 1.0f, 10.0f);
    BooleanSetting rotate = registerSetting("Rotate", false);

    public Block[] neededBlocks = {Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST, Blocks.CHEST};
    public BlockPos currentBlock = null;

    @SubscribeEvent
    public void onDamageBlock(PlayerDamageBlockEvent event) {
        for (Block block : neededBlocks) {
            if (!antiNeededBlocks.getValue()) break;
            if (mc.world.getBlockState(event.getPos()).getBlock().equals(block)) {
                return;
            }
        }
        mineBlock(currentBlock, EnumFacing.UP, true);
        if(oppositeFaceHit.getValue()) event.setCanceled(true);
        if(spamPackets.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
            currentBlock = event.getPos();
        }
        if(mode.getValue().equals(Mode.InstantMine)){
            mc.playerController.isHittingBlock = false;
            mc.playerController.blockHitDelay = 0;
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentBlock, EnumFacing.UP));
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (fullNullCheck()) return;
        if (currentBlock == null) return;
        if (mc.world.getBlockState(currentBlock).getBlock().equals(Blocks.AIR)) currentBlock = null;

    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (fullNullCheck()) return;
        if (currentBlock != null) {
            Render3DUtil.drawBlockBox(currentBlock, Colors.getGlobalColor(), true, 3F);
        }
    }

    public static void mineBlock(BlockPos pos, EnumFacing face, boolean packetMine) {
        if (packetMine) {
            packetMineStartTime = System.currentTimeMillis();
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, face));
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        else if (mc.playerController.onPlayerDamageBlock(pos, face)) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    enum Page {
        Mine,
        Render
    }

    enum Mode {
        PacketMine,
        InstantMine
    }

    enum Swap {
        None,
        Normal,
        Silent
    }

    enum ScaleMode {
        Expand,
        Shrink,
        None
    }
}
