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

import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.List;

public abstract class PatternVerifier<T> implements Callback<String, Boolean> {
    private StringConverter<T> _stringConverter;

    public StringConverter<T> getStringConverter() {
        return _stringConverter;
    }

    public void setStringConverter(StringConverter<T> stringConverter) {
        _stringConverter = stringConverter;
    }

    /**
     * An interface that can be implemented by the pattern verifier if the verifier knows how to parse the text and
     * return the value for that group.
     *
     * @param <T> the data type of the value for the group
     */
    public interface Parser<T> {
        T parse(String text);
    }

    /**
     * An interface that can be implemented by the pattern verifier if the verifier knows how to format the value for
     * that group.
     *
     * @param <T> the data type of the value for the group
     */
    public interface Formatter<T> {
        String format(T value);
    }

    /**
     * An interface that can be implemented by the pattern verifier to limit the value of the text to be in the range.
     * The value doesn't have to be a number. It could be Strings in order where the first String is min and the last
     * String is max.
     *
     * @param <T> the data type of the value for the group
     */
    public interface Range<T> {
        T getMin();

        T getMax();
    }

    /**
     * An interface that can be implemented by the pattern verifier to limit the length of the text within the minLength
     * and the maxLength.
     */
    public interface Length {
        int getMinLength();

        int getMaxLength();
    }

    /**
     * An interface that can be implemented by the pattern verifier to support enum types or an array of values.
     * Typically the text in that group must be the getValues array.
     *
     * @param <T> the data type of the value for the group
     */
    public interface Enums<T> {
        List<T> getValues();
    }

    /**
     * An interface that can be implemented by the pattern verifier to support auto-completion.
     *
     */
    public interface AutoCompletion {
        String autoComplete(String text);
    }

    /**
     * An interface that can be implemented by the pattern verifier to support the increase or decrease of the values by
     * steps. Those methods (as the method order below) will be called when the up, down, page-up, page-down, ctrl+home
     * and ctrl+end keys are pressed, respectively.
     *
     * @param <T> the data type of the value for the group
     */
    public interface Adjustable<T> {
        T getPreviousValue(T current, boolean restart);

        T getNextValue(T current, boolean restart);

        T getPreviousPage(T current, boolean restart);

        T getNextPage(T current, boolean restart);

        T getHome(T current);

        T getEnd(T current);
    }

    /**
     * An interface that can be implemented by the pattern verifier to hold the value of the whole field. The verifier
     * can implement this method if the validation process needs the value.
     *
     * @param <T>  the data type of the FormattedTextField
     * @param <TV> the data type of the target value. The target value could be the same as the FormattedTextField,
     *             could be the same as the value of the group text, or the value of the several groups that are
     *             related.
     */
    public interface Value<T, TV> {
        void setFieldValue(T fieldValue);

        T getFieldValue();

        TV toTargetValue(T fieldValue);

        T fromTargetValue(T previousFieldValue, TV targetValue);
    }
}
