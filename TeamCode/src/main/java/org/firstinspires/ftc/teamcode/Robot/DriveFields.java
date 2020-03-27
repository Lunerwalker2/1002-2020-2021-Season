package org.firstinspires.ftc.teamcode.Robot;


public class DriveFields {

    public static double movement_x = 0;
    public static double movement_y = 0;
    public static double movement_turn = 0;


    public static double lf_power = 0;
    public static double lb_power = 0;
    public static double rf_power = 0;
    public static double rb_power = 0;

    public static void distributePowers(double[] powers){
        lf_power = powers[0];
        lb_power = powers[1];
        rf_power = powers[2];
        rb_power = powers[3];
    }

}