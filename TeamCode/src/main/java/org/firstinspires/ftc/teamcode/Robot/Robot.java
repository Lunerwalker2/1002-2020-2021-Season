package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Util.Alliance;
import org.firstinspires.ftc.teamcode.Util.WheelPowerCanvas;

import static org.firstinspires.ftc.teamcode.Robot.DriveFields.movement_turn;
import static org.firstinspires.ftc.teamcode.Robot.DriveFields.movement_x;
import static org.firstinspires.ftc.teamcode.Robot.DriveFields.movement_y;

public class Robot {




    //The OpMode
    public OpMode opMode;

    //The robot components
    public DriveBase driveBase;
    public Odometry odometry;
    public BulkData bulkData;

    //Road Runner component
    public SampleMecanumDrive roadRunnerBase;

    //Ftc dashboard
    private FtcDashboard dashboard = FtcDashboard.getInstance();

    //The timer for the loop time
    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);


    //The starting positions for the robot see the "skystoneField.png" picture for reference
    private static final Pose2d redStartingPosition = new Pose2d(0, 0, Rotation2d.fromDegrees(90));

    private static final Pose2d blueStartingPosition = new Pose2d(0, 0, Rotation2d.fromDegrees(-90));

    //To be used mostly in teleop
    public static Pose2d userStartingPosition = new Pose2d(0,0, new Rotation2d(0));

    public TelemetryPacket packet = new TelemetryPacket();

    public Alliance alliance;


    public Robot(OpMode opMode, Alliance alliance) {
        this.opMode = opMode;
        this.alliance = alliance;

        //Initialize components

        //Use encoders if in auto
        driveBase = new DriveBase(this, alliance != Alliance.OTHER);

        bulkData = new BulkData(this);

        //Decide how to initialize odometry
        if (alliance == Alliance.RED) {
            odometry = new Odometry(this, redStartingPosition);
        } else if(alliance == Alliance.BLUE) {
            odometry = new Odometry(this, blueStartingPosition);
        } else {
            odometry = new Odometry(this,userStartingPosition);
        }

        roadRunnerBase = new SampleMecanumDrive(this);


        //Set dashboard update time
        dashboard.setTelemetryTransmissionInterval(25);
    }

    //Called to update all the robot's components
    public void update(){
        //This should be called before other hardware calls
        bulkData.update();
        
        //Updates odometry
        odometry.update();
         
        
        //Updates RoadRunner. If it isn't used, this does nothing
        roadRunnerBase.update();

        //If in teleOp, update using x, y, turn components
        if(alliance == Alliance.OTHER)driveBase.updateHolonomic();
        //Otherwise, assume the powers were changed already and send them through
        else driveBase.updatePowers();


        //Compile some telemetry (to dashboard as of now) for the bois
        compileTelemetry();
        timer.reset();
    }

    //Called on stop() normally to stop all motion
    public void stop(){
        driveBase.stop();
    }

    //Compiles telemetry to both dashboard and telemetry
    private void compileTelemetry(){
        packet = new TelemetryPacket();

        packet.put("Robot X", Odometry.world_x_position);
        packet.put("Robot Y", Odometry.world_y_position);
        packet.put("Robot Heading (deg)", Odometry.world_angle_deg);
        packet.put("Robot Heading (rad)", Odometry.world_angle_rad);

        packet.put("Movement X", movement_x);
        packet.put("Movement Y", movement_y);
        packet.put("Movement Turn", movement_turn);

        packet.put("Loop Time", timer.milliseconds());

        dashboard.sendTelemetryPacket(packet);
    }

    private void drawMovement(Canvas field){
        WheelPowerCanvas.drawRobotMovement(field);
    }




}
