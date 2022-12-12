package dev.canon.cue.module.modules.visual;

import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.module.modules.client.ClickGui;
import dev.canon.cue.settings.DoubleSetting;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;

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
