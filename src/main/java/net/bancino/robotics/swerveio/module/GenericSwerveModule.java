package net.bancino.robotics.swerveio.module;

import edu.wpi.first.wpilibj.SpeedController;
import net.bancino.robotics.swerveio.SwerveImplementationException;
import net.bancino.robotics.swerveio.encoder.Encoder;
import net.bancino.robotics.swerveio.pid.MiniPID;

/**
 * A generic swerve module class that provides every single function that can
 * possibly be implemented using only two speed controllers and a pivot encoder.
 * If you wish to implement your own module, it is often easier to extend this
 * class instead of implementing AbstractSwerveModule directly.
 * 
 * @author Jordan Bancino
 */
public abstract class GenericSwerveModule implements AbstractSwerveModule {

    private SpeedController driveMotor, pivotMotor;
    private Encoder pivotEncoder, driveEncoder;
    private MiniPID pivotPid = new MiniPID(0, 0, 0);

    /**
     * Construct a generic swerve module.
     * 
     * @param driveMotor   The drive motor controller.
     * @param pivotMotor   The pivot motor controller.
     * @param pivotEncoder The pivot encoder.
     */
    public GenericSwerveModule(SpeedController driveMotor, SpeedController pivotMotor, Encoder driveEncoder, Encoder pivotEncoder) {
        if (driveMotor == null) {
            throw new IllegalArgumentException("Drive motor must not be null.");
        } else if (driveEncoder == null) {
            throw new IllegalArgumentException("Drive encoder must not be null.");
        } else if (pivotMotor == null) {
            throw new IllegalArgumentException("Pivot motor must not be null.");
        } else if (pivotEncoder == null) {
            throw new IllegalArgumentException("Pivot encoder must not be null.");
        } else {
            this.driveMotor = driveMotor;
            this.pivotMotor = pivotMotor;
            this.pivotEncoder = pivotEncoder;
            this.driveEncoder = driveEncoder;
        }
    }

    /** 
     * @return The drive motor controller that this class was instantiated with.
     */
    protected SpeedController getDriveMotor() {
        return driveMotor;
    }

    /**
     * @return The pivot motor controller that this class was instantiated with.
     */
    protected SpeedController getPivotMotor() {
        return pivotMotor;
    }

    /**
     * @return The pivot encoder that this class was instantiated with.
     */
    protected Encoder getPivotEncoder() {
        return pivotEncoder;
    }

    /** 
     * @return the drive encoder that this class was instantiated with.
     */
    protected Encoder getDriveEncoder() {
        return driveEncoder;
    }

    @Override
    public void setPivotMotorSpeed(double speed) {
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
    public double getDriveMotorEncoder() {
        return driveEncoder.get();
    }

    @Override
    public void zeroDriveEncoder() {
        driveEncoder.zero();
    }

    @Override
    public double getPivotMotorEncoder() {
        return pivotEncoder.get();
    }

    @Override
    public void zeroPivotEncoder() {
        pivotEncoder.zero();
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
        pivotMotor.set(pivotPid.getOutput(pivotEncoder.get(), ref));
    }

    @Override
    public void setPivotPidP(double gain) {
        pivotPid.setP(gain);
    }

    @Override
    public void setPivotPidI(double gain) {
        pivotPid.setI(gain);
    }

    @Override
    public void setPivotPidD(double gain) {
        pivotPid.setD(gain);
    }

    @Override
    public void setPivotPidIZone(double iZone) {
        throw new SwerveImplementationException(pivotPid.getClass().getName() + " does not support setting IZone.");
    }

    @Override
    public void setPivotPidFF(double gain) {
        pivotPid.setF(gain);
    }

    @Override
    public void setPivotPidOutputLimits(double min, double max) {
        pivotPid.setOutputLimits(min, max);
    }
}