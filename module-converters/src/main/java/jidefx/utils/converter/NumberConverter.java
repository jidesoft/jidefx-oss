/*
 * @(#)LongConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * {@link jidefx.utils.converter.ObjectConverter} implementation for {@link Number}.
 */
public class NumberConverter extends AbstractNumberConverter<Number> {

    public NumberConverter() {
        this(DecimalFormat.getIntegerInstance());
    }

    public NumberConverter(NumberFormat format) {
        super(format);
    }

    /**
     * Converts the String to a Long. It will use the NumberFormat defined as {@link #PROPERTY_NUMBER_FORMAT} if any. If
     * not there, it will use {@link #getNumberFormat()} to get the NumberFormat to do the conversion.
     * <p>
     * Please note, the conversion may involve rounding or truncation.
     *
     * @param string  the string to be converted.
     * @param context the context
     * @return the Number converted from the String.
     */
    @Override
    public Number fromString(String string, ConverterContext context) {
        return numberFromString(string, context);
    }
}

