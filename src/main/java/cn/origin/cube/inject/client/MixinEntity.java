package cn.origin.cube.inject.client;

import cn.origin.cube.core.events.player.EntityCollisionEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraftforge.common.MinecraftForge;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    private int entityId;

    @Shadow
    protected boolean isInWeb;

    @Shadow
    public void move(MoverType type, double x, double y, double z) {}

    @Shadow
    public double motionX;

    @Shadow
    public double motionY;

    @Shadow
    public double motionZ;

    @Shadow
    public abstract boolean equals(Object paramObject);

    @Shadow public abstract int getEntityId();


    @Redirect(method={"applyEntityCollision"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(Entity entity, double x, double y, double z) {
        EntityCollisionEvent event = new EntityCollisionEvent(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            entity.motionX += event.x;
            entity.motionY += event.y;
            entity.motionZ += event.z;
            entity.isAirBorne = event.airborne;
        }
    }
}

