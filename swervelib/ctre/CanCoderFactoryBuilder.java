package frc.thunder.swervelib.ctre;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
// import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import frc.thunder.swervelib.AbsoluteEncoder;
import frc.thunder.swervelib.AbsoluteEncoderFactory;

public class CanCoderFactoryBuilder {
    private SensorDirectionValue direction = SensorDirectionValue.CounterClockwise_Positive;
    private int periodMilliseconds = 10;

    public CanCoderFactoryBuilder withReadingUpdatePeriod(int periodMilliseconds) {
        this.periodMilliseconds = periodMilliseconds;
        return this;
    }

    public CanCoderFactoryBuilder withDirection(SensorDirectionValue direction) {
        this.direction = direction;
        return this;
    }

    public AbsoluteEncoderFactory<CanCoderAbsoluteConfiguration> build() {
        return configuration -> {
            CANcoderConfiguration config = new CANcoderConfiguration();
            config.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
            //TODO: ^ This is improper, see below for old code
            // config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
            config.MagnetSensor.MagnetOffset = (configuration.getOffset() / (2*Math.PI));
            //TODO: Sanity Check this ^ properly matches radians to -1 - 1 range
            config.MagnetSensor.SensorDirection = direction;


            CANcoder encoder = new CANcoder(configuration.getId());
            CtreUtils.checkCtreError(encoder.getConfigurator().apply(config, 250),
                    "Failed to configure CANCoder");

            //TODO: find suitable phoenix 6 alternative to below code
            // CtreUtils.checkCtreError(encoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData,
            //         periodMilliseconds, 250), "Failed to configure CANCoder update rate");

            return new EncoderImplementation(encoder);
        };
    }

    private static class EncoderImplementation implements AbsoluteEncoder {
        private final CANcoder encoder;

        private EncoderImplementation(CANcoder encoder) {
            this.encoder = encoder;
        }

        @Override
        public double getAbsoluteAngle() {
            double angle = Math.toRadians(encoder.getAbsolutePosition().getValue());
            angle %= 2.0 * Math.PI;
            if (angle < 0.0) {
                angle += 2.0 * Math.PI;
            }

            return angle;
        }
    }
}
