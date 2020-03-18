package org.firstinspires.ftc.teamcode.TeleOp.Shooter;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;

public class TeleOpShooter extends LinearOpMode {



    private BNO055IMU imu;

    protected static final double imuOffset = 90;


    @Override
    public void runOpMode() throws InterruptedException {











    }


    static class Shooter {

        private double targetHeading = 0;
        private ShooterRotation shooterRotation;
        private ShooterOutput shooterOutput;
        private PIDFController controller = new PIDFController(new double[] {5, 0, 0, 0});
        private BNO055IMU imu;
        private Func<Double> getRobotHeading = () -> AngleUnit.normalizeRadians(imu.getAngularOrientation().firstAngle + TeleOpShooter.imuOffset);
        private Func<Double> getTargetError = () -> (targetHeading - shooterRotation.getHeading()) - getRobotHeading.value();

        public void setCurrentAsTarget(){
            targetHeading = shooterRotation.getHeading();
        }

        private double getOutputToTarget(){
            return controller.calculate(getTargetError.value(), targetHeading);
        }



        Shooter(HardwareMap hardwareMap){
            imu = hardwareMap.get(BNO055IMU.class, HardwareNames.Sensors.LEFT_HUB_IMU);
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
            imu.initialize(parameters);
            shooterRotation = new ShooterRotation(hardwareMap);
            shooterOutput = new ShooterOutput(hardwareMap);
        }


         class ShooterRotation {

             private DcMotorEx rotator;

             private final double TICKS_PER_REV = 1244;

             ShooterRotation(HardwareMap hardwareMap) {
                 rotator = hardwareMap.get(DcMotorEx.class, "rotator");
                 reset();
                 rotator.setPower(0);
             }

             public void update(double power) {
                 rotator.setPower(power);
             }

             public void reset() {
                 rotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                 rotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
             }

             public double getHeading() {
                 double position = rotator.getCurrentPosition();
                 return AngleUnit.RADIANS.toRadians(AngleUnit.normalizeDegrees(position * (TICKS_PER_REV / 360)));
             }

         }

         class ShooterOutput {

            private DcMotorSimple shooter;

            private final double minimumPower = 0.3;

            private double scaleToPower(double input){
                return Range.scale(
                        input,
                        0,
                        1,
                        minimumPower,
                        1
                );
            }

            ShooterOutput(HardwareMap hardwareMap){
                shooter = hardwareMap.get(DcMotorSimple.class, "shooter");
                shooter.setPower(0);
            }

            public void startToMinimum(){
                shooter.setPower(minimumPower);
            }

            public void stop(){
                shooter.setPower(0);
            }

            public void addPower(double power){
                shooter.setPower(scaleToPower(power));
            }
         }

    }


    static class Intake {


    }
}
