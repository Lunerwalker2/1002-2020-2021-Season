package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Robot.DriveFields;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;
import org.firstinspires.ftc.teamcode.Util.Debouncer;
import org.firstinspires.ftc.teamcode.Util.PlaySound;

import kotlin.Unit;

@TeleOp(name="Main", group = "TeleOp")
public class TeleOpMain extends OpMode {


    private Debouncer soundControl;

    private PlaySound mego;

    private Robot robot;

    @Override
    public void init() {

        robot = new Robot(this, Alliance.OTHER);

        mego = new PlaySound(hardwareMap, R.raw.megalovania);

        soundControl = new Debouncer(() -> gamepad1.x);
        soundControl.setOneAction(() -> {
            mego.play();
            return Unit.INSTANCE;
        });
        soundControl.setTwoAction(() -> {
            mego.pause();
            return Unit.INSTANCE;
        });

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

        soundControl.check();

        DriveFields.movement_x = gamepad1.left_stick_x;
        DriveFields.movement_y = -gamepad1.left_stick_y;
        DriveFields.movement_turn = gamepad1.right_stick_x;

        robot.update();
    }

    @Override
    public void stop(){

        robot.stop();
    }
}
