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
package jidefx.utils.converter.time;

import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.DefaultObjectConverter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * Abstract implementations of {@link jidefx.utils.converter.ObjectConverter} for all the data types that extends {@link
 * TemporalAccessor}.
 */
abstract public class TemporalAccessConverter<T extends Temporal> extends DefaultObjectConverter<T> {
    /**
     * A property for the converter context. You can set a {@link java.time.format.DateTimeFormatter} to it and the
     * converter will use it to do the conversion.
     */
    public static final String PROPERTY_DATE_TIME_FORMATTER = "DateTimeFormatter"; //NON-NLS

    private DateTimeFormatter _defaultFormatter = null;

    /**
     * Creates a TemporalConverter.
     */
    public TemporalAccessConverter() {
    }

    /**
     * Converts the LocalDate to String.
     *
     * @param value   the LocalDate to be converted
     * @param context the converter context.
     * @return the string
     */
    @Override
    synchronized public String toString(T value, ConverterContext context) {
        if (value == null) {
            return "";
        }
        else {
            Object formatter = context != null ? context.getProperties().get(PROPERTY_DATE_TIME_FORMATTER) : null;
            if (formatter instanceof DateTimeFormatter) {
                return ((DateTimeFormatter) formatter).withLocale(Locale.getDefault()).format(value);
            }
            else {
                return getDefaultDateTimeFormatter().withLocale(Locale.getDefault()).format(value);
            }
        }
    }

    /**
     * Converts from a String to a TemporalAccess.
     *
     * @param string  the string to be converted.
     * @param context the converter context.
     * @return the TemporalAccess. If the string is null or empty, null will be returned. If the string cannot be parsed
     *         as a date, null will be returned.
     */
    synchronized public TemporalAccessor temporalFromString(String string, ConverterContext context) {
        if (string == null) {
            return null;
        }

        string = string.trim();
        if (string.length() == 0) {
            return null;
        }

        try {
            Object formatter = context != null ? context.getProperties().get(PROPERTY_DATE_TIME_FORMATTER) : null;
            if (formatter instanceof DateTimeFormatter) {
                return ((DateTimeFormatter) formatter).withLocale(Locale.getDefault()).parse(string);
            }
            else {
                return getDefaultDateTimeFormatter().withLocale(Locale.getDefault()).parse(string);
            }
        }
        catch (Exception e1) { // if current formatter doesn't work try those default ones.

            try {
                return getShortDateTimeFormatter().withLocale(Locale.getDefault()).parse(string);
            }
            catch (Exception e2) {
                try {
                    return getMediumDateTimeFormatter().withLocale(Locale.getDefault()).parse(string);
                }
                catch (Exception e3) {
                    try {
                        return getLongDateTimeFormatter().withLocale(Locale.getDefault()).parse(string);
                    }
                    catch (Exception e4) {
                        try {
                            return getFullDateTimeFormatter().withLocale(Locale.getDefault()).parse(string);
                        }
                        catch (Exception e5) {
                            // null
                        }
                    }
                }
            }
        }

        return null;
    }

    abstract protected DateTimeFormatter getFullDateTimeFormatter();

    abstract protected DateTimeFormatter getLongDateTimeFormatter();

    abstract protected DateTimeFormatter getMediumDateTimeFormatter();

    abstract protected DateTimeFormatter getShortDateTimeFormatter();

    /**
     * Gets the default DateTimeFormatter to format/parse the LocalDate.
     *
     * @return the default DateTimeFormatter
     */
    public DateTimeFormatter getDefaultDateTimeFormatter() {
        return _defaultFormatter;
    }

    /**
     * Sets the default DateTimeFormatter to format/parse the LocalDate.
     *
     * @param defaultFormat the default DateTimeFormatter
     */
    public void setDefaultDateTimeFormatter(DateTimeFormatter defaultFormat) {
        _defaultFormatter = defaultFormat;
    }
}

