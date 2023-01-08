package frc.lightningUtil.auto;

import java.util.HashMap;
import java.util.List;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConstants.Gains;
import frc.robot.Constants.DrivetrainConstants.ThetaGains;
import frc.robot.subsystems.Drivetrain;

/** Add your docs here. */
public class AutonomousCommandFactory {

	/**
	 * This method is gooing to create a swerve trajectory using pathplanners
	 * generated wpilib json files. Max veloxity, max acceleration, and reversed
	 * should be set when creating the paths in pathplanner
	 * 
	 * @param name name of the path in the 
	 */
	public static void makeTrajectory(String name, double maxVelocity, double maxAcceleration,
			PIDConstants driveConstants, PIDConstants thetaConstants, HashMap<String, Command> eventMap,
			Drivetrain drivetrain) {
		List<PathPlannerTrajectory> trajectory = PathPlanner.loadPathGroup(name, maxVelocity, maxVelocity);

		// PID controllers
		PIDConstants driveController = new PIDConstants(Gains.kP, Gains.kI, Gains.kD);
		PIDConstants thetaController = new PIDConstants(ThetaGains.kP, ThetaGains.kI, ThetaGains.kD);

		// adds generated swerve path to chooser
		SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(drivetrain::getPose,
				drivetrain::resetOdometry,
				drivetrain.getDriveKinematics(),
				driveController,
				thetaController,
				drivetrain::setStates,
				eventMap,
				drivetrain);

		Autonomous.register(name, autoBuilder.fullAuto(trajectory));
	}

}
