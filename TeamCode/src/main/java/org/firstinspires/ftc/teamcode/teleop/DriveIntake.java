package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "DriveIntake", group = "TeleOp")
public class DriveIntake extends OpMode {

    // Drivetrain instance
    private final newMecanumDrive drive = new newMecanumDrive();

    // Dual Intake / Outtake Motors (MUST Operate simultaneously)
    private static final double TICKS_PER_REV = 537.6;
    private DcMotorEx intakeMotor1;
    private DcMotorEx intakeMotor2;

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

        // Initialize both intake motors from hardware map ("intake_motor_1" and "intake_motor_2" need to be in the drive station config)
        intakeMotor1 = hardwareMap.get(DcMotorEx.class, "intake_motor_1");
        intakeMotor2 = hardwareMap.get(DcMotorEx.class, "intake_motor_2");

        // Reverse motor 2 if mounted opposite to motor 1 so both pull/push in the same direction
        intakeMotor2.setDirection(DcMotorEx.Direction.REVERSE);

        // Set brake behavior on both motors so they stop simultaneously
        intakeMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set run mode on both motors simultaneously
        intakeMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        // 1. MECANUM DRIVETRAIN CONTROLS
        double forward = gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double rotate = -gamepad1.right_stick_x;

        // D-pad override or standard joystick drive
        if (gamepad1.dpad_up) {
            drive.drive(-1.0, 0.0, 0.0);
        } else if (gamepad1.dpad_down) {
            drive.drive(1.0, 0.0, 0.0);
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

        // Apply power to BOTH motors simultaneously - neither should operate without the other
        intakeMotor1.setPower(spinMotorPower);
        intakeMotor2.setPower(spinMotorPower);

        // Calculate Intake Motor RPMs
        double rpm1 = (intakeMotor1.getVelocity() / TICKS_PER_REV) * 60.0;
        double rpm2 = (intakeMotor2.getVelocity() / TICKS_PER_REV) * 60.0;

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
        telemetry.addData("Intake Power", spinMotorPower);
        telemetry.addData("Intake Motor 1 RPM", rpm1);
        telemetry.addData("Intake Motor 2 RPM", rpm2);

        telemetry.addData("Front Left RPM", drive.getRPMFrontLeft());
        telemetry.addData("Back Left RPM", drive.getRPMBackLeft());
        telemetry.addData("Front Right RPM", drive.getRPMFrontRight());
        telemetry.addData("Back Right RPM", drive.getRPMBackRight());
        telemetry.update();
    }
}
