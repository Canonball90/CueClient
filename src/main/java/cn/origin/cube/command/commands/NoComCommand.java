package cn.origin.cube.command.commands;

import cn.origin.cube.command.Command;
import cn.origin.cube.module.interfaces.CommandInfo;
import cn.origin.cube.module.modules.function.NocomMaster;
import cn.origin.cube.utils.client.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

@CommandInfo(name = "nocom",aliases = {"nc", "com"},descriptions = "get an ip of a server",usage = "nocom")
public class NoComCommand extends Command {
    Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void execute(String[] args) {
        int x, z;
        boolean listen;
        try {
            x = Integer.parseInt(args[0]);
            z = Integer.parseInt(args[1]);
        } catch (Exception e) {
            return;
        }

        try {
            listen = Boolean.parseBoolean(args[2]);
        } catch (Exception e) {
            listen = false;
        }

        if (! listen) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(x, 0, z), EnumFacing.UP));
            ChatUtil.sendMessage("Touched X:" + x + " Y:0 Z:" + z);
        }
        if (listen) {
            int finalX = x;
            int finalZ = z;
            new Thread(() -> {
                NocomMaster.INSTANCE.enable();
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(finalX, 0, finalZ), EnumFacing.UP));
                ChatUtil.sendMessage("Touched X:" + finalX + " Y:0 Z:" + finalZ);
                try {
                    Thread.sleep(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime() * 2L + 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NocomMaster.INSTANCE.disable();
            }, "NoCom-Touch").start();
        }
    }
}
