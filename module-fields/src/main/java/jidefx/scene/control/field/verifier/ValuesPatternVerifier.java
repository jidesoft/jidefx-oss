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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A special verifier that restricts the input string to be in a String array. The order of the string in the array will
 * be used to implement the getNextValue and getPreviousValue methods.
 */
public abstract class ValuesPatternVerifier<S, T> extends PatternVerifier<T> implements PatternVerifier.Adjustable<T>,
        PatternVerifier.Parser<T>, PatternVerifier.Formatter<T>, PatternVerifier.Range<T>, PatternVerifier.Value<S, T>, PatternVerifier.Enums<T> {
    private List<T> values;

    public ValuesPatternVerifier() {
    }

    public ValuesPatternVerifier(T[] values) {
        this.values = new ArrayList<>();
        Collections.addAll(getValues(), values);
    }

    @Override
    public T getMin() {
        List<T> values = getValues();
        return values.size() >= 1 ? values.get(0) : null;
    }

    @Override
    public T getMax() {
        List<T> values = getValues();
        return values.size() >= 1 ? values.get(values.size() - 1) : null;
    }

    @Override
    public T getNextPage(T current, boolean restart) {
        return null;
    }

    @Override
    public T getPreviousPage(T current, boolean restart) {
        return null;
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
    public T getNextValue(T current, boolean restart) {
        List<T> values = getValues();
        int index = values.indexOf(current);
        if (index != -1) {
            if (index + 1 < values.size())
                return values.get(index + 1);
            else {
                return restart ? values.get(0) : values.get(values.size() - 1);
            }
        }
        for (int i = 0; i < values.size(); i++) {
            T value = values.get(i);
            if (matches(current, value)) {
                if (i + 1 < values.size())
                    return values.get(i + 1);
                else {
                    return restart ? values.get(0) : values.get(values.size() - 1);
                }
            }
        }
        return current;
    }

    @Override
    public T getPreviousValue(T current, boolean restart) {
        List<T> values = getValues();
        int index = values.indexOf(current);
        if (index != -1) {
            if (index - 1 > 0)
                return values.get(index - 1);
            else {
                return restart ? values.get(values.size() - 1) : values.get(0);
            }
        }
        for (int i = 0; i < values.size(); i++) {
            T value = values.get(i);
            if (matches(current, value)) {
                if (i - 1 >= 0)
                    return values.get(i - 1);
                else {
                    return restart ? values.get(values.size() - 1) : values.get(0);
                }
            }
        }
        return current;
    }

    protected boolean matches(T current, T value) {
        return false;
    }

    @Override
    public Boolean call(String text) {
        return getValues().contains(parse(text));
    }

    @Override
    public List<T> getValues() {
        if (values == null) {
            values = createValues();
        }
        return values;
    }

    protected List<T> createValues() {
        return null;
    }

    public void invalidate() {
        values = null;
    }

    private S value;

    @Override
    public void setFieldValue(S fieldValue) {
        value = fieldValue;
    }

    @Override
    public S getFieldValue() {
        return value;
    }
}
