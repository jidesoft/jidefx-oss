/*
 * @(#)BoundingBoxConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.javafx;

import javafx.geometry.BoundingBox;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ObjectConverter;
import jidefx.utils.converter.ValuesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ObjectConverter} for {@link BoundingBox}.
 */
public class BoundingBoxConverter extends ValuesConverter<BoundingBox, Double> {
    public BoundingBoxConverter() {
        super("; ", Double.class);
    }

    public BoundingBoxConverter(String separator) {
        super(separator, Double.class);
    }

    public BoundingBoxConverter(String separator, Class<?> elementClass) {
        super(separator, elementClass);
    }

    /**
     * Converts the BoundingBox to String.
     *
     * @param boundingBox the BoundingBox
     * @param context     the converter context
     * @return the String representing the BoundingBox.
     */
    @Override
    public String toString(BoundingBox boundingBox, ConverterContext context) {
        if (boundingBox == null) {
            return null;
        }
        List<Double> list = new ArrayList<>(6);
        list.add(boundingBox.getMinX());
        list.add(boundingBox.getMinY());
        list.add(boundingBox.getMinZ());
        list.add(boundingBox.getWidth());
        list.add(boundingBox.getHeight());
        list.add(boundingBox.getDepth());
        return valuesToString(list, context);
    }

    /**
     * Converts from a String to a BoundingBox.
     *
     * @param string  the string
     * @param context the converter context
     * @return the BoundingBox represented by te String.
     */
    @Override
    public BoundingBox fromString(String string, ConverterContext context) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        List<Double> objects = valuesFromString(string, context);
        double minX = 0;
        if (objects.size() >= 1) {
            minX = objects.get(0);
        }
        double minY = 0;
        if (objects.size() >= 2) {
            minY = objects.get(1);
        }
        double minZ = 0;
        if (objects.size() >= 3) {
            minZ = objects.get(2);
        }
        double width = 0;
        if (objects.size() >= 4) {
            width = objects.get(3);
        }
        double height = 0;
        if (objects.size() >= 5) {
            height = objects.get(4);
        }
        double depth = 0;
        if (objects.size() >= 6) {
            depth = objects.get(5);
        }
        return new BoundingBox(minX, minY, minZ, width, height, depth);
    }
}
