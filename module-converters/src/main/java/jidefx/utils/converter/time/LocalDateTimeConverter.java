/*
 * @(#)LocalDateTimeConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.time;

import jidefx.utils.converter.ConverterContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;

public class LocalDateTimeConverter extends TemporalAccessConverter<LocalDateTime> {
    private static final DateTimeFormatter _defaultFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    private static final DateTimeFormatter _shortFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
    private static final DateTimeFormatter _mediumFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    private static final DateTimeFormatter _longFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.LONG);
    private static final DateTimeFormatter _fullFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.FULL);


    /**
     * Creates a LocalDateTimeConverter.
     */
    public LocalDateTimeConverter() {
        setDefaultDateTimeFormatter(_defaultFormatter);
    }

    /**
     * Converts from a String to a LocalDateTime.
     *
     * @param string  the string to be converted.
     * @param context the converter context.
     * @return the LocalDateTime. If the string is null or empty, null will be returned. If the string cannot be parsed
     *         as a date, null will be returned.
     */
    @Override
    synchronized public LocalDateTime fromString(String string, ConverterContext context) {
        TemporalAccessor temporalAccessor = temporalFromString(string, context);
        if (temporalAccessor != null) {
            return LocalDateTime.from(temporalAccessor);
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
}

