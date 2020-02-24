package org.firstinspires.ftc.teamcode.PPDev;

public class CurvePoint {


    public double x;
    public double y;
    public double moveSpeed;
    public double turnSpeed;
    public double followDistance;
    public double pointLength;
    public double slowDownTurnRadians;
    public double slowDownTurnAmount;


    public CurvePoint(double x, double y, double moveSpeed, double turnSpeed, double followDistance, double pointLength,
                      double slowDownTurnRadians, double slowDownTurnAmount) {
        this.x = x;
        this.y = y;
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
        this.followDistance = followDistance;
        this.pointLength = pointLength;
        this.slowDownTurnRadians = slowDownTurnRadians;
        this.slowDownTurnAmount = slowDownTurnAmount;
    }

    public CurvePoint(CurvePoint nextPoint){
        x = nextPoint.x;
        y = nextPoint.y;
        moveSpeed = nextPoint.moveSpeed;
        turnSpeed = nextPoint.turnSpeed;
        followDistance = nextPoint.followDistance;
        pointLength = nextPoint.pointLength;
        slowDownTurnRadians = nextPoint.slowDownTurnRadians;
        slowDownTurnAmount = nextPoint.slowDownTurnAmount;
    }
}
