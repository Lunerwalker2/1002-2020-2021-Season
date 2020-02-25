package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.openftc.revextensions2.ExpansionHubEx;

import java.util.ArrayList;
import java.util.List;

public class ExampleBulkReadImpl extends LinearOpMode {


    private DcMotorEx left_front_drive, left_back_drive, right_front_drive, right_back_drive = null;

    private double[] encoders = new double[4];

    private double[] velos = new double[4];

    // Important Step 2: Get access to a list of Expansion Hub Modules to enable changing caching methods.
    //Ryan: I'd rather use the Re2 ExpansionHubEx
    private ArrayList<ExpansionHubEx> allHubs = new ArrayList<>();

    private String[] motorIds = {"lf", "lb", "rf", "rb"};

    /**
     * IMPORTANT NOTE
     * The AUTO function reads a bulk rad every loop, however it will happen again if another
     * "get" method is called in that loop. Therefore, store the value once, and use that each loop.
     */
    
    /*However,
    * The MANUAL mode is the MOST efficient, and it only requires that you clear the cache at the start of each loop. 
    * The bulk read is done on the first "get" call and you can do more get calls. Aside from the first read, no more reads are
    * done until the cahce is cleared again
    */

    private ArrayList<DcMotorEx> motors = new ArrayList<>();

    private DcMotorEx findMotor(String id){
        return hardwareMap.get(DcMotorEx.class, id);
    }
    @Override
    public void runOpMode(){

        allHubs.add(hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 1"));
        allHubs.add(hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 10"));

        left_front_drive = findMotor(motorIds[0]);
        left_back_drive = findMotor(motorIds[1]);
        right_front_drive = findMotor(motorIds[2]);
        right_back_drive = findMotor(motorIds[3]);

        motors.add(left_front_drive);
        motors.add(left_back_drive);
        motors.add(right_front_drive);
        motors.add(right_back_drive);

        for (ExpansionHubEx hub : allHubs) {
            hub.getStandardModule().setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        double cumalitiveTime = 0;


        ElapsedTime cycleTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        int loopNum = 1;
        while (opModeIsActive()){
            clearBulkCache();
            
            for(DcMotorEx motor : motors){
                telemetry.addData("Motor" + motor.toString() + "Encoder Val", motor.getCurrentPosition());
                telemetry.addData("Motor" + motor.toString() + "Encoder Velo (deg/sec)", motor.getVelocity(AngleUnit.DEGREES));
            }
            cumalitiveTime += cycleTime.milliseconds();
            telemetry.addData("Cycle Time", cumalitiveTime/loopNum);
            telemetry.update();
            cycleTime.reset();
            loopNum++;
        }

    }
    private void clearBulkCache(){
        for(ExpansionHubEx hub : allHubs){
            hub.getStandardModule().clearBulkCache();
    }
}
