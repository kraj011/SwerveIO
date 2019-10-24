package net.bancino.robotics.swerveio;

import net.bancino.robotics.swerveio.module.AbstractSwerveModule;

/**
 * A functional interface to modify the modules of a swerve drive. An
 * implementation is passed to the SwerveDrive class and it will call modify()
 * on each module. You can therefore place your specific settings in an
 * implementation of this interface and have the swerve drive automatically
 * apply it to your modules. Our team uses lambda expressions to set all the PID
 * settings right in the constructor of SwerveDrive.
 * 
 * @author Jordan Bancino
 */
@FunctionalInterface
public interface ModuleModifier {
    /**
     * Apply some modifications to a swerve module.
     * 
     * @param module The module to modify. In Java, modules are passed by reference,
     *               so nothing needs to be returned.
     */
    public void modify(AbstractSwerveModule module);
}