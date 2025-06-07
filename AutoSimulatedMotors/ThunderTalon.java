// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.AutoSimulatedMotors;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.PWM;

/** Add your docs here. */
public class ThunderTalon extends ThunderPWMMotorController {

    public ThunderTalon(final int channel) {
        super("Talon", channel);
    
        m_pwm.setBoundsMicroseconds(2037, 1539, 1513, 1487, 989);
        m_pwm.setPeriodMultiplier(PWM.PeriodMultiplier.k1X);
        m_pwm.setSpeed(0.0);
        m_pwm.setZeroLatch();
    
        HAL.report(tResourceType.kResourceType_Talon, getChannel() + 1);
    }
}
