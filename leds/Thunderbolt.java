// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.leds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * <p>This class extends SubsystemBase, and your LEDs subsystem should extend this.</p>
 * <p>Create new ThunderStrips and pass them into Thunderbolt using addStrip()</p>
 * 
 * <h3> !Requires an enum located at frc.robot.Constants.LEDConstants.LEDStates! <h3/>
 */
public abstract class Thunderbolt extends SubsystemBase {
	public LightningLEDs leds;

	private List<ThunderStrip> strips;
	private ScheduledExecutorService scheduler;

	/**
	 * Constructor for Thunderbolt<br>
	 * 
	 * <h3>!Requires an enum located at frc.robot.Constants.LEDConstants.LEDStates!</h3>
	 * 
	 * @param pwmPort the PWM port the LEDs are connected to
	 * @param length the total length of LEDs
	 * @param updateFreq the frequency to update the LEDs
	 * 
	 */
	public Thunderbolt(int pwmPort, int length, double updateFreq) {
		leds = new LightningLEDs(pwmPort, length);

		strips =new ArrayList<>();

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(this::update, 0, (long) (updateFreq * 1000), java.util.concurrent.TimeUnit.MILLISECONDS);
	}

	public void addStrip(ThunderStrip strip) {
		strips.add(strip);
	}

	/**
	 * This method is scheduled on a separate thread to update the LEDs periodically
	 */
	public void update() {
		for (ThunderStrip strip : strips) {
			strip.update();
		}
		leds.update();
	}

}
