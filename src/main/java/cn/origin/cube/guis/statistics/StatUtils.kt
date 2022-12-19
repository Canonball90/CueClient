package cn.origin.cube.guis.statistics

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.stats.StatList
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

object StatUtils {

    val deaths = 0
    val kills = 0

    @JvmStatic
    fun drawHead(skin: ResourceLocation?, width: Int, height: Int) {
        GL11.glColor4f(1f, 1f, 1f, 1f)
        Minecraft.getMinecraft().textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(width, height, 8f, 8f, 8, 8, 37, 37, 64f, 64f)
    }

    @JvmStatic
    fun getPlayerKills(): Int {
        return Minecraft.getMinecraft().player.statFileWriter.readStat(StatList.PLAYER_KILLS)
    }

    @JvmStatic
    fun getPlayerDeaths(): Int {
        if (Minecraft.getMinecraft().player.isDead) {
            Minecraft.getMinecraft().player.statFileWriter.increaseStat(
                Minecraft.getMinecraft().player,
                StatList.DEATHS,
                1
            )
        }
        return Minecraft.getMinecraft().player.statFileWriter.readStat(StatList.DEATHS)
    }

    @JvmStatic
    fun getPlayerKD(): Double {
        return getPlayerKills().toDouble() / getPlayerDeaths().toDouble()
    }
}