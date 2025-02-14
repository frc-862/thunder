package frc.thunder.leds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.LEDConstants.LEDStates;

public abstract class Lightningstrip {
    private final int length;
    private final int startIndex;

	private final PriorityQueue<LEDStates> enabledStates;
    private final List<LEDStates> states = new ArrayList<>();

	private final Lightningbolt leds;

    public Lightningstrip(int length, int startIndex, Lightningbolt leds, LEDStates ...states) {
        this.length = length;
        this.startIndex = startIndex;
		
		enabledStates = new PriorityQueue<>();
        this.states.addAll(Arrays.asList(states));

		this.leds = leds;
    }

	public void enableState(LEDStates state) {
        if (states.contains(state)) {
            enabledStates.add(state);
        }
	}

    public void disableState(LEDStates state) {
        enabledStates.remove(state);
    }

    /**
	 * Updates the LEDs based on the state
	 * 
	 * Override this method to update the LEDs based on the state (Switch Case statement)
	 * 
	 * @param state the state to update the LEDs to
	 */
	protected abstract void updateLEDs(Lightningbolt leds, LEDStates state);

	/**
	 * Sets the LEDs to their default state
	 * 
	 * Override this method to set the LEDs default state
	 */
	protected abstract void defaultLEDs(Lightningbolt leds);

	/**
     * Sets the LED buffer to a rainbow pattern
     * @param length the number of LEDs to apply the effect to
     * @param startIndex the starting index of the effect
     */
    public void rainbow() {
        for (int i = startIndex; i < startIndex + length && i < leds.getLength(); i++) {
            leds.setHSV(i, (i + (int) (Timer.getFPGATimestamp() * 20)) % leds.getLength() * 180 / 14, 255, 100);
        }
    }

    /**
     * @param segmentSize size of each color segment
     * @param length the number of LEDs to apply the effect to
     * @param startIndex the starting index of the effect
     */
    public void swirl(Colors color1, Colors color2, int segmentSize) {
        for (int i = startIndex; i < startIndex + length && i < leds.getLength(); i++) {
            if (((i + (int) (Timer.getFPGATimestamp() * 10)) / segmentSize) % 2 == 0) {
                leds.setHSV(i, color1.getHue(), 255, 255);
            } else {
                leds.setHSV(i, color2.getHue(), 255, 255);
            }
        }
    }

    /**
     * @param hue the hue to blink
     * @param length the number of LEDs to apply the effect to
     * @param startIndex the starting index of the effect
     */
    public void blink(Colors color) {
        if ((int) (Timer.getFPGATimestamp() * 10) % 2 == 0) {
            leds.setSolidHSV(color.getHue(), 255, 255, length, startIndex);
        } else {
            leds.setSolidHSV(0, 0, 0, length, startIndex);
        }
    }

    /**
     * @param hue the hue to pulse
     * @param length the number of LEDs to apply the effect to
     * @param startIndex the starting index of the effect
     */
    public void pulse(Colors color, int length, int startIndex) {
        leds.setSolidHSV(color.getHue(), 255, (int) Math.abs((Math.sin(Timer.getFPGATimestamp() * 2) * 255)), length, startIndex);
    }
}
