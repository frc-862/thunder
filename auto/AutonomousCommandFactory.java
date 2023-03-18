package frc.thunder.auto;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.thunder.pathplanner.com.pathplanner.lib.PathConstraints;
import frc.thunder.pathplanner.com.pathplanner.lib.PathPlanner;
import frc.thunder.pathplanner.com.pathplanner.lib.PathPlannerTrajectory;
import frc.thunder.pathplanner.com.pathplanner.lib.PathPoint;
import frc.thunder.pathplanner.com.pathplanner.lib.auto.PIDConstants;
import frc.thunder.pathplanner.com.pathplanner.lib.auto.SwerveAutoBuilder;
import frc.thunder.pathplanner.com.pathplanner.lib.commands.PPSwerveControllerCommand;
import frc.thunder.pathplanner.com.pathplanner.lib.server.PathPlannerServer;

/**
 * Class used for making autonomous commands with pathplanner
 */
public class AutonomousCommandFactory {

    private final Supplier<Pose2d> getPose;
    private final Consumer<Pose2d> resetPose;
    private final SwerveDriveKinematics kinematics;
    private final PIDConstants driveConstants;
    private final PIDConstants thetaConstants;
    private final PIDConstants poseConstants;
    private final Consumer<SwerveModuleState[]> setStates;
    private final Runnable resyncNeo;
    private final Subsystem[] drivetrain;

    /**
     * Creates a new AutonomousCommandFactory
     * 
     * @param getPose drivetrain pose supplier
     * @param resetPose used to reset drivetrain pose
     * @param kinematics swervedrive kinematics
     * @param driveConstants drive motor PIDConstants
     * @param thetaConstants rotational motor PIDConstants
     * @param setStates used to output module states
     * @param resyncNeo method to call and resync neo and abs encoders, will run on robot init
     * @param drivetrain subsystem drivetrain
     */
    public AutonomousCommandFactory(Supplier<Pose2d> getPose, Consumer<Pose2d> resetPose, SwerveDriveKinematics kinematics, PIDConstants driveConstants, PIDConstants thetaConstants,
            PIDConstants poseConstants, Consumer<SwerveModuleState[]> setStates, Runnable resyncNeo, Subsystem... drivetrain) {
        this.getPose = getPose;
        this.resetPose = resetPose;
        this.kinematics = kinematics;
        this.driveConstants = driveConstants;
        this.thetaConstants = thetaConstants;
        this.poseConstants = poseConstants;
        this.setStates = setStates;
        this.resyncNeo = resyncNeo;
        this.drivetrain = drivetrain;
    }

    /**
     * Method to create autonomous trajectories.
     * 
     * @param name name of the .path file from pathplanner
     * @param eventMap the hashmap of events for the path
     * @param constraint the constraint for the first part of the trajectory
     * @param constraints the constraints for the remaining sections of the trajectory
     */
    public void makeTrajectory(String name, HashMap<String, Command> eventMap, PathConstraints constraint, PathConstraints... constraints) {

        List<PathPlannerTrajectory> trajectory = PathPlanner.loadPathGroup(name, constraint, constraints);

        SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(getPose, resetPose, kinematics, driveConstants, thetaConstants, poseConstants, setStates, eventMap, drivetrain);

        Autonomous.register(name, autoBuilder.fullAuto(trajectory));
    }

    /**
     * Method to produce a manual trajectory using pahtplanners {@link PathPoint}
     * 
     * @param PathConstraints velocity and acceleration cap for the path to be run
     * @param point1 the starting point of the path
     * @param point2 the second point in the path
     * @param points list of more points to run in the path
     * 
     * @return a {@link PPSwerveControllerCommand} with all the listed path points
     */
    public void createManualTrajectory(PathConstraints PathConstraints, PathPoint point1, PathPoint point2, PathPoint... points) {

        PathPlannerTrajectory trajectory = PathPlanner.generatePath(PathConstraints, point1, point2, points);

        PPSwerveControllerCommand command = new PPSwerveControllerCommand(trajectory, getPose, kinematics, new PIDController(driveConstants.kP, driveConstants.kI, driveConstants.kD),
                new PIDController(driveConstants.kP, driveConstants.kI, driveConstants.kD), new PIDController(thetaConstants.kP, thetaConstants.kI, thetaConstants.kD),
                new PIDController(poseConstants.kP, poseConstants.kI, poseConstants.kD), setStates, drivetrain);

        command.schedule();

    }

    public PathPoint makePathPoint(double x, double y, double heading) {
        return new PathPoint(new Translation2d(x, y), new Rotation2d(heading));
    }

    public void resyncNeoEncoder() {
        resyncNeo.run();
        System.out.println("Resynced Neo Encoders to Absolute");
    }

    public static void connectToServer(int ServerPort) {
        PathPlannerServer.startServer(ServerPort);
    }
}
