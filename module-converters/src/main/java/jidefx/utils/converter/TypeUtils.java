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

import java.util.Calendar;
import java.util.Date;

/**
 * An exact copy of the same name class from the JideFX Common Layer. Do it in order to remove the dependency on the
 * Common Layer. If you would like to use it directly, please use the one in the Common Layer.
 */
class TypeUtils {
    // indexes referring to columns in the PRIMITIVE_ARRAY_TYPES table.
    private static final int WRAPPER_TYPE_INDEX = 0;
    private static final int PRIMITIVE_TYPE_INDEX = 1;

    private static final Object[][] PRIMITIVE_ARRAY_TYPES = {
            {Boolean.class, boolean.class, "Z"}, //NON-NLS
            {Character.class, char.class, "C"}, //NON-NLS
            {Byte.class, byte.class, "B"}, //NON-NLS
            {Short.class, short.class, "S"}, //NON-NLS
            {Integer.class, int.class, "I"}, //NON-NLS
            {Long.class, long.class, "J"}, //NON-NLS
            {Float.class, float.class, "F"}, //NON-NLS
            {Double.class, double.class, "D"} //NON-NLS
    };

    public static boolean isPrimitive(Class<?> primitive) {
        for (Object[] primitiveArrayType : PRIMITIVE_ARRAY_TYPES) {
            if (primitiveArrayType[PRIMITIVE_TYPE_INDEX] == primitive) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPrimitiveWrapper(Class<?> wrapperType) {
        for (Object[] primitiveArrayType : PRIMITIVE_ARRAY_TYPES) {
            if (primitiveArrayType[WRAPPER_TYPE_INDEX] == wrapperType) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> convertPrimitiveToWrapperType(Class<?> primitive) {
        for (Object[] primitiveArrayType : PRIMITIVE_ARRAY_TYPES) {
            if (primitiveArrayType[PRIMITIVE_TYPE_INDEX] == primitive) {
                return (Class<?>) primitiveArrayType[WRAPPER_TYPE_INDEX];
            }
        }
        return primitive;
    }

    public static Class<?> convertWrapperToPrimitiveType(Class<?> wrapperType) {
        for (Object[] primitiveArrayType : PRIMITIVE_ARRAY_TYPES) {
            if (primitiveArrayType[WRAPPER_TYPE_INDEX] == wrapperType) {
                return (Class<?>) primitiveArrayType[PRIMITIVE_TYPE_INDEX];
            }
        }
        return wrapperType;
    }

    /**
     * Checks if the type is a numeric type.
     *
     * @param type the data type.
     * @return true if it is numeric type including all subclasses of Number, double, int, float, short and long.
     */
    public static boolean isNumericType(Class<?> type) {
        return type != null && (Number.class.isAssignableFrom(type)
                || type == double.class
                || type == int.class
                || type == float.class
                || type == short.class
                || type == long.class);
    }

    /**
     * Checks if the type is a temporal type such as Date, Calendar, long or double that can be used to represent date
     * or time.
     *
     * @param type the data type.
     * @return true if it is temporal type including all subclasses.
     */
    public static boolean isTemporalType(Class<?> type) {
        return type != null && (Date.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type)
                || type == double.class
                || type == long.class);
    }
}
