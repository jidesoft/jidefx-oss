/*
 * @(#)Dimension2DConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.utils.converter.javafx;

import javafx.geometry.Dimension2D;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ValuesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link jidefx.utils.converter.ObjectConverter} for {@link Dimension2D}.
 */
public class Dimension2DConverter extends ValuesConverter<Dimension2D, Double> {
    public Dimension2DConverter() {
        super("; ", Double.class);
    }

    public Dimension2DConverter(String separator) {
        super(separator, Double.class);
    }

    /**
     * Converts the Dimension2D to String.
     *
     * @param dimension2D the Dimension2D
     * @param context     the converter context
     * @return the String representing the Dimension2D.
     */
    @Override
    public String toString(Dimension2D dimension2D, ConverterContext context) {
        if (dimension2D == null) {
            return null;
        }
        List<Double> list = new ArrayList<>(2);
        list.add(dimension2D.getWidth());
        list.add(dimension2D.getHeight());
        return valuesToString(list, context);
    }

    /**
     * Converts from a String to a Dimension2D.
     *
     * @param string  the string
     * @param context the converter context
     * @return the Dimension2D represented by te String.
     */
    @Override
    public Dimension2D fromString(String string, ConverterContext context) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        List<Double> objects = valuesFromString(string, context);
        double width = 0;
        if (objects.size() >= 1) {
            Double value = objects.get(0);
            width = value == null ? 0.0 : value;
        }
        double height = 0;
        if (objects.size() >= 2) {
            Double value = objects.get(1);
            height = value == null ? 0.0 : value;
        }
        return new Dimension2D(width, height);
    }
}
