package cn.origin.cube.core.events.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DeathEvent extends Event {

    EntityPlayer player;

    public DeathEvent(EntityPlayer player){
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
