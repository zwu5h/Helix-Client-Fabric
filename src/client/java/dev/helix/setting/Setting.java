package dev.helix.setting;

public abstract class Setting<T> {
    private final String name;
    private T value;

    protected Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public T value() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
