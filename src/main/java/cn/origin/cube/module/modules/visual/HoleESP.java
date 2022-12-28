package cn.origin.cube.module.modules.visual;

import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.utils.player.BlockUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import cn.origin.cube.utils.render.RenderBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.HashSet;

@ModuleInfo(name = "HoleESP",
        descriptions = "Shows safe holes",
        category = Category.VISUAL)
public class HoleESP extends Module {


    HashSet<BlockPos> obsidianHoles = new HashSet<>();
    HashSet<BlockPos> bedrockHoles = new HashSet<>();

    public FloatSetting holeRadius = registerSetting("Hole Radius", 8.0f, 0.0f, 20.0f);
    private final BooleanSetting pulse = registerSetting("Pulse", true);
    private final FloatSetting pulseMax = registerSetting("Pulse Max", 1.5f, -5.0f, 5).booleanVisible(pulse);
    private final FloatSetting pulseMin = registerSetting("Pulse Min", 1.0f, -5.0f, 5).booleanVisible(pulse);
    private final FloatSetting pulseSpeed = registerSetting("Pulse Speed", 4.0f, 0.0f, 5.0f).booleanVisible(pulse);
    private final FloatSetting rollingWidth = registerSetting("Pulse W", 8.0f, 0.0f, 20.0f).booleanVisible(pulse);
    private final FloatSetting height = registerSetting("height", 1.0f, -5.0f, 5.0f);
    private final ModeSetting<RenderBuilder.Box> mode = registerSetting("Mode", RenderBuilder.Box.GLOW);

    public FloatSetting obsidianOutlineWidth = registerSetting("Obsidian Outline Width", 1.0f, 0.0f, 5.0f);

    public FloatSetting bedrockOutlineWidth = registerSetting("Bedrock Outline Width", 1.0f, 0.0f, 5.0f);


    @Override
    public void onRender3D(Render3DEvent event){
        if (!obsidianHoles.isEmpty()){
                obsidianHoles.forEach(pos -> Render3DUtil.drawBox(new RenderBuilder()
                        .position(pos)
                        .color(new Color(Colors.getHoleColorOb().getRed(),Colors.getHoleColorOb().getGreen(),Colors.getHoleColorOb().getBlue(), 150))
                        .box(mode.getValue())
                        .setup()
                        .line(obsidianOutlineWidth.getValue())
                        .depth(true)
                        .height((pulse.getValue() ? getRolledHeight(4) : height.getValue()))
                        .blend()
                        .texture()
                ));
        }
        if (!bedrockHoles.isEmpty())
                bedrockHoles.forEach(pos -> Render3DUtil.drawBox(new RenderBuilder()
                        .position(pos)
                        .color(new Color(Colors.getHoleColorBr().getRed(),Colors.getHoleColorBr().getGreen(),Colors.getHoleColorBr().getBlue(), 150))
                        .box(mode.getValue())
                        .setup()
                        .line(bedrockOutlineWidth.getValue())
                        .depth(true)
                        .height((pulse.getValue() ? getRolledHeight(4) : height.getValue()))
                        .blend()
                        .texture()
                ));
        if (!obsidianHoles.isEmpty())
            obsidianHoles.clear();
        if (!bedrockHoles.isEmpty())
            bedrockHoles.clear();
        searchHoles();
    }

    public void searchHoles() {
        for (BlockPos pos : BlockUtil.getBlocksInRadius(holeRadius.getValue(), BlockUtil.AirMode.AirOnly)) {
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) {
                bedrockHoles.add(pos);
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK)) {
                obsidianHoles.add(pos);
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) {
                bedrockHoles.add(pos);
                bedrockHoles.add(pos.north());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK)) {
                obsidianHoles.add(pos);
                obsidianHoles.add(pos.north());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.BEDROCK) {
                bedrockHoles.add(pos);
                bedrockHoles.add(pos.west());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.west()).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.OBSIDIAN)) {
                obsidianHoles.add(pos);
                obsidianHoles.add(pos.west());
            }
        }
    }

    private float getRolledHeight(float offset) {
        double s = (System.currentTimeMillis() / (double)pulseSpeed.getValue()) + (offset * rollingWidth.getValue() * 100.0f);
        s %= 300.0;
        s = (150.0f * Math.sin(((s - 75.0f) * Math.PI) / 150.0f)) + 150.0f;
        return pulseMax.getValue() + ((float)s * ((pulseMin.getValue() - pulseMax.getValue()) / 300.0f));
    }
}
