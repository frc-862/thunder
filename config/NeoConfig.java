package frc.thunder.config;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class NeoConfig {

    /**
     * Creates a CanSparkMax motor with the specified configuration so it can all be done in one
     * place
     * 
     * @param ID the device ID
     * @param invert the state of inversion
     * @param currentLimit the smart current limit to set to the motor
     * @param voltageCompensation the nominol voltage to compenstate the output to
     * @param motorType The motor type connected to the controller. Brushless motor wires must be
     *        connected to their matching colors and the hall sensor must be plugged in. Brushed
     *        motors must be connected to the Red and Black terminals only.
     * @param idleMode Idle mode (coast or brake)
     * 
     * @return The motor with the configured settings
     */
    public static CANSparkMax createMotor(int ID, boolean invert, int currentLimit,
            double voltageCompensation, MotorType motorType, IdleMode idleMode) {
        CANSparkMax motor = new CANSparkMax(ID, motorType);
        motor.restoreFactoryDefaults();
        motor.setInverted(invert);
        motor.setSmartCurrentLimit(currentLimit);
        motor.enableVoltageCompensation(voltageCompensation);
        motor.setIdleMode(idleMode);

        return motor;
    }

    /*
     * usage: SparkMaxPIDController PIDController; constructor() { PIDController =
     * NeoConfig.createPIDController(motor.getPIDController, kP, kI, kD, kF); }
     */

    /**
     * Creates a SparkMaxPIDController with the listed values
     * 
     * @param pidController the spark max pid contorller
     * @param p the kP value
     * @param i the kI value
     * @param d the kD value
     * @param ff the Feed Forward value
     * 
     * @return a spark max pid controller with a ff
     */
    public static SparkMaxPIDController createPIDController(SparkMaxPIDController pidController,
            double p, double i, double d, double ff) {
        createPIDController(pidController, p, i, d);
        pidController.setFF(ff);

        return pidController;
    }

    /**
     * Creates a SparkMaxPIDController with the listed values
     * 
     * @param pidController the spark max pid contorller
     * @param p the kP value
     * @param i the kI value
     * @param d the kD value
     * 
     * @return a spark max pid controller without a ff
     */
    public static SparkMaxPIDController createPIDController(SparkMaxPIDController pidController,
            double p, double i, double d) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);

        return pidController;
    }
}
