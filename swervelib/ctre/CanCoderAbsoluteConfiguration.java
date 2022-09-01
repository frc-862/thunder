package com.swervedrivespecialties.swervelib.ctre;

import com.swervedrivespecialties.swervelib.CanPort;

public class CanCoderAbsoluteConfiguration {
    private final CanPort port;
    private final double offset;

    public CanCoderAbsoluteConfiguration(CanPort port, double offset) {
        this.port = port;
        this.offset = offset;
    }

	public CanCoderAbsoluteConfiguration(int port, double offset) {
        this.port = new CanPort(port);
        this.offset = offset;
    }

    public CanPort getPort() {
        return port;
    }

    public double getOffset() {
        return offset;
    }
}
