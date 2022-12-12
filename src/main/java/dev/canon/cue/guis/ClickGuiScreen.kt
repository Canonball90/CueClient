package dev.canon.cue.guis

import dev.canon.cue.Cue
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Mouse
import java.awt.Color


class ClickGuiScreen : GuiScreen() {
    val panels = arrayListOf<CategoryPanel>()
    private var particleSystem: dev.canon.cue.utils.render.particle.ParticleSystem? = null

    init {
        var x = 20.0f
        for (category in dev.canon.cue.module.Category.values().asList().stream()
            .filter { !it.isHud }) {
            panels.add(CategoryPanel(x, 35.0f, 105.0f, 20.0f, category))
            x += 110
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.background.value) {
            if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.particles.value) {
                if (mc.currentScreen != null) {
                    this.particleSystem?.render(mouseX, mouseY)
                }
            } else {
                this.particleSystem =
                    dev.canon.cue.utils.render.particle.ParticleSystem(ScaledResolution(this.mc))
            }
            if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.gradient.value) {

            } else {
                drawDefaultBackground()
            }
        }
//        GL11.glPushMatrix()
//        GL11.glScaled(3.5, 3.5, 0.0)
//        mc.currentScreen?.let { Cube.fontManager.CustomFont.drawString("CueClient", 0, it.height - 5, ClickGui.getCurrentColor().rgb) }
//        GL11.glPopMatrix()
        checkDWHeel()
        for (panel in panels) {
            panel.onDraw(mouseX, mouseY)
        }
        panels.forEach {
            for (moduleButton in it.modules) {
                if (moduleButton.isHoveredButton(mouseX, mouseY)) {
                    dev.canon.cue.utils.render.Render2DUtil.drawRect(
                        mouseX + 1.2f,
                        mouseY + 0.3f,
                        Cue.fontManager.CustomFont.getStringWidth(moduleButton.father.descriptions) + 5f,
                        Cue.fontManager.CustomFont.fontHeight + 0.5f,
                        Color(0, 0, 0, 115).rgb
                    )
                    dev.canon.cue.utils.render.Render2DUtil.drawOutlineRect(
                        mouseX + 1.2,
                        mouseY + 0.3,
                        Cue.fontManager.CustomFont.getStringWidth(moduleButton.father.descriptions) + 5.0,
                        Cue.fontManager.CustomFont.fontHeight + 0.5,
                        1.0f,
                        dev.canon.cue.module.modules.client.ClickGui.getCurrentColor()
                    )
                    Cue.fontManager.CustomFont.drawStringWithShadow(
                        moduleButton.father.descriptions,
                        mouseX + 7f,
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
            dev.canon.cue.module.modules.client.ClickGui.INSTANCE.disable()
        }
        panels.forEach { it.keyTyped(typedChar, keyCode) }
    }

    override fun onGuiClosed() {
        if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.isEnabled) {
            dev.canon.cue.module.modules.client.ClickGui.INSTANCE.disable()
        }
        Cue.configManager.saveAll()
    }

    override fun updateScreen() {
        particleSystem?.update()
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}