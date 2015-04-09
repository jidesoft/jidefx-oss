/*
 * Copyright (c) 2002-2015, JIDE Software Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jidefx.scene.control.field.verifier;

import javafx.util.StringConverter;
import jidefx.utils.CommonUtils;

/**
 * A pattern verifier that verifies the input as a number to make sure it is in the range.
 * <p>
 * The adjustment value in the getNextXxx and getPreviousXxx can be changed using the adjustmentMultiplier.
 */
public class NumberRangePatternVerifier extends RangePatternVerifier<Number> {
    /**
     * The multiplier that will be used to multiply the adjustment. Say if the adjustmentMultiplier is 100, then
     * getNextValue will increase by 100 instead 1.
     */
    protected double adjustmentMultiplier = 1;

    public NumberRangePatternVerifier(Number min, Number max) {
        super(min, max);
    }

    public NumberRangePatternVerifier(Number min, Number max, double adjustmentMultiplier) {
        super(min, max);
        this.adjustmentMultiplier = adjustmentMultiplier;
    }

    protected double getAdjustment(double adjustment) {
        return adjustmentMultiplier * adjustment;
    }

    /**
     * Gets a default value for the current group value.
     *
     * @return the default value for the current group.
     */
    protected Number getDefaultValue() {
        return null;
    }

    @Override
    public String format(Number value) {
        StringConverter<Number> converter = getStringConverter();
        if (converter != null) {
            return converter.toString(value);
        }
        else {
            if (value == null) return "";
            return value.toString();
        }
    }

    @Override
    public Number parse(String text) {
        StringConverter<Number> converter = getStringConverter();
        if (converter != null) {
            return converter.fromString(text);
        }
        else {
            if (text == null || text.trim().isEmpty()) return null;
            return Double.valueOf(text);
        }
    }

    protected Number getGroupValue(double targetValue) {
        return targetValue;
    }

    protected double getTargetValue(Number groupValue) {
        return groupValue.doubleValue();
    }

    protected double getInitialValue(Number groupValue) {
        if (groupValue != null) {
            return groupValue.doubleValue();
        }
        else if (getDefaultValue() != null) {
            return getDefaultValue().doubleValue();
        }
        return 0.0;
    }

    protected Number ensureLessThanMax(Number groupValue) {
        if (groupValue.doubleValue() > getMax().doubleValue()) {
            groupValue = getMax().doubleValue();
        }
        return groupValue;
    }

    protected Number ensureMoreThanMin(Number groupValue) {
        if (groupValue.doubleValue() < getMin().doubleValue()) {
            groupValue = getMin().doubleValue();
        }
        return groupValue;
    }

    @Override
    public Number getNextValue(Number current, boolean restart) {
        double newValue = getInitialValue(current);
        double value = newValue + getAdjustment(1);
        return ensureLessThanMax(getGroupValue(value));
    }

    @Override
    public Number getPreviousValue(Number current, boolean restart) {
        double newValue = getInitialValue(current);
        double value = newValue + getAdjustment(-1);
        return ensureMoreThanMin(getGroupValue(value));
    }

    @Override
    public Number getNextPage(Number current, boolean restart) {
        double newValue = getInitialValue(current);
        double value = newValue + getAdjustment(10);
        return ensureLessThanMax(getGroupValue(value));
    }

    @Override
    public Number getPreviousPage(Number current, boolean restart) {
        double newValue = getInitialValue(current);
        double value = newValue + getAdjustment(-10);
        return ensureMoreThanMin(getGroupValue(value));
    }

    @Override
    public Number getHome(Number current) {
        return ensureMoreThanMin(getMin().doubleValue());
    }

    @Override
    public Number getEnd(Number current) {
        return ensureMoreThanMin(getMax().doubleValue());
    }

    @Override
    public Boolean call(String text) {
        if (text.length() > getMaxLength()) return false;
        try {
            Number i = parse(text);
            if (i.doubleValue() >= getMin().doubleValue() && i.doubleValue() <= getMax().doubleValue()) return true;
        }
        catch (NumberFormatException e) {
            CommonUtils.ignoreException(e);
        }
        catch (RuntimeException e) {
            CommonUtils.ignoreException(e);
        }
        return false;
    }
}
