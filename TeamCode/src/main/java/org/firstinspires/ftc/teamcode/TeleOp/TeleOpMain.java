package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.TeleOp.TeleOpSystems.*;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.firstinspires.ftc.teamcode.Util.PlaySound;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;
import java.util.List;

public class TeleOpMain extends OpMode {

    //Drive motors are going to stay here (this could be changed, but I want to keep track of them)

    private ExpansionHubMotor left_front_drive, left_back_drive, right_front_drive, right_back_drive;


    private final double[][] matrix = {{1, 1, 1, 1}, {1, -1, 1, -1}, {1, 1, -1, -1}};

    private double[] output;


    private ArrayList<TeleOpSystem> systems = new ArrayList<>();


    private List<LynxModule> hubs;


    //MAKE SURE THESE GO LAST!!


    private Drive drive = new Drive(this, () -> gamepad1.left_bumper, matrix, () -> 0.6);


    private boolean bounce1 = false;
    private boolean soundChanged = false;
    private PlaySound mego;


    @Override
    public void init() {

        new PlaySound(hardwareMap, R.raw.shutdown);
        mego = new PlaySound(hardwareMap, R.raw.megalovania);



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

        checkSound();

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

    private void controlSound(){
        if(!soundChanged){
            mego.pause();
            soundChanged = true;
        } else if(soundChanged){
            mego.play();
            soundChanged = false;
        }
    }

    private void checkSound(){
        if(gamepad1.x&&!bounce1){
            controlSound();
            bounce1=true;
        }
        else if(!gamepad1.x){
            bounce1=false;
        }
    }

    private ExpansionHubMotor findMotor(String id){
        return hardwareMap.get(ExpansionHubMotor.class, id);
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
