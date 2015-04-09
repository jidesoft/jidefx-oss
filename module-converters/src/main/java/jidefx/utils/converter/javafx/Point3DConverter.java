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

import javafx.geometry.Point3D;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.ValuesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link jidefx.utils.converter.ObjectConverter} for {@link Point3D}.
 */
public class Point3DConverter extends ValuesConverter<Point3D, Double> {
    public Point3DConverter() {
        super("; ", Double.class);
    }

    public Point3DConverter(String separator) {
        super(separator, Double.class);
    }

    /**
     * Converts the Point3D to String.
     *
     * @param point3D the Point3D
     * @param context the converter context
     * @return the String representing the Point3D.
     */
    @Override
    public String toString(Point3D point3D, ConverterContext context) {
        if (point3D == null) {
            return null;
        }
        List<Double> list = new ArrayList<>(3);
        list.add(point3D.getX());
        list.add(point3D.getY());
        list.add(point3D.getZ());
        return valuesToString(list, context);
    }

    /**
     * Converts from a String to a Point3D.
     *
     * @param string  the string
     * @param context the converter context
     * @return the Point3D represented by te String.
     */
    @Override
    public Point3D fromString(String string, ConverterContext context) {
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
        double z = 0;
        if (objects.size() >= 3) {
            Double value = objects.get(2);
            z = value == null ? 0.0 : value;
        }
        return new Point3D(x, y, z);
    }
}
