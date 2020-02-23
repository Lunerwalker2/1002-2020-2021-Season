package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Func;

public class Drive {


    public static double[] m_v_mult(double[][] m, double[] v) {
        double[] out = new double[4];
        out[0] = v[0] * m[0][0] + v[1] * m[1][0] + v[2] * m[2][0];
        out[1] = v[0] * m[0][1] + v[1] * m[1][1] + v[2] * m[2][1];
        out[2] = v[0] * m[0][2] + v[1] * m[1][2] + v[2] * m[2][2];
        out[3] = v[0] * m[0][3] + v[1] * m[1][3] + v[2] * m[2][3];
        return out;
    }

    private Gamepad gamepad1;

    private Func<Boolean> slowModeOn;

    private static double[][] basic_matrix = {{1, 1, 1, 1}, {1, -1, 1, -1}, {1, 1, -1, -1}};

    public Drive(OpMode opMode){
        this(opMode, () -> opMode.gamepad1.left_bumper);
    }

    public Drive(OpMode opMode, Func<Boolean> slowModeOn){
        this(opMode, slowModeOn, basic_matrix);
    }

    public Drive(OpMode opMode, Func<Boolean> slowModeOn, double[][] matrix){
        this.gamepad1 = opMode.gamepad1;
        this.slowModeOn = slowModeOn;
        basic_matrix = matrix;
    }

    public double[] drive(double[] inputs){

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
