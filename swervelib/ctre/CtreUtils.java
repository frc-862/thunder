package frc.thunder.swervelib.ctre;

import com.ctre.phoenix6.StatusCode;
import edu.wpi.first.wpilibj.DriverStation;

public final class CtreUtils {
    private CtreUtils() {}

    public static void checkCtreError(StatusCode errorCode, String message) {
        if (errorCode != StatusCode.OK) {
            DriverStation.reportError(String.format("%s: %s", message, errorCode.toString()),
                    false);
        }
    }
}
