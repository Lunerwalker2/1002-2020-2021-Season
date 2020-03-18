package org.firstinspires.ftc.teamcode.PPDev.PPAuto;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.PPDev.CurvePoint;
import org.firstinspires.ftc.teamcode.PPDev.PPOdo;

import java.util.EnumMap;


public abstract class PurePursuitBase extends LinearOpMode {



    //The odometry object
    PPOdo odometry;


    //The drive class
    Drive drive;

    //The starting position of the robot (use inches and the origin at the red loading zone and measured in inches. heading in radians
    private Pose2d starting_position = new Pose2d(0, 0, new Rotation2d(0.5));


    //The MultiMap that holds the paths ( CurvePoints and the "markers" (experimental))
    public void setPaths(EnumMap<?, ArrayListMultimap<CurvePoint, PositionalMarker>> paths){
        this.paths = paths;
    }

    private EnumMap<?, ArrayListMultimap<CurvePoint, PositionalMarker>> paths;

    public void setStartingPosition(Pose2d starting_position){
        this.starting_position = starting_position;
    }

    public void initDriveMechanisms(){
        odometry = new PPOdo(hardwareMap, starting_position, PPOdo.HeadingMode.HEADING_FROM_ENCODERS);
        drive = new Drive(this);

        drive.initHardware();
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.reverseRightSide();
        drive.stop();

    }




}
