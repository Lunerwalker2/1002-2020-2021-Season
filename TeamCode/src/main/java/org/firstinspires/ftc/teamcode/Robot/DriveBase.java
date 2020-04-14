package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.Util.Math.DriveBaseVectors;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.firstinspires.ftc.teamcode.Util.Motors.MotorProfile;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.teamcode.Util.Math.MathThings.m_v_mult;

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


    public DriveBase(Robot robot, boolean useEncoders){
        super(robot);

        motors = (ArrayList<ExpansionHubMotor>) Arrays.asList(left_front_drive, left_back_drive, right_front_drive, right_back_drive);

        left_front_drive = findMotor(HardwareNames.Drive.LEFT_FRONT);
        left_back_drive = findMotor(HardwareNames.Drive.LEFT_BACK);
        right_front_drive = findMotor(HardwareNames.Drive.RIGHT_FRONT);
        right_back_drive = findMotor(HardwareNames.Drive.RIGHT_BACK);

        motors.forEach((motor) -> motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));
        right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back_drive.setDirection(DcMotorSimple.Direction.REVERSE);



        if(useEncoders){
            motors.forEach((motor) -> motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER));
        } else {
            motors.forEach((motor) -> motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER));
        }
    }

    private ExpansionHubMotor findMotor(String name){
        return hardwareMap.get(ExpansionHubMotor.class, name);
    }

    /**
     * Update the drive base using the
     * {@link DriveFields#movement_x}, {@link DriveFields#movement_y}, and
     * {@link DriveFields#movement_turn}. This uses those components and turns them into
     * drive powers with the order of {@code {lf, lb, rf, rb}}. Then it updates the drive motors.
     */
    public void updateHolonomic(){

        double x = DriveFields.movement_x;
        double y = DriveFields.movement_y;
        double turn = DriveFields.movement_turn;

        double[] inputs = {x, y, turn};

        double[] output = m_v_mult(matrix, inputs);
        DriveFields.distributePowers(output);

        setPower();
    }

    /**
     * Simply updates motor powers with stored powers
     */
    public void updatePowers(){
        setPower();
    }

    /**
     * Returns the positions of each motor
     * @return An array with the motor positions
     */
    public double[] getPositions(){
        return new double[] {
                left_front_drive.getCurrentPosition(),
                left_back_drive.getCurrentPosition(),
                right_front_drive.getCurrentPosition(),
                right_back_drive.getCurrentPosition()
        };
    }

    /**
     * Set PIDF coefficients to all drive motors
     * @param coefficients The PIDF coefficients
     * @param runMode The mode to set them as (usually RUN_USING_ENCODER)
     */
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
}
