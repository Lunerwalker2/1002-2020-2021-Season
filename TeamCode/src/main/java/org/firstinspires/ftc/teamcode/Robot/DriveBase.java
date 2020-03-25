package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Util.DriveBaseVectors;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.openftc.revextensions2.ExpansionHubMotor;

import static org.firstinspires.ftc.teamcode.Util.MathThings.m_v_mult;

public class DriveBase extends Component {






    private ExpansionHubMotor left_front_drive;
    private ExpansionHubMotor left_back_drive;
    private ExpansionHubMotor right_front_drive;
    private ExpansionHubMotor right_back_drive;


    private static double[][] matrix = {DriveBaseVectors.forward, DriveBaseVectors.strafeR, DriveBaseVectors.turnCW};


    public DriveBase(Robot robot, boolean useEncoders){
        super(robot);
        left_front_drive = hardwareMap.get(ExpansionHubMotor.class, HardwareNames.Drive.LEFT_FRONT);
        left_back_drive = hardwareMap.get(ExpansionHubMotor.class, HardwareNames.Drive.LEFT_BACK);
        right_front_drive = hardwareMap.get(ExpansionHubMotor.class, HardwareNames.Drive.RIGHT_FRONT);
        right_back_drive = hardwareMap.get(ExpansionHubMotor.class, HardwareNames.Drive.RIGHT_BACK);

        right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        left_front_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_back_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

        double x = Robot.movement_x;
        double y = Robot.movement_y;
        double turn = Robot.movement_turn;

        double[] inputs = {x, y, turn};

        double[] output = m_v_mult(matrix, inputs);

        setPower(output);
    }

    public void setPower(double[] powers){
        left_front_drive.setPower(powers[0]);
        left_back_drive.setPower(powers[1]);
        right_front_drive.setPower(powers[2]);
        right_back_drive.setPower(powers[3]);
    }

    public void stop(){
        setPower(new double[] {0,0,0,0});
    }
}
