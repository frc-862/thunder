package frc.thunder.config;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class FalconConfig {

    /**
     * Create TalonFX motor without CAN loop name
     * @param ID CAN ID
     * @param invert Boolean (true = clockwise positive, false = counterclockwise positive)
     * @param supplyCurrentLimit Input current limit from the pdh (zero to disable)
     * @param statorCurrentLimit Output current limit for the motor (zero to disable)
     * @param NeutralMode Brake or Coast
     * @return TalonFX motor
     */
    public static TalonFX createMotor(int ID, boolean invert, int supplyCurrentLimit, int statorCurrentLimit, NeutralModeValue NeutralMode) {
        TalonFX motor = new TalonFX(1);
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.Inverted = invert ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
        config.CurrentLimits.SupplyCurrentLimitEnable = supplyCurrentLimit > 0;
        config.CurrentLimits.SupplyCurrentLimit = supplyCurrentLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = statorCurrentLimit > 0;
        config.CurrentLimits.StatorCurrentLimit = statorCurrentLimit;
        config.MotorOutput.NeutralMode = NeutralMode;
        //TODO: supply current limit thresholds

        motor.getConfigurator().apply(config);
        
        return motor;
    }

    /**
     * Create TalonFX motor without CAN loop name
     * @param ID CAN ID
     * @param canbus CanBus
     * @param invert Boolean
     * @param supplyCurrentLimit Input current limit from the pdh
     * @param statorCurrentLimit Output current limit for the motor
     * @param NeutralMode Brake or Coast
     * @return TalonFX motor
     */
    public static TalonFX createMotor(int ID, String canbus, boolean invert, int supplyCurrentLimit, int statorCurrentLimit, NeutralModeValue NeutralMode) {
        TalonFX motor = new TalonFX(1, canbus);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.Inverted = invert ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
        config.CurrentLimits.SupplyCurrentLimitEnable = supplyCurrentLimit > 0;
        config.CurrentLimits.SupplyCurrentLimit = supplyCurrentLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = statorCurrentLimit > 0;
        config.CurrentLimits.StatorCurrentLimit = statorCurrentLimit;
        config.MotorOutput.NeutralMode = NeutralMode;
        //TODO: supply current limit thresholds

        motor.getConfigurator().apply(config);
        
        return motor;
    }

}
