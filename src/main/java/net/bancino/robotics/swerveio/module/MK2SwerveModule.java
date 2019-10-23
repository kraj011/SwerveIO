package net.bancino.robotics.swerveio.module;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.AnalogInput;
import net.bancino.robotics.swerveio.SwerveImplementationException;

/**
 * A swerve module implementation that uses RevRobotics Neo motors and Spark Max
 * motor controllers. This was designed for Swerve Drive Specialties' MK2
 * Module.
 * 
 * @author Jordan Bancino
 */
public class MK2SwerveModule implements MultiEncoderModule {
    private CANSparkMax driveMotor, pivotMotor;

    private CANPIDController pivotPid;
    private AnalogInput pivotEncoder;
    private double analogEncoderOffset = 0;
    private EncoderSetting useEncoder = EncoderSetting.ANALOG;

    /**
     * The swerve module is constructed to allow the pivot motor to coast, this
     * allows for adjustments, but as soon as the module is driven, it switches to
     * brake mode to prevent outside modifications.
     */
    private boolean setPivotIdleMode = false;

    /**
     * Create a new swerve module composed of Neo brushless motors, this uses spark
     * max motor controllers.
     */
    public MK2SwerveModule(int driveCanId, int pivotCanId, int analogEncoderChannel) {
        driveMotor = new CANSparkMax(driveCanId, MotorType.kBrushless);
        pivotMotor = new CANSparkMax(pivotCanId, MotorType.kBrushless);
        pivotEncoder = new AnalogInput(analogEncoderChannel);

        pivotMotor.setIdleMode(IdleMode.kCoast);

        pivotPid = pivotMotor.getPIDController();
        pivotPid.setOutputRange(-1, 1);
    }

    @Override
    public void setPivotMotorSpeed(double speed) {
        if (!setPivotIdleMode) {
            pivotMotor.setIdleMode(IdleMode.kBrake);
            setPivotIdleMode = true;
        }
        pivotMotor.set(speed);
    }

    @Override
    public void setDriveMotorSpeed(double speed) {
        driveMotor.set(speed);
    }

    @Override
    public double getPivotMotorSpeed() {
        return pivotMotor.get();
    }

    @Override
    public double getDriveMotorSpeed() {
        return driveMotor.get();
    }

    @Override
    public double getPivotMotorEncoder() {
        switch (useEncoder) {
        case ANALOG:
            /* Scale the encoder voltage to go from 0 -> 5 volts to 0 -> 360 degrees */
            return (360 / 5) * (pivotEncoder.getVoltage() - analogEncoderOffset);
        case INTERNAL:
            return pivotMotor.getEncoder().getPosition();
        default:
            return 0;
        }
    }

    @Override
    public double getDriveMotorEncoder() {
        return driveMotor.getEncoder().getPosition();
    }

    @Override
    public void zeroPivotEncoder() {
        analogEncoderOffset = pivotEncoder.getVoltage();
        pivotMotor.getEncoder().setPosition(0);

    }

    @Override
    public void zeroDriveEncoder() {
        driveMotor.getEncoder().setPosition(0);

    }

    @Override
    public void stopPivotMotor() {
        pivotMotor.stopMotor();
    }

    @Override
    public void stopDriveMotor() {
        driveMotor.stopMotor();
    }

    @Override
    public void setPivotReference(double ref) {
        pivotPid.setReference(ref, ControlType.kPosition);
    }

    @Override
    public void setDriveReference(double ref) {
        throw new SwerveImplementationException("setDriveReference() is not implemented for NeoSwerveModule!");
    }

    @Override
    public void setPivotClosedLoopRampRate(double rate) {
        pivotMotor.setClosedLoopRampRate(rate);
    }

    @Override
    public void setPivotOpenLoopRampRate(double rate) {
        pivotMotor.setOpenLoopRampRate(rate);
    }

    @Override
    public void setDriveClosedLoopRampRate(double rate) {
        driveMotor.setClosedLoopRampRate(rate);
    }

    @Override
    public void setDriveOpenLoopRampRate(double rate) {
        driveMotor.setClosedLoopRampRate(rate);
    }

    @Override
    public void setPivotPidP(double gain) {
        pivotPid.setP(gain);
    }

    @Override
    public void setDrivePidP(double gain) {
        throw new SwerveImplementationException("Drive motor PID not implemented!");
    }

    @Override
    public void setPivotPidI(double gain) {
        pivotPid.setI(gain);
    }

    @Override
    public void setDrivePidI(double gain) {
        throw new SwerveImplementationException("Drive motor PID not implemented!");
    }

    @Override
    public void setPivotPidD(double gain) {
        pivotPid.setD(gain);
    }

    @Override
    public void setDrivePidD(double gain) {
        throw new SwerveImplementationException("Drive motor PID not implemented!");
    }

    @Override
    public void setPivotPidIZone(double iZone) {
        pivotPid.setIZone(iZone);
    }

    @Override
    public void setDrivePidIZone(double iZone) {
        throw new SwerveImplementationException("Drive motor PID not implemented!");
    }

    @Override
    public void setPivotPidFF(double gain) {
        pivotPid.setFF(gain);
    }

    @Override
    public void setDrivePidFF(double gain) {
        throw new SwerveImplementationException("Drive motor PID not implemented!");
    }

    @Override
    public void setEncoder(EncoderSetting encoder) {
        useEncoder = encoder;

    }

    @Override
    public EncoderSetting getEncoderSetting() {
        return useEncoder;
    }
}