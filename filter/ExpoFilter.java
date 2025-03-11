// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.filter;

public class ExpoFilter implements ValueFilter {

    protected double value;
    protected double exp;
    protected double remainder;

    public ExpoFilter(double exp) {
        this.exp = exp;
        this.remainder = 1 - exp;
    }

    @Override
    public void reset() {
        value = 0;
    }

    @Override
    public double filter(double value) {
        this.value = (this.value * remainder) + (value * exp);
        return this.value;
    }

    @Override
    public double get() {
        return this.value;
    }
}
