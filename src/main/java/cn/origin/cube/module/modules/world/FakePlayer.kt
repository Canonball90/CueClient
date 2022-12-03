package cn.origin.cube.module.modules.world

import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo
import cn.origin.cube.settings.StringSetting
import cn.origin.cube.utils.client.ChatUtil
import com.mojang.authlib.GameProfile
import net.minecraft.client.entity.EntityOtherPlayerMP
import java.util.*

@ModuleInfo(name = "FakePlayer",
    descriptions = "Spawn other player",
    category = Category.WORLD)
class FakePlayer:Module() {
    val namel: StringSetting = registerSetting("name", "FakePlayer")
    private var otherPlayer: EntityOtherPlayerMP? = null

    override fun onEnable() {
        if (fullNullCheck()) {
            disable()
            return
        }
        otherPlayer = null
        if (mc.player != null) {
            otherPlayer = EntityOtherPlayerMP(mc.world, GameProfile(UUID.randomUUID(), namel.value))
            ChatUtil.sendMessage(ChatUtil.AQUA + ("%s has been spawned." + " " + namel.value))
            otherPlayer!!.copyLocationAndAnglesFrom(mc.player)
            otherPlayer!!.rotationYawHead = mc.player.rotationYawHead
            mc.world.addEntityToWorld(-100, otherPlayer)
        }
    }

    override fun onDisable() {
        if (mc.world != null && mc.player != null) {
            super.onDisable()
            if (otherPlayer == null) return
            mc.world.removeEntity(otherPlayer)
        }
    }
}