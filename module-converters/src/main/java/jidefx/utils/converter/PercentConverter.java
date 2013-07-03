/*
 * @(#)PercentConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * {@link ObjectConverter} implementation for a percentage
 */
public class PercentConverter extends DoubleConverter {

    public static final ConverterContext CONTEXT = new ConverterContext("Percent"); //NON-NLS

    public PercentConverter() {
        this(NumberFormat.getPercentInstance());
    }

    public PercentConverter(Locale locale) {
        this(NumberFormat.getPercentInstance(locale));
    }

    public PercentConverter(NumberFormat format) {
        super(format);
    }

    /**
     * Converts the String to a Double. It will use the NumberFormat defined as {@link #PROPERTY_NUMBER_FORMAT} if any.
     * If not there, it will use {@link #getNumberFormat()} to get the NumberFormat to do the conversion.
     * <p>
     * Different from a DoubleConverter, if the String doesn't have a percentage sign at the end, we will divide the
     * number from DoubleConverter by 100 and return the value. That's because "50%" actually is 0.5. If user forgot to
     * include a % sign, for example, pass in "50", we should still treat it as percentage and return 0.5 as they are
     * using a PercentConverter.
     * <p>
     * Please note, the conversion may involve rounding or truncation.
     *
     * @param string  the string to be converted.
     * @param context the context
     * @return the Number converted from the String.
     */
    @Override
    public Double fromString(String string, ConverterContext context) {
        Number number = numberFromString(string, context);
        if (number == null) {
            number = Double.parseDouble(string);
        }
        if (string != null && !string.trim().endsWith("%") && number != null) {
            number = number.doubleValue() / 100;
        }
        return number != null ? number.doubleValue() : null;
    }

}
