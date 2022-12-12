package dev.canon.cue.guis.buttons

import dev.canon.cue.guis.CategoryPanel
import dev.canon.cue.settings.Setting

abstract class SettingButton<T : Setting<*>>(
    width: Float,
    height: Float,
    val value: Setting<*>,
    val father: ModuleButton
) : Button(width, height, father.panelFather)