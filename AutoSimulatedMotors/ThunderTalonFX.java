// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.AutoSimulatedMotors;

import com.ctre.phoenix6.sim.ChassisReference;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;
import frc.robot.Robot;
import frc.thunder.hardware.ThunderBird;

/** Add your docs here. */
public class ThunderTalonFX extends ThunderBird{

    private TalonFXSimState simMotor;
    private Notifier notifier;

    public ThunderTalonFX(int deviceId, String canbus, boolean invert, double statorLimit, boolean brake) {
        super(deviceId, canbus, invert, statorLimit, brake);

        if (Robot.isSimulation()){
            simMotor = super.getSimState();

            simMotor.Orientation = invert ? ChassisReference.Clockwise_Positive
                : ChassisReference.CounterClockwise_Positive;
            
            LinearSystemSim<N2, N1, N2> physicsSim = new LinearSystemSim<N2, N1, N2>(LinearSystemId.createDCMotorSystem(
                0.55, 0.9));

            notifier = new Notifier(() -> {

                simMotor.setSupplyVoltage(RobotController.getBatteryVoltage());
                
                physicsSim.setInput(simMotor.getMotorVoltage());
                physicsSim.update(0.02);


                simMotor.setRotorVelocity(physicsSim.getOutput(1));
                simMotor.setRawRotorPosition(physicsSim.getOutput(0));
            });

            notifier.startPeriodic(0.02);
        } 
    }
    

    public void close() {
        if (notifier != null) {
            notifier.stop();
        }

        super.close();
    }
}
