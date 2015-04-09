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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An ObjectConverter that can convert between quarter names and an integer. The quarter name is in the "Qty 1", "Qty 2"
 * etc. format which can be adjusted using the {@link #setQuarterNamePattern(String)} method. By fault, the "Qty" is
 * localized as long as there is a localized properties for that locale.
 */
public class QuarterNameConverter extends DefaultObjectConverter<Integer> {

    /**
     * Default ConverterContext for MonthConverter.
     */
    public static final ConverterContext CONTEXT = new ConverterContext("QuarterName"); //NON-NLS
    private String _quarterNamePattern;

    /**
     * Creates a new CalendarConverter.
     */
    public QuarterNameConverter() {
    }

    /**
     * Converts the quarter value to a String.
     *
     * @param qtr     the value from 1 to 4.
     * @param context the converter context
     * @return the String representing the quarter.
     */
    @Override
    public String toString(Integer qtr, ConverterContext context) {
        if (qtr == null) {
            return "";
        }
        else {
            if (qtr >= 0 && qtr < 4) {
                return MessageFormat.format(getQuarterNamePattern(), (qtr + 1));
            }
            else {
                return "";
            }
        }
    }

    /**
     * Converts from string to a int value.
     *
     * @param string  the string
     * @param context the converter context
     * @return an int value from 1 to 4.
     */
    @Override
    public Integer fromString(String string, ConverterContext context) {
        String quarterNamePattern = getQuarterNamePattern();
        try {
            Object[] values = new MessageFormat(quarterNamePattern).parse(string);
            if (values.length > 0) {
                return Integer.parseInt("" + values[0]) - 1;
            }
        }
        catch (ParseException e) {
            // ignore
        }
        return 0;
    }

    /**
     * Gets the quarter name pattern when converting from an int to a String. For example, if the int is 0, it will
     * converted to "Qtr 1" if the quarter name pattern is "Qtr {0}".
     *
     * @return the prefix.
     */
    public String getQuarterNamePattern() {
        if (_quarterNamePattern == null) {
            return getResourceString("Quarter.quarter");
        }
        return _quarterNamePattern;
    }

    /**
     * Sets the quarter name pattern. For example, if the int is 0, it will converted to "Qtr 1" if the pattern is "Qtr
     * {0}".
     *
     * @param quarterName the quarter name pattern
     */
    public void setQuarterNamePattern(String quarterName) {
        _quarterNamePattern = quarterName;
    }

    protected String getResourceString(String key) {
        final ResourceBundle resourceBundle = ConverterResource.getResourceBundle(Locale.getDefault());
        return resourceBundle.getString(key);
    }

}
