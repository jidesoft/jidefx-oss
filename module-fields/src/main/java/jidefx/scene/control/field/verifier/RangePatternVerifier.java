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
