// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.AutoSimulatedMotors;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.PWM;

/** Add your docs here. */
public class ThunderVictor extends ThunderPWMMotorController {

    /**
    * Constructor.
    *
    * @param channel The PWM channel that the Victor 888 is attached to. 0-9 are on-board, 10-19
    *     are on the MXP port
    */
    @SuppressWarnings("this-escape")
    public ThunderVictor(final int channel) {
        super("Victor", channel);

        m_pwm.setBoundsMicroseconds(2027, 1525, 1507, 1490, 1026);
        m_pwm.setPeriodMultiplier(PWM.PeriodMultiplier.k2X);
        m_pwm.setSpeed(0.0);
        m_pwm.setZeroLatch();

        HAL.report(tResourceType.kResourceType_Victor, getChannel() + 1);
  }
}