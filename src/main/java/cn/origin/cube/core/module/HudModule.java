package cn.origin.cube.core.module;

import cn.origin.cube.core.settings.BindSetting;
import cn.origin.cube.core.module.interfaces.HudModuleInfo;

public class HudModule extends AbstractModule {
    public HudModule() {
        this.name = getAnnotation().name();
        this.descriptions = getAnnotation().descriptions();
        this.category = getAnnotation().category();
        this.keyBind.setValue(new BindSetting.KeyBind(getAnnotation().defaultKeyBind()));
        this.toggle = getAnnotation().defaultEnable();
        this.isHud = true;
        this.x = getAnnotation().x();
        this.y = getAnnotation().y();
        this.width = getAnnotation().width();
        this.height = getAnnotation().height();

        this.commonSettings.add(keyBind);
    }

    private HudModuleInfo getAnnotation() {
        if (getClass().isAnnotationPresent(HudModuleInfo.class)) {
            return getClass().getAnnotation(HudModuleInfo.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }
}
