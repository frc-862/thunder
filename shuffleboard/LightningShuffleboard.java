/*
* Lightning Shuffleboard - a simple, easy-to-use shuffleboard library for FRC teams
* Copyright (C) 2024 Lightning Robotics
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package frc.thunder.shuffleboard;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class LightningShuffleboard {
    /**
     * Creates and sets a double to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote must be called periodically to update
     */
    public static void setDouble(String tabName, String key, double value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, write to it using the NetworkTable, and if not, create the component
        if (hasComponent) {
            NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).setDouble(value);
        } else {
            tab.add(key, value);
        }
    }

    /**
     * Creates and sets a boolean to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote must be called periodically to update
     */
    public static void setBool(String tabName, String key, boolean value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, write to it using the NetworkTable, and if not, create the component
        if (hasComponent) {
            NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).setBoolean(value);
        } else {
            tab.add(key, value);
        }
    }

    /**
     * Creates and sets a string to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote must be called periodically to update
     */
    public static void setString(String tabName, String key, String value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, write to it using the NetworkTable, and if not, create the component
        if (hasComponent) {
            NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).setString(value);
        } else {
            tab.add(key, value);
        }
    }

    /**
     * Creates and sets a double supplier to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote will update automatically
     */
    public static void setDoubleSupplier(String tabName, String key, DoubleSupplier value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component doesnt exist, create it (since its a supplier, it will be updated automatically)
        if (!hasComponent) {
            tab.addDouble(key, value);
        }
    }

    /**
     * Creates and sets a boolean supplier to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote will update automatically
     */
    public static void setBoolSupplier(String tabName, String key, BooleanSupplier value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component doesnt exist, create it (since its a supplier, it will be updated automatically)
        if (!hasComponent) {
            tab.addBoolean(key, value);
        }
    }

    /**
     * Creates and sets a string supplier to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote will update automatically
     */
    public static void setStringSupplier(String tabName, String key, Supplier<String> value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component doesnt exist, create it (since its a supplier, it will be updated automatically)
        if (!hasComponent) {
            tab.addString(key, value);
        }
    }

    /**
     * Creates and grabs a double from NT through shuffleboard
     * @param tabName the tab to grab the value from
     * @param key the name of the shuffleboard entry
     * @param defaultValue the initial entry value
     * @return the value of the shuffleboard entry
     */
    public static double getDouble(String tabName, String key, double defaultValue) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // if the target exists, write to it using NTs, and if not, create the component
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, get it using the NetworkTable, and if not, create the component
        if (hasComponent) {
            return NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).getDouble(defaultValue);
        } else {
            tab.add(key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Creates and grabs a string from NT through shuffleboard
     * @param tabName the tab to grab the value from
     * @param key the name of the shuffleboard entry
     * @param defaultValue the initial entry value
     * @return the value of the shuffleboard entry
     */
    public static String getString(String tabName, String key, String defaultValue) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // if the target exists, write to it using NTs, and if not, create the component
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, get it using the NetworkTable, and if not, create the component
        if (hasComponent) {
            return NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).getString(defaultValue);
        } else {
            tab.add(key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Creates and grabs a boolean from NT through shuffleboard
     * @param tabName the tab to grab the value from
     * @param key the name of the shuffleboard entry
     * @param defaultValue the initial entry value
     * @return the value of the shuffleboard entry
     */
    public static boolean getBool(String tabName, String key, boolean defaultValue) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // if the target exists, write to it using NTs, and if not, create the component
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, get it using the NetworkTable, and if not, create the component
        if (hasComponent) {
            return NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tabName).getEntry(key).getBoolean(defaultValue);
        } else {
            tab.add(key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Creates and sets a double array from NT through shuffleboard
     * @param tabName the tab to set the value to
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote must be called periodically to update
     */
    public static void setDoubleArray(String tabName, String key, Supplier<double[]> value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, write to it using the NetworkTable, and if not, create the component
        if (!hasComponent) {
            tab.addDoubleArray(key, value);
        }
    }

    /**
     * Creates and sets a string array from NT through shuffleboard
     * @param tabName the tab to set the value to
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote must be called periodically to update
     */
    public static void setStringArray(String tabName, String key, Supplier<String[]> value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, write to it using the NetworkTable, and if not, create the component
        if (!hasComponent) {
            tab.addStringArray(key, value);
        }
    }

    /**
     * Creates and sets a boolean array from NT through shuffleboard
     * @param tabName the tab to set the value to
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     * @implNote must be called periodically to update
     */
    public static void setBoolArray(String tabName, String key, Supplier<boolean[]> value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component exists, write to it using the NetworkTable, and if not, create the component
        if (!hasComponent) {
            tab.addBooleanArray(key, value);
        }
    }

    /**
     * Set a {@link <a href="https://docs.wpilib.org/en/stable/docs/software/telemetry/robot-telemetry-with-sendable.html">Sendable</a>} object to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     */
    public static void send(String tabName, String key, Sendable value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component doesnt exist, create it (since its a supplier, it will be updated automatically)
        if (!hasComponent) {
            tab.add(key, value);
        }
    }

    /**
     * Set a generic object to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     */
    public static void set(String tabName, String key, Object value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component doesnt exist, create it (since its a supplier, it will be updated automatically)
        if (!hasComponent) {
            tab.add(key, value);
        }
    }

    /**
     * grabs a NT entry
     * @param table the table to grab the entry from ("Shuffleboard" for shuffleboard)
     * @param subTable the subtable to grab the entry from (tab name for shuffleboard)
     * @param key the name of the NT entry
     * @return the NT entry
     * 
     * @implNote if entry does not exist, returns null. will NOT create entry automatically.
     */
    public static NetworkTableEntry getEntry(String table, String subTable, String key) {
        // if the component exists, get it using the NetworkTable, and if not, create the component
        try {
            return NetworkTableInstance.getDefault().getTable(table).getSubTable(subTable).getEntry(key);
        } catch (Exception e) {
            System.out.println("entry grab failed: " + e);
            return null;
        }
    }


    /**
     * @deprecated use {@link #set(String, String, Sendable)} instead
     * 
     * Set a {@link <a href="https://docs.wpilib.org/en/stable/docs/software/telemetry/robot-telemetry-with-sendable.html">Sendable</a>} object to NT through shuffleboard
     * @param tabName the tab this shuffleboard entry will be placed in
     * @param key the name of the shuffleboard entry
     * @param value the value of the shuffleboard entry
     */
    public static void set(String tabName, String key, Sendable value) {
        boolean hasComponent = false;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        // iterate through the components in the tab, check if the component exists
        for (int i = 0; i < tab.getComponents().size(); i++) {
            if (tab.getComponents().get(i).getTitle() == key) {
                hasComponent = true;
                break;
            }
        }

        // if the component doesnt exist, create it (since its a supplier, it will be updated automatically)
        if (!hasComponent) {
            tab.add(key, value);
        }
    }
}
