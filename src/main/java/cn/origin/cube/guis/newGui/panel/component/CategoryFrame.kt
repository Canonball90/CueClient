package cn.origin.cube.guis.newGui.panel.component

import cn.origin.cube.Cube
import cn.origin.cube.core.module.AbstractModule.mc
import cn.origin.cube.core.module.Category
import cn.origin.cube.guis.newGui.panel.component.button.ModuleButton
import cn.origin.cube.module.huds.WaterMark
import cn.origin.cube.module.modules.client.Colors
import cn.origin.cube.module.modules.client.NewClickGui
import cn.origin.cube.utils.render.Render2DUtil
import me.surge.animation.Animation
import me.surge.animation.ColourAnimation
import me.surge.animation.Easing
import java.awt.Color

class CategoryFrame(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var category: Category,
) {
    val modules = arrayListOf<ModuleButton>()
    var isShowModules = true
    var showingModuleCount = 0
    var opening = false
    var closing = false
    private var dragging = false
    private var x2 = 0.0f
    private var y2 = 0.0f
    private val hover = ColourAnimation(Color(33,33,33), Color(33,33,33).darker().darker(), 300f, false, Easing.QUAD_IN)
    private val one = ColourAnimation(Color(0,0,0,0), Color.WHITE, 500f, false, Easing.QUAD_IN)
    private val two = ColourAnimation(Color(0,0,0,0), Color.WHITE, 500f, false, Easing.QUAD_IN)
    val expanded = Animation({ 1500F }, true, { Easing.LINEAR })
    var colorOffset: Double =
        Math.abs(System.currentTimeMillis() / 20.0) / 50 + 50 / (mc.fontRenderer.FONT_HEIGHT + width * 14f + 50.0)
    var color = getGradientOffset1(Color(0x001762FF), Color(0x0081FF), colorOffset, 255)

    init {
        for (module in Cube.moduleManager!!.getModulesByCategory(this.category).sortedBy { it.name }) {
            modules.add(ModuleButton(this.width, this.height * 0.85f, this, module))
        }
    }

    fun onDraw(mouseX: Int, mouseY: Int) {
        if (this.dragging) {
            this.x = this.x2 + mouseX
            this.y = this.y2 + mouseY
        }
        hover.state = isHoveredCategoryTab(mouseX,mouseY)
        Render2DUtil.drawRect(x, y, width, height, hover.getColour().rgb)
        Render2DUtil.drawLine(x.toDouble(),y.toDouble() + height.toDouble() - 1,x.toDouble() + width.toDouble(),y.toDouble() + height.toDouble() - 1, 2F, Colors.getClickGuiColor())

        Cube.fontManager!!.IconFont.drawStringWithShadow(
            category.icon,
            x + 3,
            y + (height / 2) - (Cube.fontManager!!.IconFont.height / 4),
            Color.WHITE.rgb
        )

        Cube.fontManager!!.CustomFont.drawStringWithShadow(
            category.getName(),
            x + 5 + Cube.fontManager!!.IconFont.getStringWidth(category.icon),
            y + (height / 2) - (Cube.fontManager!!.CustomFont.height / 4),
            Color.WHITE.rgb
        )
        if (opening)
        {
            showingModuleCount++;
            if (showingModuleCount == modules.size)
            {
                opening = false;
                isShowModules = true;
            }
        }

        if (closing)
        {
            showingModuleCount--;
            if (showingModuleCount == 0)
            {
                closing = false;
                isShowModules = false;
            }
        }
        if (modules.isEmpty() || !isShowModules) return
        var calcYPos = this.y + this.height
        for (moduleButton in modules) {
            moduleButton.drawButton(this.x, calcYPos, mouseX, mouseY)
            calcYPos += moduleButton.height * expanded.getAnimationFactor().toFloat()
            if (moduleButton.isShowSettings && moduleButton.settings.isNotEmpty()) {
                var buttonX: Double = (moduleButton.x + moduleButton.height).toDouble()
                for (settingButton in moduleButton.settings.filter { it.value.isVisible }) {
                    settingButton.drawButton(x + (this.width - settingButton.width) / 2, calcYPos, mouseX, mouseY)
                    calcYPos += settingButton.height
                    buttonX = (x + (this.width - settingButton.width) / 2).toDouble()
                }
                Render2DUtil.drawLine(
                    buttonX,
                    (moduleButton.y + moduleButton.height).toDouble(),
                    buttonX,
                    calcYPos.toDouble(),
                    0.75f,
                    Colors.getClickGuiColor()
                )
            }
        }

    }

    fun getCenter():Float{
        return width / 2
    }

    fun getGradientOffset1(color1: Color, color2: Color, offset: Double, alpha: Int): Color? {
        var offset = offset
        if (offset > 1) {
            val left = offset % 1
            val off = offset.toInt()
            offset = if (off % 2 == 0) left else 1 - left
        }
        val inverse_percent = 1 - offset
        val redPart = (color1.red * inverse_percent + color2.red * offset).toInt()
        val greenPart = (color1.green * inverse_percent + color2.green * offset).toInt()
        val bluePart = (color1.blue * inverse_percent + color2.blue * offset).toInt()
        return Color(redPart, greenPart, bluePart, alpha)
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0 && this.isHoveredCategoryTab(mouseX, mouseY)) {
            this.x2 = this.x - mouseX
            this.y2 = this.y - mouseY
            this.dragging = true
        }

        if (isHoveredCategoryTab(mouseX, mouseY) && mouseButton == 1) {
            expanded.state = !expanded.state
            return
        }

        if(expanded.getAnimationFactor() > 0) {
            modules.forEach { it.mouseClicked(mouseX, mouseY, mouseButton) }
        }
    }

    fun mouseReleased(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0) {
            this.dragging = false
        }
        if (mouseButton == 1 && this.isHoveredCategoryTab(mouseX, mouseY)) {
            this.isShowModules = !this.isShowModules
        }
        if (expanded.getAnimationFactor() > 0) {
            modules.forEach { it.mouseReleased(mouseX, mouseY, mouseButton) }
        }
    }

    private fun isHoveredCategoryTab(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        if (expanded.getAnimationFactor() > 0) {
            modules.forEach { it.keyTyped(typedChar, keyCode) }
        }
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