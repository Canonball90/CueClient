package dev.canon.cue.guis

import dev.canon.cue.Cue
import dev.canon.cue.guis.buttons.ModuleButton
import java.awt.Color
import java.util.*

class CategoryPanel(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var category: dev.canon.cue.module.Category,
) {
    val modules = arrayListOf<ModuleButton>()
    var isShowModules = true
    var showingModuleCount = 0
    var opening = false
    var closing = false
    private var dragging = false
    private var x2 = 0.0f
    private var y2 = 0.0f

    init {
        for (module in Cue.moduleManager!!.getModulesByCategory(this.category).sortedBy { it.name }) {
            modules.add(ModuleButton(this.width, this.height * 0.85f, this, module))
        }
    }

    fun onDraw(mouseX: Int, mouseY: Int) {
        if (this.dragging) {
            this.x = this.x2 + mouseX
            this.y = this.y2 + mouseY
        }
        dev.canon.cue.utils.render.Render2DUtil.drawRect(
            x - 1,
            y,
            width + 2,
            height,
            if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.gay.value) category.color.rgb else dev.canon.cue.module.modules.client.ClickGui.getCurrentColor().rgb
        )
        Cue.fontManager!!.IconFont.drawStringWithShadow(
            category.icon,
            x + 3,
            y + (height / 2) - (Cue.fontManager!!.IconFont.height / 4),
            Color.WHITE.rgb
        )
        Cue.fontManager!!.CustomFont.drawStringWithShadow(
            category.getName(),
            x + 5 + Cue.fontManager!!.IconFont.getStringWidth(category.icon),
            y + (height / 2) - (Cue.fontManager!!.CustomFont.height / 4),
            Color.WHITE.rgb
        )
        if (isShowModules) {
            Cue.fontManager!!.IconFont.drawStringWithShadow(
                "I",
                x + width - 15,
                y + (height / 2) - (Cue.fontManager!!.CustomFont.height / 4),
                Color.WHITE.rgb
            )
        } else {
            Cue.fontManager!!.IconFont.drawStringWithShadow(
                "G",
                x + width - 15,
                y + (height / 2) - (Cue.fontManager!!.CustomFont.height / 4),
                Color.WHITE.rgb
            )
        }
        if (opening) {
            showingModuleCount++
            if (showingModuleCount == modules.size) {
                opening = false
                isShowModules = true
            }
        }

        if (closing) {
            showingModuleCount--
            if (showingModuleCount == 0) {
                closing = false
                isShowModules = false
            }
        }
        if (modules.isEmpty() || !isShowModules) return
        var calcYPos = this.y + this.height
        for (moduleButton in modules) {
            moduleButton.drawButton(this.x, calcYPos, mouseX, mouseY)
            calcYPos += moduleButton.height
            if (moduleButton.isShowSettings && moduleButton.settings.isNotEmpty()) {
                var buttonX: Double = (moduleButton.x + moduleButton.height).toDouble()
                for (settingButton in moduleButton.settings.filter { it.value.isVisible }) {
                    settingButton.drawButton(x + (this.width - settingButton.width) / 2, calcYPos, mouseX, mouseY)
                    calcYPos += settingButton.height
                    buttonX = (x + (this.width - settingButton.width) / 2).toDouble()
                }
                dev.canon.cue.utils.render.Render2DUtil.drawLine(
                    buttonX,
                    (moduleButton.y + moduleButton.height).toDouble(),
                    buttonX,
                    calcYPos.toDouble(),
                    0.75f,
                    if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.gay.value)
                        moduleButton.father.category.color
                    else
                        dev.canon.cue.module.modules.client.ClickGui.getCurrentColor()
                )
            }
        }

    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0 && this.isHoveredCategoryTab(mouseX, mouseY)) {
            this.x2 = this.x - mouseX
            this.y2 = this.y - mouseY
            this.dragging = true
        }
        modules.forEach { it.mouseClicked(mouseX, mouseY, mouseButton) }
    }

    fun mouseReleased(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0) {
            this.dragging = false
        }
        if (mouseButton == 1 && this.isHoveredCategoryTab(mouseX, mouseY)) {
            this.isShowModules = !this.isShowModules
        }
        modules.forEach { it.mouseReleased(mouseX, mouseY, mouseButton) }
    }

    private fun isHoveredCategoryTab(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        modules.forEach { it.keyTyped(typedChar, keyCode) }
    }

    fun isOpening(): Boolean {
        return opening
    }

    fun isClosing(): Boolean {
        return closing
    }

    fun processRightClick() {
        if (!isShowModules) {
            showingModuleCount = 0
            opening = true
        } else {
            showingModuleCount = modules.size
            closing = true
        }
    }
}