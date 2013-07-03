/*
 * @(#)NumberValuePatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

/**
 * A pattern verifier that verifies the input as a number to make sure it is in the range. It also implements the Value
 * interface and left toTargetValue and fromTargetValue unimplemented.
 */
public abstract class NumberValuePatternVerifier<T> extends NumberRangePatternVerifier implements PatternVerifier.Value<T, Number> {
    /**
     * The multiplier between the target value and the group value. The target value * valueMultiplier = the group
     * value. Both min and max are for the group value. The adjustment is also done on the target value unless the
     * target value is null.
     */
    private double valueMultiplier = 1;

    protected T fieldValue;
    protected Number targetValue;

    public NumberValuePatternVerifier() {
        this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public NumberValuePatternVerifier(Number min, Number max) {
        super(min, max);
    }

    protected NumberValuePatternVerifier(Number min, Number max, double adjustmentMultiplier) {
        super(min, max, adjustmentMultiplier);
    }

    protected NumberValuePatternVerifier(Number min, Number max, double adjustmentMultiplier, double valueMultiplier) {
        super(min, max, adjustmentMultiplier);
        this.valueMultiplier = valueMultiplier;
    }

    protected double getMultipliedValue(double value) {
        return value * valueMultiplier;
    }

    protected double getDemultipliedValue(double value) {
        return value / valueMultiplier;
    }

    @Override
    protected Number getGroupValue(double targetValue) {
        this.targetValue = getDemultipliedValue(targetValue);
        return super.getGroupValue(targetValue);
    }

    @Override
    protected double getInitialValue(Number groupValue) {
        double newValue;
        if (targetValue != null) {
            newValue = targetValue.doubleValue();
        }
        else if (fieldValue != null) {
            newValue = toTargetValue(fieldValue).doubleValue();
        }
        else {
            newValue = super.getInitialValue(groupValue);
        }
        return getMultipliedValue(newValue);
    }

    @Override
    protected Number ensureLessThanMax(Number groupValue) {
        groupValue = super.ensureLessThanMax(groupValue);
        targetValue = getTargetValue(groupValue);
        fieldValue = fromTargetValue(fieldValue, targetValue);
        return null;
    }

    @Override
    protected Number ensureMoreThanMin(Number groupValue) {
        groupValue = super.ensureMoreThanMin(groupValue);
        targetValue = getTargetValue(groupValue);
        fieldValue = fromTargetValue(fieldValue, targetValue);
        return null;
    }

    @Override
    public void setFieldValue(T fieldValue) {
        this.fieldValue = fieldValue;
        targetValue = toTargetValue(fieldValue);
    }

    @Override
    public T getFieldValue() {
        return fieldValue;
    }
}
