package frc.thunder.math;

import java.lang.Math;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;

public class OTFShootingMath {

    /**
     * Calculates the time of flight of a projectile given the initial velocity and distance from the target
     * @param velocity - initial  velocity of the projectile in meters per second (relative to the target)
     * @param launchAngle - angle of the projectile relative to the ground
     * @param distance - distance from the target to release point in meters
     * @return time of flight in seconds
     */
    public static double getTimeOfFlight(double velocity, Rotation2d launchAngle, double distance) {
        return distance / (launchAngle.getCos() * velocity);
    }

    /**
     * Calculates the distance the robot will be from the target when the projectile hits the target.
     * This is primarily useful if {@link #getProjectedPose(double, double, double)} is not accurate enough
     * @param velocity - initial velocity of the robot in meters per second (relative to the target)
     * @param acceleration - acceleration of the robot in meters per second squared (relative to the target)
     * @param timeOfFlight - time of flight in seconds
     * @return distance from the target to the robot in meters
     */
    public static double getProjectedDistance(double distance, double velocity, double acceleration, double timeOfFlight) {
        return distance + (velocity * timeOfFlight) + (0.5 * acceleration * Math.pow(timeOfFlight, 2));
    }

    /**
     * Calculates the projected position of the robot when the projectile hits the target
     * @param initialPose - initial position of the robot
     * @param velocity - initial velocity of the robot in meters per second (relative to the field) as a wpilib Vector
     * @param acceleration - acceleration of the robot in meters per second squared (relative to the field) as a wpilib Vector
     * @param timeOfFlight - time of flight in seconds
     * @return projected position of the robot when the projectile hits the target as a Pose2d (field-relative)
     * @see edu.wpi.first.math.VecBuilder
     */
    public static Pose2d getProjectedPose(Pose2d initialPose, Translation2d velocity, Translation2d acceleration, double timeOfFlight) {
        Transform2d offsetPose;
        double offsetX = (velocity.getX() * timeOfFlight) + (0.5 * acceleration.getX() * Math.pow(timeOfFlight, 2));
        double offsetY = (velocity.getY() * timeOfFlight) + (0.5 * acceleration.getY() * Math.pow(timeOfFlight, 2));
        offsetPose = new Transform2d(offsetX, offsetY, new Rotation2d());
        return initialPose.plus(offsetPose);
    }

    /**
     * Calculates the ideal angle to shoot the ball at to hit the target (assumes shot is a straight line, not an arc)
     * <ul>
     * <li>units don't matter as long as both parameters match</li>
     * </ul>
     * @param distance - distance from the target to the robot
     * @param targetHeight - height of the target
     * @return angle as a Rotation2d
     */
    public static Rotation2d getIdealShooterAngle(double distance, double targetHeight) {
        return new Rotation2d(distance, targetHeight);
    }

    /**
     * Calculates the ideal yaw of the ejector to point at the target
     * @param robotPose - current position of the robot as a Pose2d with meters (relative to the field)
     * @param targetPose - position of the target as a Pose2d with meters (relative to the field)
     * @return yaw as a field-relatice Rotation2d
     */
    public static Rotation2d getIdealRobotYaw(Pose2d robotPose, Pose2d targetPose) {
        return new Rotation2d(targetPose.getX() - robotPose.getX(), targetPose.getY() - robotPose.getY());
    }

    /**
     * a completely redundant function that michael fought hard for us not to include. Performs a coordinate transform on a vector by the given rotation
     * @param vector the vector to rotate, as a Translation2d
     * @param rot how much to rotate the vector by, as a Rotation2d
     * @return rotated vector, as a Translation2d
     * @see Translation2d#rotateBy(Rotation2d)
     */
    public static Translation2d vectorTransform(Translation2d vector, Rotation2d rot) {
        return vector.rotateBy(rot);
    }
}
