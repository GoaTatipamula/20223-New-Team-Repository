package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class MecanumDriveSubsystem {

    private static final double TICKS_PER_REV = 537.6;

    private DcMotorEx frontLeftMotor;
    private DcMotorEx backLeftMotor;
    private DcMotorEx frontRightMotor;
    private DcMotorEx backRightMotor;
    private IMU imu;

    /**
     * Initializes the mecanum drive motors and IMU from the HardwareMap.
     */
    public void init(HardwareMap hwMap) {
        frontLeftMotor = hwMap.get(DcMotorEx.class, "motor_front_left");
        backLeftMotor = hwMap.get(DcMotorEx.class, "motor_back_left");
        frontRightMotor = hwMap.get(DcMotorEx.class, "motor_front_right");
        backRightMotor = hwMap.get(DcMotorEx.class, "motor_back_right");

        frontRightMotor.setDirection(DcMotorEx.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorEx.Direction.REVERSE);

        frontLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        imu = hwMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT);

        imu.initialize(new IMU.Parameters(revOrientation));
    }

    /**
     * Robot-centric mecanum drive control.
     */
    public void drive(double forward, double strafe, double rotate) {
        double frontLeftPower = forward + strafe + rotate;
        double backLeftPower = forward - strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;

        double maxSpeed = 0.5;
        double maxPower = Math.max(1.0, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        if (frontLeftMotor != null) frontLeftMotor.setPower(maxSpeed * (frontLeftPower / maxPower));
        if (backLeftMotor != null) backLeftMotor.setPower(maxSpeed * (backLeftPower / maxPower));
        if (frontRightMotor != null) frontRightMotor.setPower(maxSpeed * (frontRightPower / maxPower));
        if (backRightMotor != null) backRightMotor.setPower(maxSpeed * (backRightPower / maxPower));
    }

    /**
     * Field-centric mecanum drive control.
     */
    public void driveFieldRelative(double forward, double strafe, double rotate) {
        if (imu == null) {
            drive(forward, strafe, rotate);
            return;
        }
        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotX = strafe * Math.cos(-heading) - forward * Math.sin(-heading);
        double rotY = strafe * Math.sin(-heading) + forward * Math.cos(-heading);
        drive(rotY, rotX, rotate);
    }

    /**
     * Resets the IMU heading/yaw to zero.
     */
    public void resetHeading() {
        if (imu != null) {
            imu.resetYaw();
        }
    }

    public double getRPMFrontLeft() {
        if (frontLeftMotor == null) return 0.0;
        return (frontLeftMotor.getVelocity() / TICKS_PER_REV) * 60.0;
    }

    public double getRPMBackLeft() {
        if (backLeftMotor == null) return 0.0;
        return (backLeftMotor.getVelocity() / TICKS_PER_REV) * 60.0;
    }

    public double getRPMFrontRight() {
        if (frontRightMotor == null) return 0.0;
        return (frontRightMotor.getVelocity() / TICKS_PER_REV) * 60.0;
    }

    public double getRPMBackRight() {
        if (backRightMotor == null) return 0.0;
        return (backRightMotor.getVelocity() / TICKS_PER_REV) * 60.0;
    }
}
