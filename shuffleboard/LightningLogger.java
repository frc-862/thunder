// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.shuffleboard;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.opencv.core.Mat.Tuple2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.BooleanArrayPublisher;
import edu.wpi.first.networktables.BooleanArraySubscriber;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSub;
import edu.wpi.first.networktables.Publisher;
import edu.wpi.first.networktables.StringArrayPublisher;
import edu.wpi.first.networktables.StringArraySubscriber;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringSubscriber;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.networktables.Subscriber;
import edu.wpi.first.util.datalog.BooleanArrayLogEntry;
import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DataLogEntry;
import edu.wpi.first.util.datalog.DoubleArrayLogEntry;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.datalog.StringArrayLogEntry;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.util.datalog.StructLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.thunder.util.Tuple;

/**
 * The LightningLogger class is responsible for logging and publishing data to the network table and data log.
 * It supports different data types and update frequencies.
 */
// @SuppressWarnings({ "unchecked", "rawtypes" })
public class LightningLogger {
    /** MASTER TO-DO LIST
     * TEST:
      * does DataLog functionality work?
      * edge cases (e.g. null values)
      * LOGGING:
        * boolean
        * string
        * double array
        * boolean array
        * string array
      * GRABBING:
        * double
        * boolean
        * string
        * double array
        * boolean array
        * string array
      
     * FEATURES:
      * more documentation
      * do i have to suppress warnings?
      * allow debug flags to be dynamically registered in-code
      
     * KNOWN ISSUES:
      * FAST logging doesn't work
      * DEBUG flags don't publish
     */


    /**
     * Enum representing the log levels. All log levels log to DataLog. <p>
     * DEBUG: logs to NT if the DEBUG flag is set to true for a given tab <p>
     * IMPORTANT: logs to NT if not connected to FMS <p>
     * CRITICAL: logs to NT always
     */
    public enum LogLevel {
        DEBUG, IMPORTANT, CRITICAL
    }

    /**
     * Enum representing the update frequencies. <p>
     * FAST updates every 500ms by default
     */
    public enum UpdateFrequency {
        PERIODIC, FAST
    }


    public enum DataType {
        DOUBLE, BOOLEAN, STRING, DOUBLE_ARRAY, BOOLEAN_ARRAY, STRING_ARRAY, POSE2D
    }

    private static final NetworkTableInstance ntInstance = NetworkTableInstance.getDefault();
    private static final DataLog dataLogManager = DataLogManager.getLog();
    private static double FAST_UPDATE_FREQ = 0.5; //s
    private static LoggingThread loggingThread;

    public static void initialize() {
        loggingThread = new LoggingThread();

        loggingThread.start();
    }


    /**
     * call this before running {@link initialize} to set the debug flags for each tab
     * @param flags
     */
    public static void setDebugFlags(Tuple<String, Boolean>... flags) {
        for (Tuple<String, Boolean> flag : flags) {
            loggingThread.registerDebug(flag.k, flag.v);
        }
    }


    private static class LoggingThread extends Thread {
        private static Map<Tuple<String, String>, LoggableValue> values = new HashMap<Tuple<String, String>, LoggableValue>();
        private static HashMap<Tuple<String, String>, SubscribeableValue> subscribables = new HashMap<Tuple<String, String>, SubscribeableValue>();

        // maps tab name to a boolean representing whether or not to log debug data
        private static HashMap<String, SubscribeableValue<Boolean>> debugFlags = new HashMap<String, SubscribeableValue<Boolean>>();



        public LoggingThread() {
            super();
        }

        @Override
        public void run() {
            while (true) {
                double startTime = Timer.getFPGATimestamp();

                // publish a value to the network table every 0.5 seconds from its registrationTime, iff shouldLog is true
                for (var entry : values.entrySet()) {
                    Tuple<String, String> key = entry.getKey();
                    LoggableValue value = entry.getValue();

                    // if shouldLog is true, and it's been a 0.5 second interval since the value was registered, publish to NT
                    if (value.freq.equals(UpdateFrequency.FAST)) {
                        value.publishToNT();
                        System.out.println("MUSTAAARRD: " + (startTime - value.registrationTime));
                    }
                    // System.out.println("meoowowow");
                }

                // sleep for the remainder of the loop if possible; else, report a logging loop overrun
                double endTime = Timer.getFPGATimestamp();

                //total number of seconds the loop took
                double processingTime = endTime - startTime;
                long sleepTime = Math.round((FAST_UPDATE_FREQ - processingTime) * 1000); //convert to ms


                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    System.out.println("[WARNING] Logging loop overrun: " + -sleepTime + "ms over");
                }
            }
        }

        // primary logging functions

        /**
         * This method accepts a value to be logged and registers it if not already registered.
         * If the log level is DEBUG and there is no entry in the debug flag map for the tab, it registers the debug flag.
         * If the update frequency is PERIODIC and the value should be logged, it publishes the value to the network table.
         *
         * @param tab       The name of the tab where the value will be logged.
         * @param key       The key associated with the value.
         * @param value     The value to be logged.
         * @param freq      The frequency at which the value should be updated.
         * @param logLevel  The log level of the value.
         */
        public synchronized void accept(String tab, String key, Object value, UpdateFrequency freq, LogLevel logLevel) {
            Tuple<String, String> tuple = new Tuple<String, String>(tab, key);
            LoggableValue loggableValue;

            // if the value is not already registered, register it
            if (!values.containsKey(tuple)) {
                loggableValue = registerValue(tuple, value, freq, logLevel);

                // if there's no entry in the debug flag map for this tab, create one
                if(logLevel.equals(LogLevel.DEBUG)) {
                    registerDebug(tab, false);
                }
            } else {
                // if the value is already registered, update it
                loggableValue = values.get(tuple).accept(value);
            }

            //TODO: periodically grabbing from NT like this might cause some lag; investigate
            if(freq == UpdateFrequency.PERIODIC) {
                loggableValue.publishToNT();
            }

            loggableValue.publishToDataLog();
        }

        public void accept(String tab, String key, Object value) throws IllegalArgumentException {
            accept(tab, key, value, UpdateFrequency.PERIODIC, LogLevel.IMPORTANT);
        }


        public void accept(String tab, String key, Object value, LogLevel logLevel) throws IllegalArgumentException {
            accept(tab, key, value, UpdateFrequency.PERIODIC, logLevel);
        }

        public void accept(String tab, String key, Object value, LogLevel logLevel, UpdateFrequency updateFrequency) throws IllegalArgumentException {
            accept(tab, key, value, updateFrequency, logLevel);
        }


        private LoggableValue registerValue(Tuple<String, String> tuple, Object value, UpdateFrequency freq, LogLevel logLevel) throws IllegalArgumentException {
            LoggableValue loggableValue = LoggableValue.create(value, freq, logLevel, tuple);
            values.put(tuple, loggableValue);
            return loggableValue;
        }

        private boolean shouldLog(Tuple<String, String> key) {
            //primary logic for determining whether or not to log a value to NT
            /**
             * if the debug flag is set to true for the tab, and the log level is DEBUG, log the value to NT
             * if the log level is IMPORTANT and the robot is not connected to the FMS, log the value to NT
             * if the log level is CRITICAL, log the value to NT
             */
            var value = values.get(key);
            return debugFlags.get(key.k).grabFromNT() && value.logLevel == LogLevel.DEBUG
                || value.logLevel == LogLevel.IMPORTANT && !DriverStation.isFMSAttached()
                || value.logLevel == LogLevel.CRITICAL;
        }


        //functions relating to grabbing values from NT and setting debug flags
        public synchronized <T> T grab(String tab, String key, T defaultValue) {
            Tuple<String, String> tuple = new Tuple<String, String>(tab, key);
            if (!subscribables.containsKey(tuple)) {
                registerSubscribable(tuple, defaultValue);
            }

            return (T) subscribables.get(tuple).grabFromNT();
        }

        public void registerDebug(String tab, Boolean value) {
            if (!debugFlags.containsKey(tab)) {
                debugFlags.put(tab, SubscribeableValue.createDebug(tab, Boolean.FALSE));
            }
        }

        private void registerSubscribable(Tuple<String, String> tuple, Object defaultValue) {
            subscribables.put(tuple, SubscribeableValue.createSubscribable(defaultValue, tuple));
        }


    }       
    

    private static class LoggableValue<T> {
        private T value;
        private T lastSentValue;
        private final Publisher publisher;
        public final UpdateFrequency freq;
        private final DataType type;
        private final DataLogEntry dataLog;
        public final long registrationTime;
        public LogLevel logLevel;

        private LoggableValue(T value, UpdateFrequency freq, LogLevel logLevel, Tuple<String, String> tuple) {
            this.value = value;
            this.lastSentValue = value;
            this.freq = freq;
            this.registrationTime = (long) Timer.getFPGATimestamp();
            this.logLevel = logLevel;

            String logName = "/" + tuple.k + "/" + tuple.v;

            NetworkTable table = ntInstance.getTable("Shuffleboard").getSubTable(tuple.k);

            // basic typechecking, creating LogEntries of the correct type, and mapping the java types to our enum
            if(value instanceof Double) {
                dataLog = new DoubleLogEntry(dataLogManager, logName, registrationTime);
                publisher = table.getDoubleTopic(tuple.v).publish();
                type = DataType.DOUBLE;
            } else if(value instanceof Boolean) {
                dataLog = new BooleanLogEntry(dataLogManager, logName, registrationTime);
                publisher = table.getBooleanTopic(tuple.v).publish();
                type = DataType.BOOLEAN;
            } else if(value instanceof String) {
                dataLog = new StringLogEntry(dataLogManager, logName, registrationTime);
                publisher = table.getStringTopic(tuple.v).publish();
                type = DataType.STRING;
            } else if(value instanceof double[]) {
                dataLog = new DoubleArrayLogEntry(dataLogManager, logName, registrationTime);
                publisher = table.getDoubleArrayTopic(tuple.v).publish();
                type = DataType.DOUBLE_ARRAY;
            } else if(value instanceof boolean[]) {
                dataLog = new BooleanArrayLogEntry(dataLogManager, logName, registrationTime);
                publisher = table.getBooleanArrayTopic(tuple.v).publish();
                type = DataType.BOOLEAN_ARRAY;
            } else if(value instanceof String[]) {
                dataLog = new StringArrayLogEntry(dataLogManager, logName, registrationTime);
                publisher = table.getStringArrayTopic(tuple.v).publish();
                type = DataType.STRING_ARRAY;
            } else if(value instanceof Pose2d) {
                dataLog = StructLogEntry.create(dataLogManager, logName, Pose2d.struct, registrationTime);
                publisher = table.getStructTopic(tuple.v, Pose2d.struct).publish();
                type = DataType.POSE2D;
            } else {
                // any type issue should be caught here.
                // also, this should literally never be thrown; this class is inacessable publicly, and whoever's editing this should be smart about it
                // with that being said, ensure any newly implemented loggable types are thoroughly tested
                throw new IllegalArgumentException("Invalid data type");
            }
        }


        public static <T> LoggableValue<T> create(T value, UpdateFrequency freq, LogLevel logLevel, Tuple<String, String> tuple) throws IllegalArgumentException{
            return new LoggableValue<>(value, freq, logLevel, tuple);
        }

        /**
         * Accepts a value and sends it to the network table if it is different from the last sent value <p>
         * only use this function when updating to NT <p>
         * 
         * suppresses unchecked warning as the type of the publisher is ensured to be the same as the type of the value in the constructor 
         * @param value the value to be sent to the network table, has to be part of {@link DataType}
         */
        public LoggableValue accept(T value) {
            this.value = value;
            return this;
        }

        public void publishToNT() {
            if(hasUpdate()) {
                switch(type) {
                    case DOUBLE:
                        ((DoublePublisher) publisher).accept((Double) value);
                        break;
                    case BOOLEAN:
                        ((BooleanPublisher) publisher).accept((Boolean) value);
                        break;
                    case STRING:
                        ((StringPublisher) publisher).accept((String) value);
                        break;
                    case DOUBLE_ARRAY:
                        ((DoubleArrayPublisher) publisher).accept((double[]) value);
                        break;
                    case BOOLEAN_ARRAY:
                        ((BooleanArrayPublisher) publisher).accept((boolean[]) value);
                        break;
                    case STRING_ARRAY:
                        ((StringArrayPublisher) publisher).accept((String[]) value);
                        break;
                    case POSE2D:
                        ((StructPublisher<Pose2d>) publisher).accept((Pose2d) value);
                        break;                 
                }


                lastSentValue = value;
            }
        }
    
        public void publishToDataLog() {
            if(hasUpdate()) {
                switch(type) {
                    case DOUBLE:
                        ((DoubleLogEntry) dataLog).append((double) value, (long) Timer.getFPGATimestamp());
                        break;
                    case BOOLEAN:
                        ((BooleanLogEntry) dataLog).append((boolean) value, (long) Timer.getFPGATimestamp());
                        break;
                    case STRING:
                        ((StringLogEntry) dataLog).append((String) value, (long) Timer.getFPGATimestamp());
                        break;
                    case DOUBLE_ARRAY:
                        ((DoubleArrayLogEntry) dataLog).append((double[]) value, (long) Timer.getFPGATimestamp());
                        break;
                    case BOOLEAN_ARRAY:
                        ((BooleanArrayLogEntry) dataLog).append((boolean[]) value, (long) Timer.getFPGATimestamp());
                        break;
                    case STRING_ARRAY:
                        ((StringArrayLogEntry) dataLog).append((String[]) value, (long) Timer.getFPGATimestamp());
                        break;
                    case POSE2D:
                        ((StructLogEntry<Pose2d>) dataLog).append((Pose2d) value, (long) Timer.getFPGATimestamp());
                        break;                  
                }

                lastSentValue = value;
            }
        }

        public boolean hasUpdate() {
            return !lastSentValue.equals(value);
        }
    }

    private static final class SubscribeableValue<T> {
        public T defaultValue;
        private Subscriber subscriber;
        public final DataType type;


        private SubscribeableValue(T defaultValue, Tuple<String, String> tuple) {
            this.defaultValue = defaultValue;

            NetworkTable table = ntInstance.getTable("Shuffleboard").getSubTable(tuple.k);

            // basic typechecking, creating LogEntries of the correct type, and mapping the java types to our enum
            if(defaultValue instanceof Double) {
                subscriber = table.getDoubleTopic(tuple.v).subscribe((Double) defaultValue);
                type = DataType.DOUBLE;
            } else if(defaultValue instanceof Boolean) {
                subscriber = table.getBooleanTopic(tuple.v).subscribe((Boolean) defaultValue);
                type = DataType.BOOLEAN;
            } else if(defaultValue instanceof String) {
                subscriber = table.getStringTopic(tuple.v).subscribe((String) defaultValue);
                type = DataType.STRING;
            } else if(defaultValue instanceof double[]) {
                subscriber = table.getDoubleArrayTopic(tuple.v).subscribe((double[]) defaultValue);
                type = DataType.DOUBLE_ARRAY;
            } else if(defaultValue instanceof boolean[]) {
                subscriber = table.getBooleanArrayTopic(tuple.v).subscribe((boolean[]) defaultValue);
                type = DataType.BOOLEAN_ARRAY;
            } else if(defaultValue instanceof String[]) {
                subscriber = table.getStringArrayTopic(tuple.v).subscribe((String[]) defaultValue);
                type = DataType.STRING_ARRAY;
            } else {
                // any type issue should be caught here.
                // also, this should literally never be thrown; this class is inacessable publicly, and whoever's editing this should be smart about it
                // with that being said, ensure any newly implemented loggable types are thoroughly tested
                throw new IllegalArgumentException("Invalid data type");
            }
        }

        public static <T> SubscribeableValue<T> createSubscribable(T value, Tuple<String, String> tuple) throws IllegalArgumentException{
            return new SubscribeableValue<>(value, tuple);
        }

        // creates a debug flag subscribable value
        public static SubscribeableValue<Boolean> createDebug(String tabName, Boolean value) throws IllegalArgumentException{
            return new SubscribeableValue<>(value, new Tuple<String, String>("Debug", "Debug " + tabName));
        }

        public T grabFromNT() {
            switch(type) {
                case DOUBLE: return (T) (Double) ((DoubleSubscriber) subscriber).get((Double) defaultValue);
                case BOOLEAN: return (T) (Boolean) ((BooleanSubscriber) subscriber).get((Boolean) defaultValue);
                case STRING: return (T) (String) ((StringSubscriber) subscriber).get((String) defaultValue);
                case DOUBLE_ARRAY: return (T) (double[]) ((DoubleArraySubscriber) subscriber).get((double[]) defaultValue);
                case BOOLEAN_ARRAY: return (T) (boolean[]) ((BooleanArraySubscriber) subscriber).get((boolean[]) defaultValue);
                case STRING_ARRAY: return (T) (String[]) ((StringArraySubscriber) subscriber).get((String[]) defaultValue);
                default: throw new IllegalArgumentException("Invalid data type");
            }
        }
    }


    //onslaught of repeating setters and getters below

    /**
     * Sets a double value to be logged with a specific log level and update frequency.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the double value
     * @param logLevel the log level
     * @param updateFrequency the update frequency
     */
    public static void setDouble(String tab, String key, double value, LogLevel logLevel, UpdateFrequency updateFrequency) {
        loggingThread.accept(tab, key, value, logLevel, updateFrequency);
    }

    /**
     * Sets a boolean value to be logged with a specific log level and update frequency.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the boolean value
     * @param logLevel the log level
     * @param updateFrequency the update frequency
     */
    public static void setBoolean(String tab, String key, boolean value, LogLevel logLevel, UpdateFrequency updateFrequency) {
        loggingThread.accept(tab, key, value, logLevel, updateFrequency);
    }

    /**
     * Sets a string value to be logged with a specific log level and update frequency.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the string value
     * @param logLevel the log level
     * @param updateFrequency the update frequency
     */
    public static void setString(String tab, String key, String value, LogLevel logLevel, UpdateFrequency updateFrequency) {
        loggingThread.accept(tab, key, value, logLevel, updateFrequency);
    }

    /**
     * Sets a double array value to be logged with a specific log level and update frequency.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the double array value
     * @param logLevel the log level
     * @param updateFrequency the update frequency
     */
    public static void setDoubleArray(String tab, String key, double[] value, LogLevel logLevel, UpdateFrequency updateFrequency) {
        loggingThread.accept(tab, key, value, logLevel, updateFrequency);
    }

    /**
     * Sets a boolean array value to be logged with a specific log level and update frequency.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the boolean array value
     * @param logLevel the log level
     * @param updateFrequency the update frequency
     */
    public static void setBooleanArray(String tab, String key, boolean[] value, LogLevel logLevel, UpdateFrequency updateFrequency) {
        loggingThread.accept(tab, key, value, logLevel, updateFrequency);
    }

    /**
     * Sets a string array value to be logged with a specific log level and update frequency.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the string array value
     * @param logLevel the log level
     * @param updateFrequency the update frequency
     */
    public static void setStringArray(String tab, String key, String[] value, LogLevel logLevel, UpdateFrequency updateFrequency) {
        loggingThread.accept(tab, key, value, logLevel, updateFrequency);
    }

    /**
     * Sets a Pose2d value to be logged with a specific log level and update frequency.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the Pose2d value
     * @param logLevel the log level
     * @param updateFrequency the update frequency
     */
    public static void setPose2d(String tab, String key, Pose2d value, LogLevel logLevel, UpdateFrequency updateFrequency) {
        loggingThread.accept(tab, key, value, logLevel, updateFrequency);
    }


    /**
     * Sets a double value to be logged with a specific log level.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the double value
     * @param logLevel the log level
     */
    public static void setDouble(String tab, String key, double value, LogLevel logLevel) {
        loggingThread.accept(tab, key, value, logLevel);
    }

    /**
     * Sets a boolean value to be logged with a specific log level.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the boolean value
     * @param logLevel the log level
     */
    public static void setBoolean(String tab, String key, boolean value, LogLevel logLevel) {
        loggingThread.accept(tab, key, value, logLevel);
    }

    /**
     * Sets a string value to be logged with a specific log level.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the string value
     * @param logLevel the log level
     */
    public static void setString(String tab, String key, String value, LogLevel logLevel) {
        loggingThread.accept(tab, key, value, logLevel);
    }

    /**
     * Sets a double array value to be logged with a specific log level.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the double array value
     * @param logLevel the log level
     */
    public static void setDoubleArray(String tab, String key, double[] value, LogLevel logLevel) {
        loggingThread.accept(tab, key, value, logLevel);
    }

    /**
     * Sets a boolean array value to be logged with a specific log level.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the boolean array value
     * @param logLevel the log level
     */
    public static void setBooleanArray(String tab, String key, boolean[] value, LogLevel logLevel) {
        loggingThread.accept(tab, key, value, logLevel);
    }

    /**
     * Sets a string array value to be logged with a specific log level.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the string array value
     * @param logLevel the log level
     */
    public static void setStringArray(String tab, String key, String[] value, LogLevel logLevel) {
        loggingThread.accept(tab, key, value, logLevel);
    }

    /**
     * Sets a Pose2d value to be logged with a specific log level.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the Pose2d value
     * @param logLevel the log level
     */
    public static void setPose2d(String tab, String key, Pose2d value, LogLevel logLevel) {
        loggingThread.accept(tab, key, value, logLevel);
    }


    /**
     * Sets a double value to be logged.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the double value
     */
    public static void setDouble(String tab, String key, double value) {
        loggingThread.accept(tab, key, value);
    }


    /**
     * Sets a boolean value to be logged.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the boolean value
     */
    public static void setBoolean(String tab, String key, boolean value) {
        loggingThread.accept(tab, key, value);
    }

    /**
     * Sets a string value to be logged.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the string value
     */
    public static void setString(String tab, String key, String value) {
        loggingThread.accept(tab, key, value);
    }

    /**
     * Sets a double array value to be logged.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the double array value
     */
    public static void setDoubleArray(String tab, String key, double[] value) {
        loggingThread.accept(tab, key, value);
    }

    /**
     * Sets a boolean array value to be logged.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the boolean array value
     */
    public static void setBooleanArray(String tab, String key, boolean[] value) {
        loggingThread.accept(tab, key, value);
    }

    /**
     * Sets a string array value to be logged.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the string array value
     */
    public static void setStringArray(String tab, String key, String[] value) {
        loggingThread.accept(tab, key, value);
    }

    /**
     * Sets a Pose2d value to be logged.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param value the Pose2d value
     */
    public static void setPose2d(String tab, String key, Pose2d value) {
        loggingThread.accept(tab, key, value);
    }

    /**
     * Gets a double value from the network table.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param defaultValue the default value
     * @return the double value
     */
    public static double getDouble(String tab, String key, double defaultValue) {
        return loggingThread.grab(tab, key, defaultValue);
    }

    /**
     * Gets a boolean value from the network table.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param defaultValue the default value
     * @return the boolean value
     */
    public static boolean getBoolean(String tab, String key, boolean defaultValue) {
        return loggingThread.grab(tab, key, defaultValue);
    }

    /**
     * Gets a string value from the network table.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param defaultValue the default value
     * @return the string value
     */
    public static String getString(String tab, String key, String defaultValue) {
        return loggingThread.grab(tab, key, defaultValue);
    }

    /**
     * Gets a double array value from the network table.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param defaultValue the default value
     * @return the double array value
     */
    public static double[] getDoubleArray(String tab, String key, double[] defaultValue) {
        return loggingThread.grab(tab, key, defaultValue);
    }

    /**
     * Gets a boolean array value from the network table.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param defaultValue the default value
     * @return the boolean array value
     */
    public static boolean[] getBooleanArray(String tab, String key, boolean[] defaultValue) {
        return loggingThread.grab(tab, key, defaultValue);
    }

    /**
     * Gets a string array value from the network table.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param defaultValue the default value
     * @return the string array value
     */
    public static String[] getStringArray(String tab, String key, String[] defaultValue) {
        return loggingThread.grab(tab, key, defaultValue);
    }

    /**
     * Gets a Pose2d value from the network table.
     * 
     * @param tab the tab name
     * @param key the key name
     * @param defaultValue the default value
     * @return the Pose2d value
     */
    public static Pose2d getPose2d(String tab, String key, Pose2d defaultValue) {
        return loggingThread.grab(tab, key, defaultValue);
    }
}
