/*
 * @(#)LocalDateConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.time;

import jidefx.utils.converter.ConverterContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * {@link jidefx.utils.converter.ObjectConverter} for {@link LocalDate}.
 */
public class LocalDateConverter extends TemporalAccessConverter<LocalDate> {
    private static final DateTimeFormatter _defaultFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    private static final DateTimeFormatter _shortFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    private static final DateTimeFormatter _mediumFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    private static final DateTimeFormatter _longFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
    private static final DateTimeFormatter _fullFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);


    /**
     * Creates a DateConverter.
     */
    public LocalDateConverter() {
        setDefaultDateTimeFormatter(_defaultFormatter);
    }

    synchronized public LocalDate fromString(String string, String pattern) {
        return LocalDate.from(DateTimeFormatter.ofPattern(pattern).parse(string));
    }
    /**
     * Converts from a String to a LocalDate. It will try all the SHORT, MEDIUM, LONG, FULL format as defined in
     * FormatStyle for the date. If failed, it will further try the following format in order until it found a match:
     * "M/d/yyyy", "MM/dd/yyyy", "MMM d, yy", "MMM d, yyyy", "MMMMM d, yyyy", "yyyy-MM-dd", "yy-MM-dd", "yyyyMMdd",
     * "yyMMdd", "dd-MMM-yy", "d-MMM-yy", "dd-MMM-yyyy", "d-MMM-yyyy".
     *
     * @param string  the string to be converted.
     * @param context the converter context.
     * @return the LocalDate. If the string is null or empty, null will be returned. If the string cannot be parsed as a
     *         date, null will be returned.
     */
    @Override
    synchronized public LocalDate fromString(String string, ConverterContext context) {
        TemporalAccessor temporalAccessor = temporalFromString(string, context);
        if (temporalAccessor != null) {
            return LocalDate.from(temporalAccessor);
        }
        else {
            // try other default formats
            String[] formatStrings = {"M/d/yyyy", "MM/dd/yyyy", "MMM d, yy", "MMM d, yyyy", "MMMMM d, yyyy", "yyyy-MM-dd", "yy-MM-dd", "yyyyMMdd", "yyMMdd", "dd-MMM-yy", "d-MMM-yy", "dd-MMM-yyyy", "d-MMM-yyyy"}; //NON-NLS
            for (String formatString : formatStrings) {
                try {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatString);
                    return LocalDate.from(dtf.withLocale(Locale.getDefault()).parse(string));
                }
                catch (Exception ignored) {
                    // empty on purpose
                }
            }
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

