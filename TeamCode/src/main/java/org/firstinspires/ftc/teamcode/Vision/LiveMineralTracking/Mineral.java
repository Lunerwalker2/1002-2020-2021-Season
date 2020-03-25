package org.firstinspires.ftc.teamcode.Vision.LiveMineralTracking;

import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;

import java.util.ArrayList;
import java.util.EnumMap;

public class Mineral {

    private static boolean determineDistanceFromWidth = true;

    private static double cameraFocalLength = 822.317;


    public enum MineralType {
        SILVER("Silver Mineral"),
        GOLD("Gold Mineral");

        String label;


        MineralType(String label){
            this.label = label;
        }

        public static MineralType findByLabel(String label){
            if(label.equals(SILVER.label)) return SILVER;
            else if(label.equals(GOLD.label)) return GOLD;
            else return null;
        }
    }


    private final MineralType mineralType;

    private final Recognition parentRecognition;

    private final double distance;

    private final double angle;

    /*
    2"x2"x2" for the gold
and 2.5" diameter for the silver
     */


    public Mineral(MineralType mineralType, Recognition parentRecognition){
        this.mineralType = mineralType;
        this.parentRecognition = parentRecognition;
        Vector2d relativePose = findPose(parentRecognition);
        distance = relativePose.magnitude();
        angle = relativePose.angle();
    }

    public static Vector2d findPose(Recognition recognition){
        //Form vector representing the position of the object on the frame (relative to the top left)
        Vector2d onImage = new Vector2d(recognition.getLeft(), recognition.getTop());
        //Translate the origin to the center
        onImage.minus(new Vector2d(recognition.getImageWidth()/2, recognition.getImageHeight()/2));

        //Find the angle of the object relative to the y-axis (because that is the center of the screen vertically)
        double angle = new Vector2d(onImage).rotateBy(-90).angle();
        double magnitude = onImage.magnitude();

        com.acmerobotics.roadrunner.geometry.Vector2d relativePose = com.acmerobotics.roadrunner.geometry.Vector2d.polar(magnitude, angle);

        return new Vector2d(relativePose.getX(), relativePose.getY());
    }


    public MineralType getMineralType(){
        return mineralType;
    }

    public Recognition getParentRecognition(){
        return parentRecognition;
    }





}
