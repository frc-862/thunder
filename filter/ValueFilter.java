package frc.thunder.filter;

public interface ValueFilter {

    void reset();

    double filter(double value);

    double get();

}