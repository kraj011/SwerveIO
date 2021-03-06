# SwerveIO

Pronounced "Swerve - ee - oh" (rhymes with oreo, we decided to keep this mis-pronunciation by one of our members because we thought it was funny and different), **SwerveIO** is another swerve drive library written in Java by Team 6090, just to see if we could.

**Notice**: This library is under _heavy_ developement and is nowhere near ready for actual use. Often, there are lots of breaking API changes. Additionally, this library is not published to any repositories other than this one, so you'll need to include it as a source dependency in gradle, or compile the JAR yourself to actually use it. This notice will be updated when SwerveIO is ready for production, and instructions will be provided on the best way to include it in your project.

## Features

- **Expandable**: A collection of interfaces allow the use of any motor controllers and encoders, with the option to use a combination of motor controllers and encoders.
- **Sensible Defaults**: SwerveIO provides built-in swerve module implementations for popular configurations, including REVRobotics Spark Max, and CTRE Talon motor controllers. If Team 6090 has experience with it, we will have an implementation for you. You're also more than welcome to add your own module implementations to our library via Github pull requests! Please make sure that you don't add custom modules, but if it's a kit, by all means, we want it!
- **Java**: Written in Java by Java developers, SwerveIO takes advantage of the Java language and follows all the conventions of Java libraries. This makes for seamless integration with your Java robotics projects.
- **Open**: All the classes used in this library are open and can be used independently if desired, such as `SwerveDriveCalculator` or the PID loop calculator (`MiniPID`).
- **Simple**: All the hard work is done beneath the abstraction layer of this library, all you need to do is pass encoders and motors in the form of modules to the library.

## Basic Usage

To include SwerveIO in your robot project, add this to your `settings.gradle`:

```groovy
sourceControl {
    gitRepository("https://github.com/Team6090/SwerveIO") {
        producesModule("net.bancino.robotics:SwerveIO")
    }
}
```

Then, add the dependency in `build.gradle`:

```groovy
dependencies {
  compile('net.bancino.robotics:SwerveIO') {
    version {
        branch = 'master'
    }
  }
}
```

If you are using the Gradle Wrapper and your IDE does not download the dependencies automatically, you may need to run the command `./gradlew build` inside the project directory. Otherwise, run `gradle build`. You may need to refresh your IDE or reload the build/classpath configuration.

This is, of course, temporary, and should only be used for developement. When we start the 2020 season, we should hopefully have this published so you can use it as a normal binary dependency.

---

This library provides a `SwerveDrive` class that extends WPILib's `Subsystem` class. You'll want your drivetrain subsystem to extend `SwerveDrive` which will then automatically inherit `Subsystem`. For our code, we generally follow this format:

```java
package frc.robot.subsystems;

import frc.robot.RobotMap;
import frc.robot.commands.DriveWithJoystick;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;

public class DriveTrain extends SwerveDrive {
  /**
   * Create the SwerveDrive with the default settings and the
   * robot map.
   */
  public DriveTrain() {
    /* Create the modules and pass them to the superclass constructor along with the base dimensions. */
    super(/* Base width: */ 20, /* Base length: */ 22, /* Counts per pivot revolution: */ 360,
      new MK2SwerveModule(RobotMap.FRONT_LEFT_DRIVE_MOTOR, RobotMap.FRONT_LEFT_PIVOT_MOTOR, RobotMap.FRONT_LEFT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.FRONT_RIGHT_DRIVE_MOTOR, RobotMap.FRONT_RIGHT_PIVOT_MOTOR, RobotMap.FRONT_RIGHT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.REAR_LEFT_DRIVE_MOTOR, RobotMap.REAR_LEFT_PIVOT_MOTOR, RobotMap.REAR_LEFT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.REAR_RIGHT_DRIVE_MOTOR, RobotMap.REAR_RIGHT_PIVOT_MOTOR, RobotMap.REAR_RIGHT_ANALOG_ENCODER),
      (module) -> {
        /**
         * Here we set the module's settings. Everything
         * in this block is run automatically on each module.
         */
        module.setPivotClosedLoopRampRate(0);
        module.setPivotPidP(0.1);
        module.setPivotPidI(1e-4);
        module.setPivotPidD(1);
        module.setPivotPidIZone(0);
        module.setPivotPidFF(0);
      }
    );
  }

  /**
   * Set your startup command here.
   */
  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }
}
```

This creates all the modules and passes them to the superclass, which has a default implementation of the `drive()` function responsible for handing everything. To drive this swerve drive, just pass the joysticks Y, X and Z values in for `drive()`s FWD, STR, and RCW parameters respectively. This is of course very bare-bones, but this will get the job done. Optionally pass a gyro angle in as the fourth parameter for field centric navigation. See how to do all of this in the next code block below.

If your swerve module does not have a default implementation, just write one that extends the `GenericSwerveModule` class. This will be the most basic way to do it, but advanced programmers may want to directly implement `AbstractSwerveModule`. See the `MK2SwerveModule` class for inspiration.

As you can see, to create a fully functioning swerve drive subsystem, you just need to extend the `SwerveDrive` class, and know these values:

- The base width
- The base length
- How many counts on the encoder it takes to go one full revolution. This will usually be done by manually twisting a module and watching the counts.

In the example above, you'll need to implement the `DriveWithJoystick` command sort of like this:

```java
package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Subsystems;

public class DriveWithJoystick extends Command {
  /**
   * Create the command, requiring the drivetrain to drive
   * and the gyro to provide field-centric navigation.
   */
  public DriveWithJoystick() {
    /* Use requires() to require both the drivetrain and the gyro
     * (if using field-centric navigation)
     */
  }

  @Override
  protected void initialize() {
    /*
     * Perform initializtion here
     */
  }

  @Override
  protected void execute() {
    double x = 0; /* Get from joystick*/
    double y = 0; /* Get from joystick*/
    double z = 0; /* Get from joystick*/
    double gyroAngle = 0; /* Get from gyro */
    /**
     * Drive the drivetrain using the axes from the joystick and the gyro
     * angle.
     *
     * You'll obviously need to declare an instance of the swerve drive somewhere, then call
     * this function on it:
     *
     * drive(y, x, z, gyroAngle);
     */
  }

  /**
   * Usually you won't end this command on it's own,
   * it will require an interruption
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * Stop moving when the joystick is no longer in control.
   * Generally, autonomous commands will take over from here.
   */
  @Override
  protected void end() {
    /* Stop the drivetrain here. */
  }

  @Override
  protected void interrupted() {
    end();
  }
}

```

Obviously you'll need to modify the above command structure a little bit, but this is basically how to implement a fully functioning swerve drive using SwerveIO.
