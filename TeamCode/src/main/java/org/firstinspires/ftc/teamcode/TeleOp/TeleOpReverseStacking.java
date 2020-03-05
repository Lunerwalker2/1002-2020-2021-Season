package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.RRDev.Quickstart.util.AssetsTrajectoryManager;
import org.firstinspires.ftc.teamcode.TeleOp.TeleOpSystems.*;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.ArrayList;
import java.util.List;

public class TeleOpReverseStacking extends OpMode {

    //Drive motors are going to stay here (this could be changed, but I want to keep track of them)

    private ExpansionHubMotor left_front_drive, left_back_drive, right_front_drive, right_back_drive;


    //Reverse stacking things
    private Intake intake;
    private Outake outake;
    private Grabber grabber;

    private final double[][] matrix = {{1, 1, 1, 1}, {1, -1, 1, -1}, {1, 1, -1, -1}};

    private double[] output;


    private ArrayList<TeleOpSystem> systems = new ArrayList<>();


    private List<LynxModule> hubs;

    private boolean bounce1 = false;
    private boolean grabberMove = false;

    private void moveGrabber(){
        if(!grabberMove){
            grabber.close();
            grabberMove = true;
        } else if(grabberMove){
            grabber.open();
            grabberMove = false;
        }
    }

    //MAKE SURE THESE GO LAST!!


    private Drive drive = new Drive(this, () -> gamepad1.left_bumper, matrix, () -> 0.6);


    @Override
    public void init(){

        intake = new Intake(hardwareMap);
        outake = new Outake(hardwareMap);
        grabber = new Grabber(hardwareMap);






        //Add all systems to the list for easy access
        systems.add(drive);

        //Init Hardware

        setBulkModeManual();

        for(TeleOpSystem system : systems){
            system.initHardware();
        }

        //Drive Motors stay here
        left_front_drive = findMotor(HardwareNames.Drive.LEFT_FRONT);
        left_back_drive = findMotor(HardwareNames.Drive.LEFT_BACK);
        right_front_drive = findMotor(HardwareNames.Drive.RIGHT_FRONT);
        right_back_drive = findMotor(HardwareNames.Drive.RIGHT_BACK);

        //Reverse right side
        right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        //Set to encoder movement
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Set to BRAKE
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        stopDrive();



    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){
        for(TeleOpSystem system : systems){
            system.onStart();
        }
    }

    @Override
    public void loop(){
        clearBulkCache();
        //Update all the systems
        for(TeleOpSystem system : systems){
            system.update();
        }

        //INTAKE
        if(gamepad2.left_trigger > 0.2){
            intake.in();
        } else if(gamepad2.right_trigger > 0.2){
            intake.out();
        } else {
            intake.stop();
        }

        //OUTTAKE
        if(gamepad2.left_bumper){
            outake.forward();
        } else {
            outake.stop();
        }

        //GRABBER

        if(gamepad2.right_bumper&&!bounce1){
            moveGrabber();
            bounce1=true;
        }
        else if(!gamepad2.left_bumper){
            bounce1=false;
        }

        //LIFT

        //Move up
        if(gamepad2.dpad_up){
            grabber.up();
        }
        //Move down
        else if(gamepad2.dpad_down && !grabber.atBottom()){
            grabber.down();
        }
        //Hold position
        else if(!grabber.atBottom()) {
            grabber.hold();
        }
        else {
            grabber.stop();
        }

        //DRIVE

        output = drive.drive(new double[] {gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x});

        left_front_drive.setPower(output[0]);
        left_back_drive.setPower(output[1]);
        right_front_drive.setPower(output[2]);
        right_back_drive.setPower(output[3]);
    }

    @Override
    public void stop(){
        //Stop all systems
        for(TeleOpSystem system : systems){
            system.stop();
        }
        //Stop drive base
        stopDrive();
    }

    //Since this is all one class, I'm adding subsystems here
    static class Intake {
        private ExpansionHubMotor left_intake, right_intake;

        Intake(HardwareMap hardwareMap){
            left_intake = hardwareMap.get(ExpansionHubMotor.class, "left_intake");
            right_intake = hardwareMap.get(ExpansionHubMotor.class, "right_intake");
            right_intake.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        public void stop(){
            setPower(0);
        }

        public void in(){
            setPower(0.5);
        }

        public void out(){
            setPower(-0.7);
        }

        private void setPower(double power){
            left_intake.setPower(power);
            right_intake.setPower(power);
        }
    }

    static class Outake {
        private CRServo left_top, left_bottom, right_top, right_bottom;

        Outake(HardwareMap hardwareMap){
            left_top = hardwareMap.crservo.get("left_top");
            left_bottom = hardwareMap.crservo.get("left_bottom");
            right_top = hardwareMap.crservo.get("right_top");
            right_bottom = hardwareMap.crservo.get("right_bottom");
            right_top.setDirection(DcMotorSimple.Direction.REVERSE);
            right_bottom.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        public void stop(){
            setPower(0);
        }

        public void forward(){
            setPower(0.5);
        }

        public void backward(){
            setPower(-0.7);
        }

        private void setPower(double power){
            left_top.setPower(power);
            left_bottom.setPower(power);
            right_top.setPower(power);
            right_bottom.setPower(power);
        }
    }

    static class Grabber {
        private Servo grabber;
        private ExpansionHubMotor lift_left, lift_right;
        private DigitalChannel left_switch, right_switch;

        private static final double OPEN_POSITION = 0;
        private static final double CLOSE_POSITION = 1;

        Grabber(HardwareMap hardwareMap){
            grabber = hardwareMap.servo.get("grabber");
            lift_left = hardwareMap.get(ExpansionHubMotor.class, "lift_left");
            lift_right = hardwareMap.get(ExpansionHubMotor.class, "lift_right");
            left_switch = hardwareMap.digitalChannel.get("left_switch");
            right_switch = hardwareMap.digitalChannel.get("right_switch");
            left_switch.setMode(DigitalChannel.Mode.INPUT);
            right_switch.setMode(DigitalChannel.Mode.INPUT);
        }

        public boolean atBottom(){
            return (!left_switch.getState() || !right_switch.getState());
        }

        public void stop(){
            liftSetPower(0);
        }

        public void liftSetPower(double power){
            lift_left.setPower(power);
            lift_right.setPower(power);
        }

        public void up(){
            liftSetPower(0.7);
        }

        public void down(){
            liftSetPower(-0.2);
        }

        public void hold(){
            liftSetPower(0.05);
        }

        public void close(){
            grabber.setPosition(CLOSE_POSITION);
        }

        public void open(){
            grabber.setPosition(OPEN_POSITION);
        }
    }


    private ExpansionHubMotor findMotor(String id){
        return hardwareMap.get(ExpansionHubMotor.class, id);
    }
    
    private CRServo findCRServo(String id){
      return hardwareMap.get(CRServo.class, id);
    }
    
    private Servo findServo(String id){
      return hardwareMap.get(Servo.class, id);
    }

    private void setMode(DcMotor.RunMode runMode){
        left_front_drive.setMode(runMode);
        left_back_drive.setMode(runMode);
        right_front_drive.setMode(runMode);
        right_back_drive.setMode(runMode);
    }

    private void setZeroPowerBehavior(ExpansionHubMotor.ZeroPowerBehavior behavior){
        left_front_drive.setZeroPowerBehavior(behavior);
        left_back_drive.setZeroPowerBehavior(behavior);
        right_front_drive.setZeroPowerBehavior(behavior);
        right_back_drive.setZeroPowerBehavior(behavior);
    }

    private void stopDrive(){
        left_front_drive.setPower(0);
        left_back_drive.setPower(0);
        right_front_drive.setPower(0);
        right_back_drive.setPower(0);
    }

    private void setBulkModeManual(){
        for(LynxModule module : hardwareMap.getAll(LynxModule.class)){
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    private void clearBulkCache(){
        for(LynxModule module : hardwareMap.getAll(LynxModule.class)){
            module.clearBulkCache();
        }
    }
}
