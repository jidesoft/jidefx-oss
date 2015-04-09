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
package jidefx.utils.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * {@link ObjectConverter} abstract implementation for Number. The fromString method is not implemented.
 *
 * @param <T> the data type of this converter.
 */
abstract public class AbstractNumberConverter<T extends Number> extends DefaultObjectConverter<T> {

    /**
     * A converter context to tell the NumberConverter to use fixed 2 digit fraction.
     */
    public static final ConverterContext CONTEXT_FIXED_1_DIGIT_FRACTION = new ConverterContext("Fixed1DigitFraction"); //NON-NLS
    /**
     * A converter context to tell the NumberConverter to use fixed 2 digit fraction.
     */
    public static final ConverterContext CONTEXT_FIXED_2_DIGIT_FRACTION = new ConverterContext("Fixed2DigitFraction"); //NON-NLS
    /**
     * A converter context to tell the NumberConverter to use fixed 2 digit fraction.
     */
    public static final ConverterContext CONTEXT_FIXED_4_DIGIT_FRACTION = new ConverterContext("Fixed4DigitFraction"); //NON-NLS
    /**
     * A property for the converter context. You can set a {@link NumberFormat} to it and the converter will use it to
     * do the conversion.
     */
    public static final String PROPERTY_NUMBER_FORMAT = "NumberFormat"; //NON-NLS

    private final Locale _locale;
    private final String _pattern;
    private final NumberFormat _numberFormat;

    public AbstractNumberConverter() {
        this(Locale.getDefault());
    }

    public AbstractNumberConverter(Locale locale) {
        this(locale, null);
    }

    public AbstractNumberConverter(String pattern) {
        this(Locale.getDefault(), pattern);
    }

    public AbstractNumberConverter(Locale locale, String pattern) {
        this(locale, pattern, null);
    }

    public AbstractNumberConverter(NumberFormat numberFormat) {
        this(null, null, numberFormat);
    }

    AbstractNumberConverter(Locale locale, String pattern, NumberFormat numberFormat) {
        _locale = locale;
        _pattern = pattern;
        _numberFormat = numberFormat;
    }

    /**
     * Returns a {@code NumberFormat} instance to use for formatting and parsing in this {@link
     * AbstractNumberConverter}.
     *
     * @return a NumberFormat.
     */
    protected NumberFormat getNumberFormat() {
        Locale locale = _locale == null ? Locale.getDefault() : _locale;

        if (_numberFormat != null) {
            return _numberFormat;
        }
        else if (_pattern != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
            return new DecimalFormat(_pattern, symbols);
        }
        else {
            return NumberFormat.getNumberInstance(locale);
        }
    }

    /**
     * Converts the String to a Number. It will use the NumberFormat defined as {@link #PROPERTY_NUMBER_FORMAT} if any.
     * If not there, it will use {@link #getNumberFormat()} to get the NumberFormat to do the conversion.
     *
     * @param string  the string to be converted.
     * @param context the context
     * @return the Number converted from the String.
     */
    public Number numberFromString(String string, ConverterContext context) {
        if (string == null) {
            return null;
        }

        string = string.trim();
        if (string.length() == 0) {
            return null;
        }

        Object format = context != null ? context.getProperties().get(PROPERTY_NUMBER_FORMAT) : null;
        if (format instanceof NumberFormat) {
            try {
                return ((NumberFormat) format).parse(string);
            }
            catch (Exception e) {
                // ignore here. we will use the default way to convert it below
            }
        }

        NumberFormat parser = getNumberFormat();
        try {
            return parser.parse(string);
        }
        catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String toString(T number, ConverterContext context) {
        // If the specified value is null, return a zero-length String
        if (number == null) {
            return "";
        }

        if (Double.isNaN(number.doubleValue())) {
            return "";
        }

        Object format = context != null ? context.getProperties().get(PROPERTY_NUMBER_FORMAT) : null;
        if (format instanceof NumberFormat) {
            try {
                return ((NumberFormat) format).format(number);
            }
            catch (Exception e) {
                // ignore here. we will use the default way to convert it below
            }
        }

        NumberFormat formatter = getNumberFormat();
        try {
            // Perform the requested formatting
            return formatter.format(number);
        }
        catch (Exception e) {
            return number.toString();
        }
    }
}

