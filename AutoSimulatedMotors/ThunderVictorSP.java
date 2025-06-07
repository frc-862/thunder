package frc.thunder.AutoSimulatedMotors;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.PWM;

public class ThunderVictorSP extends ThunderPWMMotorController {

    /**
    * Constructor.
    *
    * @param channel The PWM channel that the Victor SP is attached to. 0-9 are on-board, 10-19
    *     are on the MXP port
    */
    @SuppressWarnings("this-escape")
    public ThunderVictorSP(final int channel) {
        super("VictorSP", channel);

        m_pwm.setBoundsMicroseconds(2004, 1520, 1500, 1480, 997);
        m_pwm.setPeriodMultiplier(PWM.PeriodMultiplier.k1X);
        m_pwm.setSpeed(0.0);
        m_pwm.setZeroLatch();

        HAL.report(tResourceType.kResourceType_VictorSP, getChannel() + 1);
    }
}