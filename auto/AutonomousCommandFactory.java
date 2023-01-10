package frc.thunder.auto;

import java.util.HashMap;
import java.util.List;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

/** Add your docs here. */
public class AutonomousCommandFactory {

	/**
	 * This method is gooing to create a swerve trajectory using pathplanners
	 * generated wpilib json files. Max veloxity, max acceleration, and reversed
	 * should be set when creating the paths in pathplanner.
	 * 
	 * @param name            name of the pathplanner path
	 * @param maxVelocity     the max velocity for the path
	 * @param maxAcceleration the max accelertaion for the path
	 * @param driveConstants  PIDConstants for the drive controller
	 * @param thetaConstants  PIDConstants for the theta controller
	 * @param eventMap        hashmap to run the markers in pathplanner
	 * @param drivetrain      the drivetrain subsystem to be required
	 */
	public static void makeTrajectory(String name, double maxVelocity, double maxAcceleration,
			PIDConstants driveConstants, PIDConstants thetaConstants, HashMap<String, Command> eventMap,
			Drivetrain drivetrain) {

		List<PathPlannerTrajectory> trajectory = PathPlanner.loadPathGroup(name, maxVelocity, maxVelocity);

		SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(drivetrain::getPose,
				drivetrain::resetOdometry,
				drivetrain.getDriveKinematics(),
				driveConstants,
				thetaConstants,
				drivetrain::setStates,
				eventMap,
				drivetrain);

		Autonomous.register(name, autoBuilder.fullAuto(trajectory));
	}

}
