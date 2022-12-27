package cn.origin.cube.guis.newGui.panel.component.button

import cn.origin.cube.core.settings.Setting
import cn.origin.cube.guis.newGui.panel.component.Button

abstract class SettingButton<T : Setting<*>>(
    width: Float,
    height: Float,
    val value: Setting<*>,
    val father: ModuleButton
) : Button(width, height, father.panelFather) {

}