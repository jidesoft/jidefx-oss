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
