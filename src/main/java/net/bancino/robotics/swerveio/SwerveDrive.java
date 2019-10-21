package net.bancino.robotics.swerveio;

import java.util.HashMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;

/**
 * A class designed to be extended extended and used as a WPILib
 * subsystem. This takes care of consolidating swerve modules
 * so they can be driven as a system easily.
 * @author Jordan Bancino
 */
public abstract class SwerveDrive extends Subsystem {
    /**
     * A module map that contains all the swerve modules in this
     * swerve drive. For internal use only, this can be used to pull
     * specific modules, and also iterate over all the modules.
     */
    protected final HashMap<SwerveModule, AbstractSwerveModule> moduleMap = new HashMap<SwerveModule, AbstractSwerveModule>();
    
    /**
     * A calculator is provided by default so that calculations can be
     * easily retrieved. If invalid base dimensions are provide, the
     * default of a 1:1 base is used.
     */
    protected final SwerveDriveCalculator calc;

    protected double countsPerPivotRevolution = 360;

    /**
     * Create the swerve drive with the base dimensions and the modules.
     */
    public SwerveDrive(double baseWidth, double baseLength, AbstractSwerveModule frontLeftModule, AbstractSwerveModule frontRightModule, AbstractSwerveModule rearLeftModule, AbstractSwerveModule rearRightModule) {
        StringBuilder nullModule = new StringBuilder("The following modules are null: [");
        boolean haveNullModule = false;
        if (frontRightModule == null) {
            nullModule.append(" FrontRight");
            haveNullModule = true;
        }
        if (frontLeftModule == null) {
            nullModule.append(" FrontLeft");
            haveNullModule = true;
        }
        if (rearLeftModule == null) {
            nullModule.append(" RearLeft");
            haveNullModule = true;
        }
        if (rearRightModule == null) {
            nullModule.append(" RearRight");
            haveNullModule = true;
        }
        if (haveNullModule) {
            nullModule.append("] Please provide an implemented swerve module for these parameters.");
            throw new SwerveImplementationException(nullModule.toString());
        } else {
            moduleMap.put(SwerveModule.FRONT_LEFT, frontLeftModule);
            moduleMap.put(SwerveModule.FRONT_RIGHT, frontRightModule);
            moduleMap.put(SwerveModule.REAR_LEFT, rearLeftModule);
            moduleMap.put(SwerveModule.REAR_RIGHT, rearRightModule);
            if (baseWidth <= 0 || baseLength <= 0) {
                this.calc = new SwerveDriveCalculator();
            } else {
                this.calc = new SwerveDriveCalculator(baseWidth, baseLength);
            }
        }
    }

    /**
     * A compatibility constructor that allows a raw module map to be passed to the
     * Swerve Drive
     * @param baseWidth The width of the base
     * @param baseLength The length of the base
     * @param moduleMap A raw module map
     */
    public SwerveDrive(double baseWidth, double baseLength, HashMap<SwerveModule, AbstractSwerveModule> moduleMap) {
        this(baseWidth, baseLength, moduleMap.get(SwerveModule.FRONT_LEFT), moduleMap.get(SwerveModule.FRONT_RIGHT), moduleMap.get(SwerveModule.REAR_LEFT), moduleMap.get(SwerveModule.REAR_RIGHT));
    }

    /**
     * A drive function that should be implemented to drive the robot
     * with the joystick.
     * @param fwd The Y value
     * @param str The X value
     * @param rcw The Z value
     * @param gyroAngle The angle of the gyro, used for field centric navigation.
     * @throws SwerveImplementationException If there is an error with the implementation of 
     * any swerve module.
     */
    public void drive(double fwd, double str, double rcw, double gyroAngle) throws SwerveImplementationException {
        for (SwerveModule module : moduleMap.keySet()) {
            if (module != null) {
                double speed = calc.getWheelSpeed(module, fwd, str, rcw);
                double targetAngle = calc.getWheelAngle(module, fwd, str, rcw, gyroAngle);

                AbstractSwerveModule swerveModule = moduleMap.get(module);
                double currentPos = swerveModule.getPivotMotorEncoder();
                double targetPos = SwerveDriveCalculator.convertFromDegrees(targetAngle, countsPerPivotRevolution);
                double distance = (targetPos - (currentPos % countsPerPivotRevolution));
                if (distance > (countsPerPivotRevolution / 2.0) || distance < -(countsPerPivotRevolution / 2.0)) {
                    distance = countsPerPivotRevolution - Math.abs(distance);
                }
                double pivotRef = currentPos + distance;

                swerveModule.setPivotReference(pivotRef);
                swerveModule.setDriveMotorSpeed(speed);
            }
        }
    }

    /**
     * Drive in robot-centric navigation mode.
     * @param fwd The Y value
     * @param str The X value
     * @param rcw The Z value
     */
    public void drive(double fwd, double str, double rcw) {
        drive(fwd, str, rcw, 0);
    }

    /**
     * Stop all the modules, stopping this swerve drive.
     */
    public void stop() {
        for (AbstractSwerveModule module : moduleMap.values()) {
            module.stop();
        }
    }

    /**
     * Zero all the pivot and drive encoders in this swerve drive.
     */
    public void zero() {
        for (AbstractSwerveModule module : moduleMap.values()) {
            module.zero();
        }
    }

    /**
     * Calls the reset function on all the modules, stopping each
     * module and resetting all the encoders
     */
    public void reset() {
        for (AbstractSwerveModule module : moduleMap.values()) {
            module.reset();
        }
    }
    
}