package org.firstinspires.ftc.teamcode.Auto;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.res.ResourcesCompat;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Util.Logging.ImageSaver;


@TeleOp(name = "Test Logging")
public class Placeholder extends LinearOpMode {

    private ImageSaver imageSaver;


    @Override
    public void runOpMode() throws InterruptedException {


        Bitmap image = BitmapFactory.decodeFile("res/drawable/skystoneField.png");

        imageSaver = new ImageSaver("testImage.png", image);
        waitForStart();
        imageSaver.save();


    }

}
