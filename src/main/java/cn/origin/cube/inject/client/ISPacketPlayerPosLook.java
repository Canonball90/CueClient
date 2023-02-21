package cn.origin.cube.inject.client;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketPlayerPosLook.class)
public interface ISPacketPlayerPosLook {

    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("yaw")
    float getYaw();

    @Accessor("pitch")
    void setPitch(float pitch);

    @Accessor("pitch")
    float getPitch();

    @Accessor("x")
    void setX(double d0);

    @Accessor("y")
    void setY(double d0);

    @Accessor("z")
    void setZ(double d0);
}
