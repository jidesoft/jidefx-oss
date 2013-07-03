/*
 * @(#)InsetsConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.javafx;

import javafx.geometry.Insets;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ObjectConverter;
import jidefx.utils.converter.ValuesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ObjectConverter} for {@link Insets}.
 */
public class InsetsConverter extends ValuesConverter<Insets, Double> {
    public InsetsConverter() {
        super("; ", Double.class);
    }

    public InsetsConverter(String separator) {
        super(separator, Double.class);
    }

    /**
     * Converts the Insets to String.
     *
     * @param insets  the Insets
     * @param context the converter context
     * @return the String representing the Insets.
     */
    @Override
    public String toString(Insets insets, ConverterContext context) {
        if (insets == null) {
            return null;
        }
        List<Double> list = new ArrayList<>(4);
        list.add(insets.getTop());
        list.add(insets.getRight());
        list.add(insets.getBottom());
        list.add(insets.getLeft());
        return valuesToString(list, context);
    }

    /**
     * Converts from a String to a Insets.
     *
     * @param string  the string
     * @param context the converter context
     * @return the Insets represented by te String.
     */
    @Override
    public Insets fromString(String string, ConverterContext context) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        List<Double> objects = valuesFromString(string, context);
        double top = 0;
        if (objects.size() >= 1) {
            top = objects.get(0);
        }
        double right = 0;
        if (objects.size() >= 2) {
            right = objects.get(1);
        }
        double bottom = 0;
        if (objects.size() >= 3) {
            bottom = objects.get(2);
        }
        double left = 0;
        if (objects.size() >= 4) {
            left = objects.get(3);
        }
        return new Insets(top, right, bottom, left);
    }
}
