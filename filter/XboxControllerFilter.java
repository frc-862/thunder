package frc.thunder.filter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import frc.thunder.math.LightningMath;

public class XboxControllerFilter {

    private XboxController controller;
    private filterMode mode;

    private double deadband;
    private double minPower;
    private double maxPower;
    private double rampDelta;
    private double lastOutput;

    private enum filterMode {
        CUBIC, SQUARED, LINEAR
    }

    /**
     * Constructor for XboxControllerFilter
     * @param controller XboxController to filter
     * @param deadBand  Deadband to apply to controller
     * @param minPower  Minimum power to apply to controller
     * @param maxPower  Maximum power to apply to controller
     * @param mode      Filter mode to apply to controller
     */
    public XboxControllerFilter(XboxController controller, double deadBand, double minPower, double maxPower, filterMode mode) {
        this.controller = controller;
        this.mode = mode;
        this.deadband = deadBand;
        this.minPower = minPower;
        this.maxPower = maxPower;
    }

    /**
     * Get the filtered left X value
     * @return Get the filtered left X value
     */
    public double getLeftX() {
        return filter(controller.getLeftX(), controller.getLeftY())[0];
    }

    /**
     * Get the filtered left Y value
     * @return Filtered left Y value
     */
    public double getLeftY() {
        return filter(controller.getLeftX(), controller.getLeftY())[1];
    }

    /**
     * Get the filtered right X value
     * @return Filtered right x value
     */
    public double getRightX() {
        return filter(controller.getRightX(), controller.getRightY())[0];
    }

    /**
     * Get the filtered right Y value
     * @return Filtered right Y value
     */
    public double getRightY() {
        return filter(controller.getRightX(), controller.getRightY())[1];
    }

    /**
     * Get the filtered x and y values
     * @param X X value to filter
     * @param Y Y value to filter
     * @return Returns a double[] with the filtered X and Y values
     */    
    private double[] filter(double X, double Y) {
        double xOutput = 0;
        double yOutput = 0;
        double magnitude = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        Rotation2d theta = Rotation2d.fromRadians(Math.atan2(Y, X));

        if (Math.abs(magnitude) < deadband) {
            xOutput = 0;
            yOutput = 0;
        } else {
            switch (mode) {
                case CUBIC:
                    xOutput = LightningMath.scale(Math.pow(magnitude, 3) * theta.getCos(), deadband, 1, minPower, maxPower);
                    yOutput = LightningMath.scale(Math.pow(magnitude, 3) * theta.getSin(), deadband, 1, minPower, maxPower);
                    break;
                case SQUARED:
                    xOutput = LightningMath.scale(Math.pow(magnitude, 2) * theta.getCos(), deadband, 1, minPower, maxPower);
                    yOutput = LightningMath.scale(Math.pow(magnitude, 2) * theta.getSin(), deadband, 1, minPower, maxPower);
                    break;
                case LINEAR:
                    xOutput = LightningMath.scale(magnitude * theta.getCos(), deadband, 1, minPower, maxPower);
                    yOutput = LightningMath.scale(magnitude * theta.getSin(), deadband, 1, minPower, maxPower);
                    break;
            }
        }

        return new double[] {MathUtil.clamp(xOutput, lastOutput - rampDelta, lastOutput + rampDelta), MathUtil.clamp(yOutput, lastOutput - rampDelta, lastOutput + rampDelta)};
    }

}