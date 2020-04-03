package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Robot.DriveFields;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;
import org.firstinspires.ftc.teamcode.Util.PlaySound;

@TeleOp(name="Main TeleOp")
public class TeleOpMain extends OpMode {



    private boolean bounce1 = false;
    private boolean soundChanged = false;
    private PlaySound mego;


    private Robot robot;


    @Override
    public void init() {

        robot = new Robot(this, Alliance.OTHER);

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

        DriveFields.movement_x = gamepad1.left_stick_x;
        DriveFields.movement_y = -gamepad1.left_stick_y;
        DriveFields.movement_turn = gamepad1.right_stick_x;
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
