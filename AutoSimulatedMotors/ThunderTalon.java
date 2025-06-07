// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.AutoSimulatedMotors;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import frc.robot.Robot;

/** Add your docs here. */
public class ThunderTalon extends Talon {

    private double power;
    private boolean isInverted = false;

    /**
     * Creates a new ThunderTalon.
     *
     * @param channel The PWM channel that the Talon is connected to.
     */
    public ThunderTalon(int channel) {
        super(channel);
    }

    /**
    * Set the PWM value.
    *
    * <p>The PWM value is set using a range of -1.0 to 1.0, appropriately scaling the value for the
    * FPGA.
    *
    * <p>In simulation, this method will store the power value instead of sending to the hardware.
    *
    * @param speed The speed value between -1.0 and 1.0 to set.
    */
    @Override
    public void set(double power) {

        if (Robot.isSimulation()) this.power = isInverted ? -power : power;
        else super.set(power);
    }

    /**
    * Get the recently set value of the PWM. This value is affected by the inversion property. If you
    * want the value that is sent directly to the MotorController, use {@link
    * edu.wpi.first.wpilibj.PWM#getSpeed()} instead.

    * <p>In simulation, this method will return the stored power value instead of reading from the hardware
    *
    * @return The most recently set value for the PWM between -1.0 and 1.0.
    */
    @Override
    public double get() {
        if (Robot.isSimulation()) return power;
        else return super.get();
    }

    @Override
    public void stopMotor(){
        // Don't use set(0) as that will feed the watch kitty

        if (Robot.isSimulation()) power = 0.0;
        else super.stopMotor();
    }

    @Override
    public void setInverted(boolean isInverted) {

        if (Robot.isSimulation()) this.isInverted = isInverted;
        else super.setInverted(isInverted);
    }
}
