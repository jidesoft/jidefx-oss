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

import javafx.geometry.Dimension2D;
import jidefx.utils.converter.javafx.Dimension2DConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * ArrayConverter is an abstract converter that converts between a list of values to a String with a specified
 * separator. The subclass of it will take care of the conversion between an object and a list of values. Together it
 * will convert between an object and a String with a specified separator. Typical use cases for this converter is to
 * convert Rectangle(0, 0, 100, 100) to "0, 0, 100, 100", or Color(255,255,255) to "255, 255, 255".
 *
 * @param <T> the type of the object
 * @param <S> the type of the value in the list.
 */
abstract public class ValuesConverter<T, S> extends DefaultObjectConverter<T> implements RequiringConverterManager {

    private String _separator;

    private Class<?> _elementClass;

    private Class<?>[] _elementClasses;

    /**
     * Creates an ArrayConverter.
     *
     * @param separator    separator to separate values. It should contain at least non-empty character.
     * @param elementClass class of the array element if all elements have the same class type.
     */
    public ValuesConverter(String separator, Class<?> elementClass) {
        _separator = separator;
        _elementClass = elementClass;
    }

    /**
     * Creates an ArrayConverter.
     *
     * @param separator      separator to separate values. It should contain at least non-empty character.
     * @param elementClasses classes of the elements, if the elements in the list have different types. In this case,
     *                       you may have to a common super class as the generic type S. If there is no common super
     *                       class, use {@code Object}.
     */
    public ValuesConverter(String separator, Class<?>[] elementClasses) {
        if (separator == null || separator.trim().length() == 0) {
            throw new IllegalArgumentException("separator cannot be empty.");
        }
        if (elementClasses == null) {
            throw new IllegalArgumentException("elementClasses cannot be null.");
        }
        _separator = separator;
        _elementClasses = elementClasses;
    }

    /**
     * Converts from a list of values to string by concatenating them with separators.
     *
     * @param objects a list of values
     * @param context converter context
     * @return string all objects concatenated with separators
     */
    public String valuesToString(List<S> objects, ConverterContext context) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < objects.size(); i++) {
            S o = objects.get(i);
            buffer.append(toString(i, o, context));
            if (i != objects.size() - 1) {
                buffer.append(_separator);
            }
        }
        return new String(buffer);
    }

    /**
     * Converts the value to String using the ObjectConverterManager.
     *
     * @param i       the index of the value in the list.
     * @param o       the value
     * @param context the context.
     * @return the String representation of the value.
     */
    protected String toString(int i, S o, ConverterContext context) {
        ObjectConverterManager instance = getObjectConverterManager(context);
        return instance.toString(o, getElementClass(i), context);
    }

    /**
     * Converts from string to a list of values, using separator to separate the string.
     *
     * @param string  string to be converted
     * @param context the converter context
     * @return the list of values.
     */
    public List<S> valuesFromString(String string, ConverterContext context) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        String[] ss = string.split(_separator.trim());
        List<S> objects = new ArrayList<>();
        for (int i = 0; i < ss.length && i < ss.length; i++) {
            String s = ss[i].trim();
            objects.add(fromString(i, s, context));
        }
        return objects;
    }

    /**
     * Converts the String to a value using the ObjectConverterManager.
     *
     * @param i       the index of the value in the list.
     * @param s       the string
     * @param context the context.
     * @return the value that is represented as the string.
     */
    protected S fromString(int i, String s, ConverterContext context) {
        ObjectConverterManager instance = getObjectConverterManager(context);
        return (S) instance.fromString(s, getElementClass(i), context);
    }

    /**
     * Gets the element class for the value at the specified index.
     *
     * @param index the index of the value in the values array.
     * @return the element class for the value at the specified index.
     */
    public Class<?> getElementClass(int index) {
        return _elementClasses != null ? _elementClasses[index] : _elementClass;
    }

    public static void main(String[] args) {
        ObjectConverterManager manager = new ObjectConverterManager();
        manager.setAutoInit(false);
        manager.unregisterAllConverters();
        manager.registerConverter(Dimension2D.class, new Dimension2DConverter("|"));
        String s = manager.toString(new Dimension2D(100, 200), Dimension2D.class);
        System.out.println(s);
    }
}
