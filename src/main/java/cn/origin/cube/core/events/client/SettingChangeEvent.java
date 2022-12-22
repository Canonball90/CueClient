package cn.origin.cube.core.events.client;

import cn.origin.cube.core.events.EventStage;
import cn.origin.cube.core.settings.Setting;

public class SettingChangeEvent<T> extends EventStage {
    public final Setting<?> setting;

    public final T oldValue;

    public final T newValue;

    public SettingChangeEvent(Setting<?> setting, T oldValue, T newValue) {
        this.setting = setting;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
