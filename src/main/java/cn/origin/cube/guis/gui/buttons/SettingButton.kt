package cn.origin.cube.guis.gui.buttons

import cn.origin.cube.core.settings.Setting

abstract class SettingButton<T : Setting<*>>(
    width: Float,
    height: Float,
    val value: Setting<*>,
    val father: ModuleButton
) : Button(width, height, father.panelFather) {

}