/*
 * @(#)FractionDigitsPatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

/**
 * A pattern verifier that verifies the digits that is the fraction digits (after the floating point) of the whole
 * number.
 */
public abstract class FractionDigitsPatternVerifier<T> extends NumberValuePatternVerifier<T> {
    public FractionDigitsPatternVerifier(int length) {
        this(length, 1);
    }

    public FractionDigitsPatternVerifier(int length, double valueMultiplier) {
        super(0, (int) Math.pow(10, length) - 1, 1.0 / Math.pow(10, length), valueMultiplier);
        if (length <= 0) {
            throw new IllegalArgumentException("The length must be greater than 1");
        }
    }

    @Override
    protected Number getGroupValue(double targetValue) {
        super.getGroupValue(targetValue);
        double value = targetValue - Math.floor(targetValue);
        return Math.round(value / adjustmentMultiplier);
    }

    @Override
    protected double getTargetValue(Number groupValue) {
        double value = getMultipliedValue(targetValue.doubleValue());
        return getDemultipliedValue(Math.floor(value) + groupValue.intValue() * adjustmentMultiplier);
    }
}
