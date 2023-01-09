package frc.lightningUtil.auto;

import java.util.HashMap;
import java.util.Set;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Class for registering and loading autonomous command to the shuffleboard for
 * choosing
 */
public class Autonomous {

    // Number of autonomous commands
    private static int autonCommandCount = 0;

    // Hashmap of possible autons
    private static HashMap<String, Command> autons = new HashMap<>();

    // Sendable chooser to select an auton from the dashboard
    private static SendableChooser<Command> chooser = new SendableChooser<>();

    /**
     * @return returns the hashmap of autonomous commands
     */
    public static HashMap<String, Command> getCommands() {
        return autons;
    }

    /**
     * Used to add commands to the autons hashmap to later be loaded onto the
     * dashboard
     * 
     * @param name name of the command to be displayed to the shuffleboard
     * @param cmd  the desired command to registerd
     */
    public static void register(String name, Command cmd) {
        autons.put(name, cmd);
    }

    /**
     * Load method should be called on robotInit to add all the desired commands to
     * the chooser and disply it on the dashboard
     */
    public static void load() {
        ShuffleboardTab tab = Shuffleboard.getTab("Autonomous");
        if (autons != null && !autons.isEmpty()) {
            Set<String> names = autons.keySet();
            for (var name : names) {
                loadRegisteredCommand(name, autons.get(name));
                System.out.println("Autonomous.load " + name);
            }
        }
        tab.add("Auto Mode", chooser);
    }

    /**
     * @return returns the currently selected autonomous command
     */
    public static Command getAutonomous() {
        return chooser.getSelected();
    }

    private static void loadRegisteredCommand(String name, Command command) {
        if (autonCommandCount == 0)
            chooser.setDefaultOption(name, command);
        else
            chooser.addOption(name, command);
        autonCommandCount++;
    }

}
