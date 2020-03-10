package org.firstinspires.ftc.teamcode.PPDev;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.kinematics.ConstantVeloMecanumOdometry;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.function.DoubleSupplier;


/**
 * ALL UNITS ARE IN INCHES!!!!!
 */
public class PPOdo {


    public static Pose2d worldPosition;

    //By default it uses the absolute angle with the y encoders
    public static DoubleSupplier angleGetter;

    private ConstantVeloMecanumOdometry odometry;

    private ExpansionHubMotor left_y_encoder, right_y_encoder, x_encoder;

    public static final double TICKS_PER_REV = 1440;
    public static final double WHEEL_RADIUS = 1.41732283465; // in

    public static final double LATERAL_DISTANCE = 14.5138; // in; distance between the left and right wheels
    public static final double FORWARD_OFFSET = 3.55598425197; // in; offset of the lateral wheel

    public PPOdo(HardwareMap hardwareMap, Pose2d startingPosition){
        this(hardwareMap, startingPosition, 0);
    }

    public PPOdo(HardwareMap hardwareMap, Pose2d startingPosition, double gyroAngle){
        odometry = new ConstantVeloMecanumOdometry(new Rotation2d(gyroAngle), startingPosition, LATERAL_DISTANCE, FORWARD_OFFSET);
        
    }



    public static double encoderTicksToInches(int ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * ticks / TICKS_PER_REV;
    }


}
