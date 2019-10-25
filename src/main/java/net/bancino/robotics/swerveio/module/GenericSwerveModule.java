package net.bancino.robotics.swerveio.module;

import edu.wpi.first.wpilibj.SpeedController;
import net.bancino.robotics.swerveio.SwerveImplementationException;
import net.bancino.robotics.swerveio.encoder.Encoder;
import net.bancino.robotics.swerveio.pid.MiniPID;

public abstract class GenericSwerveModule implements AbstractSwerveModule {

    private SpeedController driveMotor, pivotMotor;
    private Encoder pivotEncoder;
    private MiniPID pivotPid = new MiniPID(0, 0, 0);

    public GenericSwerveModule(SpeedController driveMotor, SpeedController pivotMotor, Encoder pivotEncoder) {
        if (driveMotor == null) {
            throw new IllegalArgumentException("Drive motor must not be null.");
        } else if (pivotMotor == null) {
            throw new IllegalArgumentException("Pivot motor must not be null.");
        } else if (pivotEncoder == null) {
            throw new IllegalArgumentException("Pivot encoder must not be null.");
        } else {
            this.driveMotor = driveMotor;
            this.pivotMotor = pivotMotor;
            this.pivotEncoder = pivotEncoder;
        }
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
}