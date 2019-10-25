package net.bancino.robotics.swerveio;

import java.util.HashMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.function.ModuleModifier;

/**
 * A class designed to be extended extended and used as a WPILib subsystem. This
 * takes care of consolidating swerve modules so they can be driven as a system
 * easily.
 * 
 * @author Jordan Bancino
 */
public abstract class SwerveDrive extends Subsystem {
    /**
     * A module map that contains all the swerve modules in this swerve drive. For
     * internal use only, this can be used to pull specific modules, and also
     * iterate over all the modules.
     */
    protected final HashMap<SwerveModule, AbstractSwerveModule> moduleMap = new HashMap<SwerveModule, AbstractSwerveModule>();

    /**
     * A calculator is provided by default so that calculations can be easily
     * retrieved. If invalid base dimensions are provide, the default of a 1:1 base
     * is used.
     */
    protected final SwerveDriveCalculator calc;

    protected final double countsPerPivotRevolution;

    /**
     * Create the swerve drive with the base dimensions and the modules.
     * 
     * @param baseWidth                The width of the swerve drive base. Used for
     *                                 trig.
     * @param baseLength               The length of the swerve drive base. Used for
     *                                 trig.
     * @param countsPerPivotRevolution The number of counts it takes to rotate the
     *                                 module a full 360 degrees.
     * @param frontLeftModule          The front left swerve module.
     * @param frontRightModule         The front right swerve module.
     * @param rearLeftModule           The rear left swerve module.
     * @param rearRightModule          the rear right swerve module.
     * @param modifier                  The modifier that will apply settings to each
     *                                 passed module.
     */
    public SwerveDrive(double baseWidth, double baseLength, double countsPerPivotRevolution,
            AbstractSwerveModule frontLeftModule, AbstractSwerveModule frontRightModule,
            AbstractSwerveModule rearLeftModule, AbstractSwerveModule rearRightModule, ModuleModifier modifier) {
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
            if (modifier != null) {
                for (var module : moduleMap.values()) {
                    modifier.modify(module);
                }
            }
            if (baseWidth <= 0 || baseLength <= 0) {
                this.calc = new SwerveDriveCalculator();
            } else {
                this.calc = new SwerveDriveCalculator(baseWidth, baseLength);
            }
            this.countsPerPivotRevolution = countsPerPivotRevolution;
        }
    }

    /**
     * A compatibility constructor that allows a raw module map to be passed to the
     * Swerve Drive
     * 
     * @param baseWidth                The width of the base
     * @param baseLength               The length of the base
     * @param countsPerPivotRevolution The number of counts it takes to rotate the
     *                                 module a full 360 degrees.
     * @param moduleMap                A raw module map
     * @param modifier                 The modifier that will apply settings to each
     *                                 passed module.
     */
    public SwerveDrive(double baseWidth, double baseLength, double countsPerPivotRevolution,
            HashMap<SwerveModule, AbstractSwerveModule> moduleMap, ModuleModifier modifier) {
        this(baseWidth, baseLength, countsPerPivotRevolution, moduleMap.get(SwerveModule.FRONT_LEFT),
                moduleMap.get(SwerveModule.FRONT_RIGHT), moduleMap.get(SwerveModule.REAR_LEFT),
                moduleMap.get(SwerveModule.REAR_RIGHT), modifier);
    }

    /**
     * A drive function that should be implemented to drive the robot with the
     * joystick.
     * 
     * @param fwd       The Y value
     * @param str       The X value
     * @param rcw       The Z value
     * @param gyroAngle The angle of the gyro, used for field centric navigation.
     * @throws SwerveImplementationException If there is an error with the
     *                                       implementation of any swerve module.
     */
    public void drive(double fwd, double str, double rcw, double gyroAngle) throws SwerveImplementationException {
        /**
         * This default implementation has been tested using MK2 modules and works
         * really well for both the internal encoders and the analog encoders.
         * 
         * Iterate over the modules.
         */
        for (SwerveModule module : moduleMap.keySet()) {
            /* Make sure a null module isn't operated on. */
            if (module != null) {
                /* Use the swerve drive calculator to calculate target speeds and angles. */
                double speed = calc.getWheelSpeed(module, fwd, str, rcw);
                double targetAngle = calc.getWheelAngle(module, fwd, str, rcw, gyroAngle);

                /* Get a reference to the module to get feedback from it. */
                AbstractSwerveModule swerveModule = moduleMap.get(module);
                double currentPos = swerveModule.getPivotMotorEncoder();

                /* Convert the target angle into a target position on the pivot encoder. */
                double targetPos = SwerveDriveCalculator.convertFromDegrees(targetAngle, countsPerPivotRevolution);
                /* Calculate the distance between the current position and the pivot target. */
                double distance = (targetPos - (currentPos % countsPerPivotRevolution));
                /*
                 * If the distance between the target and the current position is longer than
                 * half a revolution, pivot the other way for efficiency.
                 */
                if (distance > (countsPerPivotRevolution / 2.0) || distance < -(countsPerPivotRevolution / 2.0)) {
                    distance = countsPerPivotRevolution - Math.abs(distance);
                }
                /* Calculate the output pivot reference. */
                double pivotRef = currentPos + distance;

                /* Feed the pivot reference and drive motor speed to the module. */
                swerveModule.setPivotReference(pivotRef);
                swerveModule.setDriveMotorSpeed(speed);
            }
        }
    }

    /**
     * Drive in robot-centric navigation mode.
     * 
     * @param fwd The Y value
     * @param str The X value
     * @param rcw The Z value
     */
    public void drive(double fwd, double str, double rcw) {
        /*
         * Sending a gyro angle of 0 all the time forces the bot to always think it is
         * going forward, thus putting it into an effective bot-centric drive mode.
         */
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
     * Calls the reset function on all the modules, stopping each module and
     * resetting all the encoders
     */
    public void reset() {
        for (AbstractSwerveModule module : moduleMap.values()) {
            module.reset();
        }
    }

}