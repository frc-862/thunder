package frc.lightningUtil.filter;

public interface ValueFilter {

    void reset();

    double filter(double value);

    double get();
    
}