package org.firstinspires.ftc.teamcode.PPDev.PPAuto;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.google.common.collect.ListMultimap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.PPDev.CurvePoint;
import org.firstinspires.ftc.teamcode.PPDev.PPMovement;
import org.firstinspires.ftc.teamcode.PPDev.PPOdo;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;

import static org.firstinspires.ftc.teamcode.PPDev.PPMovement.followCurve;
import static org.firstinspires.ftc.teamcode.PPDev.PPMovement.withinRangeOfEnd;
import static org.firstinspires.ftc.teamcode.PPDev.PPOdo.world_x_position;
import static org.firstinspires.ftc.teamcode.PPDev.PPOdo.world_y_position;

import java.util.EnumMap;


public abstract class PurePursuitBase<K extends Enum<K>> extends LinearOpMode {

    //The minimum distance to be to trigger a marker associated with a CurvePoint (inches)
    private static double markerDistance = 5;

    //Boolean to stop the current path in it's tracks (only use sparingly)
    private static boolean stopCurrentpath = false;

    //The odometry object
    public PPOdo odometry;


    //The drive class
    public Robot robot;

    //The starting position of the robot (use inches and the origin at the red loading zone and measured in inches. heading in radians
    private Pose2d starting_position = new Pose2d(0, 0, new Rotation2d(0.5));


    //The MultiMap that holds the paths ( CurvePoints and the "markers" (experimental))
    public void setPaths(EnumMap<K, ListMultimap<CurvePoint, PositionalMarker>> paths){
        this.paths = paths;
    }

    private EnumMap<K, ListMultimap<CurvePoint, PositionalMarker>> paths;
    private ListMultimap<CurvePoint, PositionalMarker> currentPath;

    public void setStartingPosition(Pose2d starting_position){
        this.starting_position = starting_position;
    }

    public void initDriveMechanisms(){
        odometry = new PPOdo(hardwareMap, starting_position, PPOdo.HeadingMode.HEADING_FROM_ENCODERS);
        robot = new Robot(this, Alliance.RED);

    }

    @SuppressWarnings("ConstantConditions")
    public void loadAndStart(K path){
        currentPath = paths.get(path);
        PPMovement.followCurve(currentPath, Math.toRadians(90));
    }

    //Due to the rules of the Google Guava library, any MultiMap must have at least one value for every key
    //Obviously, that doesn't help if there are no markers for the point, so this is a convenient way to get around that
    public static PositionalMarker addNothing(){
        return () -> {};
    }

    public void update(){
        odometry.update();
        //Check all the points in the path and see if we are close enough to trigger any markers
        currentPath.keySet().forEach((key) -> {
            if(shouldTriggerMarkers(key)){
                currentPath.get(key).forEach(PositionalMarker::act);
            }
        });
        followCurve(currentPath, Math.toRadians(90));
        if(!currentPathDone() && !stopCurrentpath){
            robot.update();
        } else {
            robot.stop();
        }
    }

    public void stopCurrentPath(){
        stopCurrentpath = true;
    }

    public void allowCurrentPath(){
        stopCurrentpath = false;
    }

    public boolean currentPathDone(){
        return withinRangeOfEnd;
    }

    public static boolean shouldTriggerMarkers(CurvePoint markerPoint){
        return (Math.hypot(markerPoint.x - world_x_position, markerPoint.y - world_y_position) <= markerDistance);
    }




}
