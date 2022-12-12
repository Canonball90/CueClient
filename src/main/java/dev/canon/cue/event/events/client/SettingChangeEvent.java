package dev.canon.cue.event.events.client;

import dev.canon.cue.event.events.EventStage;
import dev.canon.cue.settings.Setting;
import dev.canon.cue.event.events.EventStage;
import dev.canon.cue.settings.Setting;

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
