package cn.origin.cube.core.settings;

import cn.origin.cube.core.module.AbstractModule;

public class DoubleSetting extends NumberSetting<Double> {

    public Double maxValue;
    public Double minValue;

    public DoubleSetting(String name, Double value, Double minValue, Double maxValue, AbstractModule father) {
        super(name, value, father);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public Double getMin(){
        return minValue;
    }

    public Double getMax(){
        return maxValue;
    }

    public DoubleSetting booleanVisible(BooleanSetting setting) {
        return (DoubleSetting) this.visible(v -> setting.getValue());
    }

    public DoubleSetting booleanDisVisible(BooleanSetting setting) {
        return (DoubleSetting) this.visible(v -> !setting.getValue());
    }

    public DoubleSetting modeVisible(ModeSetting<?> setting, Enum<?> currentValue) {
        return (DoubleSetting) this.visible(v -> setting.getValue().equals(currentValue));
    }

    public DoubleSetting modeOrVisible(ModeSetting<?> setting, Enum<?> currentValue, Enum<?> secondValue) {
        return (DoubleSetting) this.visible(v -> setting.getValue().equals(currentValue) || setting.getValue().equals(secondValue));
    }

    public DoubleSetting modeDisVisible(ModeSetting<?> setting, Enum<?> currentValue) {
        return (DoubleSetting) this.visible(v -> !setting.getValue().equals(currentValue));
    }
}
