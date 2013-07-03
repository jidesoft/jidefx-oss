/*
 * @(#)LocalDateConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.time;

import jidefx.utils.converter.ConverterContext;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * {@link jidefx.utils.converter.ObjectConverter} for {@link java.time.YearMonth}.
 */
public class YearMonthConverter extends TemporalAccessConverter<YearMonth> {
    private static final DateTimeFormatter _defaultFormatter = DateTimeFormatter.ofPattern("MM/yy"); //NON-NLS
    private static final DateTimeFormatter _shortFormat = DateTimeFormatter.ofPattern("MMyy"); //NON-NLS
    private static final DateTimeFormatter _mediumFormat = DateTimeFormatter.ofPattern("MM/yy"); //NON-NLS
    private static final DateTimeFormatter _longFormat = DateTimeFormatter.ofPattern("MMM, yyyy"); //NON-NLS
    private static final DateTimeFormatter _fullFormat = DateTimeFormatter.ofPattern("MMMMM, yyyy"); //NON-NLS


    /**
     * Creates a YearMonthConverter.
     */
    public YearMonthConverter() {
        setDefaultDateTimeFormatter(_defaultFormatter);
    }

    /**
     * Converts from a String to a YearMonth.
     *
     * @param string  the string to be converted.
     * @param context the converter context.
     * @return the YearMonth. If the string is null or empty, null will be returned. If the string cannot be parsed as a
     *         date, null will be returned.
     */
    @Override
    synchronized public YearMonth fromString(String string, ConverterContext context) {
        TemporalAccessor temporalAccessor = temporalFromString(string, context);
        if (temporalAccessor != null) {
            return YearMonth.from(temporalAccessor);
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

