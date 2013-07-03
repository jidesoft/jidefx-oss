/*
 * @(#)DateConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.util.Date;

/**
 * {@link ObjectConverter} implementation for {@link Number}. The number is normally a long value of the number of
 * milliseconds since January 1, 1970, 00:00:00 GMT as in {@link java.util.Date#getTime()}
 */
public class NumberDateConverter extends AbstractDateConverter<Number> {

    /**
     * Creates a DateConverter.
     */
    public NumberDateConverter() {
    }

    /**
     * Converts from a String to a Number which is normally a long value of the number of milliseconds since January 1,
     * 1970, 00:00:00 GMT as in {@link java.util.Date#getTime()}. It will use the DateFormat defined as {@link
     * #PROPERTY_DATE_FORMAT} if any. Otherwise it will try different default DateFormat according to the context (Date,
     * Time or DateTime). At last it will try the following commonly used format patterns in order ("yyyy-mm-dd",
     * "yy-mm-dd", "yyyymmdd", "yymmdd", "dd-MMM-yy", "dd-MMM-yyyy") until it finds a match. We do that so that users
     * could type in other date formats and still could be recognized.
     *
     * @param string  the string to be converted.
     * @param context the context.
     * @return the Calendar. If the string is null or empty, null will be returned. If the string cannot be parsed as a
     *         date, the string itself will be returned.
     */
    @Override
    public Number fromString(String string, ConverterContext context) {
        Object date = super.fromStringToDate(string, context);
        if (date instanceof Date) {
            return ((Date) date).getTime();
        }
        else {
            return null;
        }
    }

    @Override
    public String toString(Number value, ConverterContext context) {
        return super.anyDateToString(value, context);
    }
}
