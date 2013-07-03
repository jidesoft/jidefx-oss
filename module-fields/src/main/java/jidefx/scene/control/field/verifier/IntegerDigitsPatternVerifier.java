/*
 * @(#)IntegerDigitsPatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

/**
 * A pattern verifier that verifies the digits that is the integer digits (before the floating point) of the whole
 * number.
 */
public abstract class IntegerDigitsPatternVerifier<T> extends NumberValuePatternVerifier<T> {
    public IntegerDigitsPatternVerifier() {
        this(0, Integer.MAX_VALUE, 1, 1);
    }

    public IntegerDigitsPatternVerifier(int min, int max) {
        this(min, max, 1, 1);
    }

    public IntegerDigitsPatternVerifier(int min, int max, double valueMultiplier) {
        this(min, max, 1, valueMultiplier);
    }

    public IntegerDigitsPatternVerifier(int min, int max, double adjustmentMultiplier, double valueMultiplier) {
        super(min, max, adjustmentMultiplier, valueMultiplier);
    }

    @Override
    protected Number getGroupValue(double targetValue) {
        super.getGroupValue(targetValue);
        return Math.round(targetValue);
    }

    @Override
    protected double getTargetValue(Number groupValue) {
        double value = getMultipliedValue(targetValue.doubleValue());
        return getDemultipliedValue(Math.round(value - Math.round(value)) + groupValue.intValue());
    }
}
