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
    private SpeedController driveMotor, pivotMotor;
    private Encoder pivotEncoder;
    private MiniPID pivotPid = new MiniPID(0, 0, 0);

    public VirtualSwerveModule(SpeedController driveMotor, SpeedController pivotMotor, Encoder pivotEncoder) {
        this.driveMotor = driveMotor;
        this.pivotMotor = pivotMotor;
        this.pivotEncoder = pivotEncoder;
    }

    @Override
    public void setPivotMotorSpeed(double speed) {
        // TODO Auto-generated method stub
        this.pivotMotor.set(speed);
    }

    @Override
    public void setDriveMotorSpeed(double speed) {
        // TODO Auto-generated method stub
        this.driveMotor.set(speed);

    }

    @Override
    public double getPivotMotorSpeed() {
        // TODO Auto-generated method stub
        return this.pivotMotor.get();
    }

    @Override
    public double getDriveMotorSpeed() {
        // TODO Auto-generated method stub
        return this.driveMotor.get();
    }

    @Override
    public double getPivotMotorEncoder() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void zeroPivotEncoder() {
        // TODO Auto-generated method stub
        this.pivotEncoder.zero();

    }

    @Override
    public void stopPivotMotor() {
        // TODO Auto-generated method stub
        this.pivotMotor.stopMotor();

    }

    @Override
    public void stopDriveMotor() {
        // TODO Auto-generated method stub
        this.driveMotor.stopMotor();

    }

    @Override
    public double getDriveMotorEncoder() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void zeroDriveEncoder() {
        // TODO Auto-generated method stub

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