package frc.thunder.shuffleboard;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class LightningShuffleboard {
    public static void setDouble(String tabName, String key, double value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        //iterate through the components in the tab, check if the component exists
        for(int i = 0; i < tab.getComponents().size(); i++) {     
            if(tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;                
            }
        }

        //if the component exists, write to it using the NetworkTable, and if not, create the component
        if(hasComponent) {
            NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).setDouble(value);
        } else {
            tab.add(key, value);
        }
    }

    public static double getDouble(String tabName, String key, double defaultValue) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        //if the target exists, write to it using NTs, and if not, create the component
        for(int i = 0; i < tab.getComponents().size(); i++) {     
            if(tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;                
            }
        }

        //if the component exists, get it using the NetworkTable, and if not, create the component
        if(hasComponent) {
            return NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).getDouble(defaultValue);
        } else {
            tab.add(key, defaultValue);
            return defaultValue;
        }
    }

    public static void setString(String tabName, String key, String value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        //iterate through the components in the tab, check if the component exists
        for(int i = 0; i < tab.getComponents().size(); i++) {     
            if(tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;                
            }
        }

        //if the component exists, write to it using the NetworkTable, and if not, create the component
        if(hasComponent) {
            NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).setString(value);
        } else {
            tab.add(key, value);
        }
    }

    public static String getString(String tabName, String key, String defaultValue) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        //if the target exists, write to it using NTs, and if not, create the component
        for(int i = 0; i < tab.getComponents().size(); i++) {     
            if(tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;                
            }
        }

        //if the component exists, get it using the NetworkTable, and if not, create the component
        if(hasComponent) {
            return NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).getString(defaultValue);
        } else {
            tab.add(key, defaultValue);
            return defaultValue;
        }
    }

    public static void setBool(String tabName, String key, boolean value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        //iterate through the components in the tab, check if the component exists
        for(int i = 0; i < tab.getComponents().size(); i++) {     
            if(tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;                
            }
        }

        //if the component exists, write to it using the NetworkTable, and if not, create the component
        if(hasComponent) {
            NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).setBoolean(value);
        } else {
            tab.add(key, value);
        }
    }

    public static boolean getBool(String tabName, String key, boolean defaultValue) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        //if the target exists, write to it using NTs, and if not, create the component
        for(int i = 0; i < tab.getComponents().size(); i++) {     
            if(tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;                
            }
        }

        //if the component exists, get it using the NetworkTable, and if not, create the component
        if(hasComponent) {
            return NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).getBoolean(defaultValue);
        } else {
            tab.add(key, defaultValue);
            return defaultValue;
        }
    }
}
