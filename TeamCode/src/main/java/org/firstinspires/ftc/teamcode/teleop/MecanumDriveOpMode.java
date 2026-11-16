package org.firstinspires.ftc.teamcode.teleop;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.internal.ui.GamepadUser;


@TeleOp


public class MecanumDriveOpMode extends OpMode {


    newMecanumDrive drive = new newMecanumDrive();
    double forward, strafe, rotate;






    @Override
    public void init() {
        drive.init(hardwareMap);


    }


    @Override
    public void loop() {
        forward = gamepad1.left_stick_y;
        strafe = -gamepad1.left_stick_x;
        rotate = -gamepad1.right_stick_x;


        if (gamepad1.dpad_up) {
            drive.drive(-1.0, 0, 0);
        }
        else if (gamepad1.dpad_down) {
            drive.drive(1.0, 0, 0);
        }
        else {
            drive.drive(forward, strafe, rotate);
        }


        telemetry.addData("Front Left", drive.getRPMFrontLeft());
        telemetry.addData("Back Left", drive.getRPMBackLeft());
        telemetry.addData("Front Right", drive.getRPMFrontRight());
        telemetry.addData("Back Right", drive.getRPMBackRight());
        telemetry.update();






    }
}

