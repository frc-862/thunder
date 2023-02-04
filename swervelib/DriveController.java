package frc.thunder.swervelib;

public interface DriveController {
    void setReferenceSpeed(double speedMetersPerSecond);

    double getStateVelocity();

    double getStatePosition();
}
