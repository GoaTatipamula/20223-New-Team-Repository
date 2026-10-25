package org.firstinspires.ftc.teamcode.utils;

// numbers we might need to change go here
public class Constants {

    //VISION (Limelight)

    // pipeline 6 is the apriltag one
    public static final int VISION_PIPELINE = 6;

    // how fast it turns to face the tag. too high and it shakes
    // if it turns the wrong way make this negative
    public static final double AIM_KP = 0.02;

    // stop turning when its this close (degrees)
    public static final double AIM_DEADBAND_DEG = 1.0;

    // fastest it can turn while aiming
    public static final double AIM_MAX_POWER = 0.5;

    // give up aiming after this many seconds
    public static final double AIM_TIMEOUT_SEC = 2.0;
}
