package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSubsystem {

    private static final double TICKS_PER_REV = 537.6;

    private DcMotorEx intakeMotor1;
    private DcMotorEx intakeMotor2;

    private double currentPower = 0.0;

    public void init(HardwareMap hardwareMap) {
        // Initialize both intake motors from hardware map ("intake_motor_1" and "intake_motor_2")
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

    /**
     * Sets power to both intake motors simultaneously.
     *
     * @param power Power level between -1.0 and 1.0
     */
    public void setPower(double power) {
        this.currentPower = power;
        if (intakeMotor1 != null) {
            intakeMotor1.setPower(power);
        }
        if (intakeMotor2 != null) {
            intakeMotor2.setPower(power);
        }
    }

    /**
     * Stops both intake motors.
     */
    public void stop() {
        setPower(0.0);
    }

    /**
     * Gets the current target power set to the intake motors.
     */
    public double getPower() {
        return currentPower;
    }

    /**
     * Calculates and returns the current RPM of Intake Motor 1.
     */
    public double getMotor1RPM() {
        if (intakeMotor1 == null) return 0.0;
        return (intakeMotor1.getVelocity() / TICKS_PER_REV) * 60.0;
    }

    /**
     * Calculates and returns the current RPM of Intake Motor 2.
     */
    public double getMotor2RPM() {
        if (intakeMotor2 == null) return 0.0;
        return (intakeMotor2.getVelocity() / TICKS_PER_REV) * 60.0;
    }
}
