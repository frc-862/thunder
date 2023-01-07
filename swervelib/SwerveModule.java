package frc.lightningUtil.swervelib;

import edu.wpi.first.math.kinematics.SwerveModulePosition;

public interface SwerveModule {
    double getDriveVelocity();

    double getSteerAngle();

    SwerveModulePosition getDrivePosition();

    void set(double driveVoltage, double steerAngle);
}
