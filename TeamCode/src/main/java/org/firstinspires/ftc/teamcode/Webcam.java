package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

public class Webcam extends OpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);
    }
    public void loop() {
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagById(20);
        aprilTagWebcam.displayDetectionTelemetry(id20);

        //move to apriltag

    }
}
