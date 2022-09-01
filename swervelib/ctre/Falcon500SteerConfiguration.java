package com.swervedrivespecialties.swervelib.ctre;

import java.util.Objects;

import com.swervedrivespecialties.swervelib.CanPort;

public class Falcon500SteerConfiguration<EncoderConfiguration> {
    private final CanPort motorPort;
    private final EncoderConfiguration encoderConfiguration;

    public Falcon500SteerConfiguration(CanPort motorPort, EncoderConfiguration encoderConfiguration) {
        this.motorPort = motorPort;
        this.encoderConfiguration = encoderConfiguration;
    }

    public CanPort getMotorPort() {
        return motorPort;
    }

    public EncoderConfiguration getEncoderConfiguration() {
        return encoderConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Falcon500SteerConfiguration<?> that = (Falcon500SteerConfiguration<?>) o;
        return getMotorPort().equals(that.getMotorPort()) && getEncoderConfiguration().equals(that.getEncoderConfiguration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMotorPort(), getEncoderConfiguration());
    }

    @Override
    public String toString() {
        return "Falcon500SteerConfiguration{" +
                "motorPort=" + motorPort +
                ", encoderConfiguration=" + encoderConfiguration +
                '}';
    }
}
