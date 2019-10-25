package net.bancino.robotics.swerveio.encoder;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import net.bancino.robotics.swerveio.SwerveImplementationException;

public class SparkMaxEncoder implements Encoder {

    CANEncoder encoder;

    public SparkMaxEncoder(CANSparkMax sparkMax) {
        setController(sparkMax);
    }

    public SparkMaxEncoder() {

    }

    public void setController(CANSparkMax sparkMax) {
        if (sparkMax == null) {
            throw new IllegalArgumentException("Spark Max cannot be null.");
        }
        encoder = sparkMax.getEncoder();
        if (encoder == null) {
            throw new UnsupportedOperationException("No encoder attached to Spark Max.");
        }
    }

    @Override
    public double get() {
        if (encoder != null) {
            return encoder.getPosition();
        } else {
            throw new UnsupportedOperationException("This encoder is not monitoring a motor controller.");
        }
    }

    @Override
    public void set(double val) throws SwerveImplementationException {
        if (encoder != null) {
            encoder.setPosition(val);
        } else {
            throw new UnsupportedOperationException("This encoder is not monitoring a motor controller.");
        }
    }
}