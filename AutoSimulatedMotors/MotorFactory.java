// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.AutoSimulatedMotors;

/** Add your docs here. */
public class MotorFactory {

    public static ThunderVictor getVictor(int channel) {
        return new ThunderVictor(channel);
    }

    public static ThunderTalonSRX getTalonSRX(int channel) {
        return new ThunderTalonSRX(channel);
    }

    public static ThunderVictorSPX getVictorSPX(int channel) {
        return new ThunderVictorSPX(channel);
    }

    public static ThunderTalon getTalon(int channel) {
        return new ThunderTalon(channel);
    }

    public static ThunderTalonFX getTalonFX(int deviceId, String canbus, boolean invert, double statorLimit, boolean brake) {
        return new ThunderTalonFX(deviceId, canbus, invert, statorLimit, brake);
    }

    public static ThunderTalonFX getTalonFX(int deviceId, String canbus){
        return getTalonFX(deviceId, canbus, false, 40, false);
    }

    public static ThunderTalonFX getTalonFX(int deviceId) {
        return getTalonFX(deviceId, "Canivore");
    }
  
    public static ThunderVictorSP getVictorSP(int channel) {
        return new ThunderVictorSP(channel);
    }
}
