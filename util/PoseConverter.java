package frc.thunder.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public class PoseConverter {

    /**
     * Convert an array of 7 doubles to a Pose4d
     * @param ntValues array of 7 doubles containing translation (X,Y,Z) Rotation(Roll,Pitch,Yaw), total latency (cl+tl)
     * @return a new Pose4d object with the values from the array
     */
    public static Pose4d toPose4d(double[] ntValues) {
        if (ntValues.length == 7){
            return new Pose4d(new Translation3d(ntValues[0], ntValues[1], ntValues[2]), new Rotation3d(Math.toRadians(ntValues[3]), Math.toRadians(ntValues[4]), Math.toRadians(ntValues[5])), ntValues[6]);
        } else {
            return new Pose4d();
        }
    }

    /**
     * Convert an array of 7 doubles to a Pose4d
     * @param ntValues array of 7 doubles containing translation (X,Y,Z) Rotation(Roll,Pitch,Yaw), total latency (cl+tl)
     * @return a new Pose4d object with the values from the array
     */
    public static Pose4d toPose4d(double[] ntValues, double timestamp) {
        if (ntValues.length == 7){
            return new Pose4d(new Translation3d(ntValues[0], ntValues[1], ntValues[2]), new Rotation3d(Math.toRadians(ntValues[3]), Math.toRadians(ntValues[4]), Math.toRadians(ntValues[5])), ntValues[6], timestamp);
        } else {
            return new Pose4d();
        }
    }

    /**
     * Convert an array of 6 doubles to a Pose3d
     * @param ntValues array of 6 doubles containing translation (X,Y,Z) Rotation(Roll,Pitch,Yaw)
     * @return a new Pose3d object with the values from the array
     */
    public static Pose3d toPose3d(double[] ntValues) {
        if (ntValues.length == 6){
            return new Pose3d(new Translation3d(ntValues[0], ntValues[1], ntValues[2]), new Rotation3d(Math.toRadians(ntValues[3]), Math.toRadians(ntValues[4]), Math.toRadians(ntValues[5])));
        } else {
            return null;
        }
    }

    /**
     * Convert an array of 7 doubles to a Pose4d
     * @param jsonData a jsonNode containing an array of 6 doubles (X,Y,Z,Roll,Pitch,Yaw) (meters, degrees)
     * @return a new Pose4d object with the values from the array
     */
    public static Pose3d toPose3d(JsonNode jsonData) {
        if (jsonData.isArray()) {
            jsonData = (ArrayNode) jsonData;
        } else {
            return new Pose3d();
        }

        if (jsonData.size() == 6) {
            return new Pose3d(new Translation3d(jsonData.get(0).asDouble(), jsonData.get(1).asDouble(), jsonData.get(2).asDouble()), new Rotation3d(Math.toRadians(jsonData.get(3).asDouble()), Math.toRadians(jsonData.get(4).asDouble()), Math.toRadians(jsonData.get(5).asDouble())));
        } else {
            return new Pose3d();
        }
    }
}
