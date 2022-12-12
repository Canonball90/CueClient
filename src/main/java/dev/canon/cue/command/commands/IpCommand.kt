package dev.canon.cue.command.commands

import dev.canon.cue.command.Command
import dev.canon.cue.command.CommandInfo
import dev.canon.cue.utils.client.ChatUtil
import net.minecraft.client.Minecraft
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

@CommandInfo(
    name = "ip",
    aliases = ["ip"],
    descriptions = "get an ip of a server",
    usage = "ip"
)
class IpCommand : Command() {

    var mc: Minecraft = Minecraft.getMinecraft()

    override fun execute(args: Array<String>) {
        if (mc.getCurrentServerData() != null) {
            val contents = StringSelection(mc.getCurrentServerData()!!.serverIP)
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(contents, null)
            ChatUtil.sendMessage("Ip Copied")
        } else {
            ChatUtil.sendMessage("Please join a server")
        }
    }
}