package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Pedro Test Auto", group = "Autonomous")
public class TestAuto extends OpMode {

    private Follower follower;

    // Define coordinate poses (X, Y, Heading in Radians)
    private final Pose startPose  = new Pose(0, 0, Math.toRadians(0));
    private final Pose point1     = new Pose(5, 0, Math.toRadians(0));   // 5" Forward
    private final Pose point2     = new Pose(5, 5, Math.toRadians(0));  // 5" Left
    private final Pose endPose    = new Pose(0, 0, Math.toRadians(0));   // Back to Origin

    private PathChain testPath;

    @Override
    public void init() {
        // Initialize the follower with your tuned Constants
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        follower.setMaxPower(0.10);

        // Build the chained path sequence passing Pose directly into BezierLine
        testPath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, point1))
                .setLinearHeadingInterpolation(startPose.getHeading(), point1.getHeading())

                .addPath(new BezierLine(point1, point2))
                .setLinearHeadingInterpolation(point1.getHeading(), point2.getHeading())

                .addPath(new BezierLine(point2, endPose))
                .setLinearHeadingInterpolation(point2.getHeading(), endPose.getHeading())

                .build();

        telemetry.addData("Status", "Initialized. Press Play to start path.");
        telemetry.update();
    }

    @Override
    public void start() {
        // Start following the generated path sequence
        follower.followPath(testPath);
    }

    @Override
    public void loop() {
        // Update follower state on every iteration
        follower.update();

        // Print real-time robot pose to Driver Station telemetry
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading (Deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.update();
    }
}