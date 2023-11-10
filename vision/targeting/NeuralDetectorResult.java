package frc.thunder.vision.targeting;

import com.fasterxml.jackson.databind.JsonNode;

public class NeuralDetectorResult extends NeuralClassifierResult {
    private Object points;
    private double area;
    private double tx;
    private double txp;
    private double ty;
    private double typ;

    public NeuralDetectorResult(JsonNode jsonData) {
        super(jsonData);
        this.points = jsonData.get("points");
        this.area = jsonData.get("area").asDouble();
        this.tx = jsonData.get("tx").asDouble();
        this.txp = jsonData.get("txp").asDouble();
        this.ty = jsonData.get("ty").asDouble();
        this.typ = jsonData.get("typ").asDouble();
    }

    public Object getPoints() {
        return points;
    }

    public double getArea() {
        return area;
    }

    public double getTx() {
        return tx;
    }

    public double getTxp() {
        return txp;
    }

    public double getTy() {
        return ty;
    }

    public double getTyp() {
        return typ;
    }
}
