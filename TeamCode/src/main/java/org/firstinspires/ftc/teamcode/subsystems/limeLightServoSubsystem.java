package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class limeLightServoSubsystem {
    private final Servo servo;
    private static final double UP = 1.0; //max servo value
    private static final double DOWN = 0.0; //minimum servo value

    // hardware map
    public limeLightServoSubsystem(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "limelight_servo");
    }
    //moves servo up
    public void up() {
        servo.setPosition(UP);
    }
    //moves servo down
    public void down() {
        servo.setPosition(DOWN);
    }
    //moves servo to a user specifed position
    public void moveToPosistion(double position){
        servo.setPosition(position);
    }
}
