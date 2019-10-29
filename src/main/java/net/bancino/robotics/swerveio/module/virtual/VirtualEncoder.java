package net.bancino.robotics.swerveio.module.virtual;

import net.bancino.robotics.swerveio.SwerveImplementationException;
import net.bancino.robotics.swerveio.encoder.Encoder;

public class VirtualEncoder implements Encoder {

    // TODO add implementation for calculating how far the encoder has turned based
    // off of the revolutionsPerTurn variable
    private double revolutionsPerTurn, currentPosition;

    public VirtualEncoder(double revolutionsPerTurn) {
        this.revolutionsPerTurn = revolutionsPerTurn;
    }

    public VirtualEncoder() {
        this.revolutionsPerTurn = 1;
    }

    @Override
    public double get() {
        return this.currentPosition;
    }

    @Override
    public void set(double val) throws SwerveImplementationException {
        this.currentPosition = val;
    }

}