package dev.canon.cue.module.modules.function;

import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.utils.client.ChatUtil;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ModuleInfo(name = "PearlAlert", descriptions = "Prevents you from taking fall damage", category = Category.FUNCTION)
public class PearlAlert extends Module {
    ConcurrentHashMap<UUID, Integer> uuidMap = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) return;
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderPearl) {
                EntityPlayer closest = null;
                for (EntityPlayer p : mc.world.playerEntities) {
                    if (closest == null || entity.getDistance(p) < entity.getDistance(closest)) {
                        closest = p;
                    }
                }
                if (closest != null && closest.getDistance(entity) < 2 && !uuidMap.containsKey(entity.getUniqueID()) && !closest.getName().equalsIgnoreCase(mc.player.getName())) {
                    uuidMap.put(entity.getUniqueID(), 200);
                    ChatUtil.sendColoredMessage(closest.getName() + " threw a pearl towards " + getTitle(entity.getHorizontalFacing().getName()) + "!");
                }
            }
        }
        this.uuidMap.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.uuidMap.remove(name);
            } else {
                this.uuidMap.put(name, timeout - 1);
            }
        });
    }

    public String getTitle(String in) {
        if (in.equalsIgnoreCase("west")) {
            return "east";
        } else if (in.equalsIgnoreCase("east")) {
            return "west";
        } else {
            return in;
        }
    }
}
