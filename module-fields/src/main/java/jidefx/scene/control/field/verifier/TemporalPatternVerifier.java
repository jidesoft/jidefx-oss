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

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

/**
 * {@code BaseCalendarFieldPatternVerifier} is an abstract implementation of the pattern verifier for the Date
 * type. It can verify a Calendar field.
 */
public abstract class TemporalPatternVerifier extends PatternVerifier<Temporal> implements PatternVerifier.Range<Long> {
    protected TemporalField _temporalField;
    protected TemporalUnit _temporalUnit;
    protected Temporal _value;
    private final long _min;
    private final long _max;
    private boolean _minMaxSet = true;

    public TemporalPatternVerifier(TemporalField temporalField, TemporalUnit temporalUnit) {
        this(temporalField, temporalUnit, Integer.MIN_VALUE, Integer.MAX_VALUE);
        _minMaxSet = false;
    }

    TemporalPatternVerifier(TemporalField temporalField, TemporalUnit temporalUnit, long min, long max) {
        _temporalField = temporalField;
        _temporalUnit = temporalUnit;
        _min = min;
        _max = max;
        _minMaxSet = _max != Integer.MAX_VALUE && _min != Integer.MIN_VALUE;
        _value = LocalDate.now();
    }

    public TemporalField getTemporalField() {
        return _temporalField;
    }

    @Override
    public Long getMin() {
        return !_minMaxSet && _value != null ? _value.range(_temporalField).getLargestMinimum() : _min;
    }

    @Override
    public Long getMax() {
        return !_minMaxSet && _value != null ? _value.range(_temporalField).getSmallestMaximum() : _max;
    }
}
