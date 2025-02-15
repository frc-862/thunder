package frc.thunder.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class Lightningbolt extends AddressableLED {
    private final AddressableLEDBuffer buffer;

    public Lightningbolt(int pwmPort, int length) {
        super(pwmPort);

        setLength(length);
        start();

        buffer = new AddressableLEDBuffer(length);
    }

    public int getLength() {
        return buffer.getLength();
    }

    public void update() {
        setData(buffer);
    }

    public void setHSV(int index, int h, int s, int v) {
        buffer.setHSV(index, h, s, v);
    }

    public void setHSV(int index, Colors color) {
        buffer.setHSV(index, color.getHue(), color.getSaturation(), color.getValue());
    }

    /**
     * @param h hue
     * @param s saturation
     * @param v value
     * @param length the number of LEDs to apply the effect to
     * @param startIndex the starting index of the effect
     */
    public void setSolidHSV(int h, int s, int v, int length, int startIndex) {
        for (var i = startIndex; i < startIndex + length && i < buffer.getLength(); i++) {
            setHSV(i, h, s, v);
        }
    }

    /**
     * @param color the color to set the LEDs to
     * @param length the number of LEDs to apply the effect to
     * @param startIndex the starting index of the effect
     */
    public void setSolidHSV(Colors color, int length, int startIndex) {
        for (var i = startIndex; i < startIndex + length && i < buffer.getLength(); i++) {
            setHSV(i, color);
        }
    }
}
