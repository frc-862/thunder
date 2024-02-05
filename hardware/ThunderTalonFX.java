package frc.thunder.hardware;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ThunderTalonFX extends TalonFX {
    TalonFXConfiguration config;

    /**
     * Creat ThunderTalonFX instance with default supply limit settings (for a 40A breaker)
     * @param deviceId CAN Id Of the TalonFX
     * @param canbus CAN Bus name ("rio" for rio bus)
     * @param invert boolean (true = clockwise positive, false = counterclockwise positive)
     * @param statorLimit Stator current limit for the motor (zero to disable)
     * @param brake boolean (true = brake, false = coast)
     */
    public ThunderTalonFX(int deviceId, String canbus, boolean invert, double statorLimit, boolean brake) {
        super(deviceId, canbus);
        this.config = new TalonFXConfiguration();
        configInvert(invert);
        configSupplyLimit(40d, 40d, 100d);
        configStatorLimit(statorLimit);
        configBrake(brake);
        applyConfig();
    }

    /**
     * @param inverted boolean (true = clockwise positive, false = counterclockwise positive)
     */
    public void configInvert(boolean inverted) {
        this.config.MotorOutput.Inverted = inverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
    }

    /**
     * @param supplyLimit Input current limit from the pdh (zero to disable)
     */
    public void configSupplyLimit(double supplyLimit) {
        config.CurrentLimits.SupplyCurrentLimitEnable = supplyLimit > 0;
        config.CurrentLimits.SupplyCurrentLimit = supplyLimit;
    }

    /**
     * @param supplyLimit Input current limit from the pdh (zero to disable)
     * @param triggerThreshold allow the current to exceed the limit for the given time
     * @param timeThreshold time where current is allowed to exceed the threshold
     */
    public void configSupplyLimit(double supplyLimit, double triggerThreshold, double timeThreshold) {
        configSupplyLimit(supplyLimit);
        config.CurrentLimits.SupplyCurrentThreshold = triggerThreshold;
        config.CurrentLimits.SupplyTimeThreshold = timeThreshold;
    }

    public void configStatorLimit(double statorLimit) {
        config.CurrentLimits.StatorCurrentLimitEnable = statorLimit > 0;
        config.CurrentLimits.StatorCurrentLimit = statorLimit;
    }

    public void configBrake(boolean brake) {
        config.MotorOutput.NeutralMode = brake ? NeutralModeValue.Brake : NeutralModeValue.Coast;
    }

    public void configPID(double kP, double kI, double kD, int slotNumber) {
        switch (slotNumber) {
            case 0:
                config.Slot0.kP = kP;
                config.Slot0.kI = kI;
                config.Slot0.kD = kD;
                break;
            case 1:
                config.Slot1.kP = kP;
                config.Slot1.kI = kI;
                config.Slot1.kD = kD;
                break;
            case 2:
                config.Slot2.kP = kP;
                config.Slot2.kI = kI;
                config.Slot2.kD = kD;
                break;
        }
    }

    public void configKs(double kS, int slotNumber) {
        switch (slotNumber) {
            case 0:
                config.Slot0.kS = kS;
                break;
            case 1:
                config.Slot1.kS = kS;
                break;
            case 2:
                config.Slot2.kS = kS;
                break;
        }
    }

    public void configKv(double kV, int slotNumber) {
        switch (slotNumber) {
            case 0:
                config.Slot0.kV = kV;
                break;
            case 1:
                config.Slot1.kV = kV;
                break;
            case 2:
                config.Slot2.kV = kV;
                break;
        }
    }

    public StatusCode applyConfig(TalonFXConfiguration config) {
        this.config = config;
        return super.getConfigurator().apply(config);
    }

    public StatusCode applyConfig() {
        return super.getConfigurator().apply(this.config);
    }

    public TalonFXConfiguration getConfig() {
        return this.config;
    }

    @Deprecated
    @Override
    public TalonFXConfigurator getConfigurator() {
        return super.getConfigurator();
    }


}
