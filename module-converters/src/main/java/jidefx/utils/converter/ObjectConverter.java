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


import javafx.util.StringConverter;

/**
 * An interface that can convert an object to a String and convert from String to an object.
 */
public interface ObjectConverter<T> {

    /**
     * Converts from object to String. Generally speaking, if the object is null, "" will be returned.
     *
     * @param object  object to be converted
     * @param context converter context to be used
     * @return the String
     */
    String toString(T object, ConverterContext context);

    /**
     * Converts from String to an object. Generally speaking, if the String is null or empty, null will be returned. It
     * we failed to convert the String to the specified data type, null will be returned too.
     *
     * @param string  the string
     * @param context context to be converted
     * @return the object converted from string, or null if we failed to convert the String to the specified data type.
     */
    T fromString(String string, ConverterContext context);

    /**
     * Creates a compatible StringConverter from ObjectConverter using the ConverterContext.DEFAULT_CONTEXT.
     *
     * @return a StringConverter.
     */
    StringConverter<T> toStringConverter();
}

