package frc.thunder.tuning;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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

		var tab = Shuffleboard.getTab(name);

		kPTuner = tab.add("kP", 0d).getEntry();
		kITuner = tab.add("kI", 0d).getEntry();
		kDTuner = tab.add("kD", 0d).getEntry();

		kPTuner.setDouble(controller.getP());
		kITuner.setDouble(controller.getI());
		kDTuner.setDouble(controller.getD());

	}

	@Override
	public void periodic() {
		controller.setP(kPTuner.getDouble(controller.getP()));
		controller.setI(kITuner.getDouble(controller.getI()));
		controller.setD(kDTuner.getDouble(controller.getD()));

	}

}
