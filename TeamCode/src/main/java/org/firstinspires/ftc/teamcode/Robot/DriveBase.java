package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Util.DriveBaseVectors;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.firstinspires.ftc.teamcode.Util.MotorProfile;
import org.openftc.revextensions2.ExpansionHubMotor;

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
}
