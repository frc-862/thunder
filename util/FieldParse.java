package frc.thunder.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;

public class FieldParse {
    
    public static AprilTagFieldLayout wpicalParse() throws IOException{ 
        AprilTagFieldLayout reefscape = AprilTagFieldLayout.loadField(AprilTagFields.k2025Reefscape);
        
        List<AprilTag> wpical = new AprilTagFieldLayout("~/nautilus/src/main/deploy/example.txt").getTags();
        List<AprilTag> reefscapeTags = reefscape.getTags();



        for(AprilTag tag : wpical){
            reefscapeTags.set(tag.ID, tag);
        }

        return new AprilTagFieldLayout(reefscapeTags, reefscape.getFieldLength(), reefscape.getFieldWidth());
    }
}
