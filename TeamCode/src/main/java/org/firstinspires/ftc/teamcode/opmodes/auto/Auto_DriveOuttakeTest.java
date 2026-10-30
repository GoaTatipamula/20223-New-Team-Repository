
package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import static com.pedropathing.api.Paths.line;

@Autonomous(name = "Auto_DriveOuttakeTest", group = "Autonomous")
public class Auto_DriveOuttakeTest extends OpMode {

    private Follower follower;

    private DcMotorEx intakeMotor1;
    private DcMotorEx intakeMotor2;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startPose =
            poseFactory.of(17, 13.78, 0);

    private final Pose targetPose =
            poseFactory.of(68, 15, 0);

    private Path drivePath;

    // Autonomous states
    private int autoState = 0;

    private long outtakeStartTime;

    private static final long OUTTAKE_TIME_MS = 1500;

    @Override
    public void init() {

        // Initialize PedroPathing
        follower = Constants.create(hardwareMap);

        // Set the robot's starting position
        follower.setPose(startPose);

        // Create the path
        drivePath = line(startPose, targetPose)
                .linear(startPose, targetPose);

        // Initialize the outtake motors
        intakeMotor1 = hardwareMap.get(
                DcMotorEx.class, "intake_motor_1");

        intakeMotor2 = hardwareMap.get(
                DcMotorEx.class, "intake_motor_2");

        telemetry.addLine("Test Auto initialized");
        telemetry.addData("Start X", startPose.x());
        telemetry.addData("Start Y", startPose.y());
        telemetry.addData("Target X", targetPose.x());
        telemetry.addData("Target Y", targetPose.y());
        telemetry.update();
    }

    @Override
    public void start() {
        // Begin driving to the target
        follower.follow(drivePath);
        autoState = 0;
    }

    @Override
    public void loop() {

        // Update PedroPathing every loop
        follower.update();

        switch (autoState) {

            case 0:
                // Wait for the robot to finish driving
                if (!follower.isBusy()) {

                    // Start outtaking the balls
                    intakeMotor1.setPower(-1.0);
                    intakeMotor2.setPower(-1.0);

                    outtakeStartTime = System.currentTimeMillis();

                    autoState = 1;
                }
                break;

            case 1:
                // Continue outtaking for 1.5 seconds
                if (System.currentTimeMillis() - outtakeStartTime
                        >= OUTTAKE_TIME_MS) {

                    // Stop both motors
                    intakeMotor1.setPower(0);
                    intakeMotor2.setPower(0);

                    autoState = 2;
                }
                break;

            case 2:
                // All actions are complete
                break;
        }

        // Telemetry
        telemetry.addData("Auto State", autoState);
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData(
                "Heading (degrees)",
                Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Busy", follower.isBusy());
        telemetry.addData(
                "Distance to Target",
                follower.distanceToEndpoint());
        telemetry.update();
    }

    @Override
    public void stop() {

        // Ensure the outtake motors stop
        if (intakeMotor1 != null) {
            intakeMotor1.setPower(0);
        }

        if (intakeMotor2 != null) {
            intakeMotor2.setPower(0);
        }

        if (follower != null) {
            follower.stop();
        }
    }
}
