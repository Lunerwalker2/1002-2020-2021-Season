package org.firstinspires.ftc.teamcode.Util.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MotorProfile {
    String hardwareName();
    DcMotor.ZeroPowerBehavior defaultZeroPowerBehavior();
    DcMotorSimple.Direction defaultDirection();
}
