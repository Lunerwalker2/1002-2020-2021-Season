package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TeleOpMain extends OpMode {

    Drive drive;

    private final double[][] matrix = {{1, 1, 1, 1}, {1, -1, 1, -1}, {1, 1, -1, -1}};

    private double[] output;
    @Override
    public void init(){
        drive = new Drive(this, () -> gamepad1.right_bumper, matrix);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){
        output = drive.drive(new double[] {gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x});
    }

    @Override
    public void stop(){

    }
}
