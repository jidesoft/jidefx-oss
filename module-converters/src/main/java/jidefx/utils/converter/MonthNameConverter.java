/*
 * @(#)MonthNameConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An ObjectConverter that can convert between month names and an integer.
 */
public class MonthNameConverter extends DefaultObjectConverter<Integer> {

    /**
     * Default ConverterContext for MonthConverter.
     */
    public static final ConverterContext CONTEXT = new ConverterContext("MonthName"); //NON-NLS

    /**
     * 0 -&gt; "1", 1 -&gt; "2", ..., 11 -&gt; "12"
     */
    public static final DateFormat CONCISE_FORMAT = new SimpleDateFormat("M");

    /**
     * 0 -&gt; "01", 1 -&gt; "02", ..., 11 -&gt; "12"
     */
    public static final DateFormat SHORT_FORMAT = new SimpleDateFormat("MM");

    /**
     * 0 -&gt; "Jan", 1 -&gt; "Feb", ..., 11 -&gt; "Dec"
     */
    public static final DateFormat MEDIUM_FORMAT = new SimpleDateFormat("MMM");

    /**
     * 0 -&gt; "January", 1 -&gt; "February", ..., 11 -&gt; "December"
     */
    public static final DateFormat LONG_FORMAT = new SimpleDateFormat("MMMMM");

    private DateFormat _defaultDateFormat = MEDIUM_FORMAT;

    /**
     * Creates a new CalendarConverter.
     */
    public MonthNameConverter() {
    }

    /**
     * Converts the integer to the month names. It uses the default DateFormat from the {@link #getDefaultDateFormat()}
     * which is "MMM" by default.
     *
     * @param value   the integer value of the month.
     * @param context the converter context.
     * @return the month name.
     */
    @Override
    public String toString(Integer value, ConverterContext context) {
        if (value == null) {
            return "";
        }
        else {
            return getDefaultDateFormat().format((getCalendarByMonth(value).getTime()));
        }
    }

    static final Calendar CAL = Calendar.getInstance();

    static {
        CAL.set(Calendar.DAY_OF_MONTH, 1);
    }

    private static Calendar getCalendarByMonth(int month) {
        CAL.set(Calendar.MONTH, month);
        return CAL;
    }

    /**
     * Converts the month name to the int value. If you ever set a default one using {@link
     * #setDefaultDateFormat(java.text.DateFormat)}, that will be the first DateFormat to be tried. If failed, in order
     * to ensure the conversion is success, it will try more DateFormats in the following order: "MM", "MMM", "MMMMM",
     * "M".
     *
     * @param string  the string
     * @param context the converter context.
     * @return the int month value.
     */
    @Override
    public Integer fromString(String string, ConverterContext context) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date time = getDefaultDateFormat().parse(string);
            calendar.setTime(time);
        }
        catch (ParseException e1) { // if current formatter doesn't work try those default ones.
            try {
                Date time = MEDIUM_FORMAT.parse(string);
                calendar.setTime(time);
            }
            catch (ParseException e2) {
                try {
                    Date time = SHORT_FORMAT.parse(string);
                    calendar.setTime(time);
                }
                catch (ParseException e3) {
                    try {
                        Date time = LONG_FORMAT.parse(string);
                        calendar.setTime(time);
                    }
                    catch (ParseException e4) {
                        try {
                            Date time = CONCISE_FORMAT.parse(string);
                            calendar.setTime(time);
                        }
                        catch (ParseException e5) {
                            return null;  // nothing works just return null so that old value will be kept.
                        }
                    }
                }
            }
        }
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Gets default format to format a month.
     *
     * @return DefaultFormat
     */
    public DateFormat getDefaultDateFormat() {
        return _defaultDateFormat;
    }

    /**
     * Sets default format to format a month. Default is {@link #MEDIUM_FORMAT}.
     *
     * @param defaultDateFormat the default format to format the month.
     */
    public void setDefaultDateFormat(DateFormat defaultDateFormat) {
        _defaultDateFormat = defaultDateFormat;
    }

}
