package frc.thunder.leds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.LEDConstants.LEDStates;

/**
 * ThunderStrips are using to control sections of the LEDs, you should create new ThunderStrips and Override the abstract updateLEDs and defaultLEDs
 */
public abstract class ThunderStrip {
    private final int length;
    private final int startIndex;

	private final PriorityQueue<LEDStates> states;
    private final List<LEDStates> unusedStates = new ArrayList<>();

	private final LightningLEDs leds;

    public ThunderStrip(int length, int startIndex, LightningLEDs leds, LEDStates ...unusedStates) {
        this.length = length;
        this.startIndex = startIndex;
		
		states = new PriorityQueue<>();
        this.unusedStates.addAll(Arrays.asList(unusedStates));

		this.leds = leds;
    }

    /**
	 * @param state the state to enable
	 * @return a command that enables the state
	 */
	public Command enableState(LEDStates state) {
		return new StartEndCommand(() -> {
			if (!unusedStates.contains(state)) {
                states.add(state);
            }
		}, () -> {
			states.remove(state);
		}).ignoringDisable(true);
	}

    /**
     * @param state the state to enable
     * @param duration the duration to enable the state for
     * @return a command that enables the state
     */
    public Command enableStateFor(LEDStates state, int duration) {
        return new StartEndCommand(() -> {
            if (!unusedStates.contains(state)) {
                states.add(state);
            }
        }, () -> {
            states.remove(state);
        })
        .withDeadline(new WaitCommand(duration))
        .ignoringDisable(true);
    }

    /**
     * Sets the current state of the LED strip
     * 
     * @param state the state to set
     * @param enabled whether to enable or disable the state
     * @return an InstantCommand that sets the state
     */
    public Command setState(LEDStates state, boolean enabled) {
        return new InstantCommand(() -> {
            if (enabled) {
                if (!unusedStates.contains(state)) {
                    states.add(state);
                }
            } else {
                states.remove(state);
            }
        });
    }

    /**
     * @return the current state of the LED strip
     */
    public LEDStates getState() {
        return states.peek();
    }

    /**
     * Updates the LED strip
     */
    public void update() {
        if (states.isEmpty()) {
            defaultLEDs();
        } else {
            updateLEDs(getState());
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
    public void solid(LightningColors color) {
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
    public void swirl(LightningColors color1, LightningColors color2, int segmentSize) {
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
    public void blink(LightningColors color) {
        if ((int) (Timer.getFPGATimestamp() * 20) % 2 == 0) { // Increased the multiplier to 20 for faster strobe
            leds.setSolidHSV(color, length, startIndex);
        } else {
            leds.setSolidHSV(LightningColors.BLACK, length, startIndex);
        }
    }

    /**
     * @param hue the hue to pulse
     */
    public void pulse(LightningColors color) {
        leds.setSolidHSV(color.getHue(), color.getSaturation(), (int) Math.abs((Math.sin(Timer.getFPGATimestamp() * 2) * color.getValue())), length, startIndex);
    }
}
