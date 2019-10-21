package net.bancino.robotics.swerveio.pid;

/**
 * An extremely simple PI calculator that takes P and I values,
 * current and target values, and computes an output value. For
 * more specific implementations, this class can be extended.
 */
public class PICalculator {

    double p, i;

    /**
     * Construct this calculator with the given controlling params.
     * @param p The propotional value.
     * @param i An integral value.
     */
    public PICalculator(double p, double i) {
        this.p = p;
        this.i = i;
    }

    /**
     * Generate a reference using the PI values and 
     * @param sensor The current sensor value.
     * @param target The target value.
     * @return The reference.
     */
    public double get(double sensor, double target) {
        throw new UnsupportedOperationException("PICalculator.get() not implemented.");
    }
}