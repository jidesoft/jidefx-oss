/*
 * @(#)DefaultArrayConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
