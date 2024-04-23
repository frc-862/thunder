package frc.thunder.shuffleboard;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

public class LightningController{
    
    private String name;
    public boolean portFound = true;
    public XboxController realController;

    // values from shuffleboard

    public DoubleSupplier leftX;
    public DoubleSupplier leftY;
    public DoubleSupplier rightX;
    public DoubleSupplier rightY;
    public DoubleSupplier leftTriggerAxis;
    public DoubleSupplier rightTriggerAxis;
    public BooleanSupplier simController;
    public BooleanSupplier leftBumper;
    public BooleanSupplier rightBumper;
    public BooleanSupplier leftStickButton;
    public BooleanSupplier rightStickButton;
    public BooleanSupplier AButton;
    public BooleanSupplier BButton;
    public BooleanSupplier XButton;
    public BooleanSupplier YButton;
    public BooleanSupplier backButton;
    public BooleanSupplier startButton;
    public DoubleSupplier POV;

    public LightningController(String name, int port){
        try {
            realController = new XboxController(port);
        } catch (Exception e){
            if (!DriverStation.isFMSAttached()){
                portFound = false;
            } else {
                throw e;
            }
        } finally {
            this.name = name;
            if (!DriverStation.isFMSAttached()){
                initValues();
            }
        }
    }

    public LightningController(int port){
        try {
            realController = new XboxController(port);
        } catch (Exception e){
            portFound = false;
        } finally {
            this.name = "Controller " + port + " ";
        }
    }

    /**
     * @return if the controller is being simulated
     */
    public boolean simController(){
        return !portFound || simController.getAsBoolean();
    }

    /**
     * @return the leftX value from the controller/shuffleboard
     */
    public double getLeftX(){
        if (simController()){
            return leftX.getAsDouble();
        } else {
            return realController.getLeftX();
        }
    }

    /**
     * @return the leftY value from the controller/shuffleboard
     */
    public double getLeftY(){
        if (simController()){
            return leftY.getAsDouble();
        } else {
            return realController.getLeftY();
        }
    }

    /**
     * @return the rightX value from the controller/shuffleboard
     */
    public double getRightX(){
        if (simController()){
            return rightX.getAsDouble();
        } else {
            return realController.getRightX();
        }
    }

    /**
     * @return the rightY value from the controller/shuffleboard
     */
    public double getRightY(){
        if (simController()){
            return rightY.getAsDouble();
        } else {
            return realController.getRightY();
        }
    }

    /**
     * @return the leftTriggerAxis value from the controller/shuffleboard
     */
    public double getLeftTriggerAxis(){
        if (simController()){
            return leftTriggerAxis.getAsDouble();
        } else {
            return realController.getLeftTriggerAxis();
        }
    }

    /**
     * @return the rightTriggerAxis value from the controller/shuffleboard
     */
    public double getRightTriggerAxis(){
        if (simController()){
            return rightTriggerAxis.getAsDouble();
        } else {
            return realController.getRightTriggerAxis();
        }
    }

    /**
     * @return the leftBumper value from the controller/shuffleboard
     */
    public boolean getLeftBumper(){
        if (simController()){
            return leftBumper.getAsBoolean();
        } else {
            return realController.getLeftBumper();
        }
    }

    /**
     * @return the rightBumper value from the controller/shuffleboard
     */
    public boolean getRightBumper(){
        if (simController()){
            return rightBumper.getAsBoolean();
        } else {
            return realController.getRightBumper();
        }
    }

    /**
     * @return the leftStickButton value from the controller/shuffleboard
     */
    public boolean getLeftStickButton(){
        if (simController()){
            return leftStickButton.getAsBoolean();
        } else {
            return realController.getLeftStickButton();
        }
    }

    /**
     * @return the rightStickButton value from the controller/shuffleboard
     */
    public boolean getRightStickButton(){
        if (simController()){
            return rightStickButton.getAsBoolean();
        } else {
            return realController.getRightStickButton();
        }
    }

    /**
     * @return the AButton value from the controller/shuffleboard
     */
    public boolean getAButton(){
        if (simController()){
            return AButton.getAsBoolean();
        } else {
            return realController.getAButton();
        }
    }

    /**
     * @return the BButton value from the controller/shuffleboard
     */
    public boolean getBButton(){
        if (simController()){
            return BButton.getAsBoolean();
        } else {
            return realController.getBButton();
        }
    }

    /**
     * @return the XButton value from the controller/shuffleboard
     */
    public boolean getXButton(){
        if (simController()){
            return XButton.getAsBoolean();
        } else {
            return realController.getXButton();
        }
    }

    /**
     * @return the YButton value from the controller/shuffleboard
     */
    public boolean getYButton(){
        if (simController()){
            return YButton.getAsBoolean();
        } else {
            return realController.getYButton();
        }
    }

    /**
     * @return the backButton value from the controller/shuffleboard
     */
    public boolean getBackButton(){
        if (simController()){
            return backButton.getAsBoolean();
        } else {
            return realController.getBackButton();
        }
    }

    /**
     * @return the startButton value from the controller/shuffleboard
     */
    public boolean getStartButton(){
        if (simController()){
            return startButton.getAsBoolean();
        } else {
            return realController.getStartButton();
        }
    }

    /**
     * @return the POV value from the controller/shuffleboard
     */
    public double getPOV(){
        if (simController()){
            return POV.getAsDouble();
        } else {
            return realController.getPOV();
        }
    }

    /**
     * @return set controller rumble
     */
    public void setRumble(RumbleType type, double rumble){
        if (simController()){
            LightningShuffleboard.setDouble(name, name + "Rumble", rumble);
        } else {
            realController.setRumble(type, rumble);
        }
    }

    

    /**
     * @return get controller
     */
    public XboxController getController(){
        if (simController()){
            return null;
        } else {
            return realController;
        }
    }

    /**
     * Initializes all the values for the controller on shuffleboard
     */
    public void initValues(){
        simController = () -> LightningShuffleboard.getBool(name, name + "simController", false);
        if (simController()){
            leftX = () -> LightningShuffleboard.getDouble(name, name + "LeftX", 0d);
            leftY = () -> LightningShuffleboard.getDouble(name, name + "LeftY", 0d);
            rightX = () -> LightningShuffleboard.getDouble(name, name + "RightX", 0d);
            rightY = () -> LightningShuffleboard.getDouble(name, name + "RightY", 0d);
            leftTriggerAxis = () -> LightningShuffleboard.getDouble(name, name + "LeftTriggerAxis", 0d);
            rightTriggerAxis = () -> LightningShuffleboard.getDouble(name, name + "RightTriggerAxis", 0d);
            leftBumper = () -> LightningShuffleboard.getBool(name, name + "LeftBumper", false);
            rightBumper = () -> LightningShuffleboard.getBool(name, name + "RightBumper", false);
            leftStickButton = () -> LightningShuffleboard.getBool(name, name + "LeftStickButton", false);
            rightStickButton = () -> LightningShuffleboard.getBool(name, name + "RightStickButton", false);
            AButton = () -> LightningShuffleboard.getBool(name, name + "AButton", false);
            BButton = () -> LightningShuffleboard.getBool(name, name + "BButton", false);
            XButton = () -> LightningShuffleboard.getBool(name, name + "XButton", false);
            YButton = () -> LightningShuffleboard.getBool(name, name + "YButton", false);
            backButton = () -> LightningShuffleboard.getBool(name, name + "BackButton", false);
            startButton = () -> LightningShuffleboard.getBool(name, name + "StartButton", false);
            POV = () -> LightningShuffleboard.getDouble(name, name + "POV", 0d);
        }
    }
}
