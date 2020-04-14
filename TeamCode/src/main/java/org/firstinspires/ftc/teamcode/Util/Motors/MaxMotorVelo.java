package org.firstinspires.ftc.teamcode.Util.Motors;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.util.List;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ReadWriteFile;

@TeleOp
public class MaxMotorVelo extends LinearOpMode {

  private ExpansionHubMotor left_front_drive, left_back_drive, right_front_drive, right_back_drive;

  private double velocityToRPM(double velo){
    return (velo * 60)/474.94839;
  }

  private void saveLog(String text){
    ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile("DriveMotorMaxRPM.txt"), text);
  }

  @Override
  public void runOpMode() throws InterruptedException {

    //Drive Motors stay here
    left_front_drive = findMotor(HardwareNames.Drive.LEFT_FRONT);
    left_back_drive = findMotor(HardwareNames.Drive.LEFT_BACK);
    right_front_drive = findMotor(HardwareNames.Drive.RIGHT_FRONT);
    right_back_drive = findMotor(HardwareNames.Drive.RIGHT_BACK);

    //Reverse right side
    right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
    right_back_drive.setDirection(DcMotorSimple.Direction.REVERSE);


    List<LynxModule> moduleList = hardwareMap.getAll(LynxModule.class);

    for(LynxModule module : moduleList){
      module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
    }
    double[] velos = {0,0,0,0};


    telemetry.addData("Left Front Max RPM", () ->{
      velos[0] = Math.max(velos[0], left_front_drive.getVelocity());
      return velocityToRPM(velos[0]);
    });
    telemetry.addData("Left Back Max RPM", () -> {
      velos[1] = Math.max(velos[1], left_back_drive.getVelocity());
      return velocityToRPM(velos[1]);
    });
    telemetry.addData("Right Front Max RPM", () -> {
      velos[2] = Math.max(velos[2], right_front_drive.getVelocity());
      return velocityToRPM(velos[2]);
    });
    telemetry.addData("Right Back Max RPM", () -> {
      velos[3] = Math.max(velos[3], right_back_drive.getVelocity());
      return velocityToRPM(velos[3]);
    });


    while (opModeIsActive()) {

      for(LynxModule module : moduleList){
        module.clearBulkCache();
      }

      //Drive a tank formation for simplicity
      left_front_drive.setPower(-gamepad1.left_stick_y);
      left_back_drive.setPower(-gamepad1.left_stick_y);
      right_front_drive.setPower(-gamepad1.right_stick_y);
      right_back_drive.setPower(-gamepad1.right_stick_y);

      telemetry.update();
    }

    saveLog("RPM Maximums \n" +
            "Left Front Max RPM" + velocityToRPM(Math.max(velos[0], left_front_drive.getVelocity())) + "\n" +
            "Left Back Max RPM" + velocityToRPM(Math.max(velos[1], left_back_drive.getVelocity())) + "\n" +
            "Right Front Max RPM" + velocityToRPM(Math.max(velos[2], right_front_drive.getVelocity())) + "\n" +
            "Right Back Max RPM" + velocityToRPM(Math.max(velos[3], right_back_drive.getVelocity()))
    );
  
  }

  private ExpansionHubMotor findMotor(String id){
    return hardwareMap.get(ExpansionHubMotor.class, id);
  }
}
