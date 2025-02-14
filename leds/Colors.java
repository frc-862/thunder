package frc.thunder.leds;

public enum Colors {
    RED(0),
    ORANGE(5),
    YELLOW(15),
    GREEN(240),
    LIGHT_BLUE(195),
    BLUE(120),
    PURPLE(315),
    PINK(355);

    private final int hue;

    Colors(int hue) {
        this.hue = hue;
    }

    public int getHue() {
        return hue;
    }
}
