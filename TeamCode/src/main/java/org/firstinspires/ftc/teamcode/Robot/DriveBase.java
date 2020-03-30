package org.firstinspires.ftc.teamcode.Robot;

import com.google.common.collect.Lists;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.Util.DriveBaseVectors;
import org.firstinspires.ftc.teamcode.Util.DriveMotor;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.firstinspires.ftc.teamcode.Util.MotorProfile;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.teamcode.Util.MathThings.m_v_mult;

public class DriveBase extends Component {


    //These are public because reflection is weird. Just leave them
    @MotorProfile(hardwareName = HardwareNames.Drive.LEFT_FRONT, defaultZeroPowerBehavior =  DcMotor.ZeroPowerBehavior.BRAKE, defaultDirection = DcMotorSimple.Direction.FORWARD)
     public ExpansionHubMotor left_front_drive;
    @MotorProfile(hardwareName = HardwareNames.Drive.LEFT_BACK, defaultZeroPowerBehavior =  DcMotor.ZeroPowerBehavior.BRAKE, defaultDirection = DcMotorSimple.Direction.FORWARD)
     public ExpansionHubMotor left_back_drive;
    @MotorProfile(hardwareName = HardwareNames.Drive.RIGHT_FRONT, defaultZeroPowerBehavior =  DcMotor.ZeroPowerBehavior.BRAKE, defaultDirection = DcMotorSimple.Direction.REVERSE)
     public ExpansionHubMotor right_front_drive;
    @MotorProfile(hardwareName = HardwareNames.Drive.RIGHT_BACK, defaultZeroPowerBehavior =  DcMotor.ZeroPowerBehavior.BRAKE, defaultDirection = DcMotorSimple.Direction.REVERSE)
     public ExpansionHubMotor right_back_drive;

    private ArrayList<ExpansionHubMotor> motors;


    private static double[][] matrix = {DriveBaseVectors.forward, DriveBaseVectors.strafeR, DriveBaseVectors.turnCW};

    private void applyProfile(ExpansionHubMotor motor){
        try {
            MotorProfile profile = getClass().getField(motor.toString()).getAnnotation(MotorProfile.class);
            motor = hardwareMap.get(ExpansionHubMotor.class, profile.hardwareName());
            motor.setZeroPowerBehavior(profile.defaultZeroPowerBehavior());
            motor.setDirection(profile.defaultDirection());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public DriveBase(Robot robot, boolean useEncoders){
        super(robot);

        motors = (ArrayList<ExpansionHubMotor>) Arrays.asList(left_front_drive, left_back_drive, right_front_drive, right_back_drive);

        applyProfile(left_front_drive);
        applyProfile(left_back_drive);
        applyProfile(right_front_drive);
        applyProfile(right_back_drive);


        if(useEncoders){
            left_front_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_back_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_front_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_back_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            left_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            left_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void update(){

        double x = DriveFields.movement_x;
        double y = DriveFields.movement_y;
        double turn = DriveFields.movement_turn;

        double[] inputs = {x, y, turn};

        double[] output = m_v_mult(matrix, inputs);
        DriveFields.distributePowers(output);

        setPower();
    }

    public double[] getPositions(){
        return new double[] {
                left_front_drive.getCurrentPosition(),
                left_back_drive.getCurrentPosition(),
                right_front_drive.getCurrentPosition(),
                right_back_drive.getCurrentPosition()
        };
    }

    public void setPIDCoefficients(PIDFCoefficients coefficients, DcMotor.RunMode runMode){
        motors.forEach((motor) -> motor.setPIDFCoefficients(runMode, coefficients));
    }

    public PIDFCoefficients getPIDCoefficients(DcMotor.RunMode runMode){
        return left_front_drive.getPIDFCoefficients(runMode);//Random motor :)
    }

    public void setMaxRPMFraction(double fraction){
        motors.forEach((motor) -> {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(fraction);
            motor.setMotorType(motorConfigurationType);
        });
    }


    public void setPower(){
        left_front_drive.setPower(DriveFields.lf_power);
        left_back_drive.setPower(DriveFields.lb_power);
        right_front_drive.setPower(DriveFields.rf_power);
        right_back_drive.setPower(DriveFields.rb_power);
    }

    public void stop(){
        DriveFields.distributePowers(new double[] {0,0,0,0});
        setPower();
    }

    //RR uses a different order than us, so the following methods are specific to that


    public List<Integer> getWheelEncoderPositionsRR(){
        return Arrays.asList(
                left_front_drive.getCurrentPosition(),
                left_back_drive.getCurrentPosition(),
                right_back_drive.getCurrentPosition(),
                right_front_drive.getCurrentPosition()

        );
    }

    public List<Double> getWheelEncoderVelocitiesRR(){
        return Arrays.asList(
                left_front_drive.getVelocity(),
                left_back_drive.getVelocity(),
                right_front_drive.getVelocity(),
                right_back_drive.getVelocity()
        );
    }

    public void setPowerRR(double[] powers){
        DriveFields.lf_power = powers[0];
        DriveFields.lb_power = powers[1];
        DriveFields.rb_power = powers[2];
        DriveFields.rf_power = powers[3];
        setPower();
    }
}
