package frc.thunder.auto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import com.pathplanner.lib.auto.BaseAutoBuilder;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
<<<<<<< Updated upstream
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
=======
import com.pathplanner.lib.server.PathPlannerServer;
>>>>>>> Stashed changes

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Class used for making autonomous commands with pathplanner
 */
public class AutonomousCommandFactory {

	private final Supplier<Pose2d> getPose;
	private final Consumer<Pose2d> resetPose;
	private final SwerveDriveKinematics kinematics;
	private final PIDConstants driveConstants;
	private final PIDConstants thetaConstants;
	private final Consumer<SwerveModuleState[]> setStates;
	private final Subsystem[] drivetrain;

	/**
	 * Creates a new AutonomousCommandFactory
	 * 
	 * @param getPose        drivetrain pose supplier
	 * @param resetPose      used to reset drivetrain pose
	 * @param kinematics     swervedrive kinematics
	 * @param driveConstants drive motor PIDConstants
	 * @param thetaConstants rotational motor PIDConstants
	 * @param setStates      used to output module states
	 * @param drivetrain     subsystem drivetrain
	 */
	public AutonomousCommandFactory(Supplier<Pose2d> getPose, Consumer<Pose2d> resetPose,
			SwerveDriveKinematics kinematics, PIDConstants driveConstants, PIDConstants thetaConstants,
			Consumer<SwerveModuleState[]> setStates, Subsystem... drivetrain) {
		this.getPose = getPose;
		this.resetPose = resetPose;
		this.kinematics = kinematics;
		this.driveConstants = driveConstants;
		this.thetaConstants = thetaConstants;
		this.setStates = setStates;
		this.drivetrain = drivetrain;
	}

	/**
	 * Method to create autonomous trajectories.
	 * 
	 * @param name        name of the .path file from pathplanner
	 * @param eventMap    the hashmap of events for the path
	 * @param constraint  the constraint for the first part of the trajectory
	 * @param constraints the constraints for the remaining sections of the
	 *                    trajectory
	 */
	public void makeTrajectory(String name, HashMap<String, Command> eventMap, PathConstraints constraint,
			PathConstraints... constraints) {

		List<PathPlannerTrajectory> trajectory = PathPlanner.loadPathGroup(name, constraint, constraints);

		SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(getPose,
				resetPose,
				kinematics,
				thetaConstants,
				driveConstants,
				setStates,
				eventMap,
				drivetrain);

		Autonomous.register(name, autoBuilder.fullAuto(trajectory));
	}

<<<<<<< Updated upstream
	public Command createManualTrajectory(PathConstraints PathConstraints, PathPoint point1, PathPoint point2,
			PathPoint... points) {

		PathPlannerTrajectory trajectory = PathPlanner.generatePath(PathConstraints, point1, point2, points);

		return new PPSwerveControllerCommand(trajectory, getPose, kinematics,
				new PIDController(driveConstants.kP, driveConstants.kI, driveConstants.kD),
				new PIDController(driveConstants.kP, driveConstants.kI, driveConstants.kD),
				new PIDController(thetaConstants.kP, thetaConstants.kI, thetaConstants.kD), setStates,
				drivetrain);

=======
	public static void connectToServer(int ServerPort){
		PathPlannerServer.startServer(ServerPort);
>>>>>>> Stashed changes
	}

}
