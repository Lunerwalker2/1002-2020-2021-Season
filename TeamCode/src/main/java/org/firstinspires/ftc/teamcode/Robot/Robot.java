package org.firstinspires.ftc.teamcode.Robot;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.PPDev.PPOdo;
import org.firstinspires.ftc.teamcode.Util.ALLIANCE;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;

public class Robot {


    //Components for holonomic movement
    public static double movement_x = 0;
    public static double movement_y = 0;
    public static double movement_turn = 0;


    //The OpMode
    public OpMode opMode;

    //The robot components
    public DriveBase driveBase;
    public Odometry odometry;
    public BulkData bulkData;


    //The starting positions for the robot see the "skystoneField.png" picture for reference
    private static final Pose2d redStartingPosition = new Pose2d(0, 0, Rotation2d.fromDegrees(90));

    private static final Pose2d blueStartingPosition = new Pose2d(0, 0, Rotation2d.fromDegrees(-90));

    //To be used mostly in teleop
    public static Pose2d userStartingPosition = new Pose2d(0,0, new Rotation2d(0));

    public Robot(OpMode opMode, ALLIANCE alliance) {
        this.opMode = opMode;

        //Initialize components

        //Use encoders if in auto
        driveBase = new DriveBase(this, alliance != ALLIANCE.OTHER);

        bulkData = new BulkData(this);

        //Decide how to initialize odometry
        if (alliance == ALLIANCE.RED) {
            odometry = new Odometry(this, redStartingPosition);
        } else if(alliance == ALLIANCE.BLUE) {
            odometry = new Odometry(this, blueStartingPosition);
        } else {
            odometry = new Odometry(this,userStartingPosition);
        }
    }

    //Called to update all the robot's components
    public void update(){
        //This should be called before other hardware calls
        bulkData.update();

        driveBase.update();

        odometry.update();
    }

    //Called on stop() normally to stop all motion
    public void stop(){
        driveBase.stop();
    }




}
