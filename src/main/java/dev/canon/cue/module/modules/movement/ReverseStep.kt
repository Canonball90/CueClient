package dev.canon.cue.module.modules.movement

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.settings.DoubleSetting
import dev.canon.cue.settings.IntegerSetting

@ModuleInfo(
    name = "ReverseStep",
    descriptions = "Reverse",
    category = Category.MOVEMENT
)
class ReverseStep : Module() {

    private var power: DoubleSetting = registerSetting("Power", 10.0, 1.0, 100.0)

    override fun onUpdate() {
        if (mc.player.onGround && !mc.player.isInWater && !mc.player.isInLava && !mc.player.isOnLadder && !mc.gameSettings.keyBindJump.isKeyDown) {
            mc.player.motionY = -power.value
        }
    }
}