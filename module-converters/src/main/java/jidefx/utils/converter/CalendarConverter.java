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

import java.util.Calendar;
import java.util.Date;

/**
 * {@link ObjectConverter} implementation for {@link Date}, {@link Calendar} or {@link Number}. All of aforementioned
 * data types can be accepted by the toString method. For the fromString method, it will only return {@link Calendar}.
 */
public class CalendarConverter extends AbstractDateConverter<Calendar> {

    /**
     * Creates a new CalendarConverter.
     */
    public CalendarConverter() {
    }

    /**
     * Converts from a String to a Calendar. It will use the DateFormat defined as {@link #PROPERTY_DATE_FORMAT} if any.
     * Otherwise it will try different default DateFormat according to the context (Date, Time or DateTime). At last it
     * will try the following commonly used format patterns in order ("yyyy-mm-dd", "yy-mm-dd", "yyyymmdd", "yymmdd",
     * "dd-MMM-yy", "dd-MMM-yyyy") until it finds a match. We do that so that users could type in other date formats and
     * still could be recognized.
     *
     * @param string  the string to be converted.
     * @param context the context.
     * @return the Calendar. If the string is null or empty, null will be returned. If the string cannot be parsed as a
     *         date, the string itself will be returned.
     */
    @Override
    public Calendar fromString(String string, ConverterContext context) {
        Object date = super.fromStringToDate(string, context);
        if (date instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) date);
            return calendar;
        }
        else {
            return null;
        }
    }

    @Override
    public String toString(Calendar value, ConverterContext context) {
        return super.anyDateToString(value, context);
    }
}
