package org.firstinspires.ftc.teamcode.Auto;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Event;
import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.robotcore.external.StateMachine;
import org.firstinspires.ftc.robotcore.external.StateTransition;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class Placeholder extends LinearOpMode {



    public enum States{
        START,
        MOVING_TO_LEFT_STONE,
        MOVING_TO_RIGHT_STONE,
        MOVING_TO_CENTER_STONE;
    }



    States state = States.MOVING_TO_CENTER_STONE;


    @Override
    public void runOpMode() throws InterruptedException {

        switch(state){

            case START:
        }
        double heading = 4.0;

        ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile("tt.txt"), String.valueOf(heading));
        double previ = Double.parseDouble(ReadWriteFile.readFile(AppUtil.getInstance().getSettingsFile("t.txt")));


    }

}
