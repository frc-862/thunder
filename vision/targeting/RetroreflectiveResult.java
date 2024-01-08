package frc.thunder.vision.targeting;


import com.fasterxml.jackson.databind.JsonNode;

import edu.wpi.first.math.geometry.Pose3d;
import frc.thunder.util.PoseConverter;

public class RetroreflectiveResult {
    private Object pts;
    private Pose3d t6c_ts;
    private Pose3d t6r_fs;
    private Pose3d t6r_ts;
    private Pose3d t6t_cs;
    private Pose3d t6t_rs;
    private double area;
    private double tx;
    private double ty;
    private double txp;
    private double typ;

    public RetroreflectiveResult(JsonNode jsonData) {
        this.pts = jsonData.get("pts");
        this.t6c_ts = PoseConverter.toPose3d(jsonData.get("t6c_ts"));
        this.t6r_fs = PoseConverter.toPose3d(jsonData.get("t6r_fs"));
        this.t6r_ts = PoseConverter.toPose3d(jsonData.get("t6r_ts"));
        this.t6t_cs = PoseConverter.toPose3d(jsonData.get("t6t_cs"));
        this.t6t_rs = PoseConverter.toPose3d(jsonData.get("t6t_rs"));
        this.area = jsonData.get("area").asDouble();
        this.tx = jsonData.get("tx").asDouble();
        this.ty = jsonData.get("ty").asDouble();
        this.txp = jsonData.get("txp").asDouble();
        this.typ = jsonData.get("typ").asDouble();
    }

    public Object getPts() {
        return pts;
    }

    public Pose3d getT6c_ts() {
        return t6c_ts;
    }

    public Pose3d getT6r_fs() {
        return t6r_fs;
    }

    public Pose3d getT6r_ts() {
        return t6r_ts;
    }

    public Pose3d getT6t_cs() {
        return t6t_cs;
    }

    public Pose3d getT6t_rs() {
        return t6t_rs;
    }

    public double getArea() {
        return area;
    }

    public double getTx() {
        return tx;
    }

    public double getTy() {
        return ty;
    }

    public double getTxp() {
        return txp;
    }

    public double getTyp() {
        return typ;
    }
}
