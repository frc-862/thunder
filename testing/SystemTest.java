package frc.lightningUtil.testing;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SystemTest {

    private static ShuffleboardTab systemTestTab = Shuffleboard.getTab("SystemTest"); ;

    private static NetworkTableEntry interrupt = systemTestTab.add("interrupt", false).withWidget(BuiltInWidgets.kToggleButton).getEntry();
    

    private static final HashMap<String, CommandBase> systemTests = new HashMap<>();

    public static void registerTest(String name, CommandBase command) {
        systemTests.put(name, command.withInterrupt(() -> interrupt.getBoolean(false)));
    }
 
    public static void loadTests() {
        for(Map.Entry<String, CommandBase> entry : systemTests.entrySet()) {
            systemTestTab.add(entry.getKey(), entry.getValue());
        }
    }

}