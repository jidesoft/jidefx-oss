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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * {@link jidefx.utils.converter.ObjectConverter} implementation for {@link java.util.Date}, {@link java.util.Calendar}
 * or {@link Number}. All of aforementioned data types can be accepted by the toString method. For the fromString
 * method, it will only return {@link java.util.Date}. Its subclasses might return Calendar or Number.
 */
abstract public class AbstractDateConverter<T> extends DefaultObjectConverter<T> {

    /**
     * A converter context to tell the DateConverter using a DateFormat from SimpleDateFormat.getDateTimeInstance to do
     * the conversion.
     */
    public static final ConverterContext CONTEXT_DATETIME = new ConverterContext("DateTime"); //NON-NLS
    /**
     * A converter context to tell the DateConverter using a DateFormat from SimpleDateFormat.getTimeInstance to do the
     * conversion.
     */
    public static final ConverterContext CONTEXT_TIME = new ConverterContext("Time"); //NON-NLS
    /**
     * A converter context to tell the DateConverter using a DateFormat from SimpleDateFormat.getDateInstance to do the
     * conversion. It is the same as using CONTEXT_DEFAULT.
     */
    public static final ConverterContext CONTEXT_DATE = new ConverterContext("");

    /**
     * A property for the converter context. You can set a {@link java.text.DateFormat} to it and the converter will use
     * it to do the conversion.
     */
    public static final String PROPERTY_DATE_FORMAT = "DateFormat"; //NON-NLS

    private DateFormat _shortFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
    private DateFormat _mediumFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
    private DateFormat _longFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG);

    private DateFormat _defaultFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT);

    private DateFormat _shortDateTimeFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private DateFormat _mediumDateTimeFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
    private DateFormat _longDateTimeFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

    private DateFormat _defaultDateTimeFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);

    private DateFormat _shortTimeFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
    private DateFormat _mediumTimeFormat = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM);
    private DateFormat _longTimeFormat = SimpleDateFormat.getTimeInstance(DateFormat.LONG);

    private DateFormat _defaultTimeFormat = SimpleDateFormat.getTimeInstance(DateFormat.DEFAULT);


    /**
     * Creates a DateConverter.
     */
    public AbstractDateConverter() {
    }

    /**
     * Converts the object to String. The object can be a Calendar, a Date or a Number. As long as the DateFormat can
     * format it correctly, it will be converted to a String. If the object is already a String, we will return it
     * directly as it is.
     *
     * @param object  the object to be converted
     * @param context the converter context.
     * @return the string
     */
    synchronized public String anyDateToString(Object object, ConverterContext context) {
        if (object == null) {
            return "";
        }

        TimeZone timeZone;
        if (object instanceof Calendar) {
            timeZone = ((Calendar) object).getTimeZone();
            object = ((Calendar) object).getTime();
        }
        else if (object instanceof Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(((Date) object));
            timeZone = cal.getTimeZone();
        }
        else {
            timeZone = TimeZone.getDefault();
        }

        if (object instanceof Date || object instanceof Number) {
            Object format = context != null ? context.getProperties().get(PROPERTY_DATE_FORMAT) : null;
            if (format instanceof DateFormat) {
                return ((DateFormat) format).format(object);
            }
            else if (CONTEXT_DATETIME.equals(context)) {
                _defaultDateTimeFormat.setTimeZone(timeZone);
                return _defaultDateTimeFormat.format(object);
            }
            else if (CONTEXT_TIME.equals(context)) {
                _defaultTimeFormat.setTimeZone(timeZone);
                return _defaultTimeFormat.format(object);
            }
            else /* if (CONTEXT_DATE.equals(context)) */ {
                _defaultFormat.setTimeZone(timeZone);
                return _defaultFormat.format(object);
            }
        }
        else if (object instanceof String) {
            return (String) object;
        }
        else {
            return null;
        }
    }

    /**
     * Converts from a String to a Date. It will use the DateFormat defined as {@link #PROPERTY_DATE_FORMAT} if any.
     * Otherwise it will try different default DateFormat according to the context (Date, Time or DateTime). At last it
     * will try the following commonly used format patterns in order of "M/d/yyyy", "MM/dd/yyyy", "yyyy-MM-dd",
     * "yy-MM-dd", "yyyyMMdd", "yyMMdd", "dd-MMM-yy", "dd-MMM-yyyy", until it finds a match. We do that so that users
     * could type in other date formats and still could be recognized.
     *
     * @param string  the string to be converted.
     * @param context the context.
     * @return the Date. If the string is null or empty, null will be returned. If the string cannot be parsed as a
     *         date, the string itself will be returned.
     */
    synchronized public Object fromStringToDate(String string, ConverterContext context) {
        if (string == null) {
            return null;
        }

        string = string.trim();
        if (string.length() == 0) {
            return null;
        }

        try {
            Object format = context != null ? context.getProperties().get(PROPERTY_DATE_FORMAT) : null;
            if (format instanceof DateFormat) {
                try {
                    return ((DateFormat) format).parse(string);
                }
                catch (ParseException e) {
                    // ignore
                }
            }

            if (CONTEXT_DATETIME.equals(context)) {
                return _defaultDateTimeFormat.parse(string);
            }
            else if (CONTEXT_TIME.equals(context)) {
                return _defaultTimeFormat.parse(string);
            }
            else /* if (CONTEXT_DATE.equals(context)) */ {
                return _defaultFormat.parse(string);
            }
        }
        catch (ParseException e1) { // if current formatter doesn't work try those default ones.
            if (CONTEXT_DATETIME.equals(context)) {
                try {
                    return _shortDateTimeFormat.parse(string);
                }
                catch (ParseException e2) {
                    try {
                        return _mediumDateTimeFormat.parse(string);
                    }
                    catch (ParseException e3) {
                        try {
                            return _longDateTimeFormat.parse(string);
                        }
                        catch (ParseException e4) {
                            // null
                        }
                    }
                }
            }
            else if (CONTEXT_TIME.equals(context)) {
                try {
                    return _shortTimeFormat.parse(string);
                }
                catch (ParseException e2) {
                    try {
                        return _mediumTimeFormat.parse(string);
                    }
                    catch (ParseException e3) {
                        try {
                            return _longTimeFormat.parse(string);
                        }
                        catch (ParseException e4) {
                            return string;  // nothing works just return null so that old value will be kept.
                        }
                    }
                }
            }
            else /* if (CONTEXT_DATE.equals(context)) */ {
                try {
                    return _shortFormat.parse(string);
                }
                catch (ParseException e2) {
                    try {
                        return _mediumFormat.parse(string);
                    }
                    catch (ParseException e3) {
                        try {
                            return _longFormat.parse(string);
                        }
                        catch (ParseException e4) {
                            // null
                        }
                    }
                }
            }
        }

        // try other default formats
        String[] formatStrings = {"M/d/yyyy", "MM/dd/yyyy", "yyyy-MM-dd", "yy-MM-dd", "yyyyMMdd", "yyMMdd", "dd-MMM-yy", "dd-MMM-yyyy"}; //NON-NLS
        SimpleDateFormat sdf;
        for (String formatString : formatStrings) {
            try {
                sdf = new SimpleDateFormat(formatString);
                return sdf.parse(string);
            }
            catch (ParseException ex) {
                // break;
            }
        }
        return null;  // nothing works just return null so that old value will be kept.
    }

    /**
     * Gets the default format for date. This is used only when context is {@link #CONTEXT_DATE}.
     *
     * @return the default format for date.
     */
    public DateFormat getDefaultDateFormat() {
        return _defaultFormat;
    }

    /**
     * Sets the default format to format date. This is used only when context is {@link #CONTEXT_DATE}.
     *
     * @param defaultDateFormat the new default format for date.
     */
    public void setDefaultDateFormat(DateFormat defaultDateFormat) {
        _defaultFormat = defaultDateFormat;
    }

    /**
     * Gets the default format for time. This is used only when context is {@link #CONTEXT_TIME}.
     *
     * @return the default format for time.
     */
    public DateFormat getDefaultTimeFormat() {
        return _defaultTimeFormat;
    }

    /**
     * Sets the default format to format time. This is used only when context is {@link #CONTEXT_TIME}.
     *
     * @param defaultTimeFormat the new default format for time.
     */
    public void setDefaultTimeFormat(DateFormat defaultTimeFormat) {
        _defaultTimeFormat = defaultTimeFormat;
    }

    /**
     * Gets the default format for date/time. This is used only when context is {@link #CONTEXT_DATETIME}.
     *
     * @return the default format for date/time.
     */
    public DateFormat getDefaultDateTimeFormat() {
        return _defaultDateTimeFormat;
    }

    /**
     * Sets the default format to format date/time. This is used only when context is {@link #CONTEXT_DATETIME}.
     *
     * @param defaultDateTimeFormat the new default format for date/time.
     */
    public void setDefaultDateTimeFormat(DateFormat defaultDateTimeFormat) {
        _defaultDateTimeFormat = defaultDateTimeFormat;
    }
}

