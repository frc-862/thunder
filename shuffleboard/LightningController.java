package frc.thunder.shuffleboard;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

public class LightningController{
    
    private String name;
    public boolean portFound = true;
    public XboxController realController;

    public LightningController(String name, int port){
        try {
            realController = new XboxController(port);
        } catch (Exception e){
            portFound = false;
        } finally {
            this.name = name;
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

    public boolean simController(){
        return LightningShuffleboard.getBool(name, name + "simController", false);
    
    }

    /**
     * @return the leftX value from the controller/shuffleboard
     */
    public double getLeftX(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "LeftX", 0d);
        } else {
            return realController.getLeftX();
        }
    }

    /**
     * @return the leftY value from the controller/shuffleboard
     */
    public double getLeftY(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "LeftY", 0d);
        } else {
            return realController.getLeftY();
        }
    }

    /**
     * @return the rightX value from the controller/shuffleboard
     */
    public double getRightX(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "RightX", 0d);
        } else {
            return realController.getRightX();
        }
    }

    /**
     * @return the rightY value from the controller/shuffleboard
     */
    public double getRightY(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "RightY", 0d);
        } else {
            return realController.getRightY();
        }
    }

    /**
     * @return the leftTriggerAxis value from the controller/shuffleboard
     */
    public double getLeftTriggerAxis(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "LeftTriggerAxis", 0d);
        } else {
            return realController.getLeftTriggerAxis();
        }
    }

    /**
     * @return the rightTriggerAxis value from the controller/shuffleboard
     */
    public double getRightTriggerAxis(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "RightTriggerAxis", 0d);
        } else {
            return realController.getRightTriggerAxis();
        }
    }

    /**
     * @return the leftBumper value from the controller/shuffleboard
     */
    public boolean getLeftBumper(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "LeftBumper", false);
        } else {
            return realController.getLeftBumper();
        }
    }

    /**
     * @return the rightBumper value from the controller/shuffleboard
     */
    public boolean getRightBumper(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "RightBumper", false);
        } else {
            return realController.getRightBumper();
        }
    }

    /**
     * @return the leftStickButton value from the controller/shuffleboard
     */
    public boolean getLeftStickButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "LeftStickButton", false);
        } else {
            return realController.getLeftStickButton();
        }
    }

    /**
     * @return the rightStickButton value from the controller/shuffleboard
     */
    public boolean getRightStickButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "RightStickButton", false);
        } else {
            return realController.getRightStickButton();
        }
    }

    /**
     * @return the AButton value from the controller/shuffleboard
     */
    public boolean getAButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "AButton", false);
        } else {
            return realController.getAButton();
        }
    }

    /**
     * @return the BButton value from the controller/shuffleboard
     */
    public boolean getBButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "BButton", false);
        } else {
            return realController.getBButton();
        }
    }

    /**
     * @return the XButton value from the controller/shuffleboard
     */
    public boolean getXButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "XButton", false);
        } else {
            return realController.getXButton();
        }
    }

    /**
     * @return the YButton value from the controller/shuffleboard
     */
    public boolean getYButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "YButton", false);
        } else {
            return realController.getYButton();
        }
    }

    /**
     * @return the backButton value from the controller/shuffleboard
     */
    public boolean getBackButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "BackButton", false);
        } else {
            return realController.getBackButton();
        }
    }

    /**
     * @return the startButton value from the controller/shuffleboard
     */
    public boolean getStartButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "StartButton", false);
        } else {
            return realController.getStartButton();
        }
    }

    /**
     * @return the POV value from the controller/shuffleboard
     */
    public double getPOV(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "POV", 0d);
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
        LightningShuffleboard.getBool(name, name + "simController", false);
        LightningShuffleboard.getDouble(name, name + "LeftX", 0d);
        LightningShuffleboard.getDouble(name, name + "LeftY", 0d);
        LightningShuffleboard.getDouble(name, name + "RightX", 0d);
        LightningShuffleboard.getDouble(name, name + "RightY", 0d);
        LightningShuffleboard.getDouble(name, name + "LeftTriggerAxis", 0d);
        LightningShuffleboard.getDouble(name, name + "RightTriggerAxis", 0d);
        LightningShuffleboard.getBool(name, name + "LeftBumper", false);
        LightningShuffleboard.getBool(name, name + "RightBumper", false);
        LightningShuffleboard.getBool(name, name + "LeftStickButton", false);
        LightningShuffleboard.getBool(name, name + "RightStickButton", false);
        LightningShuffleboard.getBool(name, name + "AButton", false);
        LightningShuffleboard.getBool(name, name + "BButton", false);
        LightningShuffleboard.getBool(name, name + "XButton", false);
        LightningShuffleboard.getBool(name, name + "YButton", false);
        LightningShuffleboard.getBool(name, name + "BackButton", false);
        LightningShuffleboard.getBool(name, name + "StartButton", false);
        LightningShuffleboard.getDouble(name, name + "POV", 0d);
    }
}
