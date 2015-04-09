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

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

/**
 * A special verifier for the Calendar field that has an integer type. It knows how to verify the value for a Calendar
 * field based on its actual maximum and minimum values.
 */
public class IntegerTemporalPatternVerifierNotAdjustable extends TemporalPatternVerifier implements PatternVerifier.Length,
        PatternVerifier.Formatter<Integer>, PatternVerifier.Parser<Integer>, PatternVerifier.Value<Temporal, Integer> {
    private boolean _fixedLength = false;
    private final DateTimeFormatter _formatter;

    public IntegerTemporalPatternVerifierNotAdjustable(TemporalField field, TemporalUnit unit, String pattern) {
        this(field, unit, pattern, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }

    public IntegerTemporalPatternVerifierNotAdjustable(TemporalField field, TemporalUnit unit, String pattern, boolean fixedLength) {
        this(field, unit, pattern, Integer.MIN_VALUE, Integer.MAX_VALUE, fixedLength);
    }

    public IntegerTemporalPatternVerifierNotAdjustable(TemporalField field, TemporalUnit unit, String pattern, long min, long max) {
        this(field, unit, pattern, min, max, false);
    }

    public IntegerTemporalPatternVerifierNotAdjustable(TemporalField field, TemporalUnit unit, String pattern, long min, long max, boolean fixedLength) {
        super(field, unit, min, max);
        _fixedLength = fixedLength;
        _formatter = DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.SMART);
    }

    @Override
    public void setFieldValue(Temporal fieldValue) {
        if (fieldValue != null) {
            _value = fieldValue;
        }
    }

    @Override
    public Temporal getFieldValue() {
        return _value;
    }

    @Override
    public Integer toTargetValue(Temporal fieldValue) {
        return fieldValue.get(_temporalField);
    }

    @Override
    public Temporal fromTargetValue(Temporal existingValue, Integer value) {
        existingValue.with(_temporalField, value);
        return existingValue;
    }

    @Override
    public int getMinLength() {
        return _fixedLength ? getMaxLength() : 0;
    }

    @Override
    public int getMaxLength() {
        return format(getMax().intValue()).length();
    }

    @Override
    public String format(Integer value) {
        if (value == null) return null;
        return _formatter.format(getFieldValue().with(getTemporalField(), value));
    }

    @Override
    public Integer parse(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        return _formatter.parse(text).get(getTemporalField());
    }

    @Override
    public Boolean call(String text) {
        if (text.length() > getMaxLength()) return false;
        try {
            long i = parse(text);
            if (i >= getMin() && i <= getMax()) return true;
        }
        catch (DateTimeException e) {
            CommonUtils.ignoreException(e);
        }
        return false;
    }
}
