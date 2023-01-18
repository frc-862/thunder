package frc.thunder.tuning;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PIDDashboardTuner extends SubsystemBase {

	private PIDController controller;

	private GenericEntry kPTuner;
	private GenericEntry kITuner;
	private GenericEntry kDTuner;

	/**
	 * Puts kP, kI, kD values on the dashboard to be tuned on the fly
	 * 
	 * @param name       name of the tab to put the PID Tuner on
	 * @param controller the PIDController to be tuned
	 */
	public PIDDashboardTuner(String name, PIDController controller) {
		this.controller = controller;

		ShuffleboardTab tab = Shuffleboard.getTab("Tuning");

		kPTuner = tab.add(name + "kP", controller.getP()).getEntry();
		kITuner = tab.add(name + "kI", controller.getI()).getEntry();
		kDTuner = tab.add(name + "kD", controller.getD()).getEntry();

	}

	@Override
	public void periodic() {
		controller.setP(kPTuner.getDouble(controller.getP()));
		controller.setI(kITuner.getDouble(controller.getI()));
		controller.setD(kDTuner.getDouble(controller.getD()));
	}

}
