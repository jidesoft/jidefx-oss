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
 * Default object converter. It converts an object to a String using toString(). For fromString, it will most likely
 * return null unless the target type T is also String.
 *
 * @param <T> the data type of the value to be converted.
 */
public class DefaultObjectConverter<T> extends StringConverter<T> implements ObjectConverter<T> {

    public DefaultObjectConverter() {
    }

    /**
     * Converts the value to String. If the value is null, "" will be returned. Otherwise it will return
     * value.toString().
     *
     * @param value   the value to be concerted.
     * @param context converter context to be used
     * @return a String representation of the value.
     */
    @Override
    public String toString(T value, ConverterContext context) {
        return value == null ? "" : value.toString();
    }

    /**
     * Return null in most cases unless the generic type T is also a String.
     *
     * @param string  the string
     * @param context context to be converted
     * @return null or the string itself.
     */
    @Override
    public T fromString(String string, ConverterContext context) {
        try {
            return (T) string;
        }
        catch (Exception e) {
            return null;
        }
    }


    /**
     * Calls to {@link #toString(Object, ConverterContext)} with ConverterContext.CONTEXT_DEFAULT as the context.
     *
     * @param value the value
     * @return a String representation of the value.
     */
    @Override
    public String toString(T value) {
        return toString(value, ConverterContext.CONTEXT_DEFAULT);
    }

    /**
     * Calls to {@link #fromString(String, ConverterContext)} with ConverterContext.CONTEXT_DEFAULT as the context.
     *
     * @param string the string
     * @return a value converted from the string.
     */
    @Override
    public T fromString(String string) {
        return fromString(string, ConverterContext.CONTEXT_DEFAULT);
    }

    /**
     * Gets the ObjectConverterManager instance from the ConverterContext if any. Otherwise it will return the default
     * instance.
     *
     * @param context the converter context.
     * @return an instance of ObjectConverterManager.
     */
    protected ObjectConverterManager getObjectConverterManager(ConverterContext context) {
        return context.getProperties().get(ConverterContext.PROPERTY_OBJECT_CONVERTER_MANAGER) instanceof ObjectConverterManager ?
                ObjectConverterManager.getInstance() :
                (ObjectConverterManager) context.getProperties().get(ConverterContext.PROPERTY_OBJECT_CONVERTER_MANAGER);
    }

    /**
     * Return a {@link StringConverter} using the toString and fromString methods of ObjectConverter with
     * ConverterContext.CONTEXT_DEFAULT. If an ObjectConverter uses properties on the context, they will be ignored.
     *
     * @return a StringConverter.
     */
    @Override
    public StringConverter<T> toStringConverter() {
        return this;
    }
}
