package com.example.testingmeepmeep; //TODO: Change to your equivalent package

/*
 * Use the RoadRunner classes for things like Pose2d and Vector2d,
 * instead of their MeepMeep equivalents
 */
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import com.noahbres.meepmeep.core.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.ColorScheme;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.MeepMeepRoadRunner;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import static java.lang.Math.toRadians;

public class MeepMeepTesting {


    private static DriveConstraints constraints = new DriveConstraints(
            40.0, 40.0, 0.0,
            Math.toRadians(270.0), Math.toRadians(270.0), 0.0
    );

    public static void main(String[] args) {
        MeepMeepRoadRunner meepMeepRoadRunner = new MeepMeepRoadRunner(800)
                .setBackground(MeepMeep.Background.FIELD_SKYSTONE_DARK)
                .setDriveTrainType(DriveTrainType.MECANUM)
                .setConstraints(constraints)
                .setTrackWidth(14.055)
                .setBotDimensions(17.5, 18)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-40, 62, -90))
                                .splineToLinearHeading(new Pose2d(-15, 34, -toRadians(90 + 10)), -toRadians(90 + 15))
                                .turn(Math.toRadians(-25))
                                .lineToLinearHeading(new Vector2d(-20, 28), -toRadians(140))
                                .forward(3)
                                .lineTo(new Vector2d(-28, 36))
                                .setReversed(true)
                                .turn(-toRadians(20))
                                .lineToLinearHeading(new Vector2d(18, 36), toRadians(0))
                                .setReversed(false)
                                .splineToLinearHeading(new Pose2d(36, 38, -toRadians(55)), -toRadians(55))
                                .lineToLinearHeading(new Vector2d(40, 33), -toRadians(90))
                                .back(5)
                                .lineToLinearHeading(new Vector2d(0, 36), 0)
                                .build()
                )
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
                .start();
    }
}
