package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class newMecanumDrive {


    private DcMotorEx frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    private IMU imu;


    private static final double TICKS_PER_REV = 537.6;






    public void init(HardwareMap hwMap){
        frontLeftMotor = hwMap.get(DcMotorEx.class, "motor_front_left");
        backLeftMotor =  hwMap.get(DcMotorEx.class, "motor_back_left");
        frontRightMotor = hwMap.get(DcMotorEx.class, "motor_front_right");
        backRightMotor = hwMap.get(DcMotorEx.class, "motor_back_right");


        frontRightMotor.setDirection(DcMotorEx.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorEx.Direction.REVERSE);


        frontLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);


        imu = hwMap.get(IMU.class, "imu");




        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT);


        imu.initialize(new IMU.Parameters(RevOrientation));


    }


    public void drive(double forward, double strafe, double rotate){
        double frontLeftPower = forward + strafe + rotate;
        double backLeftPower = forward - strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;


        double maxPower = 1.0;
        double maxSpeed = 0.5;


        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));


        frontLeftMotor.setPower(maxSpeed*(frontLeftPower/maxPower));
        backLeftMotor.setPower(maxSpeed*(backLeftPower/maxPower));
        frontRightMotor.setPower(maxSpeed*(frontRightPower/maxPower));
        backRightMotor.setPower(maxSpeed*(backRightPower/maxPower));


    }


    public void driveFieldRelative(double forward, double strafe, double rotate){


    }


    public double getRPMFrontLeft() {
        double ticksPerSecond = frontLeftMotor.getVelocity();
        return (ticksPerSecond / TICKS_PER_REV) * 60;
    }


    public double getRPMBackLeft() {
        double ticksPerSecond = backLeftMotor.getVelocity();
        return (ticksPerSecond / TICKS_PER_REV) * 60;
    }


    public double getRPMFrontRight() {
        double ticksPerSecond = frontRightMotor.getVelocity();
        return (ticksPerSecond / TICKS_PER_REV) * 60;
    }


    public double getRPMBackRight() {
        double ticksPerSecond = backRightMotor.getVelocity();
        return (ticksPerSecond / TICKS_PER_REV) * 60;
    }
}

