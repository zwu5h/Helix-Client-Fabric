package dev.helix.setting;

public final class NumberSetting extends Setting {
    private final double min;
    private final double max;
    private final double step;
    private Double value;

    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name);
        this.min = min;
        this.max = max;
        this.step = step;
        setValue(value);
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = Math.max(min, Math.min(max, value));
    }

    public double getStep() {
        return step;
    }

    @Override
    public Object getRawValue() {
        return value;
    }
}
