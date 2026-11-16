package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Spin", group = "TeleOp")
public class Spin extends LinearOpMode {
    private DcMotor spinMotor;

    @Override
    public void runOpMode() {
        spinMotor = hardwareMap.get(DcMotor.class, "expansion_motor");
        double motorPower = 0.0;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                motorPower = 0.5;
            } else if (gamepad1.dpad_down) {
                motorPower = -0.5;
            } else if (gamepad1.dpad_left) {
                motorPower = 0.0;
            }

            spinMotor.setPower(motorPower);
        }
    }
}