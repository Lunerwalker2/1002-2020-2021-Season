package org.firstinspires.ftc.teamcode.Robot;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.kinematics.ConstantVeloMecanumOdometry;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.PPDev.PPOdo;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.openftc.revextensions2.ExpansionHubMotor;

public class Odometry extends Component {









    public Odometry(Robot robot){
        super(robot);

    }



    public enum HeadingMode {
        HEADING_FROM_IMU,
        HEADING_FROM_ENCODERS
    }


    public static double world_x_position;
    public static double world_y_position;
    public static double world_angle_rad;
    public static double world_angle_deg;

    private ConstantVeloMecanumOdometry odometry;

    private ExpansionHubMotor left_y_encoder, right_y_encoder, x_encoder;

    //Not really needed
    private BNO055IMU imu;

    private PPOdo.HeadingMode headingMode;

    private static final double TICKS_PER_REV = 1440;
    private static final double WHEEL_RADIUS = 1.41732283465; // in

    private static final double LATERAL_DISTANCE = 14.5138; // in; distance between the left and right wheels
    private static final double FORWARD_OFFSET = 3.55598425197; // in; offset of the lateral wheel

    /**
     * USE RADIANS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public Odometry(Robot robot, Pose2d startingPosition, PPOdo.HeadingMode headingMode){
        super(robot);
        this.headingMode = headingMode;
        setUp(hardwareMap);
        odometry = new ConstantVeloMecanumOdometry(startingPosition.getRotation(), startingPosition, LATERAL_DISTANCE, FORWARD_OFFSET);
        //Update all the globals
        world_x_position = startingPosition.getTranslation().getX();
        world_y_position = startingPosition.getTranslation().getY();
        world_angle_rad = startingPosition.getHeading();
        world_angle_deg = startingPosition.getRotation().getDegrees();
    }

    private void setUp(HardwareMap hardwareMap){
        left_y_encoder = hardwareMap.get(ExpansionHubMotor.class, HardwareNames.Odometry.LEFT_Y_ENCODER);
        right_y_encoder = hardwareMap.get(ExpansionHubMotor.class, HardwareNames.Odometry.RIGHT_Y_ENCODER);
        x_encoder = hardwareMap.get(ExpansionHubMotor.class, HardwareNames.Odometry.X_ENCODER);
        imu = hardwareMap.get(BNO055IMU.class, HardwareNames.Sensors.RIGHT_HUB_IMU);
        //Reset everything
        left_y_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_y_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        x_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_y_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_y_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        x_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //Just in case we need the imu
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
    }

    public void update(){
        Pose2d newPosition = odometry.update(
                //Absolute heading = rightY minus leftY, divided by the trackwidth
                getGyroHeading(),
                getLeftYInches(),
                getRightYInches(),
                getXInches()
        );
        //Update all the globals
        world_x_position = newPosition.getTranslation().getX();
        world_y_position = newPosition.getTranslation().getY();
        world_angle_rad = newPosition.getHeading();
        world_angle_deg = newPosition.getRotation().getDegrees();
    }

    private int getLeftYPos(){
        return left_y_encoder.getCurrentPosition();
    }

    private double getLeftYInches(){
        return encoderTicksToInches(getLeftYPos());
    }

    private double getRightYPos(){
        return right_y_encoder.getCurrentPosition();
    }

    private double getRightYInches(){
        return encoderTicksToInches(getRightYPos());
    }

    private double getXPos(){
        return x_encoder.getCurrentPosition();
    }

    private double getXInches(){
        return encoderTicksToInches(getXPos());
    }

    public Rotation2d getGyroHeading(){
        switch (headingMode){
            case HEADING_FROM_IMU: return new Rotation2d(imu.getAngularOrientation().firstAngle);
            case HEADING_FROM_ENCODERS: return Rotation2d.fromDegrees((getRightYInches() - getLeftYInches())/LATERAL_DISTANCE);
            default: return null;
        }
    }

    private static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * ticks / TICKS_PER_REV;
    }
}
