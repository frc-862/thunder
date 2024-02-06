package frc.thunder.tuning;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.thunder.shuffleboard.LightningShuffleboard;

public class FalconTuner {
    private Slot0Configs PIDConfigs = new Slot0Configs();
    private TalonFX motor;

    private String tabName;
    private double kP;
    private double kI;
    private double kD;
    private double kS;
    private double kV;
    private double kA;

    /**
     * creates a new FalconTuner, which publishes PIDF gains to shuffleboard and applies them when changed
     * 
     * @implNote {@link #update()} must be called periodically to apply/detect changes
     * @specNote Slot0Configs are refreshed not applied; might retain old values (TODO: check if this is necessary)
     * 
     * @param name name of the shuffleboard tab
     * @param PIDConfigs Slot0Config gains
     * @param motor motor to tune
     */
    public FalconTuner(String name, TalonFX motor, String tabName) {
        this.motor = motor;
        this.tabName = tabName;

        LightningShuffleboard.setDouble(tabName, "kP", 0d);
        LightningShuffleboard.setDouble(tabName, "kI", 0d);
        LightningShuffleboard.setDouble(tabName, "kD", 0d);
        LightningShuffleboard.setDouble(tabName, "kS", 0d);
        LightningShuffleboard.setDouble(tabName, "kV", 0d);
        LightningShuffleboard.setDouble(tabName, "kA", 0d);
    }

    /**
     * @return true if the gains have changed
     */
    private boolean checkGains() {
        return  PIDConfigs.kP != kP ||
                PIDConfigs.kI != kI ||
                PIDConfigs.kD != kD ||
                PIDConfigs.kS != kS ||
                PIDConfigs.kV != kV ||
                PIDConfigs.kA != kA;
    }

    /**
     * updates the gains from shuffleboard and applies them if they have changed
     * 
     * @implNote must be called periodically to apply/detect changes
     */
    public void update() {
        kP = LightningShuffleboard.getDouble(tabName, "kP", 0d);
        kI = LightningShuffleboard.getDouble(tabName, "kI", 0d);
        kD = LightningShuffleboard.getDouble(tabName, "kD", 0d);
        kS = LightningShuffleboard.getDouble(tabName, "kS", 0d);
        kV = LightningShuffleboard.getDouble(tabName, "kV", 0d);
        kA = LightningShuffleboard.getDouble(tabName, "kA", 0d);
            
        if(checkGains()) {
            PIDConfigs.kP = kP;
            PIDConfigs.kI = kI;
            PIDConfigs.kD = kD;
            PIDConfigs.kS = kS;
            PIDConfigs.kV = kV;
            PIDConfigs.kA = kA;

            motor.getConfigurator().refresh(PIDConfigs);
        }
    }

}
