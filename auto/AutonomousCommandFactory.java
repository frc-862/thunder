package frc.lightningUtil.auto;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.DrivetrainConstants.Gains;
import frc.robot.Constants.DrivetrainConstants.ThetaGains;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/** Add your docs here. */
public class AutonomousCommandFactory {

    /**
     * This method is gooing to create a swerve trajectory using pathplanners
     * generated wpilib json files. Max veloxity, max acceleration, and reversed
     * should be set when creating the paths in pathplanner
     * 
     * @param name name of the path in the deploy/pathplanner/generatedJSON
     *             folder (no ".wpilib.json")
     * @throws IOException
     */
    public static void makeTrajectory(String name, double maxVelocity, double maxAcceleration, Drivetrain drivetrain) {
        PathPlannerTrajectory trajectory = PathPlanner.loadPath(name, maxVelocity, maxVelocity);

        // PID controllers
        PIDController xController = new PIDController(Gains.kP, Gains.kI, Gains.kD);
        PIDController yController = new PIDController(Gains.kP, Gains.kI, Gains.kD);
        PIDController thetaController = new PIDController(ThetaGains.kP, ThetaGains.kI, ThetaGains.kD);

        // enables continuous input for the theta controller
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        // adds generated swerve path to chooser
        PPSwerveControllerCommand swerveCommand = new PPSwerveControllerCommand(trajectory,
                drivetrain::getPose,
                xController,
                yController,
                thetaController,
                drivetrain::setChassisSpeeds,
                drivetrain);

        Autonomous.register(name, new SequentialCommandGroup(
                new InstantCommand(() -> drivetrain.setInitialPose(trajectory.getInitialHolonomicPose(),
                        trajectory.getInitialHolonomicPose().getRotation())),
                swerveCommand));
    }

}
