package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.utils.Constants;

// testing the limelight
// hold A and the robot turns to face the tag
@TeleOp(name = "TeleOp_VisionTest")
public class TeleOp_VisionTest extends OpMode {

    MecanumDriveSubsystem drive = new MecanumDriveSubsystem();
    VisionSubsystem vision = new VisionSubsystem();

    double forward, strafe, rotate;

    ElapsedTime aimTimer = new ElapsedTime();
    boolean lastA = false;

    @Override
    public void init() {
        drive.init(hardwareMap);
        vision.init(hardwareMap);

        // makes telemetry update faster
        telemetry.setMsTransmissionInterval(11);
    }

    @Override
    public void loop() {
        vision.update();

        forward = -gamepad1.left_stick_y; // same as TeleOp_DriveIntake, pushing up is forward
        strafe = -gamepad1.left_stick_x;
        rotate = -gamepad1.right_stick_x;

        // reset timer when A is first pressed
        if (gamepad1.a && !lastA) {
            aimTimer.reset();
        }
        lastA = gamepad1.a;

        // holding A = camera does the turning
        boolean aiming = gamepad1.a && aimTimer.seconds() < Constants.AIM_TIMEOUT_SEC;
        if (aiming) {
            rotate = vision.getAimTurnPower();
        }

        drive.drive(forward, strafe, rotate);

        telemetry.addData("Sees Tag", vision.hasTarget());
        telemetry.addData("tx", vision.getTx());
        telemetry.addData("ty", vision.getTy());
        telemetry.addData("ta", vision.getTa());
        telemetry.addData("Aiming", aiming);
        telemetry.addData("Lined Up", vision.isAimed());
        telemetry.update();
    }

    @Override
    public void stop() {
        vision.stop();
    }
}
