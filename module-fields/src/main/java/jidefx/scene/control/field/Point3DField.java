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
package jidefx.scene.control.field;

import javafx.geometry.Point3D;
import javafx.util.Callback;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.popup.ValuesPopupContent;
import jidefx.scene.control.field.verifier.NumberValuePatternVerifier;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.javafx.Point3DConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Point3DField} is a {@code FormattedTextField} for {@link Point3D}.
 */
public class Point3DField extends PopupField<Point3D> {
    public Point3DField() {
    }

    private static final String STYLE_CLASS_DEFAULT = "point-3d-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();
        setStringConverter(new Point3DConverter() {
            @Override
            protected String toString(int i, Double o, ConverterContext context) {
                if (o == null) return "";
                return o.toString();
            }

            @Override
            protected Double fromString(int i, String s, ConverterContext context) {
                if (s == null || s.trim().isEmpty()) {
                    return null;
                }
                return Double.valueOf(s);
            }
        }.toStringConverter());

        getPatternVerifiers().put("X", new NumberValuePatternVerifier<Point3D>() { //NON-NLS
            @Override
            public Double toTargetValue(Point3D fieldValue) {
                return fieldValue.getX();
            }

            @Override
            public Point3D fromTargetValue(Point3D previousFieldValue, Number value) {
                double x = value.doubleValue();
                return previousFieldValue != null ? new Point3D(x, previousFieldValue.getY(), previousFieldValue.getZ()) : new Point3D(x, 0, 0);
            }
        });
        getPatternVerifiers().put("Y", new NumberValuePatternVerifier<Point3D>() { //NON-NLS
            @Override
            public Double toTargetValue(Point3D fieldValue) {
                return fieldValue.getY();
            }

            @Override
            public Point3D fromTargetValue(Point3D previousFieldValue, Number value) {
                double y = value.doubleValue();
                return previousFieldValue != null ? new Point3D(previousFieldValue.getX(), y, previousFieldValue.getZ()) : new Point3D(0, y, 0);
            }
        });
        getPatternVerifiers().put("Z", new NumberValuePatternVerifier<Point3D>() { //NON-NLS
            @Override
            public Double toTargetValue(Point3D fieldValue) {
                return fieldValue.getZ();
            }

            @Override
            public Point3D fromTargetValue(Point3D previousFieldValue, Number value) {
                double z = value.doubleValue();
                return previousFieldValue != null ? new Point3D(previousFieldValue.getX(), previousFieldValue.getY(), z) : new Point3D(0, 0, z);
            }
        });
        setPattern("X; Y; Z"); //NON-NLS
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setPopupContentFactory(new Callback<Point3D, PopupContent<Point3D>>() {
            @Override
            public PopupContent<Point3D> call(Point3D param) {
                ValuesPopupContent<Point3D, Double> popupContent = new ValuesPopupContent<Point3D, Double>(new String[]{getResourceString("x"), getResourceString("y"), getResourceString("z")}) {
                    @Override
                    public List<Double> toValues(Point3D value) {
                        ArrayList<Double> list = new ArrayList<>(3);
                        list.add(value.getX());
                        list.add(value.getY());
                        list.add(value.getZ());
                        return list;
                    }

                    @Override
                    public Point3D fromValues(List<Double> values) {
                        return new Point3D(values.get(0), values.get(1), values.get(2));
                    }

                    @Override
                    public FormattedTextField<Double> createTextField(String label) {
                        return new DoubleField();
                    }
                };
                popupContent.setValue(getValue());
                return popupContent;
            }
        });
    }
}
