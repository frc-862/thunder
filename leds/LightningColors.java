package frc.thunder.leds;

public enum LightningColors {
    BLACK(0, 0, 0),
    WHITE(0, 0, 255),
    RED(0, 255, 255),
    ORANGE(5, 255, 255),
    YELLOW(15, 255, 255),
    GREEN(60, 255, 255),
    LIGHT_BLUE(100, 255, 255),
    BLUE(120, 255, 255),
    PURPLE(140, 255, 255),
    PINK(165, 255, 255);

    private final int hue;
    private final int saturation;
    private final int value;

    LightningColors(int hue, int saturation, int value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    public int getHue() {
        return hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public int getValue() {
        return value;
    }
}
