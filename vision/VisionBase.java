package frc.thunder.vision;

public class VisionBase {

    private static boolean useVision = false;
    
    public static void enableVision() {
        useVision = true;
    }

    public static void disableVision() {
        useVision = false;
    }

    public static boolean isVisionEnabled() {
        return useVision;
    }

}
