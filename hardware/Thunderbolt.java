// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.hardware;

import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.LEDConstants.LEDStates;

public abstract class Thunderbolt extends SubsystemBase {
	int length;

	AddressableLED leds;
	AddressableLEDBuffer ledBuffer;

	PriorityQueue<LEDStates> states;
	ScheduledExecutorService scheduler;

	/**
	 * Constructor for Thunderbolt<br>
	 * 
	 * <STRONG>Requires an enum located at frc.robot.Constants.LEDConstants.LEDStates</STRONG>
	 * 
	 * @param pwmPort the PWM port the LEDs are connected to
	 * @param length the number of LEDs
	 * @param updateFreq the frequency to update the LEDs
	 * 
	 */
	public Thunderbolt(int pwmPort, int length, double updateFreq) {
		this.length = length;

		leds = new AddressableLED(pwmPort);
		ledBuffer = new AddressableLEDBuffer(length);

		leds.setLength(ledBuffer.getLength());
		leds.start();

		states = new PriorityQueue<>();

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(this::cycle, 0, (long) (updateFreq * 1000), java.util.concurrent.TimeUnit.MILLISECONDS);

	}

	protected enum Colors {
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

	/**
	 * This method is scheduled on a separate thread to update the LEDs periodically
	 */
	public void cycle() {
		LEDStates state = states.peek();
		if (state != null) {
			updateLEDs(state);
		} else {
			defaultLEDs();
		}
		leds.setData(ledBuffer);
	}

	/**
	 * Updates the LEDs based on the state
	 * 
	 * Override this method to update the LEDs based on the state (Switch Case statement)
	 * 
	 * @param state the state to update the LEDs to
	 */
	protected abstract void updateLEDs(LEDStates state);

	/**
	 * Sets the LEDs to their default state
	 * 
	 * Override this method to set the LEDs default state
	 */
	protected abstract void defaultLEDs();

	/**
	 * @param state the state to enable
	 * @return a command that enables the state
	 */
	public Command enableState(LEDStates state) {
		return new StartEndCommand(
			() -> {states.add(state);
		},
			() -> {
				states.remove(state);
			}).ignoringDisable(true);
	}

	/**
	 * Sets the LED buffer to a rainbow pattern
	 */
	protected void rainbow() {
		for (int i = 0; i < length; i++) {
			ledBuffer.setHSV(i, (i + (int) (Timer.getFPGATimestamp() * 20)) % ledBuffer.getLength() * 180 / 14, 255,100);
		}
	}

	/**
	 * @param segmentSize size of each color segment
	 */
	protected void swirl(int segmentSize) {
		for (int i = 0; i < length; i++) {
			if (((i + (int) (Timer.getFPGATimestamp() * 10)) / segmentSize) % 2 == 0) {
				ledBuffer.setHSV(i, Colors.BLUE.getHue(), 255, 255);
			} else {
				ledBuffer.setHSV(i, Colors.ORANGE.getHue(), 255, 255);
			}
		}
	}

	/**
	 * @param hue the hue to blink
	 */
	protected void blink(int hue) {
		if ((int) (Timer.getFPGATimestamp() * 10) % 2 == 0) {
			setSolidHSV(hue, 255, 255);
		} else {
			setSolidHSV(0, 0, 0);
		}
	}

	/**
	 * @param hue the hue to blink
	 */
	protected void pulse(int hue) {
		setSolidHSV(hue, 255, (int) Math.abs((Math.sin(Timer.getFPGATimestamp() * 2) * 255)));
	}

	/**
	 * @param h hue
	 * @param s saturation
	 * @param v value
	 */
	protected void setSolidHSV(int h, int s, int v) {
		for (var i = 0; i < length; i++) {
			ledBuffer.setHSV(i, h, s, v);
		}
	}
}
