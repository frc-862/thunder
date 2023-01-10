package frc.thunder.auto;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

/** Add your docs here. */
public class AutonomousCommandFactory {

	private final Supplier<Pose2d> getPose;
	private final Consumer<Pose2d> resetPose;
	private final SwerveDriveKinematics kinematics;
	private final PIDConstants driveConstants;
	private final PIDConstants thetaConstants;
	private final Consumer<SwerveModuleState[]> setStates;
	private final Subsystem[] drivetrain;

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

}
