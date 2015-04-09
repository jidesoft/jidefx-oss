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
public class LongTemporalPatternVerifier extends TemporalPatternVerifier implements PatternVerifier.Length,
        PatternVerifier.Formatter<Long>, PatternVerifier.Parser<Long>, PatternVerifier.Value<Temporal, Long>,
        PatternVerifier.Adjustable<Long> {
    private boolean _fixedLength = false;
    private final DateTimeFormatter _formatter;

    public LongTemporalPatternVerifier(TemporalField field, TemporalUnit unit, String pattern) {
        this(field, unit, pattern, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }

    public LongTemporalPatternVerifier(TemporalField field, TemporalUnit unit, String pattern, boolean fixedLength) {
        this(field, unit, pattern, Integer.MIN_VALUE, Integer.MAX_VALUE, fixedLength);
    }

    public LongTemporalPatternVerifier(TemporalField field, TemporalUnit unit, String pattern, long min, long max) {
        this(field, unit, pattern, min, max, false);
    }

    public LongTemporalPatternVerifier(TemporalField field, TemporalUnit unit, String pattern, long min, long max, boolean fixedLength) {
        super(field, unit, min, max);
        _fixedLength = fixedLength;
        _formatter = DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.SMART);
    }

    @Override
    public Long getEnd(Long current) {
        if (_value != null) {
            _value = _value.with(_temporalField, getMax());
        }
        return null;
    }

    @Override
    public Long getHome(Long current) {
        if (_value != null) {
            _value = _value.with(_temporalField, getMin());
        }
        return null;
    }

    @Override
    public Long getPreviousPage(Long current, boolean restart) {
        if (_value != null) {
            _value = _value.minus(10, _temporalUnit);
        }
        return null;
    }

    @Override
    public Long getNextPage(Long current, boolean restart) {
        if (_value != null) {
            _value = _value.plus(10, _temporalUnit);
        }
        return null;
    }

    @Override
    public Long getNextValue(Long current, boolean restart) {
        if (_value != null) {
            _value = _value.plus(1, _temporalUnit);
        }
        return null;
    }

    @Override
    public Long getPreviousValue(Long current, boolean restart) {
        if (_value != null) {
            _value = _value.minus(1, _temporalUnit);
        }
        return null;
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
    public Long toTargetValue(Temporal fieldValue) {
        return fieldValue.getLong(_temporalField);
    }

    @Override
    public Temporal fromTargetValue(Temporal existingValue, Long value) {
        existingValue.with(_temporalField, value);
        return existingValue;
    }

    @Override
    public int getMinLength() {
        return _fixedLength ? getMaxLength() : 0;
    }

    @Override
    public int getMaxLength() {
        return format(getMax().longValue()).length();
    }

    @Override
    public String format(Long value) {
        if (value == null) return null;
        return _formatter.format(getFieldValue().with(getTemporalField(), value));
    }

    @Override
    public Long parse(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        return _formatter.parse(text).getLong(getTemporalField());
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
