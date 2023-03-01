package frc.thunder.shuffleboard;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.Pair;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * This class is used to create a periodic shuffleboard tab. This is useful for logging data that is
 * not updated often.
 * 
 * USAGE: Create a new LightningShuffleboardPeriodic object in the constructor of your subsystem
 * (with a key, object pair for each value you want to log); then place
 * LightningShuffleboardPeriodic.loop in your periodic method.
 */

 @SuppressWarnings("unchecked")
public class LightningShuffleboardPeriodic {
    private Pair<String, Object>[] values;
    private int length = 0;
    private double lastTime = Timer.getFPGATimestamp();
    private String tab;
    private double loopTime;
    private int index = 0;

    private enum Type {
        DOUBLE, BOOLEAN, STRING, DOUBLE_ARRAY, BOOLEAN_ARRAY, STRING_ARRAY, NULL
    }

    Type type[];

    public LightningShuffleboardPeriodic(String tab, double period, Pair<String, Object>... values) {
        this.values = values;
        length = values.length;
        type = new Type[length];
        this.tab = tab;
        this.loopTime = period;

        int i = 0;
        for (Pair<String, Object> value : values) {
            if (value.getSecond() instanceof DoubleSupplier) {
                Shuffleboard.getTab(tab).add(value.getFirst(), ((DoubleSupplier) value.getSecond()).getAsDouble());
                type[i] = Type.DOUBLE;
            } else if (value.getSecond() instanceof BooleanSupplier) {
                Shuffleboard.getTab(tab).add(value.getFirst(), ((BooleanSupplier) value.getSecond()).getAsBoolean());
                type[i] = Type.BOOLEAN;
            } else if (value.getSecond() instanceof Supplier<?>) {
                //the warnings are wrong, we do check the cast
                if (((Supplier<?>) value.getSecond()).get() instanceof double[]) {
                    Shuffleboard.getTab(tab).add(value.getFirst(), ((Supplier<double[]>) value.getSecond()).get());
                    type[i] = Type.DOUBLE_ARRAY;
                } else if (((Supplier<?>) value.getSecond()).get() instanceof boolean[]) {
                    Shuffleboard.getTab(tab).add(value.getFirst(), ((Supplier<boolean[]>) value.getSecond()).get());
                    type[i] = Type.BOOLEAN_ARRAY;
                } else if (((Supplier<?>) value.getSecond()).get() instanceof String[]) {
                    Shuffleboard.getTab(tab).add(value.getFirst(), ((Supplier<String[]>) value.getSecond()).get());
                    type[i] = Type.STRING_ARRAY;
                } else if (((Supplier<?>) value.getSecond()).get() instanceof String) {
                    Shuffleboard.getTab(tab).add(value.getFirst(), ((Supplier<String>) value.getSecond()).get());
                    type[i] = Type.STRING;
                } else {
                    type[i] = Type.NULL;
                }
            } else {
                type[i] = Type.NULL;
            }
            i++;
        }
    }
    //default period is 5 seconds
    public LightningShuffleboardPeriodic(String tab, Pair<String, Object>... values) {
        this(tab, 5d, values);
    }

    
    //call this in your periodic
    public void loop() {
        double currentTime = Timer.getFPGATimestamp();

        if (currentTime-lastTime > loopTime / length) {
            switch (type[index]) {
                case DOUBLE:
                    NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getEntry(values[index].getFirst()).setDouble(((DoubleSupplier) (values[index].getSecond())).getAsDouble());
                    break;
                case BOOLEAN:
                    NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getEntry(values[index].getFirst()).setBoolean(((BooleanSupplier) values[index].getSecond()).getAsBoolean());
                    break;
                case STRING:
                    NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getEntry(values[index].getFirst()).setString(((Supplier<String>) values[index].getSecond()).get());
                    break;
                case DOUBLE_ARRAY:
                    NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getEntry(values[index].getFirst()).setDoubleArray(((Supplier<double[]>) values[index].getSecond()).get());
                    break;
                case BOOLEAN_ARRAY:
                    NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getEntry(values[index].getFirst()).setBooleanArray(((Supplier<boolean[]>) values[index].getSecond()).get());
                    break;
                case STRING_ARRAY:
                    NetworkTableInstance.getDefault().getTable("Shuffleboard").getSubTable(tab).getEntry(values[index].getFirst()).setStringArray(((Supplier<String[]>) values[index].getSecond()).get());
                    break;
                case NULL:
                    System.out.println(values[index].getFirst() + " is null");
                break;
            }

            lastTime = currentTime;
            index++;
            if (index == length) {
                index = 0;
            }
        }
    }
}
