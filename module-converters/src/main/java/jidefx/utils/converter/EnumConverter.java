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

/**
 * {@code EnumConverter} is a converter for Enums or any other data type that can be enumerated. If it is an Enum,
 * you can use {@link #EnumConverter(Class)} to create a converter. For other data types, you can use other
 * constructors.
 * <p>
 * Before JDK1.5, there is no Enum type, so this is only one way to define an enumeration. For example, in
 * SwingConstants, the following values are defined.
 * <pre>{@code
 * public static final int CENTER  = 0;
 * public static final int TOP     = 1;
 * public static final int LEFT    = 2;
 * public static final int BOTTOM  = 3;
 * public static final int RIGHT   = 4;
 * }</pre>
 * The problem comes when you want to display it in UI. You don't want to use 0, 1, 2, 3, 4 as the value doesn't mean
 * anything from user point of view. You want to use a more meaningful name such as "Center", "Top", "Left", "Bottom",
 * "Right". Obviously you need a converter here to convert from the integer to string, such as converting from 0 to
 * "Center" and vice verse. That's what <tt>EnumConverter</tt> for.
 */
public class EnumConverter<T> extends DefaultObjectConverter<T> implements LazyInitializeConverter {
    private final static String PROPERTY_ENUM_TYPE = "EnumType"; //NON-NLS
    private String _name;
    private T _default;
    private Class<?> _type;
    private Object[] _objects;
    private String[] _strings;

    /**
     * Creates an empty EnumConverter. It can be initialized later using {@link #initialize(Class, ConverterContext)}
     * method.
     */
    public EnumConverter() {
    }

    /**
     * The constructor to create an EnumConverter for an Enum type.
     *
     * @param enumType the enum type
     */
    public EnumConverter(Class<? extends Enum> enumType) {
        initializeEnums(enumType);
    }

    /**
     * Creates an EnumConverter. The type is set to the type of the first element in the value array. The default value
     * is set to the first element in the value array.
     *
     * @param name    the name of the converter. The name is used to create ConverterContext and later on the
     *                EditorContext.
     * @param values  the value array. All elements in the value array should have the same type and it must have at
     *                last one element in the array.
     * @param strings the names array. It contains the meaningful names for the elements in the value array. They should
     *                one to one match with each other. The length of name array should be the same as that of value
     *                array. Otherwise IllegalArgumentException will be thrown.
     */
    public EnumConverter(String name, T[] values, String[] strings) {
        this(name, values[0].getClass(), values, strings);
    }

    /**
     * Creates an EnumConverter. The default value is set to the first element in the value array.
     *
     * @param name    the name of the converter. The name is used to create ConverterContext and later on the
     *                EditorContext.
     * @param type    the type of the element in value array.
     * @param values  the value array. All elements in the value array should have the same type and it must have at
     *                last one element in the array.
     * @param strings the names array. It contains the meaningful names for the elements in the value array. They should
     *                one to one match with each other. The length of name array should be the same as that of value
     *                array. Otherwise IllegalArgumentException will be thrown.
     */
    public EnumConverter(String name, Class<?> type, T[] values, String[] strings) {
        this(name, type, values, strings, values[0]);
    }

    /**
     * Creates an EnumConverter.
     *
     * @param name         the name of the converter. The name is used to create ConverterContext and later on the
     *                     EditorContext.
     * @param type         the type of the element in value array.
     * @param values       the value array. All elements in the value array should have the same type and it must have
     *                     at last one element in the array.
     * @param strings      the names array. It contains the meaningful names for the elements in the value array. They
     *                     should one to one match with each other. The length of name array should be the same as that
     *                     of value array. Otherwise IllegalArgumentException will be thrown.
     * @param defaultValue the default value
     */
    public EnumConverter(String name, Class<?> type, T[] values, String[] strings, T defaultValue) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The \"name\" parameter cannot be null or empty. Please use a unique string to represent the name of the converter.");
        }
        _name = name;
        if (values == null) {
            throw new IllegalArgumentException("The \"values\" parameter cannot be null.");
        }
        if (strings == null) {
            throw new IllegalArgumentException("The \"strings\" parameter cannot be null.");
        }
        if (strings.length != values.length) {
            throw new IllegalArgumentException("The \"values\" and \"strings\" parameters should have the same length.");
        }
        _type = type;
        _objects = values;
        _strings = strings;
        _default = defaultValue;
    }

    @Override
    public void initialize(Class<?> clazz, ConverterContext converterContext) {
        if (Enum.class.isAssignableFrom(clazz)) {
            initializeEnums((Class<? extends Enum>) clazz);
        }
    }

    private void initializeEnums(Class<? extends Enum> enumType) {
        if (enumType == null || !enumType.isEnum()) {
            throw new IllegalArgumentException("To use this constructor, the type has to be an enum type.");
        }
        String name = enumType.getName();
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Empty enum type name.");
        }
        int index = name.lastIndexOf('$');
        if (index >= 0) {
            _name = name.substring(index + 1);
        }
        else {
            _name = name;
        }
        _type = enumType;
        try {
            Enum[] values = enumType.getEnumConstants();
            int length = values.length;
            _objects = new Object[length];
            _strings = new String[length];
            for (int i = 0; i < length; i++) {
                _objects[i] = values[i];
                _strings[i] = values[i].toString();
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Illegal enum type.");
        }
    }

    transient private ConverterContext _context;

    /**
     * Gets the converter context of this converter. The name of the context is the name of the converter where you pass
     * in to EnumConverter's constructor.
     *
     * @return the converter context of this converter.
     */
    public ConverterContext getConverterContext() {
        if (_context == null) {
            _context = new ConverterContext(_name);
        }
        return _context;
    }

    /**
     * Converts the object to string. It will find the object from the value array and find the matching string from
     * {@code strings} array. An empty string will be returned if nothing matches. Otherwise, it will return the
     * corresponding string.
     *
     * @param value   the object to be converted.
     * @param context the converter context.
     * @return the string for the object.
     */
    @Override
    public String toString(T value, ConverterContext context) {
        if (value == null) {
            return "";
        }

        for (int i = 0; i < _objects.length; i++) {
            if ((_objects[i] != null && _objects[i].equals(value))) {
                if (i < _strings.length) {
                    return _strings[i];
                }
            }
        }
        return "";
    }

    /**
     * Converts the string to the object. It will find the string from the {@code strings} array and find the
     * matching object from the value array. The default value will be returned if nothing matches. Otherwise, it will
     * return the string itself that is passed in.
     *
     * @param string  the string to be converted
     * @param context the converter context.
     * @return the object of the string.
     */
    @Override
    public T fromString(String string, ConverterContext context) {
        if (string == null) {
            return null;
        }

        string = string.trim();
        if (string.length() == 0) {
            return null;
        }

        Object enumType = context.getProperties().get(PROPERTY_ENUM_TYPE);
        if (_type == null && enumType instanceof Class && Enum.class.isAssignableFrom((Class) enumType)) {
            initializeEnums((Class<? extends Enum>) enumType);
        }

        for (int i = 0; i < _strings.length; i++) {
            if (_strings[i].equals(string)) {
                if (i < _objects.length) {
                    return (T) _objects[i];
                }
            }
        }
        return _default;
    }

    /**
     * Gets the name of the converter.
     *
     * @return the name of the converter.
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the data type of the converter.
     *
     * @return the data type of the converter.
     */
    public Class<?> getType() {
        return _type;
    }

    /**
     * Gets the default value of the converter if it failed to find the matching object for a particular string.
     *
     * @return the default value.
     */
    public T getDefault() {
        return _default;
    }

    /**
     * Gets the {@code objects} array.
     *
     * @return the {@code objects} array.
     */
    public Object[] getObjects() {
        return _objects;
    }

    /**
     * Gets the {@code strings} array.
     *
     * @return the {@code strings} array.
     */
    public String[] getStrings() {
        return _strings;
    }

    /**
     * Converts an object array to a String array using ObjectConverterManager.
     * <p>
     * This method can be used, for example, for Enum type, to provide a default string representation of the enum
     * values.
     * <pre>{@code
     * ObjectConverter converter = new EnumConverter("Rank", Rank.values(),
     * EnumConverter.toStrings(Rank.values()));
     * }</pre>
     * Of course, you can still define your own string array for the enum values if the default one doesn't work well.
     *
     * @param values the object array.
     * @return the string array.
     */
    public static String[] toStrings(Object[] values) {
        return toStrings(values, null);
    }

    /**
     * Converts an object array to a String array using ObjectConverterManager.
     *
     * @param values           the object array.
     * @param converterContext the converter context used when calling ObjectConverterManager.toString.
     * @return the string array.
     */
    public static String[] toStrings(Object[] values, ConverterContext converterContext) {
        String[] s = new String[values.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = ObjectConverterManager.getInstance().toString(values[i], values[i].getClass(), converterContext);
        }
        return s;
    }
}
