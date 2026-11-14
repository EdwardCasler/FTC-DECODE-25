package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamServer;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;

@TeleOp(name = "Display Image Test")
public class images extends LinearOpMode {
    @Override
    public void runOpMode() {
        // 1. Load the image from TeamCode/src/main/res/drawable/my_image.png
        final Bitmap myImage = BitmapFactory.decodeResource(
                hardwareMap.appContext.getResources(),
                R.drawable.logo
        );

        // 2. Send the Bitmap to the Driver Station's Camera Stream server
        CameraStreamServer.getInstance().setSource(new CameraStreamSource() {
            @Override
            public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
                continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(myImage));
            }
        });

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Image ready in Camera Stream");
            telemetry.update();
        }
    }
}
