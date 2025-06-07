// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.AutoSimulatedMotors;


import com.ctre.phoenix.motorcontrol.TalonSRXSimCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;
import frc.robot.Robot;

/** Add your docs here. */
public class ThunderTalonSRX extends TalonSRX{
    
    private TalonSRXSimCollection simMotor;
    private Notifier notifier;

    /**
     * Creates a new ThunderTalon.
     *
     * @param channel The PWM channel that the TalonSRX is connected to.
     */
    public ThunderTalonSRX(int channel) {
        super(channel);

        simMotor = super.getSimCollection();

        if (Robot.isSimulation()){
            simMotor = super.getSimCollection();

            
            LinearSystemSim<N2, N1, N2> physicsSim = new LinearSystemSim<N2, N1, N2>(LinearSystemId.createDCMotorSystem(
                0.55, 0.9));

            notifier = new Notifier(() -> {
                
                physicsSim.setInput(simMotor.getMotorOutputLeadVoltage());
                physicsSim.update(0.02);

                // Note: Inverts my cause weird behavior in simulation

                simMotor.setAnalogVelocity((int) Math.round(physicsSim.getOutput(0)));
            });

            notifier.startPeriodic(0.02);
        } 
    }

    public void close() {
        if (notifier != null) notifier.stop();
    }
}