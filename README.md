# SwerveIO
Pronounced "Swerve - ee - oh" (rhymes with oreo, we decided to keep this mis-pronunciation by one of our members because we thought it was funny and different), **SwerveIO** is another swerve drive library written in Java by Team 6090, just to see if we could.

## Features
- **Extendable**: A collection of interfaces allow the use of any motor controllers and encoders, with the option to use a combination of motor controllers and encoders.
- **Sensible Defaults**: SwerveIO provides built-in swerve module implementations for popular configurations, including REVRobotics Spark Max, and CTRE Talon motor controllers.
- **Java**: Written in Java by Java developers, SwerveIO takes advantage of the Java language and follows all the conventions of Java libraries.
- **Open**: All the methods used in this library are open and can be used independently if desired, such as `SwerveDriveCalculator`. 
- **Simple**: All the hard work is done beneath the abstraction layer of this library, all you need to do is pass encoders and motors in the form of modules to the library.

## Basic Usage
This library provides a `SwerveDrive` class that extends WPILib's `Subsystem` class. You'll want your drivetrain subsystem to extend `SwerveDrive` which will then automatically inherit `Subsystem`. For our code, we generally follow this format:

```java
public class DriveTrain extends SwerveDrive {

    public static final DRIVETRAIN = new DriveTrain();

    /* Define constants that represent the dimensions of the base if it isn't a perfect square. */
    private static final double BASE_WIDTH = 0;
    private static final double BASE_LENGTH = 0;

    /**
     * Create a static variable that holds all your modules. This is where modules will be
     * instantiated. Make sure you use a valid AbstractSwerveModule implementation. This
     * example uses the built-in MK2SwerveModule implementation which is a NEO swerve module
     * designed by Swerve Drive Specialties.
     */
    private static final HashMap<SwerveModule, AbstractSwerveModule> modules = createModuleMap();
    private static HashMap<SwerveModule, AbstractSwerveModule> createModuleMap() {
        /* This map should be of size 4 and have one module for each corner of the drive. */
        HashMap<SwerveModule, AbstractSwerveModule> moduleMap = new HashMap<>();
        moduleMap.put(SwerveModule.FRONT_LEFT, new MK2SwerveModule(RobotMap.FRONT_LEFT_DRIVE_MOTOR, RobotMap.FRONT_LEFT_PIVOT_MOTOR, RobotMap.FRONT_LEFT_ANALOG_ENCODER));
        moduleMap.put(SwerveModule.FRONT_RIGHT, new MK2SwerveModule(RobotMap.FRONT_RIGHT_DRIVE_MOTOR, RobotMap.FRONT_RIGHT_PIVOT_MOTOR, RobotMap.FRONT_RIGHT_ANALOG_ENCODER));
        moduleMap.put(SwerveModule.REAR_LEFT, new MK2SwerveModule(RobotMap.REAR_LEFT_DRIVE_MOTOR, RobotMap.REAR_LEFT_PIVOT_MOTOR, RobotMap.REAR_LEFT_ANALOG_ENCODER));
        moduleMap.put(SwerveModule.REAR_RIGHT, new MK2SwerveModule(RobotMap.REAR_RIGHT_DRIVE_MOTOR, RobotMap.REAR_RIGHT_PIVOT_MOTOR, RobotMap.REAR_RIGHT_ANALOG_ENCODER));

        for (var module : moduleMap.values()) {
            /* Perform any module setup here, such as setting PID settings, etc... */
            module.zeroDriveEncoder();
            module.setPivotClosedLoopRampRate(0);
            module.setPivotPidP(0.1);
            module.setPivotPidI(1e-4);
            module.setPivotPidD(1);
            module.setPivotPidIZone(0);
        }
    }

    /* Construct the superclass using the custom parameters setup above. */
    public DriveTrain() {
        super(BASE_WIDTH, BASE_LENGTH, modules);
        countsPerPivotRevolution = 360; /* Or however many counts it takes to go one full pivot revolution. */
    }

    /* Initiate the drive command. */
    @Override
    protected void initDefaultCommand() {
       /* 
        * Here you'll want to put a command that allows you to drive the bot with a joystick. 
        * This can be done with the setDefaultCommand() function.
        */
    }
}
```
This creates all the modules and passes them to the superclass, which has a default implementation of the `drive()` function responsible for handing everything. To drive this swerve drive, just pass the joysticks Y, X and Z values in for `drive()`s FWD, STR, and RCW parameters respectively. This is of course very bare-bones, but this will get the job done. Optionally pass a gyro angle in as the fourth parameter for field centric navigation.

If your swerve module does not have a default implementation, just write one that implements the `AbstractSwerveModule` interface. 

As you can see, to create a fully functioning swerve drive subsystem, you just need to extend the `SwerveDrive` class, and know these values:
- The base width
- The base length
- How many counts on the encoder it takes to go one full revolution. This will usually be done by manually twisting a module and watching the counts.