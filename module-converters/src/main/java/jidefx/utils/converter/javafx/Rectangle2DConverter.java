/*
 * @(#)Rectangle2DConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.javafx;

import javafx.geometry.Rectangle2D;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ObjectConverter;
import jidefx.utils.converter.ValuesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ObjectConverter} for {@link Rectangle2D}.
 */
public class Rectangle2DConverter extends ValuesConverter<Rectangle2D, Double> {
    public Rectangle2DConverter() {
        super("; ", Double.class);
    }

    public Rectangle2DConverter(String separator) {
        super(separator, Double.class);
    }

    /**
     * Converts the Rectangle2D to String.
     *
     * @param rectangle2d the Rectangle2D
     * @param context     the converter context
     * @return the String representing the Rectangle2D.
     */
    @Override
    public String toString(Rectangle2D rectangle2d, ConverterContext context) {
        if (rectangle2d == null) {
            return null;
        }
        List<Double> list = new ArrayList<>(4);
        list.add(rectangle2d.getMinX());
        list.add(rectangle2d.getMinY());
        list.add(rectangle2d.getWidth());
        list.add(rectangle2d.getHeight());
        return valuesToString(list, context);
    }

    /**
     * Converts from a String to a Rectangle2D.
     *
     * @param string  the string
     * @param context context to be converted
     * @return the Rectangle2D represented by te String.
     */
    @Override
    public Rectangle2D fromString(String string, ConverterContext context) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        List<Double> objects = valuesFromString(string, context);
        double minX = 0;
        if (objects.size() >= 1) {
            Double value = objects.get(0);
            minX = value == null ? 0.0 : value;
        }
        double minY = 0;
        if (objects.size() >= 2) {
            Double value = objects.get(1);
            minY = value == null ? 0.0 : value;
        }
        double width = 0;
        if (objects.size() >= 3) {
            Double value = objects.get(2);
            width = value == null ? 0.0 : value;
        }
        double height = 0;
        if (objects.size() >= 4) {
            Double value = objects.get(3);
            height = value == null ? 0.0 : value;
        }
        return new Rectangle2D(minX, minY, width, height);
    }
}
