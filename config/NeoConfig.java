package frc.thunder.config;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
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
  /*
   * usage: 
   * SparkMaxPIDController PIDController;
   * constructor() {
   *  PIDController = NeoConfig.createPIDController(motor.getPIDController, kP, kI, kD, kF);
   * }
   */
  public static SparkMaxPIDController createPIDController(SparkMaxPIDController pidController, double p, double i, double d, double ff) {
        createPIDController(pidController, p, i, d);
        pidController.setFF(ff);
        return pidController;
  }
  public static SparkMaxPIDController createPIDController(SparkMaxPIDController pidController, double p, double i, double d) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);
        return pidController;
  }
}