package org.firstinspires.ftc.teamcode.PPDev.PPAuto;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import org.firstinspires.ftc.teamcode.PPDev.CurvePoint;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.firstinspires.ftc.teamcode.PPDev.PPMovement.followCurve;

enum Paths {
    GO_TO_LEFT_STONE(MultimapBuilder.hashKeys().arrayListValues().build()),
    GO_TO_CENTER_STONE(MultimapBuilder.hashKeys().arrayListValues().build()),
    GO_TO_RIGHT_STONE(MultimapBuilder.hashKeys().arrayListValues().build());

    ListMultimap<CurvePoint, PositionalMarker> path;

    Paths(ListMultimap<CurvePoint, PositionalMarker> path){
        this.path = path;
    }
}
public class PPMain extends PurePursuitBase<Paths> {



    //The starting position of the robot (use inches and the origin at the red loading zone and measured in inches. heading in radians
    private Pose2d starting_position = new Pose2d(0, 0, new Rotation2d(0.5));


    @Override
    public void runOpMode(){


        /*
         * Todo
         * Find units for distance (odo/coords) and heading (deg/rad)
         *
         * heading = rad
         * distance = whatever we want, but please use inches
         *
         *
         */

        setStartingPosition(starting_position);
        initDriveMechanisms();


        EnumMap<Paths, ListMultimap<CurvePoint, PositionalMarker>> paths = new EnumMap<>(Paths.class);

        Paths.GO_TO_LEFT_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), addNothing());
        Paths.GO_TO_LEFT_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), addNothing());
        Paths.GO_TO_LEFT_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), () -> {
            //Grab stone
        });

        Paths.GO_TO_RIGHT_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), addNothing());
        Paths.GO_TO_RIGHT_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), addNothing());
        Paths.GO_TO_RIGHT_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), () -> {
            //Grab stone
        });

        Paths.GO_TO_CENTER_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), addNothing());
        Paths.GO_TO_CENTER_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), addNothing());
        Paths.GO_TO_CENTER_STONE.path.put(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0), () -> {
            //Grab stone
        });

        paths.put(Paths.GO_TO_LEFT_STONE, Paths.GO_TO_LEFT_STONE.path);
        paths.put(Paths.GO_TO_CENTER_STONE, Paths.GO_TO_CENTER_STONE.path);
        paths.put(Paths.GO_TO_RIGHT_STONE, Paths.GO_TO_RIGHT_STONE.path);



        ArrayList<CurvePoint> allPoints = new ArrayList<>();
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));

        waitForStart();

        while(opModeIsActive()) {

        }

        drive.stop();
    }
}
