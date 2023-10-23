package frc.thunder.vision.targeting;

import com.fasterxml.jackson.databind.JsonNode;

import edu.wpi.first.math.geometry.Pose3d;

public class NeuralDetectorResult extends NeuralClassifierResult {
    private Object points;
    private double area;
    private double tx;
    private double txp;
    private double ty;
    private double typ;

    // public NeuralDetectorResult(int pipeline, double targetLatency, double captureLatency, double timestamp, double hasTarget, Pose3d botpose, Pose3d botpose_wpired, Pose3d botpose_wpiblue, String className, int classId, double confidence, Object points, double area, double tx, double txp, double ty, double typ) {
    //     super(pipeline, targetLatency, captureLatency, timestamp, hasTarget, botpose, botpose_wpired, botpose_wpiblue, className, classId, confidence);
    //     this.points = points;
    //     this.area = area;
    //     this.tx = tx;
    //     this.txp = txp;
    //     this.ty = ty;
    //     this.typ = typ;
    // }

    public NeuralDetectorResult(JsonNode jsonData) {
        super(jsonData);
        this.points = jsonData.get("points");
        this.area = jsonData.get("area").asDouble();
        this.tx = jsonData.get("tx").asDouble();
        this.txp = jsonData.get("txp").asDouble();
        this.ty = jsonData.get("ty").asDouble();
        this.typ = jsonData.get("typ").asDouble();
    }

    public Object getPoints() {
        return points;
    }

    public double getArea() {
        return area;
    }

    public double getTx() {
        return tx;
    }

    public double getTxp() {
        return txp;
    }

    public double getTy() {
        return ty;
    }

    public double getTyp() {
        return typ;
    }
}
