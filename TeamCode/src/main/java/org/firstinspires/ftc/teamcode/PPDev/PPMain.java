package org.firstinspires.ftc.teamcode.PPDev;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class PPMain extends LinearOpMode {


    @Override
    public void runOpMode(){



        waitForStart();


        PPMovement.goToPosition(358/2, 358/2, 0.5, Math.toRadians(90), 0.3);
    }
}
