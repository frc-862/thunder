package frc.thunder.tuning;

// import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PIDDashboardTuner extends SubsystemBase {

    private PIDController pidController;
    private ProfiledPIDController profiledPIDController;
//     private SparkMaxPIDController sparkMaxPIDController;

    private GenericEntry kPTuner;
    private GenericEntry kITuner;
    private GenericEntry kDTuner;
    private GenericEntry kFFTuner;

    private double kP;
    private double kI;
    private double kD;
    private double kFF;

    private static int tunerNumber = 0;

    /**
     * Puts kP, kI, kD values on the dashboard to be tuned on the fly
     * 
     * @param name name of the tab to put the PID Tuner on
     * @param controller the PIDController to be tuned
     */
    public PIDDashboardTuner(String name, PIDController pidController) {
        this.pidController = pidController;
        tunerNumber++;

        ShuffleboardTab tab = Shuffleboard.getTab("PID Tuning");

        if (tunerNumber <= 5) {
            kPTuner = tab.add(name + "kP", pidController.getP()).withPosition(0, tunerNumber)
                    .getEntry();
            kITuner = tab.add(name + "kI", pidController.getI()).withPosition(1, tunerNumber)
                    .getEntry();
            kDTuner = tab.add(name + "kD", pidController.getD()).withPosition(2, tunerNumber)
                    .getEntry();

        } else {
            kPTuner = tab.add(name + "kP", pidController.getP()).withPosition(3, tunerNumber)
                    .getEntry();
            kITuner = tab.add(name + "kI", pidController.getI()).withPosition(4, tunerNumber)
                    .getEntry();
            kDTuner = tab.add(name + "kD", pidController.getD()).withPosition(5, tunerNumber)
                    .getEntry();
        }

    }

    /**
     * Puts kP, kI, kD values on the dashboard to be tuned on the fly
     * 
     * @param name name of the tab to put the PID Tuner on
     * @param profiledPIDController the PIDController to be tuned
     */
    public PIDDashboardTuner(String name, ProfiledPIDController profiledPIDController) {
        this.profiledPIDController = profiledPIDController;
        tunerNumber++;

        ShuffleboardTab tab = Shuffleboard.getTab("PID Tuning");

        if (tunerNumber <= 5) {
            kPTuner = tab.add(name + "kP", profiledPIDController.getP())
                    .withPosition(0, tunerNumber).getEntry();
            kITuner = tab.add(name + "kI", profiledPIDController.getI())
                    .withPosition(1, tunerNumber).getEntry();
            kDTuner = tab.add(name + "kD", profiledPIDController.getD())
                    .withPosition(2, tunerNumber).getEntry();

        } else {
            kPTuner = tab.add(name + "kP", profiledPIDController.getP())
                    .withPosition(3, tunerNumber).getEntry();
            kITuner = tab.add(name + "kI", profiledPIDController.getI())
                    .withPosition(4, tunerNumber).getEntry();
            kDTuner = tab.add(name + "kD", profiledPIDController.getD())
                    .withPosition(5, tunerNumber).getEntry();
        }

    }

    /**
     * Puts kP, kI, kD values on the dashboard to be tuned on the fly
     * 
     * @param name name of the tab to put the PID Tuner on
     * @param controller the PIDController to be tuned
     */
    /* public PIDDashboardTuner(String name, SparkMaxPIDController sparkMaxPIDController) {
        this.sparkMaxPIDController = sparkMaxPIDController;
        tunerNumber++;

        ShuffleboardTab tab = Shuffleboard.getTab("PID Tuning");

        if (tunerNumber <= 5) {
            kPTuner = tab.add(name + "kP", sparkMaxPIDController.getP())
                    .withPosition(0, tunerNumber).getEntry();
            kITuner = tab.add(name + "kI", sparkMaxPIDController.getI())
                    .withPosition(1, tunerNumber).getEntry();
            kDTuner = tab.add(name + "kD", sparkMaxPIDController.getD())
                    .withPosition(2, tunerNumber).getEntry();
            kFFTuner = tab.add(name + "kF", sparkMaxPIDController.getFF())
                    .withPosition(3, tunerNumber).getEntry();

        } else {
            kPTuner = tab.add(name + "kP", sparkMaxPIDController.getP())
                    .withPosition(3, tunerNumber).getEntry();
            kITuner = tab.add(name + "kI", sparkMaxPIDController.getI())
                    .withPosition(4, tunerNumber).getEntry();
            kDTuner = tab.add(name + "kD", sparkMaxPIDController.getD())
                    .withPosition(5, tunerNumber).getEntry();
            kFFTuner = tab.add(name + "kF", sparkMaxPIDController.getFF())
                    .withPosition(6, tunerNumber).getEntry();
        }

    } */

    private void updateGains() {
        kP = kPTuner.getDouble(0);
        kI = kPTuner.getDouble(0);
        kD = kPTuner.getDouble(0);
        kFF = kPTuner.getDouble(0);
    }

    private boolean checkGains() {
        return kP != kPTuner.getDouble(0) || kI != kITuner.getDouble(0)
                || kD != kDTuner.getDouble(0) || kFF != kFFTuner.getDouble(0);
    }

    @Override
    public void periodic() {

        if (pidController != null && checkGains()) {
            pidController.setP(kPTuner.getDouble(pidController.getP()));
            pidController.setI(kITuner.getDouble(pidController.getI()));
            pidController.setD(kDTuner.getDouble(pidController.getD()));

        } else if (profiledPIDController != null && checkGains()) {
            profiledPIDController.setP(kPTuner.getDouble(profiledPIDController.getP()));
            profiledPIDController.setI(kITuner.getDouble(profiledPIDController.getI()));
            profiledPIDController.setD(kDTuner.getDouble(profiledPIDController.getD()));

        } /*else if (sparkMaxPIDController != null && checkGains()) {
            sparkMaxPIDController.setP(kPTuner.getDouble(sparkMaxPIDController.getP()));
            sparkMaxPIDController.setI(kITuner.getDouble(sparkMaxPIDController.getI()));
            sparkMaxPIDController.setD(kDTuner.getDouble(sparkMaxPIDController.getD()));
            sparkMaxPIDController.setFF(kFFTuner.getDouble(sparkMaxPIDController.getFF()));

        } */

        updateGains();
    }

}
