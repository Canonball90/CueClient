package cn.origin.cube.core.settings;

import cn.origin.cube.core.module.AbstractModule;

public class StringSetting extends Setting<String>{
    public StringSetting(String name, String value, AbstractModule father) {
        super(name, value, father);
    }
}
