package cn.origin.cube.module.modules.world

import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo
import cn.origin.cube.settings.IntegerSetting
import cn.origin.cube.utils.Timer

@ModuleInfo(name = "AutoRacist", descriptions = "", category = Category.WORLD)
class AutoRacist: Module() {
    val delay: IntegerSetting = registerSetting("Delay", 3, 0, 5);
    var timer: Timer = Timer()
    var messageCount = 1

    override fun onUpdate() {
        if (messageCount == 1) {
            mc.player.sendChatMessage("Fuck All Black Niggas.")
            ++messageCount
            timer.reset()
            return
        }
        if (messageCount == 2 && timer.passedS(delay.value.toDouble())) {
            mc.player.sendChatMessage("God Made Black Niggas To Serve White People.")
            ++messageCount
            timer.reset()
            return
        }
        if (messageCount == 3 && timer.passedS(delay.value.toDouble())) {
            mc.player.sendChatMessage("I Middle Finger Every Black Nigga I See.")
            ++messageCount
            timer.reset()
            return
        }
        if (messageCount == 4 && timer.passedS(delay.value.toDouble())) {
            mc.player.sendChatMessage("White People Rule The World.")
            ++messageCount
            timer.reset()
            return
        }
        if (messageCount == 5 && timer.passedS(delay.value.toDouble())) {
            mc.player.sendChatMessage("All Black Niggas Have The Brain Of Monkeys.")
            ++messageCount
            timer.reset()
            return
        }
        if (messageCount == 6 && timer.passedS(delay.value.toDouble())) {
            mc.player.sendChatMessage("All Black Niggas Go To Hell")
            messageCount = 0
            ++messageCount
            timer.reset()
            return
        }
    }
}