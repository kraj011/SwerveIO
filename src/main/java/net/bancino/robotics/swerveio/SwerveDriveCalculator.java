package net.bancino.robotics.swerveio;

import java.awt.Rectangle;

import static java.lang.Math.*;

/**
 * A swerve drive calculator that performs all the calculations required to
 * obtain wheel angles and speeds. The proofs behind why this works is not
 * documented here, but there is plenty online. It is just a little
 * trigonometry.
 * 
 * @author Jordan Bancino
 */
public class SwerveDriveCalculator {
    private double baseWidth;
    private double baseLength;

    /**
     * Instantiate a calculator with the given base dimensions. Note that these
     * don't have to be exact measurements, they just need to be proportionate to
     * each other, because they're used in ratios. Because of this, it doesn't
     * matter whether imperical or metric measurements are taken.
     * 
     * @param baseWidth  The width of the drive base.
     * @param baseLength The length of the drive base.
     */
    public SwerveDriveCalculator(double baseWidth, double baseLength) {
        setBase(baseWidth, baseLength);
    }

    /**
     * Create the swerve drive calculator using a rectangle object for dimensions.
     * 
     * @param base The rectangle that represents the dimensions of the base.
     */
    public SwerveDriveCalculator(Rectangle base) {
        setBase(base.getWidth(), base.getHeight());
    }

    /**
     * Create a calculator with the default base ratio, which is 1.
     */
    public SwerveDriveCalculator() {
        this(1.0, 1.0);
    }

    /**
     * If your base magically decides to dynamically change widths at runtime, you
     * can set new widths here.
     * 
     * @param baseWidth The new base width to be used in future calculations.
     */
    public void setBaseWidth(double baseWidth) {
        this.baseWidth = baseWidth;
    }

    /**
     * Get the width of the drive base this calculator is using to perform its
     * calculations.
     * 
     * @return The base width.
     */
    public double getBaseWidth() {
        return baseWidth;
    }

    /**
     * If your base magically decides to dynamically change lengths at runtime, you
     * can set new lengths here.
     * 
     * @param baseLength The new base length to be used in future calculations.
     */
    public void setBaseLength(double baseLength) {
        this.baseLength = baseLength;
    }

    /**
     * Get the length of the drive base this calculator is using to perform its
     * calculations.
     * 
     * @return The base length.
     */
    public double getBaseLength() {
        return baseLength;
    }

    /**
     * Set the base dimensions. This is a convenience method for setBaseWidth() and
     * setBaseLength().
     * 
     * @param baseWidth  The width to set.
     * @param baseLength The length to set.
     */
    public void setBase(double baseWidth, double baseLength) {
        setBaseWidth(baseWidth);
        setBaseLength(baseLength);
    }

    /**
     * Get the wheel angle for the given degrees.
     * 
     * @param module The wheel to get the angle of.
     * @param fwd    The Y degree (from an input device)
     * @param str    The X degree (from an input device)
     * @param rcw    The Z degree (from an input device)
     * @return The angle (in degrees) that the given wheel should be set to.
     */
    public double getWheelAngle(SwerveModule module, double fwd, double str, double rcw) {
        double[] tmp = getWheelConstants(module, fwd, str, rcw);
        return toDegrees(atan2(tmp[0], tmp[1]));
    }

    /**
     * Get the wheel angle for the given degrees. This is the field-centric method
     * that recalculates the values based on the gyro angle.
     * 
     * @param module    The wheel to get the angle of.
     * @param fwd       The Y degree (from an input device)
     * @param str       The X degree (from an input device)
     * @param rcw       The Z degree (from an input device)
     * @param gyroAngle The gyro angle (in degrees) measured from the zero position
     *                  (sraight down field)
     * @return The angle (in degrees) that the given wheel should be set to.
     */
    public double getWheelAngle(SwerveModule module, double fwd, double str, double rcw, double gyroAngle) {
        System.out.println("Gyro: " + gyroAngle);
        double cosAngle = cos(toRadians(gyroAngle));
        double sinAngle = sin(toRadians(gyroAngle));
        double modFwd = (fwd * cosAngle) + (str * sinAngle);
        double modStr = (fwd * sinAngle) + (str * cosAngle);
        return getWheelAngle(module, modFwd, modStr, rcw);
    }

    /**
     * Get the wheel speed for the given degrees.
     * 
     * @param module The wheel to get the speed of.
     * @param fwd    The Y degree (from an input device)
     * @param str    The X degree (from an input device)
     * @param rcw    The Z degree (from an input device)
     * @return The speed (scaled -1 to 1) that the wheel given wheel should be set
     *         to.
     */
    public double getWheelSpeed(SwerveModule module, double fwd, double str, double rcw) {
        double[] tmp = getWheelConstants(module, fwd, str, rcw);
        double wheelSpeed = sqrt(pow(tmp[0], 2) + pow(tmp[1], 2));
        return (wheelSpeed > 1f) ? 1f : wheelSpeed;
    }

    /**
     * Wheel constants are generated by using the degrees provided, these aren't
     * really "constants" exactly since they're caculated depending on the provided
     * degree values, but there's really no other name for them
     * 
     * @param module The wheel to fetch constants for.
     * @param fwd    The Y degree
     * @param str    The X degree
     * @param rcw    the Z degree
     * @return An array of floats containing the wheel constants. This array will
     *         always be of size 2.
     */
    private double[] getWheelConstants(SwerveModule module, double fwd, double str, double rcw) {
        /* Calculate the ratio constant */
        final double R = sqrt(pow(baseLength, 2) + pow(baseWidth, 2));
        /* Allocate the array */
        double tmp[] = new double[2];
        /*
         * The exact way these are calculated may seem arbitrary, but reading up on any
         * swerve guide, or looking at any diagram will show you exactly how these are
         * calcuated, it's just a little basic trig.
         */
        switch (module) {
        case FRONT_LEFT:
            tmp[0] = str + rcw * (baseLength / R);
            tmp[1] = fwd - rcw * (baseWidth / R);
            break;
        case FRONT_RIGHT:
            tmp[0] = str + rcw * (baseLength / R);
            tmp[1] = fwd + rcw * (baseWidth / R);
            break;
        case REAR_RIGHT:
            tmp[0] = str - rcw * (baseLength / R);
            tmp[1] = fwd + rcw * (baseWidth / R);
            break;
        case REAR_LEFT:
            tmp[0] = str - rcw * (baseLength / R);
            tmp[1] = fwd - rcw * (baseWidth / R);
            break;
        }
        return tmp;
    }

    /**
     * Convert an encoder reading to degrees in terms of 360. This is useful for
     * finding the current angle at which a module is pivoted.
     * 
     * @param currentEncoderCount The count you wish to find the angle for.
     * @param countsPerRevolution How many counts are in a revolution.
     * @return The angle (in degrees) that the current encoder count represents in
     *         correlation to the total counts per revolution.
     */
    public static double convertToDegrees(double currentEncoderCount, double countsPerRevolution) {
        return (360 / countsPerRevolution) * currentEncoderCount;
    }

    /**
     * Convert an angle measure into an encoder count. This useful for finding the
     * encoder position to go to for a given angle.
     * 
     * @param degreeMeasure       The angle measure in degrees to find the count
     *                            for.
     * @param countsPerRevolution How many counts are in a revolution.
     * @return The location on the encoder that will put the motor at that angle.
     */
    public static double convertFromDegrees(double degreeMeasure, double countsPerRevolution) {
        return (countsPerRevolution / 360) * degreeMeasure;
    }
}
