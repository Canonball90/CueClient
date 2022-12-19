package cn.origin.cube.module.modules.client

import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.interfaces.ModuleInfo

@ModuleInfo(name = "Test", descriptions = "Chat suffix", category = Category.CLIENT)
class Test: Module() {


    override fun onEnable() {
        super.onEnable()
    }
}