package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.TeleOp.TeleOpSystems.*;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;

public class TeleOpMain extends OpMode {

    //Drive motors are going to stay here (this could be changed, but I want to keep track of them)

    private ExpansionHubMotor left_front_drive, left_back_drive, right_front_drive, right_back_drive;


    private final double[][] matrix = {{1, 1, 1, 1}, {1, -1, 1, -1}, {1, 1, -1, -1}};

    private double[] output;


    private ArrayList<TeleOpSystem> systems = new ArrayList<>();


    //MAKE SURE THESE GO LAST!!


    private Drive drive = new Drive(this, () -> gamepad1.left_bumper, matrix, () -> 0.6);
    @Override
    public void init(){


        //Add all systems to the list for easy access
        systems.add(drive);

        //Init Hardware

        setBulkModeAuto();

        for(TeleOpSystem system : systems){
            system.initHardware();
        }

        //Drive Motors stay here
        left_front_drive = findMotor(HardwareNames.Drive.LEFT_FRONT);
        left_back_drive = findMotor(HardwareNames.Drive.LEFT_BACK);
        right_front_drive = findMotor(HardwareNames.Drive.RIGHT_FRONT);
        right_back_drive = findMotor(HardwareNames.Drive.RIGHT_BACK);



    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){


        //Update all the systems
        for(TeleOpSystem system : systems){
            system.update();
        }

        output = drive.drive(new double[] {gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x});
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

    private void stopDrive(){
        left_front_drive.setPower(0);
        left_back_drive.setPower(0);
        right_front_drive.setPower(0);
        right_back_drive.setPower(0);
    }

    private void setBulkModeAuto(){
        for(LynxModule module : hardwareMap.getAll(LynxModule.class)){
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }
}
