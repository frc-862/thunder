package frc.thunder.config;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.revrobotics.SparkMaxAnalogSensor.Mode;

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
            SparkMaxPIDGains gains) {
        pidController.setP(gains.getP());
        pidController.setI(gains.getI());
        pidController.setD(gains.getD());
        pidController.setFF(gains.getF());

        return pidController;
    }

    /**
     * Creates a SparkMaxAbsoluteEncoder with the listed values
     * 
     * @param motor the motor controller being used
     * @param inverted whether the encoder should be inverted
     * @param offset the zero offset to use on the encoder
     * @return a SparkMaxAbsoluteEncoder configured as specified
     */
    public static SparkMaxAbsoluteEncoder createAbsoluteEncoder(CANSparkMax motor, double offset) {
        SparkMaxAbsoluteEncoder encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        encoder.setZeroOffset(offset);
        return encoder;
    }

    /**
     * Creates a MotorFeedbackSensor with the listed values
     * 
     * @param encoder the encoder to configure
     * @param inverted whether the encoder is inverted
     * @return a MotorFeedbackSensor configured as specified
     */
    private static MotorFeedbackSensor createEncoder(MotorFeedbackSensor encoder) {
        return encoder;
    }

    /**
     * Creates a SparkMaxAnalogSensor with the listed values
     * 
     * @param motor the motor to attach the sensor to
     * @param mode the mode to use for the encoder (Absolute or Relative)
     * @param inverted whether the sensor is inverted
     * @return a SparkMaxAnalogSensor, configured as specified
     */
    public static SparkMaxAnalogSensor createAnalogSensor(CANSparkMax motor, Mode mode) {
        return (SparkMaxAnalogSensor) createEncoder(motor.getAnalog(mode));
    }

    /**
     * Creates a RelativeEncoder with the listed values.
     * 
     * BE CAREFUL USING
     * THIS,<a href= "https://docs.revrobotics.com/sparkmax/operating-modes/alternate-encoder-mode">
     * IT DISABLES LIMIT SWITCHES</a>
     * 
     * @param motor the motor to attach the sensor to
     * @param countsPerRev the counts per revolution of the encoder
     * @return a RelativeEncoder, configured as specified
     */
    public static RelativeEncoder createRelativeEncoder(CANSparkMax motor, int countsPerRev) {
        return (RelativeEncoder) createEncoder(motor.getAlternateEncoder(countsPerRev));
    }

    /**
     * Creates a RelativeEncoder with the listed values, using the built-in encoder
     * 
     * @param motor the motor controller being used
     * @param inverted whether the encoder is inverted
     * @return the builtin RelativeEncoder, configured as specified
     */
    public static RelativeEncoder createBuiltinEncoder(CANSparkMax motor) {
        return (RelativeEncoder) createEncoder(motor.getEncoder());
    }
}
