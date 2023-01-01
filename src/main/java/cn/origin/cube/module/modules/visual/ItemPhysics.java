package cn.origin.cube.module.modules.visual;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.DoubleSetting;

@ModuleInfo(name = "ItemPhysics",
descriptions = "",
category = Category.VISUAL)
public class ItemPhysics extends Module {
    public DoubleSetting scale = registerSetting("Scale", 3.0, 0.1, 10.0);

    public static ItemPhysics INSTANCE;

    public ItemPhysics() {
        INSTANCE = this;
    }
}
