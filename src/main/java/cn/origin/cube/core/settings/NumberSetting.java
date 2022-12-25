package cn.origin.cube.core.settings;

import cn.origin.cube.core.module.AbstractModule;

public abstract class NumberSetting<T extends Number> extends Setting<T> {

    public NumberSetting(String name, T value, AbstractModule father) {
        super(name, value, father);
    }
}
