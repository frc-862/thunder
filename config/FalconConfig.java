package frc.thunder.config;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class FalconConfig {

    /**
     * Create TalonFX motor without CAN loop name
     * @param ID CAN ID
     * @param invert Boolean
     * @param supplyCurrentLimit Input current limit from the pdh
     * @param statorCurrentLimit Output current limit for the motor
     * @param NeutralMode Brake or Coast
     * @return TalonFX motor
     */
    public static TalonFX createMotor(int ID, boolean invert, int supplyCurrentLimit, int statorCurrentLimit, NeutralMode NeutralMode) {
        TalonFX motor = new TalonFX(1);

        motor.setInverted(invert);
        motor.setNeutralMode(NeutralMode);
        motor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, supplyCurrentLimit, supplyCurrentLimit, .25), 250);
        motor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, statorCurrentLimit, statorCurrentLimit, .25), 250);
        
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
    public static TalonFX createMotor(int ID, String canbus, boolean invert, int supplyCurrentLimit, int statorCurrentLimit, NeutralMode NeutralMode) {
        TalonFX motor = new TalonFX(1, canbus);

        motor.setInverted(invert);
        motor.setNeutralMode(NeutralMode);
        motor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, supplyCurrentLimit, supplyCurrentLimit, .25), 250);
        motor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, statorCurrentLimit, statorCurrentLimit, .25), 250);
        
        return motor;
    }

}
