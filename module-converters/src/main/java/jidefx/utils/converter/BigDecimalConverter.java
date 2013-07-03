/*
 * @(#)BigDecimalConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigDecimalConverter extends AbstractNumberConverter<BigDecimal> {

    public BigDecimalConverter() {
        super(new DecimalFormat("#,##0.00"));
    }

    public BigDecimalConverter(NumberFormat format) {
        super(format);
    }

    @Override
    public BigDecimal fromString(String string, ConverterContext context) {
        Number value = numberFromString(string, context);
        if (value instanceof Double) {
            return new BigDecimal(string);
        }
        else if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }
        else if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        else if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        return null;
    }

    @Override
    public String toString(BigDecimal decimal, ConverterContext convertercontext) {
        if (Double.isNaN(decimal.doubleValue()))
            return "";
        return super.toString(decimal, convertercontext);
    }
}
