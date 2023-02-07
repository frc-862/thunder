package frc.thunder.config;

public class SparkMaxPIDGains {
    
    private final double kP;
    private final double kI;
    private final double kD;
    private final double FF;

    public SparkMaxPIDGains(double kP, double kI, double kD, double FF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.FF = FF;
    }

    public double getKP() {
        return kP;
    }

    public double getKI() {
        return kI;
    }

    public double getKD() {
        return kD;
    }

    public double getFF() {
        return FF;
    }

}
