package frc.thunder.swervelib.ctre;

public class CanCoderAbsoluteConfiguration {
    private final int id;
    private final double offset;

    
    /**
     * @param id
     * @param offset encoder offset, in radians
     */
    public CanCoderAbsoluteConfiguration(int id, double offset) {
        this.id = id;
        this.offset = offset;
    }

    public int getId() {
        return id;
    }


    /**
     * @return encoder offset, in radians
     */
    public double getOffset() {
        return offset;
    }
}
