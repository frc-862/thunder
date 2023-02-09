package frc.thunder.config;

public class SparkMaxPIDGains {

    private final double kP;
    private final double kI;
    private final double kD;
    private final double kF;

    public SparkMaxPIDGains(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public double getP() {
        return kP;
    }

    public double getI() {
        return kI;
    }

    public double getD() {
        return kD;
    }

    public double getF() {
        return kF;
    }

}
