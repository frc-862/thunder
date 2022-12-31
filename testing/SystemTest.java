package frc.lightningUtil.testing;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SystemTest {

    private static final ShuffleboardTab tab = Shuffleboard.getTab("SystemTest"); 

    private static final NetworkTableEntry interrupt = tab.add("interrupt", false).getEntry();

    public static void registerTest(String name, CommandBase command) {
        tab.add(name, command.withInterrupt(() -> interrupt.getBoolean(false)));
    }

}