/*
 * @(#)RangePatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

/**
 * A special verifier for an integer so that it is in the specified range.
 */
public abstract class RangePatternVerifier<T> extends PatternVerifier<T> implements PatternVerifier.Range<T>, PatternVerifier.Length,
        PatternVerifier.Adjustable<T>, PatternVerifier.Parser<T>, PatternVerifier.Formatter<T> {
    protected T _min;
    protected T _max;
    protected boolean _fixedLength = false;
    protected int _maxLength = -1;

    public RangePatternVerifier(T min, T max) {
        this(min, max, false);
    }

    public RangePatternVerifier(T min, T max, boolean fixedLength) {
        _min = min;
        _max = max;
        _fixedLength = fixedLength;
    }

    @Override
    public T getMin() {
        return _min;
    }

    @Override
    public T getMax() {
        return _max;
    }

    @Override
    public T getHome(T current) {
        return getMin();
    }

    @Override
    public T getEnd(T current) {
        return getMax();
    }

    @Override
    public int getMinLength() {
        return _fixedLength ? getMaxLength() : 0;
    }

    @Override
    public int getMaxLength() {
        if (_maxLength == -1 && getMax() != null) {
            _maxLength = format(getMax()).length();
        }
        return _maxLength;
    }
}
