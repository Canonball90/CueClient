package cn.origin.cube.guis.newGui.panel

import cn.origin.cube.Cube
import cn.origin.cube.core.module.Category
import cn.origin.cube.guis.newGui.panel.component.CategoryFrame
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.module.modules.client.NewClickGui
import cn.origin.cube.utils.render.Render2DUtil
import cn.origin.cube.utils.render.particle.ParticleSystem
import me.surge.animation.ColourAnimation
import me.surge.animation.Easing
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Mouse
import java.awt.Color

class NewGui : GuiScreen(){
    val panels = arrayListOf<CategoryFrame>()
    private var particleSystem: ParticleSystem? = null
    private val hover = ColourAnimation(Color(0, 0, 0, 0), Color(0, 0, 0, 115), 300f, false, Easing.LINEAR)
    private val hover1 = ColourAnimation(Color(0, 0, 0, 0), NewClickGui.getCurrentColor(), 300f, false, Easing.LINEAR)

    init {
        var x = 20.0f
        for (category in Category.values().asList().stream().filter { !it.isHud }) {
            panels.add(CategoryFrame(x, 35.0f, 105.0f, 20.0f, category))
            x += 110
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if(NewClickGui.INSTANCE.background.value) {
            if(NewClickGui.INSTANCE.particles.value){
                if(mc.currentScreen != null) {
                    this.particleSystem?.render(mouseX, mouseY);
                }
            }
            else {
                this.particleSystem = ParticleSystem(ScaledResolution(this.mc));
            }
            if(NewClickGui.INSTANCE.gradient.value){

            }else {
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
                    Cube.fontManager.CustomFont.drawStringWithShadow(
                        moduleButton.father.descriptions,
                        moduleButton.panelFather.x,
                        moduleButton.panelFather.y - 7,
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
            NewClickGui.INSTANCE.disable()
        }
        panels.forEach { it.keyTyped(typedChar, keyCode) }
    }

    override fun onGuiClosed() {
        if (NewClickGui.INSTANCE.isEnabled) {
            NewClickGui.INSTANCE.disable()
        }
        Cube.configManager.saveAll()
    }

    override fun updateScreen() {
        particleSystem?.update()
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}