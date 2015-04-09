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

import java.text.NumberFormat;

/**
 * {@link ObjectConverter} implementation for {@link Float}.
 */
public class FloatConverter extends AbstractNumberConverter<Float> {
    public FloatConverter() {
    }

    public FloatConverter(NumberFormat format) {
        super(format);
    }

    /**
     * Converts the String to a Float. It will use the NumberFormat defined as {@link #PROPERTY_NUMBER_FORMAT} if any.
     * If not there, it will use {@link #getNumberFormat()} to get the NumberFormat to do the conversion.
     * <p>
     * Please note, the conversion may involve rounding or truncation.
     *
     * @param string  the string to be converted.
     * @param context the context
     * @return the Number converted from the String.
     */
    @Override
    public Float fromString(String string, ConverterContext context) {
        Number number = numberFromString(string, context);
        return number != null ? number.floatValue() : null;
    }

}
