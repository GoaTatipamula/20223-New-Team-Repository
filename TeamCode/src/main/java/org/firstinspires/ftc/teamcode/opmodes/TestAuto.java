package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Pedro Test Auto", group = "Autonomous")
public class TestAuto extends LinearOpMode {

    private Follower follower;

    private final Pose startPose  = new Pose(0, 0, Math.toRadians(0));
    private final Pose point1     = new Pose(24, 0, Math.toRadians(0));   // Increased distance to 24 inches
    private final Pose point2     = new Pose(24, 24, Math.toRadians(0));  // Increased distance to 24 inches
    private final Pose endPose    = new Pose(0, 0, Math.toRadians(0));

    private PathChain testPath;

    public void buildPaths() {
        testPath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, point1))
                .setLinearHeadingInterpolation(startPose.getHeading(), point1.getHeading())
                .addPath(new BezierLine(point1, point2))
                .setLinearHeadingInterpolation(point1.getHeading(), point2.getHeading())
                .addPath(new BezierLine(point2, endPose))
                .setLinearHeadingInterpolation(point2.getHeading(), endPose.getHeading())
                .build();
    }

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.setMaxPower(0.25   );

        buildPaths();

        telemetry.addData("Status", "Initialized. Ready to start.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        follower.followPath(testPath, true);

        while (opModeIsActive()) {
            follower.update();

            telemetry.addData("X", follower.getPose().getX());
            telemetry.addData("Y", follower.getPose().getY());
            telemetry.addData("Heading (Deg)", Math.toDegrees(follower.getPose().getHeading()));
            telemetry.addData("Is Busy", follower.isBusy());
            telemetry.update();
        }
    }
}