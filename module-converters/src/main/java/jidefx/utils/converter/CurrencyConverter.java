/*
 * @(#)CurrencyConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * {@link ObjectConverter} implementation for the currency.
 */
public class CurrencyConverter extends DoubleConverter {

    public static final ConverterContext CONTEXT = new ConverterContext("Currency"); //NON-NLS

    public CurrencyConverter() {
        this(NumberFormat.getCurrencyInstance());
    }

    public CurrencyConverter(Locale locale) {
        this(NumberFormat.getCurrencyInstance(locale));
    }

    public CurrencyConverter(NumberFormat format) {
        super(format);
    }
}
