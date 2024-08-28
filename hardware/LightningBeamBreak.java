package frc.thunder.hardware;

import com.ctre.phoenix6.Utils;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.Constants.RobotMap.DIO;

public class LightningBeamBreak {

    private DigitalInput beamBreak;
    private boolean simIsTriggered;

    /**
     * Constructs a new beambreak
     * @param channel DIO port
     */
    public LightningBeamBreak(int channel){
        if (RobotBase.isReal()){
            beamBreak = new DigitalInput(channel);
        } else {
            simIsTriggered = false;
        }
    }

    /**
     * set the state of the beambreak during simulations
     * @param state
     */
    public void setIsTriggered(boolean state){
        simIsTriggered = state;
    }

    /**
     * The beambreak will be marked true for a specified duration (simulation only)
     * @param triggerDuration
     */
    public void trigger(double triggerDuration){
        double initialTime = Utils.getCurrentTimeSeconds();
        while(Utils.getCurrentTimeSeconds() < initialTime + triggerDuration){
            // isTriggered = true;
        }
        simIsTriggered = false;

    }

    /**
     * @return if the beambreak is triggered
     */
    public boolean get(){
        if (RobotBase.isReal()){
            return beamBreak.get();
        }
        return simIsTriggered;
    }

    /**
     * Can be used to access the other methods in the DigitalInput class
     * @return real beambreak
     */
    public DigitalInput getRealBeamBreak(){
        return beamBreak;
    }

}
