package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Robot.DriveFields;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;
import org.firstinspires.ftc.teamcode.Util.PlaySound;
import org.openftc.revextensions2.ExpansionHubMotor;

public class TeleOpReverseStacking extends OpMode {





    //Drive motors are going to stay here (this could be changed, but I want to keep track of them)

    private Robot robot;

    //Reverse stacking things
    private Intake intake;
    private Outake outake;
    private Grabber grabber;


    //MAKE SURE THESE GO LAST!!


    private boolean bounce1 = false;
    private boolean soundChanged = false;
    private PlaySound mego;


    @Override
    public void init(){

        robot = new Robot(this, Alliance.OTHER);

        new PlaySound(hardwareMap, R.raw.shutdown).play();
        mego = new PlaySound(hardwareMap, R.raw.megalovania);

        intake = new Intake(hardwareMap);
        outake = new Outake(hardwareMap);
        grabber = new Grabber(hardwareMap);


    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){
    }

    @Override
    public void loop(){
        clearBulkCache();

        //INTAKE
        if(gamepad2.left_trigger > 0.2){
            intake.in();
        } else if(gamepad2.right_trigger > 0.2){
            intake.out();
        } else {
            intake.stop();
        }

        //OUTTAKE
        //We are just going to tie this directly to the joystick because lack of buttons
        outake.setPower(-gamepad2.left_stick_y);

        //GRABBER

        if(gamepad2.left_bumper){
            grabber.open();
        }
        else if(!gamepad2.right_bumper){
            grabber.close();
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
        DriveFields.movement_x = gamepad1.left_stick_x;
        DriveFields.movement_y = -gamepad1.left_stick_y;
        DriveFields.movement_turn = gamepad1.right_stick_x;


        robot.update();

        //OTHER
        controlSound();
    }

    @Override
    public void stop(){

    }

    private void controlSound(){
        if(!soundChanged){
            mego.pause();
            soundChanged = true;
        } else if(soundChanged){
            mego.play();
            soundChanged = false;
        }
    }

    private void checkSound(){
        if(gamepad1.x&&!bounce1){
            controlSound();
            bounce1=true;
        }
        else if(!gamepad1.x){
            bounce1=false;
        }
    }

    //Since this is all one class, I'm adding subsystems here
    @SuppressWarnings("WeakerAccess")
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

    @SuppressWarnings("WeakerAccess")
    static class Grabber {
        private Servo grabber;
        private ExpansionHubMotor lift_left, lift_right;
        private DigitalChannel left_switch, right_switch;



        public static final double OPEN_POSITION = 0;
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
    
    private CRServo findCRServo(String id){
      return hardwareMap.get(CRServo.class, id);
    }
    
    private Servo findServo(String id){
      return hardwareMap.get(Servo.class, id);
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
