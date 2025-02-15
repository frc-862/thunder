package frc.thunder.leds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
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

    /**
	 * @param state the state to enable
	 * @return a command that enables the state
	 */
	public Command enableState(LEDStates state) {
		return new StartEndCommand(() -> {
			if (states.contains(state)) {
                enabledStates.add(state);
            }
			states.add(state);
		}, () -> {
			enabledStates.remove(state);
		}).ignoringDisable(true);
	}

    /**
     * Updates the LED strip
     */
    public void update() {
        if (!enabledStates.isEmpty()) {
            updateLEDs(enabledStates.peek());
        } else {
            defaultLEDs();
        }
    }

    /**
	 * Updates the LEDs based on the state
	 * 
	 * Override this method to update the LEDs based on the state (Switch Case statement)
	 * 
	 * @param state the state to update the LEDs to
	 */
	public abstract void updateLEDs(LEDStates state);

	/**
	 * Sets the LEDs to their default state
	 * 
	 * Override this method to set the LEDs default state
	 */
	public abstract void defaultLEDs();

    /**
     * Sets the LED buffer to a solid color
     * 
     * @param color the color to set the LEDs to
     */
    public void color(Colors color) {
        leds.setSolidHSV(color, length, startIndex);
    }

	/**
     * Sets the LED buffer to a rainbow pattern
     */
    public void rainbow() {
        for (int i = startIndex; i < startIndex + length && i < leds.getLength(); i++) {
            leds.setHSV(i, (i + (int) (Timer.getFPGATimestamp() * 20)) % leds.getLength() * 180 / 14, 255, 100);
        }
    }

    /**
     * @param segmentSize size of each color segment
     */
    public void swirl(Colors color1, Colors color2, int segmentSize) {
        for (int i = startIndex; i < startIndex + length && i < leds.getLength(); i++) {
            if (((i + (int) (Timer.getFPGATimestamp() * 10)) / segmentSize) % 2 == 0) {
                leds.setHSV(i, color1);
            } else {
                leds.setHSV(i, color2);
            }
        }
    }

    /**
     * @param hue the hue to blink
     */
    public void blink(Colors color) {
        if ((int) (Timer.getFPGATimestamp() * 10) % 2 == 0) {
            leds.setSolidHSV(color, length, startIndex);
        } else {
            leds.setSolidHSV(Colors.BLACK, length, startIndex);
        }
    }

    /**
     * @param hue the hue to pulse
     */
    public void pulse(Colors color) {
        leds.setSolidHSV(color.getHue(), color.getSaturation(), (int) Math.abs((Math.sin(Timer.getFPGATimestamp() * 2) * color.getValue())), length, startIndex);
    }
}
