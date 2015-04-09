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
package jidefx.utils.converter.javafx;

import javafx.geometry.Point2D;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ValuesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link jidefx.utils.converter.ObjectConverter} for {@link Point2D}.
 */
public class Point2DConverter extends ValuesConverter<Point2D, Double> {
    public Point2DConverter() {
        super("; ", Double.class);
    }

    public Point2DConverter(String separator) {
        super(separator, Double.class);
    }

    /**
     * Converts the Point2D to String.
     *
     * @param point2D the Point2D
     * @param context the converter context
     * @return the String representing the Point2D.
     */
    @Override
    public String toString(Point2D point2D, ConverterContext context) {
        if (point2D == null) {
            return null;
        }
        List<Double> list = new ArrayList<>(2);
        list.add(point2D.getX());
        list.add(point2D.getY());
        return valuesToString(list, context);
    }

    /**
     * Converts from a String to a Point2D.
     *
     * @param string  the string
     * @param context the converter context
     * @return the Point2D represented by te String.
     */
    @Override
    public Point2D fromString(String string, ConverterContext context) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        List<Double> objects = valuesFromString(string, context);
        double x = 0;
        if (objects.size() >= 1) {
            Double value = objects.get(0);
            x = value == null ? 0.0 : value;
        }
        double y = 0;
        if (objects.size() >= 2) {
            Double value = objects.get(1);
            y = value == null ? 0.0 : value;
        }
        return new Point2D(x, y);
    }
}
