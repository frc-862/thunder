package frc.thunder.util;

import java.util.ArrayList;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SystemFileWriter {
    ArrayList<Command> commands;
    
    /**
     * Write a series of keys and values to a file
     * @param keys the keys to use for the SmartDashboard (e.g. "FL CANCoder")
     * @param suppliers the suppliers to write to the file (e.g. drivetrain.getFLAngle())
     * @param filename the name/directory of the file to write to
     */
    public SystemFileWriter(String[] keys, Supplier<Double>[] suppliers, String filename) {
        for (int i = 0; i < keys.length; i++) {
            commands.add(new InstantCommand(/* TODO: write supplier to file */));
        }
    }

    /**
     * Write a series of keys and values to a default file
     * @param keys the keys to use for the SmartDashboard (e.g. "FL CANCoder")
     * @param suppliers the suppliers to write to the file (e.g. drivetrain.getFLAngle())
     */
    public SystemFileWriter(String[] keys, Supplier<Double>[] suppliers) {
        this(keys, suppliers, "insert default file name here"); /*TODO*/
    }

    public void publish() {
        commands.forEach(command -> SmartDashboard.putData(command));
    }
}