package frc.thunder.testing;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;

public class SystemTest {

    private static ShuffleboardTab systemTestTab = Shuffleboard.getTab("SystemTest");;

    private static GenericEntry interrupt = systemTestTab.add("interrupt", false)
            .withWidget(BuiltInWidgets.kToggleButton).getEntry();

    private static final HashMap<String, Command> systemTests = new HashMap<>();

    /**
     * Registers a system test to be sent to the dashboard
     * 
     * @param name    title for the dashboard widget
     * @param command command to be run from the dashboard
     */
    public static void registerTest(String name, SystemTestCommand systemTestCmd) {
        systemTests.put(name, systemTestCmd.until(() -> interrupt.getBoolean(false)));
    }

    /**
     * Loads system tests to system test tab on shuffleboard
     */
    public static void loadTests() {
        for (Map.Entry<String, Command> entry : systemTests.entrySet()) {
            systemTestTab.add(entry.getKey(), entry.getValue());
        }
    }

}