/*
 * @(#)HexRangePatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

import jidefx.utils.CommonUtils;

/**
 * A special verifier for an integer so that it is in the specified range.
 */
public class HexRangePatternVerifier extends RangePatternVerifier<Integer> {
    public HexRangePatternVerifier(int min, int max) {
        super(min, max);
    }

    public HexRangePatternVerifier(int min, int max, boolean fixedLength) {
        super(min, max, fixedLength);
    }

    @Override
    public String format(Integer value) {
        if (value == null) return "";
        String s = Integer.toHexString(value);
        int maxLength = getMaxLength();
        if (_fixedLength && s.length() < maxLength) {
            for (int i = 0; i < maxLength - s.length(); i++) {
                s = "0" + s;
            }
        }
        return s;
    }

    @Override
    public Integer parse(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        return Integer.parseInt(text, 16);
    }

    @Override
    public Integer getNextPage(Integer current, boolean restart) {
        int value;
        if (current != null && current + 10 < getMax()) {
            value = current + 10;
        }
        else {
            value = restart ? getMin() : getMax();
        }
        return value;
    }

    @Override
    public Integer getPreviousPage(Integer current, boolean restart) {
        int value;
        if (current != null && current - 10 > getMin()) {
            value = current - 10;
        }
        else {
            value = restart ? getMax() : getMin();
        }
        return value;
    }

    @Override
    public Integer getHome(Integer current) {
        return getMin();
    }

    @Override
    public Integer getEnd(Integer current) {
        return getMax();
    }

    @Override
    public Integer getNextValue(Integer current, boolean restart) {
        int value;
        if (current != null && current < getMax()) {
            value = current + 1;
        }
        else {
            value = restart ? getMin() : getMax();
        }
        Boolean valid = call(format(value));
        return valid ? value : getNextValue(value, true);
    }

    @Override
    public Integer getPreviousValue(Integer current, boolean restart) {
        int value;
        if (current != null && current > getMin()) {
            value = current - 1;
        }
        else {
            value = restart ? getMax() : getMin();
        }
        Boolean valid = call(format(value));
        return valid ? value : getPreviousValue(value, true);
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

    @Override
    public int getMaxLength() {
        if (_maxLength == -1 && getMax() != null) {
            _maxLength = Integer.toHexString(getMax()).length();
        }
        return _maxLength;
    }

}
