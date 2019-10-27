package net.bancino.robotics.swerveio.module;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import net.bancino.robotics.swerveio.SwerveImplementationException;
import net.bancino.robotics.swerveio.encoder.Encoder;
import net.bancino.robotics.swerveio.encoder.MK2Encoder;
import net.bancino.robotics.swerveio.encoder.SparkMaxEncoder;

/**
 * A swerve module implementation that uses RevRobotics Neo motors and Spark Max
 * motor controllers. This was designed for Swerve Drive Specialties' MK2
 * Module.
 * 
 * @author Jordan Bancino
 */
public class MK2SwerveModule extends GenericSwerveModule implements MultiEncoderModule {
    private CANSparkMax driveMotor, pivotMotor;

    private CANPIDController drivePid;

    private EncoderSetting useEncoder = EncoderSetting.ANALOG;

    private Encoder driveEncoder, pivotEncoder;

    /**
     * The swerve module is constructed to allow the pivot motor to coast, this
     * allows for adjustments, but as soon as the module is driven, it switches to
     * brake mode to prevent outside modifications.
     */
    private boolean setPivotIdleMode = false;

    /**
     * Create a new swerve module composed of Neo brushless motors, this uses spark
     * max motor controllers.
     * 
     * @param driveCanId The CAN ID of the drive motor for this module.
     * @param pivotCanId The CAN ID of the pivot motor for this module.
     * @param encoder    The encoder to use as the pivot encoder.
     */
    public MK2SwerveModule(int driveCanId, int pivotCanId, MK2Encoder encoder) {
        super(new CANSparkMax(driveCanId, MotorType.kBrushless), new CANSparkMax(pivotCanId, MotorType.kBrushless),
                new SparkMaxEncoder(), encoder);
        driveMotor = (CANSparkMax) getDriveMotor();
        driveEncoder = getDriveEncoder();
        ((SparkMaxEncoder) driveEncoder).setController(driveMotor);
        drivePid = driveMotor.getPIDController();
        pivotMotor = (CANSparkMax) getPivotEncoder();
        pivotEncoder = new SparkMaxEncoder(pivotMotor);
        pivotMotor.setIdleMode(IdleMode.kCoast);

        setPivotPidOutputLimits(-1, 1);
    }

    /**
     * Create a new swerve module composed of Neo brushless motors, this uses spark
     * max motor controllers.
     * 
     * @param driveCanId        The CAN ID of the drive motor for this module.
     * @param pivotCanId        The CAN ID of the pivot motor for this module.
     * @param analogEncoderPort The port on the roboRIO that the encoder to use as
     *                          the pivot encoder is on.
     */
    public MK2SwerveModule(int driveCanId, int pivotCanId, int analogEncoderPort) {
        this(driveCanId, pivotCanId, new MK2Encoder(analogEncoderPort));
    }

    @Override
    public void setPivotMotorSpeed(double speed) {
        if (!setPivotIdleMode) {
            pivotMotor.setIdleMode(IdleMode.kBrake);
            setPivotIdleMode = true;
        }
        super.setPivotMotorSpeed(speed);
    }

    @Override
    public double getPivotMotorEncoder() {
        switch (useEncoder) {
        case ANALOG: /* The encoder we passed to the superclass is the analog */
            return super.getPivotMotorEncoder();
        case INTERNAL:
            return pivotEncoder.get();
        default:
            return 0;
        }
    }

    @Override
    public void setDriveReference(double ref) {
        drivePid.setReference(ref, ControlType.kPosition);
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
    public void setDrivePidP(double gain) {
        drivePid.setP(gain);
    }

    @Override
    public void setDrivePidI(double gain) {
        drivePid.setI(gain);
    }

    @Override
    public void setDrivePidD(double gain) {
        drivePid.setD(gain);
    }

    @Override
    public void setPivotPidIZone(double iZone) {
        throw new SwerveImplementationException(
                "MK2 Swerve Module: Pivot motor PID does not implement IZone parameters.");
    }

    @Override
    public void setDrivePidIZone(double iZone) {
        drivePid.setIZone(iZone);
    }

    @Override
    public void setDrivePidFF(double gain) {
        drivePid.setFF(gain);
    }

    @Override
    public void setEncoder(EncoderSetting encoder) {
        useEncoder = encoder;
    }

    @Override
    public EncoderSetting getEncoderSetting() {
        return useEncoder;
    }

    @Override
    public void setDrivePidOutputLimits(double min, double max) {
        drivePid.setOutputRange(min, max);
    }
}