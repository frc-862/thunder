package frc.thunder.util;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.Timer;

public class Pose4d extends Pose3d {
    double timestamp;
    double latency;

    public Pose4d() {
        super();
        this.latency = 0d;
    }

    /**
     * In general, translation units should be meters (but other units can work)
     */
    public Pose4d(Translation3d translation, Rotation3d rotation, double latency, double timestamp) {
        super(translation, rotation);
        this.latency = latency;
        this.timestamp = timestamp;
    }

    /**
     * In general, translation units should be meters (but other units can work)
     */
    public Pose4d(Translation3d translation, Rotation3d rotation, double latency) {
        super(translation, rotation);
        this.latency = latency;
        this.timestamp = Timer.getFPGATimestamp();
    }

    /**
     * In general, translation units should be meters (but other units can work)
     * Rotation units must be radians
     */
    public Pose4d(double x, double y, double z, double yaw, double pitch, double roll, double latency) {
        this(new Translation3d(x, y, z), new Rotation3d(yaw, pitch, roll), latency);
    }

    /**
     * In general, translation units should be meters (but other units can work)
     * Rotation units must be radians
     */
    public Pose4d(double x, double y, double z, double yaw, double pitch, double roll, double latency, double timestamp) {
        this(new Translation3d(x, y, z), new Rotation3d(yaw, pitch, roll), latency, timestamp);
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public double getFPGATimestamp() {
        return timestamp - latency;
    }
}
