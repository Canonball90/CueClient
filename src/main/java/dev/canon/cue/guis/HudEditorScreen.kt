package dev.canon.cue.guis

import dev.canon.cue.Cue
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Mouse
import java.awt.Color

class HudEditorScreen : GuiScreen() {
    val panels = arrayListOf<CategoryPanel>()

    init {
        var x = 20.0f
        for (category in dev.canon.cue.module.Category.values().asList().stream()
            .filter { it.isHud }) {
            panels.add(
                CategoryPanel(
                    x,
                    35.0f,
                    105.0f,
                    20.0f,
                    category
                )
            )
            x += 110
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        checkDWHeel()
        for (panel in panels) {
            panel.onDraw(mouseX, mouseY)
        }
        panels.forEach {
            for (moduleButton in it.modules) {
                if (moduleButton.isHoveredButton(mouseX, mouseY)) {
                    dev.canon.cue.utils.render.Render2DUtil.drawRect(
                        mouseX + 0.3f,
                        mouseY + 0.3f,
                        Cue.fontManager.CustomFont.getStringWidth(moduleButton.father.descriptions) + 2f,
                        Cue.fontManager.CustomFont.fontHeight + 0.3f,
                        Color(0, 0, 0, 115).rgb
                    )
                    dev.canon.cue.utils.render.Render2DUtil.drawOutlineRect(
                        mouseX + 0.3,
                        mouseY + 0.3,
                        Cue.fontManager.CustomFont.getStringWidth(moduleButton.father.descriptions) + 2.0,
                        Cue.fontManager.CustomFont.fontHeight + 0.3,
                        1.0f,
                        Color(0, 0, 0, 115)
                    )
                    Cue.fontManager.CustomFont.drawStringWithShadow(
                        moduleButton.father.descriptions,
                        mouseX + 1.5f,
                        mouseY + 1.5f,
                        Color.WHITE.rgb
                    )
                }
            }
        }
    }

    private fun checkDWHeel() {
        val dWheel: Int = Mouse.getDWheel()
        if (dWheel < 0) {
            panels.forEach { it.y -= 10f }
        } else if (dWheel > 0) {
            panels.forEach { it.y += 10f }
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mousebutton: Int) {
        panels.forEach { it.mouseClicked(mouseX, mouseY, mousebutton) }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, mousebutton: Int) {
        panels.forEach { it.mouseReleased(mouseX, mouseY, mousebutton) }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == 1) {
            dev.canon.cue.module.modules.client.HudEditor.INSTANCE.disable()
        }
        panels.forEach { it.keyTyped(typedChar, keyCode) }
    }

    override fun onGuiClosed() {
        if (dev.canon.cue.module.modules.client.HudEditor.INSTANCE.isEnabled) {
            dev.canon.cue.module.modules.client.HudEditor.INSTANCE.disable()
        }
        Cue.configManager.saveAll()
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}