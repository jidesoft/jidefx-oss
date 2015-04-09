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

/**
 * {@link ObjectConverter} implementation for a year/month value.
 */
public class YearMonthConverter extends DefaultObjectConverter<Calendar> {
    /**
     * A property for the converter context. You can set a {@link java.text.DateFormat} to it and the converter will use
     * it to do the conversion.
     */
    public static final String PROPERTY_DATE_FORMAT = "DateFormat"; //NON-NLS

    /**
     * Default ConverterContext for MonthConverter.
     */
    public static final ConverterContext CONTEXT_YEAR_MONTH = new ConverterContext("YearMonth"); //NON-NLS

    private DateFormat _conciseFormat = new SimpleDateFormat("MMyy");
    private DateFormat _shortFormat = new SimpleDateFormat("MM/yy");
    private DateFormat _mediumFormat = new SimpleDateFormat("MM, yyyy");
    private DateFormat _longFormat = new SimpleDateFormat("MMMMM, yyyy");

    private DateFormat _defaultDateFormat = _shortFormat;

    /**
     * Creates a new CalendarConverter.
     */
    public YearMonthConverter() {
    }

    /**
     * It will convert the calendar to String. It will try to use the DateFormat set on {@link #PROPERTY_DATE_FORMAT} as
     * property. If not there, it will use the default DateFormat from {@link #getDefaultDateFormat()}.
     *
     * @param value   the value
     * @param context the converter context
     * @return the String representation of the Calendar.
     */
    @Override
    public String toString(Calendar value, ConverterContext context) {
        if (value == null) {
            return "";
        }
        else {
            Object format = context != null ? context.getProperties().get(PROPERTY_DATE_FORMAT) : null;
            if (format instanceof DateFormat) {
                return ((DateFormat) format).format(value);
            }
            return getDefaultDateFormat().format(value.getTime());
        }
    }

    /**
     * Converts string to a Calendar which has the year and month information. It will try to use the DateFormat set on
     * {@link #PROPERTY_DATE_FORMAT} as property. If not there, it will use some pre-defined formats. The formats it can
     * accept are "MMyy", "MM/yy", "MM, yyy", "MMMM, yyyy".
     *
     * @param string  the string
     * @param context the converter context
     * @return the Calendar which has the year and month information
     */
    @Override
    public Calendar fromString(String string, ConverterContext context) {
        Calendar calendar = Calendar.getInstance();
        try {

            Object format = context != null ? context.getProperties().get(PROPERTY_DATE_FORMAT) : null;
            if (format instanceof DateFormat) {
                Date time = ((DateFormat) format).parse(string);
                calendar.setTime(time);
            }
            else {
                Date time = getDefaultDateFormat().parse(string);
                calendar.setTime(time);
            }
        }
        catch (ParseException e1) { // if current formatter doesn't work try those default ones.
            try {
                Date time = _shortFormat.parse(string);
                calendar.setTime(time);
            }
            catch (ParseException e2) {
                try {
                    Date time = _mediumFormat.parse(string);
                    calendar.setTime(time);
                }
                catch (ParseException e3) {
                    try {
                        Date time = _longFormat.parse(string);
                        calendar.setTime(time);
                    }
                    catch (ParseException e4) {
                        try {
                            Date time = _conciseFormat.parse(string);
                            calendar.setTime(time);
                        }
                        catch (ParseException e5) {
                            return null;  // nothing works just return null so that old value will be kept.
                        }
                    }
                }
            }
        }
        return calendar;
    }

    /**
     * Gets the default DateFormat to format an calendar.
     *
     * @return the default DateFormat
     */
    public DateFormat getDefaultDateFormat() {
        return _defaultDateFormat;
    }

    /**
     * Sets the default DateFormat to format an calendar.
     *
     * @param defaultDateFormat the default DateFormat to format a calendar.
     */
    public void setDefaultDateFormat(DateFormat defaultDateFormat) {
        _defaultDateFormat = defaultDateFormat;
    }
}
