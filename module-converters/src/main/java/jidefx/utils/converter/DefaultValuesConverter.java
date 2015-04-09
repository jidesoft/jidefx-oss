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

import java.util.List;

/**
 * An ObjectConverter that converts a list of values to String.
 *
 * @param <S> the type in the list.
 */
public class DefaultValuesConverter<S> extends ValuesConverter<List<S>, S> {
    public DefaultValuesConverter(String separator, Class<?> elementClass) {
        super(separator, elementClass);
    }

    @Override
    public String toString(List<S> objects, ConverterContext context) {
        if (objects == null) {
            return "";
        }
        else {
            return valuesToString(objects, context);
        }
    }

    @Override
    public List<S> fromString(String string, ConverterContext context) {
        if (string == null || "".equals(string)) {
            return null;
        }
        else {
            return valuesFromString(string, context);
        }
    }

//    public static void main(String[] args) {
//        List<Integer> list = new ArrayList<>();
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        list.add(5);
//
//        ObjectConverterManager.getInstance().initDefaultConverters();
//        System.out.println(new DefaultArrayConverter<Integer>(";", int.class).toString(list, null));
//        System.out.println(new DefaultArrayConverter<Integer>(";", int.class).fromString("2;3;2;4", null));
//        System.out.println(new DefaultArrayConverter<Color>(";", Color.class).fromString("#FF0000;#FFFF00;#00FF00", HexColorConverter.CONTEXT_HEX));
//    }
}
