package frc.thunder.vision;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import frc.thunder.util.Pose4d;
import frc.thunder.util.PoseConverter;
import frc.thunder.vision.targeting.TargetingResult;

public class Limelight {
    private NetworkTable table;
    private String name;
    private String ip;
    private URL baseUrl;
    private final double ntDefaultDouble = 0.0;
    private final long ntDefaultInt = 0;
    private final String ntDefaultString = "";
    private final double[] ntDefaultArray = {};

    /**
     * Create a new Limelight object with the specified name and ip
     * @param name the name of the limelight used in network tables
     * @param ip the ip of the limelight (no slashes or http://)
     */
    public Limelight(String name, String ip) {
        this.name = name;
        this.table = NetworkTableInstance.getDefault().getTable(name);
        this.ip = ip;
        try {
            this.baseUrl = new URL("http://" + ip);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid IP");
        }
    }

    public String toString() {
        return "Limelight: " + name + " at " + ip;
    }

    /**
     * Create a new Limelight object with the specified name and default ip
     * @param name the name of the limelight used in network tables
     * ip defaults to 10.8.62.11
     * @see Limelight#Limelight(String, String)
     */
    public Limelight(String name) {
        this(name, "10.8.62.11");
    }

    /**
     * get a double from network tables with the specified key
     * @param key the key to get the value from
     * @return the value of the key, or ntDefaultDouble if the key does not exist or has some other error
     */
    private double getDoubleNT(String key) {
        return table.getEntry(key).getDouble(ntDefaultDouble);
    }

    /**
     * get a boolean from network tables with the specified key
     * @param key the key to get the value from
     * @return the value of the key, or ntDefaultDouble if the key does not exist or has some other error
     */
    private int getIntNT(String key) {
        return (int) table.getEntry(key).getInteger(ntDefaultInt);
    }

    /**
     * get a String from network tables with the specified key
     * @param key the key to get the value from
     * @return the value of the key, or ntDefaultString if the key does not exist or has some other error
     */
    private String getStringNT(String key) {
        return table.getEntry(key).getString(ntDefaultString);
    }

    /**
     * get a double array from network tables with the specified key
     * @param key the key to get the value from
     * @return the value of the key, or ntDefaultArray if the key does not exist or has some other error
     */
    private double[] getArrayNT(String key) {
        return table.getEntry(key).getDoubleArray(ntDefaultArray);
    }

    /**
     * set a double in network tables with the specified key
     * @param key the key to set the value of
     * @param value the value to set the key to (can be int or double)
     */
    private void setNumNT(String key, Number value) {
        table.getEntry(key).setNumber(value);
    }

    /**
     * set a String in network tables with the specified key
     * @param key the key to set the value of
     * @param value the value to set the key to
     */
    private void setArrayNT(String key, double[] value) {
        table.getEntry(key).setDoubleArray(value);
    }

    /**
     * @return Whether the limelight has any valid targets
     */
    public boolean hasTarget() {
        return getDoubleNT("tv") == 1.0;
    }

    /**
     * Horizontal Offset From Crosshair To Target
     * @return (LL1: -27 degrees to 27 degrees | LL2: -29.8 to 29.8 degrees | LL3: -30 to 30 degrees)
     */
    public double getTargetX() {
        return getDoubleNT("tx");
    }

    //TODO: add limelight 3 fov
    /**
     * Vertical Offset From Crosshair To Target
     * @return (LL1: -20.5 degrees to 20.5 degrees | LL2: -24.85 to 24.85 degrees | -24 to 24 degrees)
     */
    public double getTargetY() {
        return getDoubleNT("ty");
    }

    /**
     * @return Target Area (0% of image to 100% of image)
     */
    public double getTargetArea() {
        return getDoubleNT("ta");
    }

    /**
     * @return The pipeline’s latency contribution (ms)
     */
    public double getPipelineLatency() {
        return getDoubleNT("tl");
    }

    /**
     * @return Capture pipeline latency (ms). Time between the end of the exposure of the middle row of the sensor to the beginning of the tracking pipeline.
     */
    public double getCaptureLatency() {
        return getDoubleNT("cl");
    }

    /**
     * @return the total latency of the limelight (ms). This is the sum of the pipeline and capture latency.
     */
    public double getTotalLatency() {
        return getPipelineLatency() + getCaptureLatency();
    }

    /**
     * @return Sidelength of shortest side of the fitted bounding box (pixels)
     */
    public double getTShort() {
        return getDoubleNT("tshort");
    }

    /**
     * @return Sidelength of longest side of the fitted bounding box (pixels)
     */
    public double getTLong() {
        return getDoubleNT("tlong");
    }

    /**
     * @return Horizontal sidelength of the rough bounding box (0 - 320 pixels)
     */
    public double getTHor() {
        return getDoubleNT("thor");
    }

    /**
     * @return Vertical sidelength of the rough bounding box (0 - 320 pixels)
     */
    public double getTVert() {
        return getDoubleNT("tvert");
    }

    /**
     * @return Full JSON dump of targeting results
     */
    public TargetingResult getTargetJSON() {
        return new TargetingResult(parseJson(getStringNT("json")));
    }

    /**
     * @return Class ID of primary neural detector result or neural classifier result
     */
    public String getNeuralClassID() {
        return getStringNT("tclass");
    }

    /**
     * @return Get the average HSV color underneath the crosshair region as a NumberArray
     */
    public double[] getAverageHSV() {
        return getArrayNT("tc");
    }

    /**
     * Automatically return either the blue or red alliance pose based on which alliance the driver station reports
     * @see Limelight#getBotPoseBlue()
     * @see Limelight#getBotPoseRed()
     * Robot transform is in field-space (alliance color driverstation WPILIB origin)
     * @return Translation (X,Y,Z) Rotation(Roll,Pitch,Yaw), total latency (cl+tl)
     */
    public Pose4d getAlliancePose() {
            if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
                if(getArrayNT("botpose_wpiblue") != null){
                    return PoseConverter.toPose4d(getArrayNT("botpose_wpiblue"));
                } else return new Pose4d();
            } else {
                if(getArrayNT("botpose_wpired") != null){
                return PoseConverter.toPose4d(getArrayNT("botpose_wpired"));
                } else return new Pose4d();
            }
    }


    /**
     * @return 3D transform of the camera in the coordinate system of the primary in-view AprilTag
     */
    public Pose3d getCamPoseTargetSpace() {
        return PoseConverter.toPose3d(getArrayNT("camerapose_targetspace"));
    }

    /**
     * @return 3D transform of the camera in the coordinate system of the Robot
     */
    public Pose3d getCamPoseRobotSpace() {
        return PoseConverter.toPose3d(getArrayNT("camerapose_robotspace"));
    }

    /**
     * @return 3D transform of the primary in-view AprilTag in the coordinate system of the Camera
     */
    public Pose3d getTargetPoseCameraSpace() {
        return PoseConverter.toPose3d(getArrayNT("targetpose_cameraspace"));
    }

    /**
     * @return 3D transform of the primary in-view AprilTag in the coordinate system of the Robot
     */
    public Pose3d getTargetPoseRobotSpace() {
        return PoseConverter.toPose3d(getArrayNT("targetpose_robotspace"));
    }

    /**
     * @return ID of the primary in-view apriltag
     */
    public int getApriltagID() {
        return getIntNT("tid");
    }

    public enum LEDMode {
        PIPELINE(0),
        OFF(1),
        BLINK(2),
        ON(3);

        private int value;

        LEDMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Sets limelight’s LED state
     * PIPELINE: use the LED Mode set in the current pipeline
     * OFF: force off
     * BLINK: force blink
     * ON: force on
     * @param mode LED Mode
     */
    public void setLEDMode(LEDMode mode) {
        setNumNT("ledMode", mode.getValue());
    }

    /**
     * @return the current LED mode
     */
    public LEDMode getLEDMode() {
        return LEDMode.values()[getIntNT("ledMode")];
    }

    public enum CamMode {
        VISION(0),
        DRIVER(1);

        private int value;

        CamMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Sets limelight’s operation mode
     * VISION: use for vision processing
     * DRIVER: Driver Camera (Increases exposure, disables vision processing)
     * @param mode Cam Mode
     */
    public void setCamMode(CamMode mode) {
        setNumNT("camMode", mode.getValue());
    }

    /**
     * @return the current camera mode
     */
    public CamMode getCamMode() {
        return CamMode.values()[getIntNT("camMode")];
    }

    public enum StreamMode {
        STANDARD(0),
        PIP_MAIN(1),
        PIP_SECONDARY(2);

        private int value;

        StreamMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Sets limelight’s streaming mode
     * STANDARD: Side-by-side streams if a webcam is attached to Limelight
     * PIP_MAIN: The secondary camera stream is placed in the lower-right corner of the primary camera stream
     * PIP_SECONDARY: The primary camera stream is placed in the lower-right corner of the secondary camera stream
     * @param mode Stream Mode
     */
    public void setStreamMode(StreamMode mode) {
        setNumNT("stream", mode.getValue());
    }

    /**
     * @return the current stream mode
     */
    public StreamMode getStreamMode() {
        return StreamMode.values()[getIntNT("stream")];
    }

    /**
     * Sets limelight’s current pipeline
     */
    public void setPipeline(int pipeline) {
        setNumNT("pipeline", pipeline);
    }

    /**
     * @return the current pipeline (0-9)
     */
    public int getPipeline() {
        return getIntNT("pipeline");
    }

    /**
     * Sets limelight’s crop rectangle. The pipeline must utilize the default crop rectangle in the web interface.
     * @param xMin the minimum x value of the crop rectangle (-1 to 1)
     * @param yMin the minimum y value of the crop rectangle (-1 to 1)
     * @param xMax the maximum x value of the crop rectangle (-1 to 1)
     * @param yMax the maximum y value of the crop rectangle (-1 to 1)
     */
    public void setCropSize(double xMin, double yMin, double xMax, double yMax) {
        setArrayNT("crop", new double[] {xMin, xMax, yMin, yMax});
    }
    //I'm way too lazy to make a getter for this lol

    @Deprecated
    /**
     * @deprecated use limelight pipeline instead
     * @param pose the camera's position, with X as front/back, Y as left/right, and Z as up/down, in meters
     */
    public void setCameraPoseRobotSpace(Pose3d pose) {
        double[] ntValues = new double[6];
        ntValues[0] = pose.getX();
        ntValues[1] = pose.getY();
        ntValues[2] = pose.getZ();
        ntValues[3] = pose.getRotation().getX();
        ntValues[4] = pose.getRotation().getY();
        ntValues[5] = pose.getRotation().getZ();

        setArrayNT("camerapose_robotspace_set", ntValues);
    }

    /**
     * @return the current camera pose in robot space
     * @see Limelight#getCamPoseRobotSpace()
     */
    public Pose3d getCameraPoseRobotSpace() {
        return getCamPoseRobotSpace();
    }


    /**
     * @return a URL object containing the ip of the limelight
     */
    public URL getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param suffix the suffix to add to the base url
     * @return a new URL object with the suffix and port 5807 added to the base url
     */
    private URL generateURL(String suffix) {
        try {
            return new URL(baseUrl.toString() + ":5807/" + suffix);
        } catch (MalformedURLException e) {
            //we want the code to crash if this happens, since it means something is catastrophically wrong with the limelight code
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid Suffix");
        }
    }


    /**
     * generic http request to the limelight
     * @param suffix the suffix to add to the base url (eg "deletesnapshots", "capturesnapshot")
     * @param type the type of request to send (eg "GET", "POST")
     * @param headers the headers to send with the request
     * @return the response message from the limelight
     * Errors are printed to stderr, and a null value is returned
     */
    private String httpRequest(String suffix, String type, ArrayList<Pair<String, String>> headers) {
        try {
            URL url = generateURL(suffix);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(type);
            int responseCode = connection.getResponseCode();
            for (Pair<String, String> header : headers) {
                connection.setRequestProperty(header.getFirst(), header.getSecond());
            }
            if (responseCode != 200) {
                System.err.println("Bad HTTP Request to Limelight: " + responseCode + " " + connection.getResponseMessage());
            }

            //Chatgpt wrote this lol
            // Read the response content as a String
            StringBuilder content = new StringBuilder();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (SocketException e) {
            return null;
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("Limelight brokey: " + e.getMessage());
            return null;
        }
    }

    /**
     * send a GET request to the limelight with the specified suffix
     * @param suffix the suffix to add to the base url (eg "deletesnapshots", "capturesnapshot")
     * @param headers the headers to send with the request
     * @return the response message from the limelight
     * Errors are printed to stderr, and a null value is returned
     */
    private String getRequest(String suffix, ArrayList<Pair<String, String>> headers) {
        return httpRequest(suffix, "GET", headers);
    }

    /**
     * send a GET request to the limelight with the specified suffix, with no headers
     * @param suffix the suffix to add to the base url (eg "deletesnapshots", "capturesnapshot")
     * @return the response message from the limelight
     * Errors are printed to stderr, and a null value is returned
     */
    private String getRequest(String suffix) {
        return getRequest(suffix, new ArrayList<Pair<String, String>>());
    }

    /**
     * @param supplier the function to run asynchronously
     * @return the result of the function
     * Errors are printed to stderr, and a null value is returned
     */
    private String async(Supplier<Object> supplier) {
        try {
            return (String) CompletableFuture.supplyAsync(supplier).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Take exactly one snapshot with the current limelight settings. Limited to 2 snapshots per second.
     * @param name the name of the snapshot
     */
    public void takeSnapshot(String name) {
        ArrayList<Pair<String, String>> headers = new ArrayList<Pair<String, String>>();
        headers.add(new Pair<String, String>("snapname", name));
        async(() -> { getRequest("capturesnapshot", headers); return null; });
    }

    /**
     * Take exactly one snapshot with the current limelight settings with default naming (name defaults to snap)
     * @see Limelight#SynchronousSnapshot(String)
     */
    public void takeSnapshot() {
        async(() -> { getRequest("capturesnapshot"); return null; });
    }

    /**
     * Return a list of filenames of all snapshots on the limelight
     * @return
     */
    public JsonNode getSnapshotNames() {
        String rawReport = async(() -> getRequest("snapshotmanifest"));
        //Return empty object if rawReport is null
        if(rawReport == null) return parseJson("{}");
        return parseJson(rawReport);
    }

    /**
     * Delete all snapshots on the limelight
     * @see Limelight#synchronousDeleteAllSnapshots()
     */
    public void deleteAllSnapshots() {
        async(() -> { getRequest("deletesnapshots"); return null; });
    }

    /**
     * Return the limelight's current hardware report
     * @return a json object containing the hardware report
     */
    public JsonNode getHWReport() {
        String rawReport = async(() -> getRequest("hwreport"));
        if(rawReport == null) return parseJson("{}");
        return parseJson(rawReport);
    }

    /**
     * @param raw a raw String containing json data
     * @return a JsonNode containing parsed json data, or null if the data is invalid
     * Errors are printed to stderr, and a null value is returned
     */
    private JsonNode parseJson(String raw) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(raw);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Current checks:
     * <ul>
     * <li> Pose is not null </li>
     * <li> Pose is not an empty Pose4d </li>
     * <li> Limelight has a target </li>
     * </ul>
     * @return true if all checks pass, otherwise false
     */
    public boolean trustPose() {
        Pose4d pose = getAlliancePose();
        return (
            pose != null &&
            pose != new Pose4d() &&
            hasTarget()
        );
    }

    /**
     * @param limelights an array containing all limelights to filter
     * @return an array containing only the limelights that pass the trustPose() check
     */
    public static Limelight[] filterLimelights(Limelight[] limelights) {
        Limelight[] out = {};
        for (Limelight limelight : limelights) {
            if(limelight.trustPose()) {
                out = Arrays.copyOf(out, out.length + 1);
                out[out.length-1] = limelight;
            }
        }

        return out;
    }
}
