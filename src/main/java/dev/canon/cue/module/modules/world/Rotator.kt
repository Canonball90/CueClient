package dev.canon.cue.module.modules.world

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.settings.IntegerSetting
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.util.math.MathHelper

@ModuleInfo(
    name = "Rotator",
    descriptions = "Rotator",
    category = Category.WORLD
)
class Rotator : Module() {

    val speed: IntegerSetting = registerSetting("Speed", 5, 0, 15)
    var yaw = 0

    override fun onUpdate() {
        if (mc.world == null || mc.player == null) return

        if (this.isEnabled) {
            yaw += speed.value
            mc.player.connection.sendPacket(
                CPacketPlayer.Rotation(
                    MathHelper.wrapDegrees(yaw).toFloat(),
                    0f,
                    mc.player.onGround
                )
            )
        }
        super.onUpdate()
    }
}