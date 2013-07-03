/*
 * @(#)DefaultObjectConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
