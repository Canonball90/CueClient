package dev.canon.cue.settings;

import dev.canon.cue.module.AbstractModule;
import dev.canon.cue.module.AbstractModule;

public abstract class NumberSetting<T extends Number> extends Setting<T> {

    public NumberSetting(String name, T value, AbstractModule father) {
        super(name, value, father);
    }
}
