package dev.canon.cue.module.modules.world

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.settings.BooleanSetting
import dev.canon.cue.settings.IntegerSetting
import dev.canon.cue.settings.ModeSetting
import net.minecraft.init.Items
import net.minecraft.item.ItemBlock

@ModuleInfo(
    name = "FastPlace",
    descriptions = "FastPlace",
    category = Category.WORLD
)
class FastPlace : Module() {
    private var speed: IntegerSetting = registerSetting("Speed", 4, 0, 4)
    private var ghostFix: BooleanSetting = registerSetting("GhostFix", true)
    private var blocks: BooleanSetting = registerSetting("Block", true)
    private var exp: BooleanSetting = registerSetting("Exp", true)
    private var fireworks: BooleanSetting = registerSetting("FireWorks", true)
    private var crystals: BooleanSetting = registerSetting("Crystals", true)

    override fun onUpdate() {
        val holding = mc.player.heldItemMainhand.item
        if (blocks.value && holding is ItemBlock
            || exp.value && holding == Items.EXPERIENCE_BOTTLE
            || fireworks.value && holding == Items.FIREWORKS
            || crystals.value && holding == Items.END_CRYSTAL
        ) {

            mc.rightClickDelayTimer = 4 - speed.value
        }

        if (ghostFix.value) {
            // TODO
        }
        super.onUpdate()
    }
}