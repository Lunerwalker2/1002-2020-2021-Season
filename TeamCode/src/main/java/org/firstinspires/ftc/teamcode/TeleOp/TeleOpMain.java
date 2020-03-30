package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Auto.Placeholder;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.TeleOp.TeleOpSystems.*;
import org.firstinspires.ftc.teamcode.Util.ALLIANCE;
import org.firstinspires.ftc.teamcode.Util.DriveBaseVectors;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.firstinspires.ftc.teamcode.Util.PlaySound;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Main TeleOp")
public class TeleOpMain extends OpMode {



    private boolean bounce1 = false;
    private boolean soundChanged = false;
    private PlaySound mego;


    private Robot robot;


    @Override
    public void init() {

        robot = new Robot(this, ALLIANCE.OTHER, false);

        new PlaySound(hardwareMap, R.raw.shutdown);
        mego = new PlaySound(hardwareMap, R.raw.megalovania);


    }

    @Override
    public void init_loop(){
        //The big test
    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

        checkSound();

        robot.update();
    }

    @Override
    public void stop(){

        robot.stop();
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
}
