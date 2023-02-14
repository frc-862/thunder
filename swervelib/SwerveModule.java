package frc.thunder.swervelib;

import edu.wpi.first.math.kinematics.SwerveModulePosition;

public interface SwerveModule {
    double getDriveVelocity();

    double getSteerAngle();

    double getDriveVoltage();

    SwerveModulePosition getPosition();

    void set(double speedMetersPerSecond, double steerAngle);

    void setEncoderAngle();

    double getDriveTemperature();

    double getSteerTemperature();
}

