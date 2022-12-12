package dev.canon.cue.command.commands

import dev.canon.cue.Cue

@dev.canon.cue.command.CommandInfo(
    name = "prefix",
    descriptions = "Change command prefix",
    usage = "prefix <char>"
)
class PreFixCommand : dev.canon.cue.command.Command() {
    override fun execute(args: Array<String>) {
        if (args.isEmpty()) {
            dev.canon.cue.utils.client.ChatUtil.sendMessage("&c&lUsage: prefix <char>")
            return
        }
        Cue.commandPrefix = args[0]
        Cue.configManager.saveCommand()
        dev.canon.cue.utils.client.ChatUtil.sendNoSpamMessage("&aPrefix set to ${args[0]}")
    }
}