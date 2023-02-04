package frc.thunder.swervelib.rev;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

import frc.thunder.config.SparkMaxPIDGains;
import frc.thunder.swervelib.DriveController;
import frc.thunder.swervelib.DriveControllerFactory;
import frc.thunder.swervelib.ModuleConfiguration;
import frc.thunder.tuning.PIDDashboardTuner;

import static frc.thunder.swervelib.rev.RevUtils.checkNeoError;

public final class NeoDriveControllerFactoryBuilder {
    private double nominalVoltage = Double.NaN;
    private double currentLimit = Double.NaN;
    private double kP, kI, kD, FF;

    public NeoDriveControllerFactoryBuilder withVoltageCompensation(double nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
        return this;
    }

    public boolean hasVoltageCompensation() {
        return Double.isFinite(nominalVoltage);
    }

    public NeoDriveControllerFactoryBuilder withCurrentLimit(double currentLimit) {
        this.currentLimit = currentLimit;
        return this;
    }

    public NeoDriveControllerFactoryBuilder withPidConstants(SparkMaxPIDGains gains) {
        this.kP = gains.getKP();
        this.kI = gains.getKI();
        this.kD = gains.getKD();
        this.FF = gains.getFF();

        return this;
    }

    public boolean hasCurrentLimit() {
        return Double.isFinite(currentLimit);
    }

    public DriveControllerFactory<ControllerImplementation, Integer> build() {
        return new FactoryImplementation();
    }

    private class FactoryImplementation
            implements DriveControllerFactory<ControllerImplementation, Integer> {
        @Override
        public ControllerImplementation create(Integer id,
                ModuleConfiguration moduleConfiguration) {
            CANSparkMax motor = new CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless);
            motor.setInverted(moduleConfiguration.isDriveInverted());

            // Setup voltage compensation
            if (hasVoltageCompensation()) {
                checkNeoError(motor.enableVoltageCompensation(nominalVoltage),
                        "Failed to enable voltage compensation");
            }

            if (hasCurrentLimit()) {
                checkNeoError(motor.setSmartCurrentLimit((int) currentLimit),
                        "Failed to set current limit for NEO");
            }

            checkNeoError(
                    motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 100),
                    "Failed to set periodic status frame 0 rate");
            checkNeoError(
                    motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 20),
                    "Failed to set periodic status frame 1 rate");
            checkNeoError(
                    motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 20),
                    "Failed to set periodic status frame 2 rate");
            // Set neutral mode to brake
            motor.setIdleMode(CANSparkMax.IdleMode.kBrake);

            // Setup encoder
            RelativeEncoder encoder = motor.getEncoder();
            double positionConversionFactor = Math.PI * moduleConfiguration.getWheelDiameter()
                    * moduleConfiguration.getDriveReduction();
            encoder.setPositionConversionFactor(positionConversionFactor);
            encoder.setVelocityConversionFactor(positionConversionFactor / 60.0);

            return new ControllerImplementation(motor, encoder, kP, kI, kD, FF);
        }
    }

    private static class ControllerImplementation implements DriveController {
        private final CANSparkMax motor;
        private final SparkMaxPIDController controller;
        private final RelativeEncoder encoder;
        
        private ControllerImplementation(CANSparkMax motor, RelativeEncoder encoder, double kP, double kI, double kD,
                double FF) {
            this.motor = motor;
            this.encoder = encoder;

            this.controller = motor.getPIDController();
            controller.setP(kP);
            controller.setI(kI);
            controller.setD(kD);
            controller.setFF(FF);

        }

        @Override
        public void setReferenceSpeed(double speedMetersPerSecond) {
            controller.setReference(speedMetersPerSecond, ControlType.kVelocity);
        }

        @Override
        public double getStateVelocity() {
            return encoder.getVelocity();
        }

        @Override
        public double getStatePosition() {
            return encoder.getPosition();
        }
    }
}
