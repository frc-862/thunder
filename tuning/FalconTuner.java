package frc.thunder.tuning;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FalconTuner extends SubsystemBase {
    private Slot0Configs PIDConfigs;
    private TalonFX motor;

    private GenericEntry kPTuner;
    private GenericEntry kITuner;
    private GenericEntry kDTuner;
    private GenericEntry kSTuner;
    private GenericEntry kVTuner;


    private double kP;
    private double kI;
    private double kD;
    private double kS;
    private double kV;

    private static int tunerNumber = 0;

    public FalconTuner(String name, Slot0Configs PIDConfigs, TalonFX motor) {
        this.PIDConfigs = PIDConfigs;
        tunerNumber++;

        ShuffleboardTab tab = Shuffleboard.getTab("PID Tuning");

        if (tunerNumber <= 5) {
            kPTuner = tab.add(name + "kP", PIDConfigs.kP)
                    .withPosition(0, tunerNumber).getEntry();
            kITuner = tab.add(name + "kI", PIDConfigs.kI)
                    .withPosition(1, tunerNumber).getEntry();
            kDTuner = tab.add(name + "kD", PIDConfigs.kD)
                    .withPosition(2, tunerNumber).getEntry();
            kSTuner = tab.add(name + "kS", PIDConfigs.kS)
                    .withPosition(3, tunerNumber).getEntry();
            kVTuner = tab.add(name + "kV", PIDConfigs.kV)
                    .withPosition(4, tunerNumber).getEntry();

        } else {
            kPTuner = tab.add(name + "kP", PIDConfigs.kP)
                    .withPosition(3, tunerNumber).getEntry();
            kITuner = tab.add(name + "kI", PIDConfigs.kI)
                    .withPosition(4, tunerNumber).getEntry();
            kDTuner = tab.add(name + "kD", PIDConfigs.kD)
                    .withPosition(5, tunerNumber).getEntry();
            kSTuner = tab.add(name + "kS", PIDConfigs.kS)
                    .withPosition(6, tunerNumber).getEntry();
            kVTuner = tab.add(name + "kV", PIDConfigs.kV)
                    .withPosition(4, tunerNumber).getEntry();
        }
    }

    private void updateGains() {
        kP = kPTuner.getDouble(0);
        kI = kITuner.getDouble(0);
        kD = kDTuner.getDouble(0);
        kS = kSTuner.getDouble(0);
        kV = kVTuner.getDouble(0);
    }

    private boolean checkGains() {
        return kP != kPTuner.getDouble(0) || kI != kITuner.getDouble(0)
                || kD != kDTuner.getDouble(0) || kS != kSTuner.getDouble(0) || kV != kVTuner.getDouble(0);
    }

    @Override
    public void periodic() {
        if (PIDConfigs != null && checkGains()) {
            PIDConfigs.kP = kPTuner.getDouble(PIDConfigs.kP);
            PIDConfigs.kI = kITuner.getDouble(PIDConfigs.kI);
            PIDConfigs.kD = kDTuner.getDouble(PIDConfigs.kD);
            PIDConfigs.kS = kSTuner.getDouble(PIDConfigs.kS);
            PIDConfigs.kV = kVTuner.getDouble(PIDConfigs.kV);

            motor.getConfigurator().apply(PIDConfigs);
        }

        updateGains();
    }

}
