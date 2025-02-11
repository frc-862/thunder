// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.shuffleboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.GenericPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.Publisher;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import frc.thunder.util.Tuple;

public class LightningLogger {
    public enum LogLevel {
        DEBUG, IMPORTANT, CRITICAL
    }

    public enum UpdateFrequency {
        PERIODIC, FAST
    }

    public enum DataType {
        DOUBLE, BOOLEAN, STRING, DOUBLE_ARRAY, BOOLEAN_ARRAY, STRING_ARRAY, POSE2D
    }

    private static final NetworkTableInstance ntInstance = NetworkTableInstance.getDefault();
    private static final DataLog dataLogManager = DataLogManager.getLog();
    private static double FAST_UPDATE_FREQ = 500; //ms

    private static List<String> tabs = Collections.synchronizedList(new ArrayList<String>());
    private static List<Thread> threads = new ArrayList<Thread>();

    
    

    public static void initialize(double updateFrequency) {
        FAST_UPDATE_FREQ = updateFrequency;
    }



    private class loggingThread extends Thread {
        private String tab;
        // private static Map<String, LoggableValue> publishers = new HashMap<String, LoggableValue>();
        private static Map<String, LoggableValue> latestValues = new HashMap<String, LoggableValue>();
        // private ArrayList<Publisher> publishers = new ArrayList<Publisher>();



        public loggingThread(String tab) {
            this.tab = tab;
        }

        @Override
        public void run() {
            // every updateFreq seconds, publish the latest values to the network table
            while (true) {
                try {
                    Thread.sleep((long) (1000 / FAST_UPDATE_FREQ));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Map.Entry<String, LoggableValue> entry : latestValues.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                }
            }
        }

        public void addValue(String key, Object value, UpdateFrequency freq, DataType type) {
            if (!latestValues.containsKey(key)) {
                registerValue(key, NetworkTableValue.makeDouble(0), freq, type);
            }

            if(freq == UpdateFrequency.FAST) {
                latestValues.get(key).acceptAndUpdate(value);
            }
        }

        private void registerValue(String key, NetworkTableValue value, UpdateFrequency freq, DataType type) {
            switch(type) {
                //TODO: check if these types r right
                case DOUBLE:
                    latestValues.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getTopic(key).genericPublish("double"), freq, type));
                    break;
                case BOOLEAN:
                    latestValues.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getTopic(key).genericPublish("boolean"), freq, type));
                    break;
                case STRING:
                    latestValues.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getTopic(key).genericPublish("string"), freq, type));
                    break;
                case DOUBLE_ARRAY:
                    latestValues.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getTopic(key).genericPublish("double[]"), freq, type));
                    break;
                case BOOLEAN_ARRAY:
                    latestValues.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getTopic(key).genericPublish("boolean[]"), freq, type));
                    break;
                case STRING_ARRAY:
                    latestValues.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getTopic(key).genericPublish("string[]"), freq, type));
                    break;
                case POSE2D:
                    latestValues.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getTopic(key).genericPublish("raw"), freq, type));
                    break;
            }
            }
    }       
    

    private class LoggableValue {
        public NetworkTableValue value;
        public NetworkTableValue lastSentValue;
        public final GenericPublisher publisher;
        public final UpdateFrequency freq;
        public final DataType type;

        public LoggableValue(NetworkTableValue value, GenericPublisher publisher, UpdateFrequency freq, DataType type) {
            this.value = value;
            this.lastSentValue = value;
            this.publisher = publisher;
            this.freq = freq;
            this.type = type;
        }

        public static NetworkTableValue fromRawType(Object value, DataType type) {
            switch(type) {
                case DOUBLE:
                    return NetworkTableValue.makeDouble((Double) value);
                case BOOLEAN:
                    return NetworkTableValue.makeBoolean((Boolean) value);
                case STRING:
                    return NetworkTableValue.makeString((String) value);
                case DOUBLE_ARRAY:
                    return NetworkTableValue.makeDoubleArray((double[]) value);
                case BOOLEAN_ARRAY:
                    return NetworkTableValue.makeBooleanArray((boolean[]) value);
                case STRING_ARRAY:
                    return NetworkTableValue.makeStringArray((String[]) value);
                case POSE2D:
                    return NetworkTableValue.makeRaw((byte[]) value); //TODO: oh god i dont think this is gonna work
                default:
                    throw new IllegalArgumentException("Invalid data type");
            }
        }

        public void accept(Object value) {
            if(lastSentValue != fromRawType(value, type)) {
                publisher.accept(fromRawType(value, type));
            }
        }

        public void acceptAndUpdate(Object value) {
            if(lastSentValue != fromRawType(value, type)) {
                publisher.accept(fromRawType(value, type));
                lastSentValue = fromRawType(value, type);
            }
        }
    }
}
