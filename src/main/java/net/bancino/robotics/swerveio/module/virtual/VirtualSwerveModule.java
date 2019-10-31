package net.bancino.robotics.swerveio.module.virtual;

import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.SpeedController;
import net.bancino.robotics.swerveio.encoder.Encoder;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.GenericSwerveModule;
import net.bancino.robotics.swerveio.pid.MiniPID;

/**
 * This is a virtual representation of a Swerve Module. This is meant for use in
 * "virtual" implementations of the SwerveIO library. The goal is for this
 * module to simulate the behavior of physical swerve modules, complete with PID
 * control, simulated encoder feedback, and drive functionality.
 * 
 * @author David Krajewski
 */
class VirtualSwerveModule implements AbstractSwerveModule {

    private double driveEncoderValue, pivotEncoderValue;
    private double driveMotorSpeed, pivotMotorSpeed;

    public VirtualSwerveModule() {

    }

    @Override
    public void setPivotMotorSpeed(double speed) {
        this.pivotMotorSpeed = speed;

    }

    @Override
    public void setDriveMotorSpeed(double speed) {
        this.driveMotorSpeed = speed;

    }

    @Override
    public double getPivotMotorSpeed() {
        return this.pivotMotorSpeed;
    }

    @Override
    public double getDriveMotorSpeed() {
        return this.driveMotorSpeed;
    }

    @Override
    public double getPivotMotorEncoder() {
        return this.pivotEncoderValue;
    }

    @Override
    public double getDriveMotorEncoder() {
        return this.driveEncoderValue;
    }

    @Override
    public void zeroPivotEncoder() {
        this.pivotEncoderValue = 0.0;

    }

    @Override
    public void zeroDriveEncoder() {
        this.driveEncoderValue = 0.0;

    }

    @Override
    public void stopPivotMotor() {
        this.pivotMotorSpeed = 0.0;

    }

    @Override
    public void stopDriveMotor() {
        this.driveMotorSpeed = 0.0;

    }

    @Override
    public void setPivotClosedLoopRampRate(double rate) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotOpenLoopRampRate(double rate) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDriveClosedLoopRampRate(double rate) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDriveOpenLoopRampRate(double rate) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDriveReference(double ref) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotReference(double ref) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotPidP(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDrivePidP(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotPidI(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDrivePidI(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotPidD(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDrivePidD(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotPidIZone(double iZone) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDrivePidIZone(double iZone) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotPidFF(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDrivePidFF(double gain) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPivotPidOutputLimits(double min, double max) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDrivePidOutputLimits(double min, double max) {
        // TODO Auto-generated method stub

    }

}