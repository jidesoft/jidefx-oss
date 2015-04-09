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

import jidefx.utils.CommonUtils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * A special verifier for the Calendar field that has an integer type. It knows how to verify the value for a Calendar
 * field based on its actual maximum and minimum values.
 */
public class IntegerCalendarFieldPatternVerifier extends CalendarFieldPatternVerifier implements PatternVerifier.Length, PatternVerifier.Formatter<Integer>, PatternVerifier.Parser<Integer> {
    private final boolean fixedLength;

    public IntegerCalendarFieldPatternVerifier(int field, boolean fixedLength) {
        super(field);
        this.fixedLength = fixedLength;
    }

    public IntegerCalendarFieldPatternVerifier(int field, int min, int max, boolean fixedLength) {
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
