package frc.thunder.config;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class FalconConfig {

    /* Things Needed
    - CAN ID
    - idle Mode 
    - Canbus
        - Output Mode
    - Invert 
        - Current Limit ?



    */
    public static TalonFX createMotor(int ID, boolean invert, int supplyCurrentLimit, int stator, NeutralMode idleMode) {
        TalonFX motor = new TalonFX(1);

        motor.setInverted(invert);
        motor.setNeutralMode(idleMode);
        motor.configSupplyCurrentLimit(null, supplyCurrentLimit);
        
        return motor;
    }
}
