// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.leds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Thunderbolt extends SubsystemBase {
	public Lightningbolt leds;

	private List<Lightningstrip> strips;
	private ScheduledExecutorService scheduler;

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
		leds = new Lightningbolt(pwmPort, length);

		strips =new ArrayList<>();

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(this::update, 0, (long) (updateFreq * 1000), java.util.concurrent.TimeUnit.MILLISECONDS);

	}

	public void addStrip(Lightningstrip strip) {
		strips.add(strip);
	}

	/**
	 * This method is scheduled on a separate thread to update the LEDs periodically
	 */
	public void update() {
		for (Lightningstrip strip : strips) {
			strip.update();
		}
		leds.update();
	}

}
