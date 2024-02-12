package frc.thunder.tuning;

import com.ctre.phoenix6.configs.Slot0Configs;
import frc.thunder.hardware.ThunderBird;
import frc.thunder.shuffleboard.LightningShuffleboard;
import java.util.function.Consumer;

public class FalconTuner {
    private Slot0Configs PIDConfigs;
    private ThunderBird motor;
    private Consumer<Double> setPointSupplier;
    private double defaultSetPoint = 0;

    private String tabName;
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    private double kS = 0;
    private double kV = 0;
    private double kA = 0;

    /**
     * creates a new FalconTuner, which publishes *Slot0* PIDF gains to shuffleboard and applies them when changed; existing PID gains are copied
     * 
     * @implNote {@link #update()} must be called periodically to apply/detect changes
     * @implNote Slot0Configs are refreshed not applied; might retain old values (TODO: check if this is necessary)
     * 
     * @param name name of the shuffleboard tab
     * @param motor motor to tune
     * @param setPointConsumer the DoubleConsumer to set the setpoint
     */
    public FalconTuner(ThunderBird motor, String tabName, Consumer<Double> setPointConsumer, double defaultSetPoint) {
        this.motor = motor;
        this.tabName = tabName;
        this.setPointSupplier = setPointConsumer;
        this.defaultSetPoint = defaultSetPoint;

        PIDConfigs = motor.getConfig().Slot0;

        kP = PIDConfigs.kP;
        kI = PIDConfigs.kI;
        kD = PIDConfigs.kD;
        kS = PIDConfigs.kS;
        kV = PIDConfigs.kV;
        kA = PIDConfigs.kA;
    }

    /**
     * @return true if the gains have changed
     */
    private boolean checkGains() {
        PIDConfigs = motor.getConfig().Slot0;
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
        kP = LightningShuffleboard.getDouble(tabName, "kP", kP);
        kI = LightningShuffleboard.getDouble(tabName, "kI", kI);
        kD = LightningShuffleboard.getDouble(tabName, "kD", kD);
        kS = LightningShuffleboard.getDouble(tabName, "kS", kS);
        kV = LightningShuffleboard.getDouble(tabName, "kV", kV);
        kA = LightningShuffleboard.getDouble(tabName, "kA", kA);

        setPointSupplier.accept(LightningShuffleboard.getDouble(tabName, "setpoint", defaultSetPoint));
            
        if(checkGains()) {
            motor.configPIDF(0, kP, kI, kD, kS, kV, kA);
            motor.applyConfig();
        }
    }

}
