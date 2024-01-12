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
        TalonFX motor = new TalonFX(ID);

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
        TalonFX motor = new TalonFX(ID, canbus);

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
     * Create TalonFX motor without CAN loop name and 1 Pid slot
     * @param ID CAN ID
     * @param invert Boolean (true = clockwise positive, false = counterclockwise positive)
     * @param supplyCurrentLimit Input current limit from the pdh (zero to disable)
     * @param statorCurrentLimit Output current limit for the motor (zero to disable)
     * @param NeutralMode Brake or Coast
     * @param kP slot 0 kP
     * @param kI slot 0 kI
     * @param kD slot 0 kD
     * @return TalonFX motor
     */
    public static TalonFX createMotor(int ID, String CanBus, boolean invert, int supplyCurrentLimit, int statorCurrentLimit, NeutralModeValue NeutralMode, double kP, double kI, double kD) {
        TalonFX motor = new TalonFX(ID, CanBus);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.Inverted = invert ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
        config.CurrentLimits.SupplyCurrentLimitEnable = supplyCurrentLimit > 0;
        config.CurrentLimits.SupplyCurrentLimit = supplyCurrentLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = statorCurrentLimit > 0;
        config.CurrentLimits.StatorCurrentLimit = statorCurrentLimit;
        config.MotorOutput.NeutralMode = NeutralMode;
        //TODO: supply current limit thresholds

        config.Slot0.kP = kP;
        config.Slot0.kI = kI;
        config.Slot0.kD = kD;

        motor.getConfigurator().apply(config);
        
        return motor;
    }

    /**
     * Create TalonFX motor without CAN loop name and 2 Pid slot
     * @param ID CAN ID
     * @param invert Boolean (true = clockwise positive, false = counterclockwise positive)
     * @param supplyCurrentLimit Input current limit from the pdh (zero to disable)
     * @param statorCurrentLimit Output current limit for the motor (zero to disable)
     * @param NeutralMode Brake or Coast
     * @param kP0 slot 0 kP
     * @param kI0 slot 0 kI
     * @param kD0 slot 0 kD
     * @param kP1 slot 1 kP
     * @param kI1 slot 1 kI
     * @param kD1 slot 1 kD
     * @return TalonFX motor
     */
    public static TalonFX createMotor(int ID, String CanBus, boolean invert, int supplyCurrentLimit, int statorCurrentLimit, NeutralModeValue NeutralMode, double kP0, double kI0, double kD0, double kP1, double kI1, double kD1) {
        TalonFX motor = new TalonFX(ID, CanBus);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.Inverted = invert ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
        config.CurrentLimits.SupplyCurrentLimitEnable = supplyCurrentLimit > 0;
        config.CurrentLimits.SupplyCurrentLimit = supplyCurrentLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = statorCurrentLimit > 0;
        config.CurrentLimits.StatorCurrentLimit = statorCurrentLimit;
        config.MotorOutput.NeutralMode = NeutralMode;

        
        //TODO: supply current limit thresholds

        config.Slot0.kP = kP0;
        config.Slot0.kI = kI0;
        config.Slot0.kD = kD0;

        config.Slot1.kP = kP1;
        config.Slot1.kI = kI1;
        config.Slot1.kD = kD1;

        motor.getConfigurator().apply(config);
        
        return motor;
    }

    /**
     * Create TalonFX motor without CAN loop name and 1 Pid slot with kS and kV
     * @param ID CAN ID
     * @param invert Boolean (true = clockwise positive, false = counterclockwise positive)
     * @param supplyCurrentLimit Input current limit from the pdh (zero to disable)
     * @param statorCurrentLimit Output current limit for the motor (zero to disable)
     * @param NeutralMode Brake or Coast
     * @param kP slot 0 kP
     * @param kI slot 0 kI
     * @param kD slot 0 kD
     * @param kS slot 0 kS
     * @param kV slot 0 kV
     * @return TalonFX motor
     */
    public static TalonFX createMotor(int ID, String CanBus, boolean invert, int supplyCurrentLimit, int statorCurrentLimit, NeutralModeValue NeutralMode, double kP, double kI, double kD, double kS, double kV) {
        TalonFX motor = new TalonFX(ID, CanBus);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.Inverted = invert ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
        config.CurrentLimits.SupplyCurrentLimitEnable = supplyCurrentLimit > 0;
        config.CurrentLimits.SupplyCurrentLimit = supplyCurrentLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = statorCurrentLimit > 0;
        config.CurrentLimits.StatorCurrentLimit = statorCurrentLimit;
        config.MotorOutput.NeutralMode = NeutralMode;
        //TODO: supply current limit thresholds

        config.Slot0.kP = kP;
        config.Slot0.kI = kI;
        config.Slot0.kD = kD;
        config.Slot0.kS = kS;
        config.Slot0.kV = kV;


        motor.getConfigurator().apply(config);
        
        return motor;
    }

}
