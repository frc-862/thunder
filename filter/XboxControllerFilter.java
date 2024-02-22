package frc.thunder.filter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import frc.thunder.math.LightningMath;

public class XboxControllerFilter extends XboxController{

    private filterMode mode;

    private double deadband;
    private double minPower;
    private double maxPower;
    private double rampDelta = 1;
    private double lastOutputX = 0;
    private double lastOutputY = 0;

    public enum filterMode {
        CUBIC, QUADRATIC, LINEAR
    }

    /**
     * Constructor for XboxControllerFilter
     * @param port      Port of controller
     * @param deadBand  Deadband to apply to controller
     * @param minPower  Minimum power to apply to controller
     * @param maxPower  Maximum power to apply to controller
     * @param mode      Filter mode to apply to controller
     */
    public XboxControllerFilter(int port, double deadBand, double minPower, double maxPower, filterMode mode) {
        super(port);
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
        return filter(super.getLeftX(), super.getLeftY())[0];
    }

    /**
     * Get the filtered left Y value
     * @return Filtered left Y value
     */
    public double getLeftY() {
        return filter(super.getLeftX(), super.getLeftY())[1];
    }

    /**
     * Get the filtered right X value
     * @return Filtered right x value
     */
    public double getRightX() {
        return filter(super.getRightX(), super.getRightY())[0];
    }

    /**
     * Get the filtered right Y value
     * @return Filtered right Y value
     */
    public double getRightY() {
        return filter(super.getRightX(), super.getRightY())[1];
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
        double magnitude = Math.hypot(X, Y);
        Rotation2d theta = Rotation2d.fromRadians(Math.atan2(Y, X));

        if (Math.abs(magnitude) < deadband) {
            xOutput = 0;
            yOutput = 0;
        } else {
            switch (mode) {
                case CUBIC:
                    xOutput = Math.pow(magnitude, 3) * theta.getCos();
                    yOutput = Math.pow(magnitude, 3) * theta.getSin();
                    break;
                case QUADRATIC:
                    xOutput = LightningMath.scale(Math.pow(magnitude, 2) * theta.getCos(), deadband, 1, minPower, maxPower);
                    yOutput = LightningMath.scale(Math.pow(magnitude, 2) * theta.getSin(), deadband, 1, minPower, maxPower);
                    break;
                case LINEAR:
                double u2 = X * X;
                double v2 = Y * Y;
                double twosqrt2 = 2.0 * Math.sqrt(2.0);
                double subtermx = 2.0 + u2 - v2;
                double subtermy = 2.0 - u2 + v2;
                double termx1 = subtermx + X * twosqrt2;
                double termx2 = subtermx - X * twosqrt2;
                double termy1 = subtermy + Y * twosqrt2;
                double termy2 = subtermy - Y * twosqrt2;
                xOutput = 0.5 * Math.sqrt(termx1) - 0.5 * Math.sqrt(termx2);
                yOutput = 0.5 * Math.sqrt(termy1) - 0.5 * Math.sqrt(termy2);
                   
            }
        }

        var result = new double[] {MathUtil.clamp(xOutput, lastOutputX - rampDelta, lastOutputX + rampDelta), MathUtil.clamp(yOutput, lastOutputY - rampDelta, lastOutputY + rampDelta)};
        lastOutputX = result[0];
        lastOutputY = result[1];

        System.out.println("xoutput " +  xOutput);   
        System.out.println("youtput " + yOutput);
        // System.out.println("theta " + theta.getDegrees());

        return result;
    }
}
