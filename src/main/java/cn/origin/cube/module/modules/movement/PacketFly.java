package cn.origin.cube.module.modules.movement;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.player.MotionEvent;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.*;
import cn.origin.cube.inject.client.INetworkManager;
import cn.origin.cube.inject.client.ISPacketPlayerPosLook;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

@Constant(constant = false)
@ModuleInfo(name = "PacketFly",
    descriptions = "Fly",
    category = Category.MOVEMENT)
public class PacketFly extends Module {


}
