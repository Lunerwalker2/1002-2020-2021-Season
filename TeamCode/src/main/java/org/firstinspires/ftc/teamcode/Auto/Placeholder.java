package org.firstinspires.ftc.teamcode.Auto;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Event;
import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.robotcore.external.StateMachine;
import org.firstinspires.ftc.robotcore.external.StateTransition;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Util.Logging.FileType;
import org.firstinspires.ftc.teamcode.Util.Logging.TeleOpLogger;

@TeleOp(name = "Test Logging")
public class Placeholder extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        TeleOpLogger logger = new TeleOpLogger("testLog.txt");

        waitForStart();

        logger.getMessagesList().add("Hi!");
        logger.getMessagesList().add("My name is Joe!");
        logger.getDataPairs().put("Random Data: ", 45.87);
        logger.getDataPairs().put("More Data: ", 3.1415926535);

        logger.log(FileType.XML);


    }

}
