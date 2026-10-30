package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Constants;

// all the limelight code is in here
public class VisionSubsystem {

    private Limelight3A limelight;

    // what the camera sees. can be null
    private LLResult result;

    public void init(HardwareMap hwMap) {
        // has to be named "limelight" in the config
        limelight = hwMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(Constants.VISION_PIPELINE);
        limelight.start(); // camera doesnt send anything without this
    }

    // call this every loop to get new camera data
    public void update() {
        result = limelight.getLatestResult();
    }

    // true if the camera sees a tag
    // checks null first so it doesnt crash when theres no tag
    public boolean hasTarget() {
        return result != null && result.isValid();
    }

    // how far left/right the tag is in degrees. 0 = straight ahead
    public double getTx() {
        if (!hasTarget()) return 0;
        return result.getTx();
    }

    // how far up/down the tag is. just for telemetry
    public double getTy() {
        if (!hasTarget()) return 0;
        return result.getTy();
    }

    // how big the tag looks. bigger = closer
    public double getTa() {
        if (!hasTarget()) return 0;
        return result.getTa();
    }

    // how much to turn to face the tag
    public double getAimTurnPower() {
        // no tag, dont turn
        if (!hasTarget()) return 0;

        double tx = getTx();

        // close enough, stop
        if (Math.abs(tx) < Constants.AIM_DEADBAND_DEG) return 0;

        // farther away = turn harder
        double turn = -Constants.AIM_KP * tx;

        // dont go over max power
        return Math.max(-Constants.AIM_MAX_POWER, Math.min(Constants.AIM_MAX_POWER, turn));
    }

    // true if we're facing the tag
    public boolean isAimed() {
        return hasTarget() && Math.abs(getTx()) < Constants.AIM_DEADBAND_DEG;
    }

    public void stop() {
        limelight.stop();
    }
}
