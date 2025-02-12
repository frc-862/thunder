// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.thunder.shuffleboard;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.BooleanArrayPublisher;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.Publisher;
import edu.wpi.first.networktables.StringArrayPublisher;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StructPublisher;
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
        private static Map<String, LoggableValue> values = new HashMap<String, LoggableValue>();
        // private ArrayList<Publisher> publishers = new ArrayList<Publisher>();



        public loggingThread(String tab) {
            this.tab = tab;
        }

        @Override
        public void run() {
            // every updateFreq seconds, publish the latest values to the network table
            while (true) {
                boolean isUpdateLoop = Timer.getFPGATimestamp() % FAST_UPDATE_FREQ == 0;
                for (Map.Entry<String, LoggableValue> entry : values.entrySet()) {
                    LoggableValue value = entry.getValue();

                    

                    if(entry.getValue().freq == UpdateFrequency.PERIODIC) {
                        entry.getValue().publishToNT(value);
                    } else if(isUpdateLoop) {
                        entry.getValue().publishToNT(value);
                    }
                }
            }
        }

        public void accept(String key, Object value, UpdateFrequency freq, DataType type) throws IllegalArgumentException {
            if (!values.containsKey(key)) {
                registerValue(key, value, freq, type);
            } else if(freq == UpdateFrequency.PERIODIC) {
                values.get(key).publishToNT(value);
            } else {
                values.get(key).accept(value);
            }
        }

        private void registerValue(String key, Object value, UpdateFrequency freq, DataType type) throws IllegalArgumentException {
            switch(type) {
                case DOUBLE:
                    if(value instanceof Double) {
                        values.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getDoubleTopic(key).publish(), freq, type));
                    } else {
                        throw new IllegalArgumentException("Invalid data type");
                    }
                    break;

                case BOOLEAN:
                    if (value instanceof Boolean) {
                        values.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getBooleanTopic(key).publish(), freq, type));
                    } else {
                        throw new IllegalArgumentException("Invalid data type");
                    }
                    break;
                case STRING:
                    if (value instanceof String) {
                        values.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getStringTopic(key).publish(), freq, type));
                    } else {
                        throw new IllegalArgumentException("Invalid data type");
                    }
                    break;
                case DOUBLE_ARRAY:
                    if (value instanceof double[]) {
                        values.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getDoubleArrayTopic(key).publish(), freq, type));
                    } else {
                        throw new IllegalArgumentException("Invalid data type");
                    }
                    break;
                case BOOLEAN_ARRAY:
                    if (value instanceof boolean[]) {
                        values.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getBooleanArrayTopic(key).publish(), freq, type));
                    } else {
                        throw new IllegalArgumentException("Invalid data type");
                    }
                    break;
                case STRING_ARRAY:
                    if (value instanceof String[]) {
                        values.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getStringArrayTopic(key).publish(), freq, type));
                    } else {
                        throw new IllegalArgumentException("Invalid data type");
                    }
                    break;
                
                case POSE2D:
                        if (value instanceof Pose2d) {
                            values.put(key, new LoggableValue(value, NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getStructTopic(key, Pose2d.struct).publish(), freq, type));
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;

                default:
                    throw new IllegalArgumentException("Invalid data type");
            }
        }
    }       
    

    private class LoggableValue {
        public Object value;
        public Object lastSentValue;
        public final Publisher publisher;
        public final UpdateFrequency freq;
        public final DataType type;

        public LoggableValue(Object value, Publisher publisher, UpdateFrequency freq, DataType type) {
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
                default:
                    // there's no Pose2d equivalent NetworkTableValue
                    throw new IllegalArgumentException("Invalid data type");
            }
        }

        /**
         * Accepts a value and sends it to the network table if it is different from the last sent value <p>
         * only use this function when updating to NT <p>
         * 
         * suppresses unchecked warning as the type of the publisher is ensured to be the same as the type of the value in the constructor 
         * @param value the value to be sent to the network table, has to be part of {@link DataType}
         */
        public boolean accept(Object value) throws IllegalArgumentException {
            if(!lastSentValue.equals(value)) {
                switch(type) {
                    case DOUBLE:
                        if(value instanceof Double) {
                            this.value = value;
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;
                    case BOOLEAN:
                        if(value instanceof Boolean) {
                            this.value = value;
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;
                    case STRING:
                        if (value instanceof String) {
                            this.value = value;
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;
                    case DOUBLE_ARRAY:
                        if (value instanceof double[]) {
                            this.value = value;
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;
                    case BOOLEAN_ARRAY:
                        if (value instanceof boolean[]) {
                            this.value = value;
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;
                    case STRING_ARRAY:
                        if (value instanceof String[]) {
                            this.value = value;
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;
                    case POSE2D:
                        if (value instanceof Pose2d) {
                            this.value = value;
                        } else {
                            throw new IllegalArgumentException("Invalid data type");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid data type");                    
                }
                return true;                
            } else {
                return false;
            }
        }

        @SuppressWarnings("unchecked")
        public void publishToNT(Object value) throws IllegalArgumentException {
            if(accept(value)) {
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
                    default:
                        throw new IllegalArgumentException("Invalid data type");                    
                }

                lastSentValue = value;
            }
        }
    }

    public void publishToDataLog(Object value) throws IllegalArgumentException {
        if(accept(value)) {
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
                default:
                    throw new IllegalArgumentException("Invalid data type");                    
            }

            lastSentValue = value;
        }
    }
}
}
