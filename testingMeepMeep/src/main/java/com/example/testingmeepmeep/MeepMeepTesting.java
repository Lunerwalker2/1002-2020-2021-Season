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

import java.awt.image.ImageProducer;

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
                .followTrajectorySequence(drive ->
                                drive.trajectorySequenceBuilder(new Pose2d(-40, -62, Math.toRadians(90)))
                                        .splineTo(new Pose2d(-10, -36, Math.toRadians(170)))
                                        .splineTo(new Pose2d(-30, -20, Math.toRadians(180)))
                                        .addDisplacementMarker(0.5, 0, () -> {System.out.println("Reached First Marker!");})
                                        .lineTo(new Vector2d(0, -50))
                                        .turn(Math.toRadians(20))
                                        .setReversed(true)
                                        .lineToLinearHeading(new Vector2d(50, -36), Math.toRadians(90))
                                        .addDisplacementMarker(0.25, 0, () -> {System.out.println("Reached Next Marker");})
                                        .setReversed(false)
                                        .lineTo(new Vector2d(50, -54))
                                        .lineToLinearHeading(new Vector2d(36, -54), 0)
                                        .forward(10)
                                        .back(15)
                                        .lineTo(new Vector2d(0, -36))
                                        .build()
                )
                .start();
    }
}
