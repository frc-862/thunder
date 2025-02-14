// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.leds;

import java.util.List;
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
	private Lightningbolt leds;

	List<Lightningstrip> strips;
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
	public Thunderbolt(int pwmPort, int length, double updateFreq, Lightningstrip ...strip) {
		leds = new Lightningbolt(pwmPort, length);

		strips = List.of(strip);

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(this::cycle, 0, (long) (updateFreq * 1000), java.util.concurrent.TimeUnit.MILLISECONDS);

	}

	/**
	 * This method is scheduled on a separate thread to update the LEDs periodically
	 */
	public void cycle() {
		if (state != null) {
			updateLEDs(state);
		} else {
			defaultLEDs();
		}
		leds.update();
	}

	/**
	 * @param state the state to enable
	 * @return a command that enables the state
	 */
	public Command enableState(LEDStates state) {
		return new StartEndCommand(() -> {
			for (Lightningstrip strip : strips) {
				strip.enableState(state);
			}
			states.add(state);
		}, () -> {
			states.remove(state);
		}).ignoringDisable(true);
	}
}
