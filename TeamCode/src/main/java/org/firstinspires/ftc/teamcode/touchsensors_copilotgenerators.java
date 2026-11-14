
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Fires the flywheel and intake, then follows the two-touch-sensor path.
 *
 * <p>Configure the hardware with these names:
 * fl, fr, bl, br,
 * flywheel, intake, touch_left, and touch_front.</p>
 */
@Autonomous(name = "Touch Sensor Auto", group = "Competition")
public class touchsensors_copilotgenerators extends LinearOpMode {

    private static final double DRIVE_POWER = 0.5;
    private static final double FLYWHEEL_POWER = 1.0;
    private static final double INTAKE_POWER = 1.0;
    private static final double FIRE_TIME_SECONDS = 5.0;
    private static final double SENSOR_CLEARANCE_SECONDS = 0.1;

    private DcMotor frontLeftDrive;
    private DcMotor backLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backRightDrive;
    private DcMotor flywheel;
    private DcMotor intake;
    private TouchSensor leftSensor;
    private TouchSensor frontSensor;

    @Override
    public void runOpMode() {
        initializeHardware();

        telemetry.addLine("Touch Sensor Auto ready");
        telemetry.addLine("Path: fire, left, right, forward, backward");
        telemetry.update();
        waitForStart();

        if (!opModeIsActive()) {
            return;
        }

        try {
            runFireSequence();

            // Move left until the left sensor is pressed.
            strafeLeft(DRIVE_POWER);
            waitForSensor(leftSensor, "left sensor");
            stopDrive();

            // Back away from the sensor before moving toward the second sensor.
            strafeRight(DRIVE_POWER);
            runForSeconds(SENSOR_CLEARANCE_SECONDS);
            stopDrive();

            // Move forward until the second sensor is pressed.
            driveForward(DRIVE_POWER);
            waitForSensor(frontSensor, "front sensor");
            stopDrive();

            driveBackward(DRIVE_POWER);
            runForSeconds(SENSOR_CLEARANCE_SECONDS);
        } finally {
            stopDrive();
            flywheel.setPower(0);
            intake.setPower(0);
        }

        telemetry.addLine("Autonomous complete");
        telemetry.update();
    }

    private void initializeHardware() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "fl");
        backLeftDrive = hardwareMap.get(DcMotor.class, "bl");
        frontRightDrive = hardwareMap.get(DcMotor.class, "fr");
        backRightDrive = hardwareMap.get(DcMotor.class, "br");
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        intake = hardwareMap.get(DcMotor.class, "intake");
        leftSensor = hardwareMap.get(TouchSensor.class, "touch_left");
        frontSensor = hardwareMap.get(TouchSensor.class, "touch_front");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    private void runFireSequence() {
        flywheel.setPower(FLYWHEEL_POWER);
        intake.setPower(INTAKE_POWER);
        runForSeconds(FIRE_TIME_SECONDS);
        flywheel.setPower(0);
        intake.setPower(0);
    }

    private void waitForSensor(TouchSensor sensor, String sensorName) {
        while (opModeIsActive() && !sensor.isPressed()) {
            telemetry.addData("Waiting for", sensorName);
            telemetry.update();
            idle();
        }
    }

    private void runForSeconds(double seconds) {
        long endTimeNanos = System.nanoTime() + (long) (seconds * 1_000_000_000L);
        while (opModeIsActive() && System.nanoTime() < endTimeNanos) {
            idle();
        }
    }

    private void driveForward(double power) {
        setDrivePower(power, power, power, power);
    }

    private void driveBackward(double power) {
        setDrivePower(-power, -power, -power, -power);
    }

    private void strafeLeft(double power) {
        setDrivePower(-power, power, power, -power);
    }

    private void strafeRight(double power) {
        setDrivePower(power, -power, -power, power);
    }

    private void setDrivePower(
            double frontLeftPower,
            double backLeftPower,
            double frontRightPower,
            double backRightPower) {
        frontLeftDrive.setPower(frontLeftPower);
        backLeftDrive.setPower(backLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backRightDrive.setPower(backRightPower);
    }

    private void stopDrive() {
        setDrivePower(0, 0, 0, 0);
    }

}
