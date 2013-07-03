/*
 * @(#)LocalTimeConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.time;

import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ObjectConverterManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;

public class LocalTimeConverter extends TemporalAccessConverter<LocalTime> {

    private static final DateTimeFormatter _defaultFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
    private static final DateTimeFormatter _shortFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static final DateTimeFormatter _mediumFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
    private static final DateTimeFormatter _longFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG);
    private static final DateTimeFormatter _fullFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL);


    /**
     * Creates a LocalTimeConverter.
     */
    public LocalTimeConverter() {
        setDefaultDateTimeFormatter(_defaultFormat);
    }

    /**
     * Converts from a String to a LocalTime.
     *
     * @param string  the string to be converted.
     * @param context the converter context.
     * @return the LocalTime. If the string is null or empty, null will be returned. If the string cannot be parsed as a
     *         date, null will be returned.
     */
    @Override
    synchronized public LocalTime fromString(String string, ConverterContext context) {
        TemporalAccessor temporalAccessor = temporalFromString(string, context);
        if (temporalAccessor != null) {
            return LocalTime.from(temporalAccessor);
        }
        else {
            return null;
        }
    }

    @Override
    protected DateTimeFormatter getFullDateTimeFormatter() {
        return _fullFormat;
    }

    @Override
    protected DateTimeFormatter getLongDateTimeFormatter() {
        return _longFormat;
    }

    @Override
    protected DateTimeFormatter getMediumDateTimeFormatter() {
        return _mediumFormat;
    }

    @Override
    protected DateTimeFormatter getShortDateTimeFormatter() {
        return _shortFormat;
    }

    public static void main(String[] args) {
        ObjectConverterManager.getInstance().getConverter(java.sql.Date.class);
    }
}
