package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

@TeleOp(name = "TeleOp_DriveIntake", group = "TeleOp")
public class TeleOp_DriveIntake extends OpMode {

    // Subsystem instances
    private final MecanumDriveSubsystem drive = new MecanumDriveSubsystem();
    private final IntakeSubsystem intake = new IntakeSubsystem();

    // Motor control variables
    private boolean isContinuousForward = false;
    private boolean isContinuousReverse = false;

    // Edge detection for gamepad buttons
    private boolean lastGamepad1A = false;
    private boolean lastGamepad1B = false;
    private boolean lastGamepad1X = false;

    @Override
    public void init() {
        // Initialize mecanum drive motors and IMU
        drive.init(hardwareMap);

        // Initialize intake subsystem
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {
        // 1. MECANUM DRIVETRAIN CONTROLS
        double forward = -gamepad1.left_stick_y; // Inverted so pushing up is positive
        double strafe = -gamepad1.left_stick_x;
        double rotate = -gamepad1.right_stick_x;

        // D-pad override or standard joystick drive
        if (gamepad1.dpad_up) {
            drive.drive(1.0, 0.0, 0.0);
        } else if (gamepad1.dpad_down) {
            drive.drive(-1.0, 0.0, 0.0);
        } else {
            drive.drive(forward, strafe, rotate);
        }

        // 2. DUAL INTAKE / OUTTAKE MOTOR CONTROLS
        // Button A: Toggle continuous forward (Intake)
        if (gamepad1.a && !lastGamepad1A) {
            isContinuousForward = !isContinuousForward;
            if (isContinuousForward) {
                isContinuousReverse = false;
            }
        }
        lastGamepad1A = gamepad1.a;

        // Button B: Toggle continuous reverse (Outtake)
        if (gamepad1.b && !lastGamepad1B) {
            isContinuousReverse = !isContinuousReverse;
            if (isContinuousReverse) {
                isContinuousForward = false;
            }
        }
        lastGamepad1B = gamepad1.b;

        // Button X: Stop intake motors
        if (gamepad1.x && !lastGamepad1X) {
            isContinuousForward = false;
            isContinuousReverse = false;
        }
        lastGamepad1X = gamepad1.x;

        // Determine intake motor power
        double spinMotorPower;
        if (gamepad1.right_trigger > 0.1) {
            spinMotorPower = gamepad1.right_trigger;
            isContinuousForward = false;
            isContinuousReverse = false;
        } else if (gamepad1.left_trigger > 0.1) {
            spinMotorPower = -gamepad1.left_trigger;
            isContinuousForward = false;
            isContinuousReverse = false;
        } else if (isContinuousForward) {
            spinMotorPower = 1.0;
        } else if (isContinuousReverse) {
            spinMotorPower = -1.0;
        } else {
            spinMotorPower = 0.0;
        }

        // Apply power via intake subsystem
        intake.setPower(spinMotorPower);

        // 3. TELEMETRY READOUTS
        String motorState;
        if (isContinuousForward) {
            motorState = "FORWARD (TOGGLED)";
        } else if (isContinuousReverse) {
            motorState = "REVERSE (TOGGLED)";
        } else if (spinMotorPower != 0.0) {
            motorState = "MANUAL (TRIGGERS)";
        } else {
            motorState = "STOPPED";
        }

        telemetry.addData("Intake Motor State", motorState);
        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Intake Motor 1 RPM", intake.getMotor1RPM());
        telemetry.addData("Intake Motor 2 RPM", intake.getMotor2RPM());

        telemetry.addData("Front Left RPM", drive.getRPMFrontLeft());
        telemetry.addData("Back Left RPM", drive.getRPMBackLeft());
        telemetry.addData("Front Right RPM", drive.getRPMFrontRight());
        telemetry.addData("Back Right RPM", drive.getRPMBackRight());
        telemetry.update();
    }
}
