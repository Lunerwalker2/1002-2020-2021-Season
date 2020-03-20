package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Util.HardwareNames;

public class Robot {


    //Components for holonomic movement
    public static double movement_x = 0;
    public static double movement_y = 0;
    public static double movement_turn = 0;


    public OpMode opMode;

    public LynxModule module1;
    public LynxModule module2;

    public DriveBase driveBase;
    public Odometry odometry;



    public Robot(OpMode opMode){
        this.opMode = opMode;

        module1 = opMode.hardwareMap.get(LynxModule.class, HardwareNames.Hubs.HUB_1);
        module2 = opMode.hardwareMap.get(LynxModule.class, HardwareNames.Hubs.HUB_10);

        module1.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        module2.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);

        driveBase = new DriveBase(this, true);
//        odometry = new Odometry();
    }

    public void update(){

        driveBase.update();

    }




}
