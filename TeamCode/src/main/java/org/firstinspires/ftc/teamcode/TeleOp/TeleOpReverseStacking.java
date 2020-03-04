package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.RRDev.Quickstart.util.AssetsTrajectoryManager;
import org.firstinspires.ftc.teamcode.TeleOp.TeleOpSystems.*;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;
import java.util.List;

public class TeleOpReverseStacking extends OpMode {

    //Drive motors are going to stay here (this could be changed, but I want to keep track of them)

    private ExpansionHubMotor left_front_drive, left_back_drive, right_front_drive, right_back_drive;
    
    private ExpansionHubMotor left_intake, right_intake;
    
    private CRServo left_outake, right_outake;
    
    private Servo left_claw, right_claw;

    private final double[][] matrix = {{1, 1, 1, 1}, {1, -1, 1, -1}, {1, 1, -1, -1}};

    private double[] output;


    private ArrayList<TeleOpSystem> systems = new ArrayList<>();


    private List<LynxModule> hubs;


    //MAKE SURE THESE GO LAST!!


    private Drive drive = new Drive(this, () -> gamepad1.left_bumper, matrix, () -> 0.6);


    @Override
    public void init(){



        //Add all systems to the list for easy access
        systems.add(drive);

        //Init Hardware

        setBulkModeManual();

        for(TeleOpSystem system : systems){
            system.initHardware();
        }

        //Drive Motors stay here
        left_front_drive = findMotor(HardwareNames.Drive.LEFT_FRONT);
        left_back_drive = findMotor(HardwareNames.Drive.LEFT_BACK);
        right_front_drive = findMotor(HardwareNames.Drive.RIGHT_FRONT);
        right_back_drive = findMotor(HardwareNames.Drive.RIGHT_BACK);

        //Reverse right side
        right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        //Set to encoder movement
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Set to BRAKE
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        
        left_intake = findMotor("left_intake");
        right_intake = findMotor("right_intake");
        left_outake = findCRServo("left_outake");
        right_outake = findMotor("right_outake");
        left_claw = findMotor("left_claw");
        right_claw = findMotor("right_claw");
        
        left_intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right_intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        stopDrive();



    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){
        for(TeleOpSystem system : systems){
            system.onStart();
        }
    }

    @Override
    public void loop(){

        clearBulkCache();


        //Update all the systems
        for(TeleOpSystem system : systems){
            system.update();
        }

        output = drive.drive(new double[] {gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x});

        left_front_drive.setPower(output[0]);
        left_back_drive.setPower(output[1]);
        right_front_drive.setPower(output[2]);
        right_back_drive.setPower(output[3]);

    }

    @Override
    public void stop(){
        //Stop all systems
        for(TeleOpSystem system : systems){
            system.stop();
        }
        //Stop drive base
        stopDrive();
    }

    private ExpansionHubMotor findMotor(String id){
        return hardwareMap.get(ExpansionHubMotor.class, id);
    }
    
    private CRServo findCRServo(String id){
      return hardwareMap.get(CRServo.class, id);
    }
    
    private Servo findServo(String id){
      return hardwareMap.get(CRServo.class, id);
    }

    private void setMode(DcMotor.RunMode runMode){
        left_front_drive.setMode(runMode);
        left_back_drive.setMode(runMode);
        right_front_drive.setMode(runMode);
        right_back_drive.setMode(runMode);
    }

    private void setZeroPowerBehavior(ExpansionHubMotor.ZeroPowerBehavior behavior){
        left_front_drive.setZeroPowerBehavior(behavior);
        left_back_drive.setZeroPowerBehavior(behavior);
        right_front_drive.setZeroPowerBehavior(behavior);
        right_back_drive.setZeroPowerBehavior(behavior);
    }

    private void stopDrive(){
        left_front_drive.setPower(0);
        left_back_drive.setPower(0);
        right_front_drive.setPower(0);
        right_back_drive.setPower(0);
    }

    private void setBulkModeManual(){
        for(LynxModule module : hardwareMap.getAll(LynxModule.class)){
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    private void clearBulkCache(){
        for(LynxModule module : hardwareMap.getAll(LynxModule.class)){
            module.clearBulkCache();
        }
    }
}
