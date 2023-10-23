package frc.thunder.vision.targeting;

import com.fasterxml.jackson.databind.JsonNode;

public class NeuralClassifierResult {
    private int classId;
    private double confidence;
    private String className;

    public NeuralClassifierResult(JsonNode jsonData) {
        this.className = jsonData.get("class").asText();
        this.classId = jsonData.get("classId").asInt();
        this.confidence = jsonData.get("confidence").asDouble();
    }

    /**
     * @return ClassID integer
     */
    public int getClassId() {
        return classId;
    }

    /**
     * @return Confidence of the predicition
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * @return Human-readable class name string
     */
    public String getClassName() {
        return className;
    }
}
