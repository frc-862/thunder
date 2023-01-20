package frc.thunder.config;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class NeoConfig {
  public static CANSparkMax createMotor(int ID, boolean invert, int currentLimit, double voltageCompensation, MotorType motorType, IdleMode idleMode) {
    CANSparkMax motor = new CANSparkMax(ID, motorType);
    motor.restoreFactoryDefaults();
    motor.setInverted(invert);
    motor.setSmartCurrentLimit(currentLimit);
    motor.enableVoltageCompensation(voltageCompensation);
    motor.setIdleMode(idleMode);
    return motor;
  }
}