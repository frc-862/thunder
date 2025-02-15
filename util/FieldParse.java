package frc.thunder.util;

import java.util.List;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;

public class FieldParse {
    
    public static AprilTagFieldLayout wpicalParse(AprilTagFieldLayout gameField, AprilTagFieldLayout wpiCalOutput) {
        
        List<AprilTag> gameTags = gameField.getTags();

        for(AprilTag tag : wpiCalOutput.getTags()) {
            gameTags.set(tag.ID, tag);
        }

        return new AprilTagFieldLayout(gameTags, gameField.getFieldLength(), gameField.getFieldWidth());
    }
}
