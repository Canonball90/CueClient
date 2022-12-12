package dev.canon.cue.module.modules.visual;

import dev.canon.cue.event.events.world.Render3DEvent;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.utils.render.RenderUtil;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "ChunkBorders",
        descriptions = "ChunkBorders",
        category = Category.VISUAL)
public class ChunkBorders extends Module {

    @Override
    public void onRender3D(Render3DEvent event) {
        if (fullNullCheck()) return;
        final BlockPos from = new BlockPos(ChunkBorders.mc.player.chunkCoordX * 16, 0, ChunkBorders.mc.player.chunkCoordZ * 16);
        final BlockPos to = new BlockPos(from.getX() + 16, 256, from.getZ() + 16);
        final AxisAlignedBB box = new AxisAlignedBB(from, to).offset(-ChunkBorders.mc.getRenderManager().viewerPosX, -ChunkBorders.mc.getRenderManager().viewerPosY, -ChunkBorders.mc.getRenderManager().viewerPosZ);
        RenderUtil.prepare();
        RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1.0f, 0.0f, 0.0f, 1.0f);
        RenderUtil.release();
        super.onRender3D(event);
    }
}
