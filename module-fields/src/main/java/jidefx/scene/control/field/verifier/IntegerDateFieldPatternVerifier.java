/*
 * @(#)IntegerDateFieldPatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

import jidefx.utils.CommonUtils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * A special verifier for the Calendar field that has an integer type. It knows how to verify the value for a Calendar
 * field based on its actual maximum and minimum values.
 */
public class IntegerDateFieldPatternVerifier extends DateFieldPatternVerifier implements PatternVerifier.Length, PatternVerifier.Formatter<Integer>, PatternVerifier.Parser<Integer> {
    private final boolean fixedLength;

    public IntegerDateFieldPatternVerifier(int field, boolean fixedLength) {
        super(field);
        this.fixedLength = fixedLength;
    }

    public IntegerDateFieldPatternVerifier(int field, int min, int max, boolean fixedLength) {
        super(field, min, max);
        this.fixedLength = fixedLength;
    }

    @Override
    public int getMinLength() {
        return fixedLength ? ("" + getMax()).length() : 0;
    }

    @Override
    public int getMaxLength() {
        return ("" + getMax()).length();
    }

    @Override
    public String format(Integer value) {
        if (value == null) return null;

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumIntegerDigits(getMaxLength());
        format.setMinimumIntegerDigits(getMinLength());
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(false);
        return format.format(value);
    }

    @Override
    public Integer parse(String text) {
        if (text == null || text.trim().isEmpty()) return null;

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumIntegerDigits(getMaxLength());
        format.setMinimumIntegerDigits(getMinLength());
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(false);

        try {
            format.parse(text).intValue(); // parse to trigger exception.
            return Integer.parseInt(text);
        }
        catch (ParseException e) {
            throw new NumberFormatException(e.getLocalizedMessage());
        }
    }

    @Override
    public Boolean call(String text) {
        if (text.length() > getMaxLength()) return false;
        try {
            int i = parse(text);
            if (i >= getMin() && i <= getMax()) return true;
        }
        catch (NumberFormatException e) {
            CommonUtils.ignoreException(e);
        }
        return false;
    }
}
