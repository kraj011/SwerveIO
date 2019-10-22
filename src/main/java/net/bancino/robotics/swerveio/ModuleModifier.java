package net.bancino.robotics.swerveio;

import net.bancino.robotics.swerveio.module.AbstractSwerveModule;

@FunctionalInterface
public interface ModuleModifier {
    public void modify(AbstractSwerveModule module);
}