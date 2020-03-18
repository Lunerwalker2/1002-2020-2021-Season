package org.firstinspires.ftc.teamcode.TeleOp.TeleOpSystems;

import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.Util.DriveBaseVectors;
import org.firstinspires.ftc.teamcode.Util.MathThings;

import static org.firstinspires.ftc.teamcode.Util.MathThings.m_v_mult;

/**
 * This class is a little special because it doesn't really use the methods it overrides
 */
public class Drive extends TeleOpSystem {


    private Func<Boolean> slowModeOn;

    private Func<Double> cubingFactor;

    private static double[][] basic_matrix = {DriveBaseVectors.forward, DriveBaseVectors.strafeR, DriveBaseVectors.turnCW};

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
        super(opMode);

        this.slowModeOn = slowModeOn;
        basic_matrix = matrix;
        this.cubingFactor = cubingFactor;
    }

    @Override
    public void initHardware(){

    }

    @Override
    public void update(){

    }

    @Override
    public void stop(){

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
