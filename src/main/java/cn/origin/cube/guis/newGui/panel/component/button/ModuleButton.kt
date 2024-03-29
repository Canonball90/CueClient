package cn.origin.cube.guis.newGui.panel.component.button

import cn.origin.cube.Cube
import cn.origin.cube.core.settings.*
import cn.origin.cube.core.module.AbstractModule
import cn.origin.cube.guis.newGui.panel.component.Button
import cn.origin.cube.guis.newGui.panel.component.CategoryFrame
import cn.origin.cube.guis.newGui.panel.component.button.setting.*
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.module.modules.client.Colors
import cn.origin.cube.module.modules.client.NewClickGui
import cn.origin.cube.utils.COG
import cn.origin.cube.utils.render.Render2DUtil
import me.surge.animation.Animation
import me.surge.animation.ColourAnimation
import me.surge.animation.Easing
import java.awt.Color

class ModuleButton(width: Float, height: Float, panel: CategoryFrame, val father: AbstractModule) :
    Button(width, height, panel) {

    var isShowSettings = false
    val settings = arrayListOf<SettingButton<*>>()
    private var x2 = 0.0f
    private var y2 = 0.0f
    private var dragging = false
    private val hover = ColourAnimation(Color(54, 54, 54), Color(54, 54, 54).darker(), 300f, false, Easing.LINEAR)
    private val pulse = ColourAnimation(Color.WHITE, Color.DARK_GRAY, 500f, false, Easing.LINEAR)

    init {
        if (father.settingList.isNotEmpty()) {
            for (setting in father.settingList) {
                if (setting is BooleanSetting) {
                    settings.add(BooleanSettingButton(width * 0.98f, height, setting, this))
                }
                if (setting is BindSetting) {
                    settings.add(BindSettingButton(width * 0.98f, height, setting, this))
                }
                if (setting is NumberSetting<*>) {
                    settings.add(NumberSliderButton(width * 0.98f, height, setting, this))
                }
                if (setting is ModeSetting<*>) {
                    settings.add(ModeSettingButton(width * 0.98f, height, setting, this))
                }
                if (setting is StringSetting) {
                    settings.add(StringSettingButton(width * 0.98f, height, setting, this))
                }
            }
        }
        if (father.commonSettings.isNotEmpty()) {
            for (setting in father.commonSettings) {
                if (setting is BooleanSetting) {
                    settings.add(BooleanSettingButton(width * 0.98f, height, setting, this))
                }
                if (setting is BindSetting) {
                    settings.add(BindSettingButton(width * 0.98f, height, setting, this))
                }
                if (setting is NumberSetting<*>) {
                    settings.add(NumberSliderButton(width * 0.98f, height, setting, this))
                }
                if (setting is ModeSetting<*>) {
                    settings.add(ModeSettingButton(width * 0.98f, height, setting, this))
                }
                if (setting is StringSetting) {
                    settings.add(StringSettingButton(width * 0.98f, height, setting, this))
                }
            }
        }
    }

    private val colourAnimation = ColourAnimation(Color(30, 30, 30), Colors.getClickGuiColor(), 200f, false, Easing.LINEAR)
    private val textcolourAnimation = ColourAnimation(Color.WHITE, Colors.getClickGuiColor(), 200f, false, Easing.LINEAR)

    override fun drawButton(x: Float, y: Float, mouseX: Int, mouseY: Int) {
        hover.state = isHoveredButton(mouseX,mouseY)

        colourAnimation.state = father.isEnabled
        Render2DUtil.drawRect(x,y,width,height,hover.getColour().rgb)
        Render2DUtil.drawLine(x + width - 5,y + 2,x+width-5,y+height - 2, 5F, colourAnimation.getColour().rgb)

        if(isHoveredButton(mouseX,mouseY)){
            Render2DUtil.drawGlow(x.toDouble()+6,y.toDouble(), x.toDouble()+Cube.fontManager.CustomFont.getStringWidth(father.name),y.toDouble()+height, Color(Colors.getClickGuiColor().red,Colors.getClickGuiColor().green,Colors.getClickGuiColor().blue,150).rgb)
        }

        if (panelFather.modules.indexOf(this) == panelFather.modules.size - 1) {
            Render2DUtil.drawLine(x.toDouble(),y+this.height.toDouble(),x + this.width.toDouble(),y+this.height.toDouble(),3F, Color(33,33,33))
        }

        textcolourAnimation.state = father.isEnabled
        Cube.fontManager!!.CustomFont.drawStringWithShadow(
            father.name,
            x + 3,
            y + (height / 2) - (Cube.fontManager!!.CustomFont.height / 4),
            textcolourAnimation.getColour().rgb
        )
        this.x = x
        this.y = y
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (settings.isNotEmpty()) {
            settings.forEach { it.mouseClicked(mouseX, mouseY, mouseButton) }
        }
        if (mouseButton == 0 && this.isHoveredHUD(mouseX, mouseY)) {
            this.x2 = this.father.x - mouseX
            this.y2 = this.father.y - mouseY
            this.dragging = true
            return
        }
        if (!isHoveredButton(mouseX, mouseY)) {
            return
        }
        when (mouseButton) {
            0 -> father.toggle()
            1 -> isShowSettings = !isShowSettings
        }
    }

    private fun isHoveredHUD(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= this.father.x.coerceAtMost(this.father.x + this.father.width) && mouseX <= this.father.x.coerceAtLeast(
            this.father.x + this.father.width
        ) && mouseY >= this.father.y.coerceAtMost(this.father.y + this.father.height) && mouseY <= this.father.y.coerceAtLeast(
            this.father.y + this.father.height
        )
    }


    private fun solveHUDPos(mouseX: Int, mouseY: Int) {
        if (dragging) {
            this.father.x = x2 + mouseX
            this.father.y = y2 + mouseY
        }
        if (this.father.x.coerceAtMost(this.father.x + this.father.width) < 0) {
            this.father.x = if (this.father.x < this.father.x + this.father.width) 0.0f else -this.father.width
        }
        if (this.father.x.coerceAtLeast(this.father.x + this.father.width) > this.mc.displayWidth / 2) {
            this.father.x =
                if (this.father.x < this.father.x + this.father.width) (this.mc.displayWidth / 2 - this.father.width) else (this.mc.displayWidth / 2).toFloat()
        }
        if (this.father.y.coerceAtMost(this.father.y + this.father.height) < 0) {
            this.father.y = if (this.father.y < this.father.y + this.father.height) 0.0f else -this.father.height
        }
        if (this.father.y.coerceAtLeast(this.father.y + this.father.height) > this.mc.displayHeight / 2) {
            this.father.y =
                if (this.father.y < this.father.y + this.father.height) (this.mc.displayHeight / 2 - this.father.height) else (this.mc.displayHeight / 2).toFloat()
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0) {
            this.dragging = false
        }
        if (settings.isNotEmpty()) {
            settings.forEach { it.mouseReleased(mouseX, mouseY, mouseButton) }
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (settings.isNotEmpty()) {
            settings.forEach { it.keyTyped(typedChar, keyCode) }
        }
    }
}