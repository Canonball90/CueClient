package dev.canon.cue.command.commands

import dev.canon.cue.Cue
import dev.canon.cue.settings.BindSetting
import org.lwjgl.input.Keyboard
import java.util.*


@dev.canon.cue.command.CommandInfo(
    name = "bind",
    aliases = ["KeyBind"],
    descriptions = "Bind module to key",
    usage = "bind <module> <key>"
)
class ModuleBindCommand : dev.canon.cue.command.Command() {
    override fun execute(args: Array<String>) {
        if (args.size == 1) {
            dev.canon.cue.utils.client.ChatUtil.sendNoSpamMessage("&cPlease specify a module.")
            return
        }
        try {
            val module = args[0]
            val rKey = args[1]
            val m: dev.canon.cue.module.AbstractModule? = Cue.moduleManager.getModuleByName(module)
            if (m == null) {
                dev.canon.cue.utils.client.ChatUtil.sendNoSpamMessage("Unknown module '$module'!")
                return
            }
            val key = Keyboard.getKeyIndex(rKey.uppercase())
            if (Keyboard.KEY_NONE == key) {
                dev.canon.cue.utils.client.ChatUtil.sendMessage("&cUnknown Key $rKey")
                return
            }
            m.keyBind.value = BindSetting.KeyBind(key)
            dev.canon.cue.utils.client.ChatUtil.sendMessage("&aSuccess bind ${m.name} to key: ${args[1]}")
        } catch (e: Exception) {
            dev.canon.cue.utils.client.ChatUtil.sendMessage("&c&lUsage: bind <module> <bind>")
        }
    }
}