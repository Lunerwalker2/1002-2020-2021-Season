package org.firstinspires.ftc.teamcode.Auto.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Util.DriveBaseVectors;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.firstinspires.ftc.teamcode.Util.MathThings;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Drive extends Subsystem {


    //Components for PP
    public static double movement_x;
    public static double movement_y;
    public static double movement_turn;



    private ExpansionHubMotor left_front_drive,left_back_drive, right_front_drive, right_back_drive;

    private List<ExpansionHubMotor> motors = Arrays.asList(left_front_drive, left_back_drive, right_front_drive, right_back_drive);


    private final double[][] matrix = {DriveBaseVectors.forward, DriveBaseVectors.strafeR, DriveBaseVectors.turnCW};



    public Drive(LinearOpMode opMode){
        super(opMode);
    }

    public Drive(HardwareMap hardwareMap){
        super(hardwareMap);
    }


    @Override
    public void initHardware(){
        left_front_drive = findMotor(HardwareNames.Drive.LEFT_FRONT);
        left_back_drive = findMotor(HardwareNames.Drive.LEFT_BACK);
        right_front_drive = findMotor(HardwareNames.Drive.RIGHT_FRONT);
        right_back_drive = findMotor(HardwareNames.Drive.RIGHT_BACK);
    }


    //Updates using the movement components
    public void update(){
        double[] output = MathThings.m_v_mult(matrix, new double[] {movement_x, movement_y, movement_turn});
        setPower(output);
    }



    private ExpansionHubMotor findMotor(String id){
        return hardwareMap.get(ExpansionHubMotor.class, id);
    }

    public void reverseRightSide(){
        right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back_drive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setZeroPowerBehavior(ExpansionHubMotor.ZeroPowerBehavior behavior){
        left_front_drive.setZeroPowerBehavior(behavior);
        left_back_drive.setZeroPowerBehavior(behavior);
        right_front_drive.setZeroPowerBehavior(behavior);
        right_back_drive.setZeroPowerBehavior(behavior);
    }

    public void setMode(ExpansionHubMotor.RunMode mode){
        left_front_drive.setMode(mode);
        left_back_drive.setMode(mode);
        right_front_drive.setMode(mode);
        right_back_drive.setMode(mode);
    }

    public ArrayList<ExpansionHubMotor> getMotors(){
        return new ArrayList<>(motors);
    }

    public double[] getPositions(){
        return new double[] {
                left_front_drive.getCurrentPosition(),
                left_back_drive.getCurrentPosition(),
                right_front_drive.getCurrentPosition(),
                right_back_drive.getCurrentPosition()
        };
    }

    public PIDFCoefficients getPIDCoefficients(ExpansionHubMotor.RunMode runMode){
        return left_front_drive.getPIDFCoefficients(runMode);
    }


    public double[] getVelocities(){
        return new double[] {
                left_front_drive.getVelocity(),
                left_back_drive.getVelocity(),
                right_front_drive.getVelocity(),
                right_back_drive.getVelocity()
        };
    }

    public void stopDrive(){
        setPower(new double[] {0,0,0,0});
    }

    public void setPower(double[] powers){
        left_front_drive.setPower(powers[0]);
        left_back_drive.setPower(powers[1]);
        right_front_drive.setPower(powers[2]);
        right_back_drive.setPower(powers[3]);
    }



    @Override
    public void stop(){
        stopDrive();
    }
}
