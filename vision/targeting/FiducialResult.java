package frc.thunder.vision.targeting;

import com.fasterxml.jackson.databind.JsonNode;

public class FiducialResult extends RetroreflectiveResult {
    private int fid;
    private String family;

    public FiducialResult(JsonNode jsonData) {
        super(jsonData);
        this.fid = jsonData.get("fid").asInt();
        this.family = jsonData.get("family").asText();
        //Skew is unused so not implementing
    }

    /**
     * @return Fiducial tag ID
     */
    public int getFid() {
        return fid;
    }

    /**
     * @return Fiducial Family (16H5C, 25H9C, 36H11C, etc)
     */
    public String getFamily() {
        return family;
    }
}
