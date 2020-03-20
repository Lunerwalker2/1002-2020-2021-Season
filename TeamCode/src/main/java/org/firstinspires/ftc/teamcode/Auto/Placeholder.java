package org.firstinspires.ftc.teamcode.Auto;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Event;
import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.robotcore.external.StateMachine;
import org.firstinspires.ftc.robotcore.external.StateTransition;

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


    }

}
