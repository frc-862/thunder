package frc.thunder.util;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.trajectory.constraint.RectangularRegionConstraint;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.VisionConstants;

public class Pose4d extends Pose3d {
    double timestamp;
    double latency;
    int tag_count = 0;
    double tag_span = 0;
    double distance = Double.POSITIVE_INFINITY;
    double area = 0;

    public Pose4d() {
        super();
        this.latency = 0d;
    }

    /**
     * In general, translation units should be meters (but other units can work)
     */
    public Pose4d(Translation3d translation, Rotation3d rotation, double latency,
            double timestamp) {
        super(translation, rotation);
        this.latency = latency;
        this.timestamp = timestamp;
        this.distance = 0;
    }

    /**
     * In general, translation units should be meters (but other units can work)
     */
    public Pose4d(Translation3d translation, Rotation3d rotation, double latency) {
        super(translation, rotation);
        this.latency = latency;
        this.timestamp = Timer.getFPGATimestamp();
        this.distance = 0;
    }

    /**
     * In general, translation units should be meters (but other units can work)
     * Rotation units must
     * be radians
     */
    public Pose4d(double x, double y, double z, double yaw, double pitch, double roll,
            double latency) {
        this(new Translation3d(x, y, z), new Rotation3d(yaw, pitch, roll), latency);
    }

    /**
     * In general, translation units should be meters (but other units can work)
     * Rotation units must
     * be radians
     */
    public Pose4d(double x, double y, double z, double yaw, double pitch, double roll,
            double latency, double timestamp) {
        this(new Translation3d(x, y, z), new Rotation3d(yaw, pitch, roll), latency, timestamp);
    }

    /**
     * Build Pose4d from limelight getpose and a timestamp
     */
    public Pose4d(double[] ntValues, double timestamp) {
        super(new Translation3d(ntValues[0], ntValues[1], ntValues[2]),
                new Rotation3d(Math.toRadians(ntValues[3]), Math.toRadians(ntValues[4]), Math.toRadians(ntValues[5])));
        latency = ntValues[6];
        tag_count = (int) ntValues[7];
        tag_span = ntValues[8];
        distance = ntValues[9];
        area = ntValues[10];
        this.timestamp = timestamp;
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public double getFPGATimestamp() {
        return timestamp - latency / 1000d;
    }

    public double getDistance() {
        return distance;
    }

    public boolean getMoreThanOneTarget() {
        return tag_count > 1;
    }

    public double getConfidence() {
        double confidence = 18.0;

        if (getMoreThanOneTarget() && getDistance() < 3) {
            confidence = 0.5;
        } else if (getMoreThanOneTarget()) {
            confidence = 0.5 + ((getDistance() - 3) / 5 * 18);
        } else if (getDistance() < 2) {
            confidence = 1.5 + (getDistance() / 2 * 5.0);
        }

        return confidence;
    }

    static RectangularRegionConstraint FIELD = new RectangularRegionConstraint(
            new Translation2d(0, 0), VisionConstants.FIELD_LIMIT, null);

    public boolean trust() {
        return (getX() != 0 && getY() != 0) && distance < 5 && FIELD.isPoseInRegion(toPose2d());
    }

    public Matrix<N3, N1> getStdDevs() {
        var confidence = getConfidence();
        if(DriverStation.isDisabled()) {
            return VecBuilder.fill(confidence, confidence, Math.toRadians(confidence));
        }
        return VecBuilder.fill(confidence, confidence, Math.toRadians(500 * confidence));
    }
}
