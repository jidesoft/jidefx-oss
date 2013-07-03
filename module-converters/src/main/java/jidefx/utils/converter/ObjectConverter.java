/*
 * @(#)ObjectConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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

