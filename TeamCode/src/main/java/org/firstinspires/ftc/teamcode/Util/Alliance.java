package org.firstinspires.ftc.teamcode.Util;

public enum Alliance {

    BLUE(-1),
    RED(-1),
    OTHER(1);

    public final int sign;

    Alliance(int sign){
        this.sign = sign;
    }

}
