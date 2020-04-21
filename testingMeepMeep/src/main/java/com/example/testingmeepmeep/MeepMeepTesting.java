package com.example.testingmeepmeep; //TODO: Change to your equivalent package

/*
 * Use the RoadRunner classes for things like Pose2d and Vector2d,
 * instead of their MeepMeep equivalents
 */
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.heading.LinearInterpolator;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import com.noahbres.meepmeep.core.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.ColorScheme;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.MeepMeepRoadRunner;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import static java.lang.Math.toRadians;

public class MeepMeepTesting {

    enum Alliance {
        RED,
        BLUE
    }

    private static Alliance alliance = Alliance.BLUE;

    private static DriveConstraints constraints = new DriveConstraints(
            40.0, 40.0, 0.0,
            Math.toRadians(270.0), Math.toRadians(270.0), 0.0
    );

    static Pose2d changeSide(Pose2d pose) {
        if(alliance == Alliance.BLUE) {
            return new Pose2d(pose.getX(), pose.getY() * -1, pose.getHeading() * -1);
        } else{
            return new Pose2d(pose.getX(), pose.getY(), pose.getHeading());
        }
    }

    static Vector2d changeSide(Vector2d vec) {
       if(alliance == Alliance.BLUE) {
            return new Vector2d(vec.getX(), vec.getY() * -1);
        } else{
            return new Vector2d(vec.getX(), vec.getY());
        }
    }

    static double changeSide(double angle){
        if (alliance == Alliance.BLUE){
            return angle * -1;
        } else{
            return angle;
        }
    }

    public static void main(String[] args) {
        alliance = Alliance.RED;
        MeepMeepRoadRunner meepMeepRoadRunner = new MeepMeepRoadRunner(800)
                .setBackground(MeepMeep.Background.FIELD_SKYSTONE_DARK)
                .setDriveTrainType(DriveTrainType.MECANUM)
                .setConstraints(constraints)
                .setTrackWidth(14.055)
                .setBotDimensions(17.5, 18)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(changeSide(new Pose2d(-40, -60, toRadians(90))))
                                .splineToLinearHeading(changeSide(new Pose2d(-36, -35, toRadians(90))), changeSide(toRadians(90))) //change to stone (x + 8)
                                .forward(2)
                                .addDisplacementMarker(() -> {})
//                                .lineTo(changeSide(new Vector2d(-28, -38)))
//                                .turn(changeSide(toRadians(20)))
                                .setReversed(true)
                                .splineTo(changeSide(new Pose2d(-36, -38, toRadians(-90)))) //change to stone (x + 8)
                                .splineToLinearHeading(changeSide(new Pose2d(-30, -42, toRadians(-20))), changeSide(toRadians(-20))) //change to stone (x + 8)
                                .setReversed(false)
                                .lineToLinearHeading(changeSide(new Vector2d(16, -38)), changeSide(toRadians(70)))
                                .addSpatialMarker(changeSide(new Vector2d(18, -40)), () -> {})
//                                .splineToLinearHeading(changeSide(new Pose2d(36, -38, toRadians(55))), changeSide(toRadians(55)))
                                .splineToLinearHeading(changeSide(new Pose2d(40, -33, toRadians(90))), changeSide(toRadians(90)))
                                .addDisplacementMarker(0.75, 1, () -> {})
                                .addDisplacementMarker(() -> {})
//                                .lineToLinearHeading(changeSide(new Vector2d(40, -33)), changeSide(toRadians(90)))
                                .back(5)
                                .lineToLinearHeading(changeSide(new Vector2d(0, -41)), changeSide(toRadians(90 + 50)))
//                                .splineToLinearHeading(changeSide(new Pose2d(-54, -40, toRadians(90 + 10))), changeSide(toRadians(90 + 10)))
                                .splineToLinearHeading(changeSide(new Pose2d(-60, -34, toRadians(90))), changeSide(toRadians(90))) //1
//                                .forward(1)//1
                                .splineToConstantHeading(changeSide(new Pose2d(-50, -38, toRadians(90))))
//                                .back(7)//1
                                .lineToLinearHeading(changeSide(new Vector2d(16, -38)), changeSide(toRadians(55)))
                                .splineToLinearHeading(changeSide(new Pose2d(48, -36, toRadians(90))), changeSide(toRadians(90)))
//                                .lineToLinearHeading(changeSide(new Vector2d(48, -36)), changeSide(toRadians(90)))
                                .forward(4)
                                .back(5)
                                .lineToSplineHeading(changeSide(new Vector2d(0, -40)), changeSide(toRadians(0)))
                                .build()
                ).start();
//                .followTrajectorySequence(drive ->
//                                drive.trajectorySequenceBuilder(new Pose2d(-40, -62, 90))
//                                        .splineToLinearHeading(new Pose2d(-15, -34, toRadians(90 + 10)), toRadians(90 + 15))
//                                        .turn(Math.toRadians(25))
//                                        .lineToLinearHeading(new Vector2d(-20, -28), toRadians(140))
//                                        .forward(3)
//                                        .lineTo(new Vector2d(-28, -36))
//                                        .setReversed(true)
//                                        .turn(toRadians(20))
//                                        .lineToLinearHeading(new Vector2d(18, -36), toRadians(0))
//                                        .setReversed(false)
//                                        .splineToLinearHeading(new Pose2d(36, -38, toRadians(55)), toRadians(55))
//                                        .lineToLinearHeading(new Vector2d(40, -33), toRadians(90))
//                                        .back(5)
//                                        .lineToLinearHeading(new Vector2d(0, -36), 0)
//                                        .build()
//                )
    }
}
