package cn.origin.cube.core.module;

import cn.origin.cube.core.settings.BindSetting;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.module.interfaces.ModuleInfo;

public class Module extends AbstractModule {

    public BooleanSetting visible = new BooleanSetting("Visible", true, this);

    public Module() {
        this.name = getAnnotation().name();
        this.descriptions = getAnnotation().descriptions();
        this.category = getAnnotation().category();
        this.keyBind.setValue(new BindSetting.KeyBind(getAnnotation().defaultKeyBind()));
        this.toggle = getAnnotation().defaultEnable();
        this.isHud = false;

        this.settingList.add(visible);
        this.commonSettings.add(keyBind);
    }

    private ModuleInfo getAnnotation() {
        if (getClass().isAnnotationPresent(ModuleInfo.class)) {
            return getClass().getAnnotation(ModuleInfo.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }
}
