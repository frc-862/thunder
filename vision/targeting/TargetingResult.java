package frc.thunder.vision.targeting;

import com.fasterxml.jackson.databind.JsonNode;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.thunder.util.PoseConverter;

public class TargetingResult {

    private int pipeline;
    private double tl;
    private double cl;
    private double ts;
    private double v;
    private Pose3d botpose;
    private Pose3d botpose_wpired;
    private Pose3d botpose_wpiblue;

    //Barcode support doesn't appear to be implemeted
    private NeuralClassifierResult[] neuralClassifierResults;
    private NeuralDetectorResult[] neuralDetectorResults;
    private FiducialResult[] fiducialResults;
    private RetroreflectiveResult[] retroreflectiveResults;

    /**
     * @param jsonData the raw json dump from the limelight
     */
    public TargetingResult(JsonNode jsonData) {
        jsonData = jsonData.get("Results");
        this.neuralClassifierResults = new NeuralClassifierResult[jsonData.get("neural_classifier").size()];
        for (int i = 0; i < jsonData.get("neural_classifier").size(); i++) {
            this.neuralClassifierResults[i] = new NeuralClassifierResult(jsonData.get("neural_classifier").get(i));
        }

        this.neuralDetectorResults = new NeuralDetectorResult[jsonData.get("neural_detector").size()];
        for (int i = 0; i < jsonData.get("neural_detector").size(); i++) {
            this.neuralDetectorResults[i] = new NeuralDetectorResult(jsonData.get("neural_detector").get(i));
        }

        this.fiducialResults = new FiducialResult[jsonData.get("fiducial").size()];
        for (int i = 0; i < jsonData.get("fiducial").size(); i++) {
            this.fiducialResults[i] = new FiducialResult(jsonData.get("fiducial").get(i));
        }

        this.retroreflectiveResults = new RetroreflectiveResult[jsonData.get("retroreflective").size()];
        for (int i = 0; i < jsonData.get("retroreflective").size(); i++) {
            this.retroreflectiveResults[i] = new RetroreflectiveResult(jsonData.get("retroreflective").get(i));
        }

        this.pipeline = jsonData.get("pipeline").asInt();
        this.tl = jsonData.get("tl").asDouble();
        this.cl = jsonData.get("cl").asDouble();
        this.ts = jsonData.get("ts").asDouble();
        this.v = jsonData.get("v").asDouble();
        this.botpose = PoseConverter.toPose3d(jsonData.get("pose"));
        this.botpose_wpired = PoseConverter.toPose3d(jsonData.get("pose_wpired"));
        this.botpose_wpiblue = PoseConverter.toPose3d(jsonData.get("pose_wpiblue"));
    }

    /**
     * Equivalent to calling {@link frc.lib.util.Limelight#getPipeline()} assuming json data is up to date
     * @return Current pipeline index
     */
    public int getPipeline() {
        return pipeline;
    }
    
    /**
     * Equivalent to calling {@link frc.lib.util.Limelight#getPipelineLatency()} assuming json data is up to date
     * @return Targeting latency (milliseconds consumed by tracking loop this frame)
     */
    public double getPipelineLatency() {
        return tl;
    }

    /**
     * Equivalent to calling {@link frc.lib.util.Limelight#getCaptureLatency()} assuming json data is up to date
     * @return Capture latency (milliseconds between the end of the exposure of the middle row to the beginning of the tracking loop)
     */
    public double getCaptureLatency() {
        return cl;
    }

    /**
     * Unique to TargetingResult, should NOT be used with poseEstimator
     * @return Timestamp in milliseconds from boot.
     */
    public double getTimestamp() {
        return ts;
    }

    /**
     * Equivalent to calling {@link frc.lib.util.Limelight#hasTarget()} assuming json data is up to date
     * @return True if a target is detected
     */
    public boolean hasTarget() {
        return v == 1;
    }

    //TODO: COMMENTED OUT FOR NOW, NEED TO ARGUE WITH JUSNOOR ABOUT THIS
    // /**
    //  * Equivalent to calling {@link frc.lib.util.Limelight#getBotPose()} assuming json data is up to date
    //  * @return
    //  */
    // public Pose3d getBotpose() {
    //     return botpose;
    // }

    /**
     * Equivalent to calling {@link frc.lib.util.Limelight#getAlliancePose()} assuming json data is up to date
     * @return Pose3d of the robot relative to the appropriate alliance wall
     */
    public Pose3d getAlliancePose() {
        if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
            return botpose_wpiblue;
        } else {
            return botpose_wpired;
        }
    }

    /**
     * @return an array containing all active neural classifiers' results
     */
    public NeuralClassifierResult[] getNeuralClassifierResults() {
        return neuralClassifierResults;
    }

    /**
     * @return an array containing all active neural detectors' results
     */
    public NeuralDetectorResult[] getNeuralDetectorResults() {
        return neuralDetectorResults;
    }

    /**
     * @return an array containing each detected fiducial's results
     */
    public FiducialResult[] getFiducialResults() {
        return fiducialResults;
    }

    /**
     * @return an array containing each detected retroreflective target's results
     */
    public RetroreflectiveResult[] getRetroreflectiveResults() {
        return retroreflectiveResults;
    }
}
