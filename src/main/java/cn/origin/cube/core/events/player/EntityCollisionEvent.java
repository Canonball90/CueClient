package cn.origin.cube.core.events.player;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EntityCollisionEvent extends Event {

    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airborne;

    public EntityCollisionEvent(Entity entity, double x, double y, double z, boolean airborne) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airborne = airborne;
    }

}
