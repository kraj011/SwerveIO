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
package frc.robot.subsystems;

import frc.robot.RobotMap;
import frc.robot.commands.joystick.DriveWithJoystick;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule.EncoderSetting;

public class DriveTrain extends SwerveDrive {
  /**
   * Create the SwerveDrive with the default settings and the
   * robot map.
   */
  public DriveTrain() {
    /* Create the modules and pass them to the superclass constructor along with the base dimensions. */
    super(/* Base width: */ 20, /* Base length: */ 22,
      new MK2SwerveModule(RobotMap.FRONT_LEFT_DRIVE_MOTOR, RobotMap.FRONT_LEFT_PIVOT_MOTOR, RobotMap.FRONT_LEFT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.FRONT_RIGHT_DRIVE_MOTOR, RobotMap.FRONT_RIGHT_PIVOT_MOTOR, RobotMap.FRONT_RIGHT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.REAR_LEFT_DRIVE_MOTOR, RobotMap.REAR_LEFT_PIVOT_MOTOR, RobotMap.REAR_LEFT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.REAR_RIGHT_DRIVE_MOTOR, RobotMap.REAR_RIGHT_PIVOT_MOTOR, RobotMap.REAR_RIGHT_ANALOG_ENCODER)
    );
    /* 
     * This variable is inherited from SwerveDrive and is used in the default
     * drive() implementation, setting it will allow us to not write our own
     * drive() function, but you can write it if you want.
     */
    countsPerPivotRevolution = 17.90471839904785;
    /**
     * Here we set the module's settings. Set them for each module.
     * moduleMap is inherited from SwerveDrive and populated by
     * the superclass constructor.
     */
    for (var modKey : moduleMap.keySet()) {
      MultiEncoderModule module = (MultiEncoderModule) moduleMap.get(modKey);

      module.setPivotClosedLoopRampRate(0);
      module.setPivotPidP(0.1);
      module.setPivotPidI(1e-4);
      module.setPivotPidD(1);
      module.setPivotPidIZone(0);
      module.setPivotPidFF(0);
    }
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }
}

```
This creates all the modules and passes them to the superclass, which has a default implementation of the `drive()` function responsible for handing everything. To drive this swerve drive, just pass the joysticks Y, X and Z values in for `drive()`s FWD, STR, and RCW parameters respectively. This is of course very bare-bones, but this will get the job done. Optionally pass a gyro angle in as the fourth parameter for field centric navigation.

If your swerve module does not have a default implementation, just write one that implements the `AbstractSwerveModule` interface. 

As you can see, to create a fully functioning swerve drive subsystem, you just need to extend the `SwerveDrive` class, and know these values:
- The base width
- The base length
- How many counts on the encoder it takes to go one full revolution. This will usually be done by manually twisting a module and watching the counts.