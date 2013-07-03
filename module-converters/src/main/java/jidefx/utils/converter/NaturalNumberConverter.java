/*
 * @(#)NaturalNumberConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * {@link ObjectConverter} implementation for a positive integer (a.k.a natural number)
 */
public class NaturalNumberConverter extends AbstractNumberConverter<Number> {
    /**
     * Default ConverterContext for NaturalNumberConverter.
     */
    public static final ConverterContext CONTEXT = new ConverterContext("NaturalNumber"); //NON-NLS

    public NaturalNumberConverter() {
        this(DecimalFormat.getIntegerInstance());
    }

    public NaturalNumberConverter(NumberFormat format) {
        super(format);
    }

    /**
     * Converts the String to a Byte. It will use the NumberFormat defined as {@link #PROPERTY_NUMBER_FORMAT} if any. If
     * not there, it will use {@link #getNumberFormat()} to get the NumberFormat to do the conversion.
     * <p>
     * Please note, the conversion may involve rounding or truncation. If the String represent a negative number, this
     * method will return 0 which is the smallest natural number.
     *
     * @param string  the string to be converted.
     * @param context the context
     * @return the Number converted from the String.
     */
    @Override
    public Number fromString(String string, ConverterContext context) {
        Number number = numberFromString(string, context);
        return number != null ? (number.intValue() < 0 ? 0 : number.intValue()) : null;
    }
}
