package org.firstinspires.ftc.teamcode.TeleOp;

import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.Util.MathThings;

import static org.firstinspires.ftc.teamcode.Util.MathThings.m_v_mult;

public class Drive {



    private Gamepad gamepad1;

    private Func<Boolean> slowModeOn;

    private Func<Double> cubingFactor;

    private static double[][] basic_matrix = {{1, 1, 1, 1}, {1, -1, 1, -1}, {1, 1, -1, -1}};

    public Drive(OpMode opMode){
        this(opMode, () -> opMode.gamepad1.left_bumper);
    }

    public Drive(OpMode opMode, Func<Boolean> slowModeOn){
        this(opMode, slowModeOn, basic_matrix);
    }

    public Drive(OpMode opMode, Func<Boolean> slowModeOn, double[][] matrix){
        this(opMode, slowModeOn, matrix, () -> 0.0);
    }

    /**
     * Use this to cube the inputs
     */
    public Drive(OpMode opMode, Func<Boolean> slowModeOn, double[][] matrix, Func<Double> cubingFactor){
        this.gamepad1 = opMode.gamepad1;
        this.slowModeOn = slowModeOn;
        basic_matrix = matrix;
        this.cubingFactor = cubingFactor;
    }

    /**
     *
     * @param inputs {left stick x, left stick y, right stick x}
     * @return The drive train powers
     */
    public double[] drive(double[] inputs){

        Vector2d translation = new Vector2d(inputs[0], inputs[1]);

        double newMag = MathThings.cubeInput(translation.magnitude(), cubingFactor.value());
        double theta = translation.angle();
        translation = new Vector2d(newMag*Math.cos(theta), newMag*Math.sin(theta));

        inputs[0] = translation.getX();
        inputs[1] = translation.getY();

        double[] output = m_v_mult(basic_matrix, inputs);

        if(!slowModeOn.value()){
            return output;
        }
        else {
            for(double val : output){
                val *= 0.5;
            }
            return output;
        }
    }

}
