package frc.lightningUtil.testing;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;

public class SystemTest {

    private ShuffleboardTab systemTestTab = Shuffleboard.getTab("SystemTest");

    public static void registerTest(String name, Command command) {
        Shuffleboard.getTab("systemTest").add(name, command);
    }

}