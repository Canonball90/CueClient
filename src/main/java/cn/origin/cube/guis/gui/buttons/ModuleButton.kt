package cn.origin.cube.guis.gui.buttons

import cn.origin.cube.Cube
import cn.origin.cube.core.module.AbstractModule
import cn.origin.cube.core.settings.*
import cn.origin.cube.guis.gui.CategoryPanel
import cn.origin.cube.guis.gui.buttons.setting.*
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.module.modules.client.Colors
import cn.origin.cube.utils.COG
import cn.origin.cube.utils.render.Render2DUtil
import me.surge.animation.ColourAnimation
import me.surge.animation.Easing
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class ModuleButton(width: Float, height: Float, panel: CategoryPanel, val father: AbstractModule) :
    Button(width, height, panel) {

    var isShowSettings = false
    val settings = arrayListOf<SettingButton<*>>()
    private var x2 = 0.0f
    private var y2 = 0.0f
    private var dragging = false
    private val hover = ColourAnimation(Color(15, 15, 15,100), father.category.color, 300f, false, Easing.LINEAR)
    private val pulse = ColourAnimation(Color.WHITE, Color.DARK_GRAY, 500f, false, Easing.LINEAR)
    private var progress = 0F

    init {
        this.progress = 0F;
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

    private val colourAnimation = ColourAnimation(Color(15, 15, 15, 125),father.category.color, 200f, false, Easing.LINEAR)
    private val textcolourAnimation = ColourAnimation(Color.WHITE,father.category.color, 200f, false, Easing.LINEAR)

    override fun drawButton(x: Float, y: Float, mouseX: Int, mouseY: Int) {
        hover.state = isHoveredButton(mouseX,mouseY)
        if (father.isHud && father.isEnabled) {
            this.solveHUDPos(mouseX, mouseY)
            Cube.fontManager.CustomFont.drawString(
                (father.name + " X:" + father.x).toString() + " Y:" + father.y,
                father.x,
                father.y - Cube.fontManager.CustomFont.fontHeight,
                Colors.getClickGuiColor().rgb
            )
            Render2DUtil.drawRect(
                father.x - 1,
                father.y - 1,
                father.width + 1,
                father.height + 1,
                Color(25, 25, 25, 125).rgb
            )
            Render2DUtil.drawOutlineRect(
                (father.x - 1).toDouble(),
                (father.y - 1).toDouble(),
                (father.width + 1).toDouble(),
                (father.height + 1).toDouble(),
                1F,
                Colors.getClickGuiColor()
            )
        }
        if(ClickGui.INSTANCE.outline.value) {
            Render2DUtil.drawBorderedRect(x.toDouble(), y.toDouble() - 1,x + this.width.toDouble(),y + this.height.toDouble(),
                1.0, Color(15, 15, 15, 125).rgb, Colors.getClickGuiColor().rgb)
        }else{
            colourAnimation.state = father.isEnabled
            Render2DUtil.drawGradientHRect(x,y, x+width,y+height, hover.getColour().rgb, colourAnimation.getColour().rgb)
        }

        if (panelFather.modules.indexOf(this) == panelFather.modules.size - 1) {
            Render2DUtil.drawLine(x.toDouble(),y+this.height.toDouble(),x + this.width.toDouble(),y+this.height.toDouble(),3F,father.category.color)
        }
        textcolourAnimation.state = father.isEnabled
        Cube.fontManager!!.CustomFont.drawStringWithShadow(
            father.name,
            x + 3,
            y + (height / 2) - (Cube.fontManager!!.CustomFont.height / 4),
            textcolourAnimation.getColour().rgb
        )
        pulse.state = isShowSettings

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Cube.fontManager!!.IconFont.drawStringWithShadow(
            COG, (x + width) - 3 - Cube.fontManager!!.IconFont.getStringWidth(
                COG
            ), y + (height / 2) - (Cube.fontManager!!.IconFont.height / 4), pulse.getColour().rgb
        )
        GlStateManager.rotate(calculateRotation(this.progress), 0.0f, 0.0f, 1.0f)
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        if(isShowSettings){
            progress = progress.inc()
        }
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

    fun calculateRotation(var0: Float): Float {
        var var0 = var0
        if (360.0f.also { var0 %= it } >= 180.0f) {
            var0 -= 360.0f
        }
        if (var0 < -180.0f) {
            var0 += 360.0f
        }
        return var0
    }

}