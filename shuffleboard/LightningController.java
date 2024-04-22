package frc.thunder.shuffleboard;

import edu.wpi.first.wpilibj.XboxController;

public class LightningController extends XboxController{
    
    String name;

    public LightningController(String name, int port){
        super(port);
        this.name = name;
    }

    public LightningController(int port){
        super(port);
        this.name = "Controller " + port + " ";
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
            return super.getLeftX();
        }
    }

    /**
     * @return the leftY value from the controller/shuffleboard
     */
    public double getLeftY(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "LeftY", 0d);
        } else {
            return super.getLeftY();
        }
    }

    /**
     * @return the rightX value from the controller/shuffleboard
     */
    public double getRightX(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "RightX", 0d);
        } else {
            return super.getRightX();
        }
    }

    /**
     * @return the rightY value from the controller/shuffleboard
     */
    public double getRightY(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "RightY", 0d);
        } else {
            return super.getRightY();
        }
    }

    /**
     * @return the leftTriggerAxis value from the controller/shuffleboard
     */
    public double getLeftTriggerAxis(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "LeftTriggerAxis", 0d);
        } else {
            return super.getLeftTriggerAxis();
        }
    }

    /**
     * @return the rightTriggerAxis value from the controller/shuffleboard
     */
    public double getRightTriggerAxis(){
        if (simController()){
            return LightningShuffleboard.getDouble(name, name + "RightTriggerAxis", 0d);
        } else {
            return super.getRightTriggerAxis();
        }
    }

    /**
     * @return the leftBumper value from the controller/shuffleboard
     */
    public boolean getLeftBumper(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "LeftBumper", false);
        } else {
            return super.getLeftBumper();
        }
    }

    /**
     * @return the rightBumper value from the controller/shuffleboard
     */
    public boolean getRightBumper(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "RightBumper", false);
        } else {
            return super.getRightBumper();
        }
    }

    /**
     * @return the leftStickButton value from the controller/shuffleboard
     */
    public boolean getLeftStickButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "LeftStickButton", false);
        } else {
            return super.getLeftStickButton();
        }
    }

    /**
     * @return the rightStickButton value from the controller/shuffleboard
     */
    public boolean getRightStickButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "RightStickButton", false);
        } else {
            return super.getRightStickButton();
        }
    }

    /**
     * @return the AButton value from the controller/shuffleboard
     */
    public boolean getAButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "AButton", false);
        } else {
            return super.getAButton();
        }
    }

    /**
     * @return the BButton value from the controller/shuffleboard
     */
    public boolean getBButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "BButton", false);
        } else {
            return super.getBButton();
        }
    }

    /**
     * @return the XButton value from the controller/shuffleboard
     */
    public boolean getXButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "XButton", false);
        } else {
            return super.getXButton();
        }
    }

    /**
     * @return the YButton value from the controller/shuffleboard
     */
    public boolean getYButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "YButton", false);
        } else {
            return super.getYButton();
        }
    }

    /**
     * @return the backButton value from the controller/shuffleboard
     */
    public boolean getBackButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "BackButton", false);
        } else {
            return super.getBackButton();
        }
    }

    /**
     * @return the startButton value from the controller/shuffleboard
     */
    public boolean getStartButton(){
        if (simController()){
            return LightningShuffleboard.getBool(name, name + "StartButton", false);
        } else {
            return super.getStartButton();
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
    }
}
