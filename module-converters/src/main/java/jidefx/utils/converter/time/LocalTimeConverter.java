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
package jidefx.utils.converter.time;

import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ObjectConverterManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;

public class LocalTimeConverter extends TemporalAccessConverter<LocalTime> {

    private static final DateTimeFormatter _defaultFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
    private static final DateTimeFormatter _shortFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static final DateTimeFormatter _mediumFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
    private static final DateTimeFormatter _longFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG);
    private static final DateTimeFormatter _fullFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL);


    /**
     * Creates a LocalTimeConverter.
     */
    public LocalTimeConverter() {
        setDefaultDateTimeFormatter(_defaultFormat);
    }

    /**
     * Converts from a String to a LocalTime.
     *
     * @param string  the string to be converted.
     * @param context the converter context.
     * @return the LocalTime. If the string is null or empty, null will be returned. If the string cannot be parsed as a
     *         date, null will be returned.
     */
    @Override
    synchronized public LocalTime fromString(String string, ConverterContext context) {
        TemporalAccessor temporalAccessor = temporalFromString(string, context);
        if (temporalAccessor != null) {
            return LocalTime.from(temporalAccessor);
        }
        else {
            return null;
        }
    }

    @Override
    protected DateTimeFormatter getFullDateTimeFormatter() {
        return _fullFormat;
    }

    @Override
    protected DateTimeFormatter getLongDateTimeFormatter() {
        return _longFormat;
    }

    @Override
    protected DateTimeFormatter getMediumDateTimeFormatter() {
        return _mediumFormat;
    }

    @Override
    protected DateTimeFormatter getShortDateTimeFormatter() {
        return _shortFormat;
    }

    public static void main(String[] args) {
        ObjectConverterManager.getInstance().getConverter(java.sql.Date.class);
    }
}
