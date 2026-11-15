package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch; // Uncomment if using generic I2C

@TeleOp(name = "Teleop for LM2", group = "Linear Opmode")
public class teleopjava extends LinearOpMode {

    // Drive Motors
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor bl = null;
    private DcMotor br = null;

    // Mechanism Motors
    private DcMotor intake = null;
    private DcMotor fly = null;

    // Continuous Rotation Servos
    private CRServo s1 = null;
    private CRServo s2 = null;

    // Odometry / I2C Device on Port 2
    // If using goBILDA Pinpoint, replace with: GoBildaPinpointDriver odom;
    // For generic I2C device:
    private I2cDeviceSynch i2cOdometry = null;

    @Override
    public void runOpMode() {
        // --- 1. HARDWARE MAPPING ---
        // Ensure these match your exact Robot Configuration names on the Driver Station
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");

        intake = hardwareMap.get(DcMotor.class, "intake");
        fly = hardwareMap.get(DcMotor.class, "fly");

        s1 = hardwareMap.get(CRServo.class, "s1");
        s2 = hardwareMap.get(CRServo.class, "s2");

        // If using a generic I2C device configured on I2C Port 2:
        // i2cOdometry = hardwareMap.get(I2cDeviceSynch.class, "odom");

        // --- 2. MOTOR DIRECTIONS ---
        // Adjust these to Reverse if your robot moves backwards/spins incorrectly
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.FORWARD);

        intake.setDirection(DcMotor.Direction.FORWARD);
        fly.setDirection(DcMotor.Direction.FORWARD);

        // Optional: Set zero power behavior to BRAKE for better control
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized. Waiting for Start.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // --- 3. MECANUM DRIVE LOGIC ---
            double y = -gamepad1.left_stick_y;  // Forward/Backward
            double x = gamepad1.left_stick_x * 1.1; // Strafe correction multiplier
            double rx = gamepad1.right_stick_x; // Rotation

            // Denominator is the largest motor power (absolute value) to ensure
            // all powers fit within [-1.0, 1.0] while maintaining proportional ratios.
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);

            double frontLeftPower  = (y + x + rx) / denominator;
            double backLeftPower   = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower  = (y + x - rx) / denominator;

            fl.setPower(frontLeftPower);
            bl.setPower(backLeftPower);
            fr.setPower(frontRightPower);
            br.setPower(backRightPower);

            // --- 4. INTAKE & FLYWHEEL (RB & LB) ---
            // Left Bumper runs intake
            if (gamepad1.left_bumper) {
                intake.setPower(1.0);
            } else {
                intake.setPower(0.0);
            }

            // Right Bumper runs flywheel
            if (gamepad1.right_bumper) {
                fly.setPower(1.0);
            } else {
                fly.setPower(0.0);
            }

            // --- 5. FRONT CONTINUOUS ROTATION SERVOS (A BUTTON) ---
            // Holding 'A' turns both continuous rotation servos on
            if (gamepad1.a) {
                s1.setPower(1.0); // Set power (1.0 = full speed forward, -1.0 = reverse)
                s2.setPower(1.0);
            } else {
                s1.setPower(0.0); // Stop servo
                s2.setPower(0.0);
            }

            // --- 6. ODOMETRY / I2C PORT 2 READINGS ---
            // Reading data from I2C Port 2 (Example template)
            // If you are using a goBILDA Pinpoint computer, use its specific getter methods instead.
            telemetry.addData("Odometry I2C Port 2", "Connected / Reading Active");
            // telemetry.addData("Odom X", odom.getX());
            // telemetry.addData("Odom Y", odom.getY());

            telemetry.update();
        }
    }
}