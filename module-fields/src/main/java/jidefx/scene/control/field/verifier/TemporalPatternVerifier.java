/*
 * @(#)TemporalPatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
