package frc.thunder.swervelib;

public interface SteerController {
    double getReferenceAngle();

    void setReferenceAngle(double referenceAngleRadians);

    double getStateAngle();

    void setMotorEncoderAngle();
}

