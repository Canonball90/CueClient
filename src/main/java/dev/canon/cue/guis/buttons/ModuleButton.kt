package dev.canon.cue.guis.buttons

import dev.canon.cue.Cue
import dev.canon.cue.guis.CategoryPanel
import dev.canon.cue.guis.buttons.setting.*
import dev.canon.cue.settings.*
import dev.canon.cue.utils.COG
import java.awt.Color

class ModuleButton(
    width: Float,
    height: Float,
    panel: CategoryPanel,
    val father: dev.canon.cue.module.AbstractModule
) :
    Button(width, height, panel) {

    var isShowSettings = false
    val settings = arrayListOf<SettingButton<*>>()
    private var x2 = 0.0f
    private var y2 = 0.0f
    private var dragging = false

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

    override fun drawButton(x: Float, y: Float, mouseX: Int, mouseY: Int) {
        if (father.isHud && father.isEnabled) {
            this.solveHUDPos(mouseX, mouseY)
            Cue.fontManager.CustomFont.drawString(
                (father.name + " X:" + father.x).toString() + " Y:" + father.y,
                father.x,
                father.y - Cue.fontManager.CustomFont.fontHeight,
                dev.canon.cue.module.modules.client.ClickGui.getCurrentColor().rgb
            )
            dev.canon.cue.utils.render.Render2DUtil.drawRect(
                father.x - 1,
                father.y - 1,
                father.width + 1,
                father.height + 1,
                Color(25, 25, 25, 125).rgb
            )
            dev.canon.cue.utils.render.Render2DUtil.drawOutlineRect(
                (father.x - 1).toDouble(),
                (father.y - 1).toDouble(),
                (father.width + 1).toDouble(),
                (father.height + 1).toDouble(),
                1F,
                dev.canon.cue.module.modules.client.ClickGui.getCurrentColor()
            )
        }
        if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.outline.value) {
            dev.canon.cue.utils.render.Render2DUtil.drawBorderedRect(
                x.toDouble(),
                y.toDouble() - 1,
                x + this.width.toDouble(),
                y + this.height.toDouble(),
                1.0,
                Color(15, 15, 15, 125).rgb,
                dev.canon.cue.module.modules.client.ClickGui.getCurrentColor().rgb
            )
        } else {
            if (!father.isEnabled) {
                dev.canon.cue.utils.render.Render2DUtil.drawRect(
                    x,
                    y,
                    this.width,
                    this.height,
                    Color(15, 15, 15, 125).rgb
                )
            } else {
                dev.canon.cue.utils.render.Render2DUtil.drawGradientHRect(
                    x,
                    y,
                    x + width,
                    y + height,
                    Color(15, 15, 15, 125).rgb,
                    if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.gay.value) father.category.color.rgb else dev.canon.cue.module.modules.client.ClickGui.getCurrentColor().rgb
                )
            }
        }
        Cue.fontManager!!.CustomFont.drawStringWithShadow(
            father.name,
            x + 3,
            y + (height / 2) - (Cue.fontManager!!.CustomFont.height / 4),
            if (father.isEnabled)
                if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.gay.value)
                    father.category.color.rgb
                else
                    dev.canon.cue.module.modules.client.ClickGui.getCurrentColor().rgb
            else Color.WHITE.rgb
        )
        Cue.fontManager!!.IconFont.drawStringWithShadow(
            COG, (x + width) - 3 - Cue.fontManager!!.IconFont.getStringWidth(
                COG
            ), y + (height / 2) - (Cue.fontManager!!.IconFont.height / 4), Color.WHITE.rgb
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