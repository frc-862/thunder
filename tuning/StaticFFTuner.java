package frc.thunder.tuning;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class StaticFFTuner extends SubsystemBase {

    private MotorController motor;

    private GenericEntry kSTuner;
    private double voltage = 0;

    private int tunerNumber = 0;

    /**
     * This is a tuner to find the voltage that is needed to overcome static
     * friction, then you can take that number and put it into your feedforward.
     * 
     * @param name  name of the motor you want to tune
     * @param motor the motor you want to tune
     */
    public StaticFFTuner(String name, MotorController motor) {
        this.motor = motor;
        tunerNumber++;

        ShuffleboardTab tab = Shuffleboard.getTab("FF Tuning");

        if (tunerNumber <= 5) {
            kSTuner = tab.add(name + "kS", voltage).withSize(2, 1).withPosition(0, tunerNumber)
                    .withWidget(BuiltInWidgets.kNumberSlider)
                    .withProperties(Map.of("Min", 0, "Max", 12, "Block increment", 0.00001)).getEntry();
        } else {
            kSTuner = tab.add(name + "kS", voltage).withSize(2, 1).withPosition(1, tunerNumber)
                    .withWidget(BuiltInWidgets.kNumberSlider)
                    .withProperties(Map.of("Min", 0, "Max", 12, "Block increment", 0.00001)).getEntry();
        }

    }

    @Override
    public void periodic() {
        voltage = kSTuner.getDouble(voltage);
        motor.setVoltage(voltage);
    }

}
