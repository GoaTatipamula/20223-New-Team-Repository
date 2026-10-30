package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LimelightServoSubsystem {
    private final Servo servo;
    private boolean isUp;
    private static final double UP = 1.0; //max servo value
    private static final double DOWN = 0.0; //minimum servo value

    // hardware map
    public LimelightServoSubsystem(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "limelight_servo");
    }
    //moves servo up
    public void servoUp() {
        servo.setPosition(UP);
        isUp = true;
    }
    //moves servo down
    public void servoDown() {
        servo.setPosition(DOWN);
        isUp = false;
    }
    //moves servo to a user specifed position
    public void moveToPosition(double position){
        servo.setPosition(position);
    }

    public double getServoPosition(){
        return servo.getPosition();
    }

    public boolean isServoUp(){
        if (isUp){
            return true;
        }
        else{
            return false;
        }
    }
}
