package cn.origin.cube.module.modules.client

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.ModuleInfo

@ModuleInfo(name = "Test", descriptions = "Chat suffix", category = Category.CLIENT)
class Test: Module() {


    override fun onEnable() {
        super.onEnable()
    }
}