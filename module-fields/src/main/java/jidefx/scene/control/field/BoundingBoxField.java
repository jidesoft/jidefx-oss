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

import javafx.geometry.BoundingBox;
import javafx.util.Callback;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.popup.ValuesPopupContent;
import jidefx.scene.control.field.verifier.NumberValuePatternVerifier;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.javafx.BoundingBoxConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code BoundingBoxField} is a {@code FormattedTextField} for {@link BoundingBox}.
 */
public class BoundingBoxField extends PopupField<BoundingBox> {
    public BoundingBoxField() {
    }

    private static final String STYLE_CLASS_DEFAULT = "bounding-box-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();

        setPopupContentFactory(new Callback<BoundingBox, PopupContent<BoundingBox>>() {
            @Override
            public PopupContent<BoundingBox> call(BoundingBox param) {
                ValuesPopupContent<BoundingBox, Double> popupContent = new ValuesPopupContent<BoundingBox, Double>(new String[]{getResourceString("x"), getResourceString("y"), getResourceString("z"), getResourceString("width"), getResourceString("height"), getResourceString("depth")}) {
                    @Override
                    public List<Double> toValues(BoundingBox value) {
                        ArrayList<Double> list = new ArrayList<>(6);
                        list.add(value.getMinX());
                        list.add(value.getMinY());
                        list.add(value.getMinZ());
                        list.add(value.getWidth());
                        list.add(value.getHeight());
                        list.add(value.getDepth());
                        return list;
                    }

                    @Override
                    public BoundingBox fromValues(List<Double> values) {
                        return new BoundingBox(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5));
                    }

                    @Override
                    public FormattedTextField<Double> createTextField(String label) {
                        int fieldIndex = getFieldIndex(label);
                        if (fieldIndex == 3 || fieldIndex == 4 || fieldIndex == 5) // for width and height
                            return new DoubleField(0, Double.MAX_VALUE);
                        else return new DoubleField();
                    }
                };
                popupContent.setValue(getValue());
                return popupContent;
            }
        });
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();

        setStringConverter(new BoundingBoxConverter() {
            @Override
            protected String toString(int i, Double o, ConverterContext context) {
                if (o == null) return "";
                return o.toString();
            }

            @Override
            protected Double fromString(int i, String s, ConverterContext context) {
                if (s == null || s.trim().isEmpty()) return null;
                return Double.valueOf(s);
            }
        }.toStringConverter());

        getPatternVerifiers().put("X", new NumberValuePatternVerifier<BoundingBox>() { //NON-NLS
            @Override
            public Double toTargetValue(BoundingBox fieldValue) {
                return fieldValue.getMinX();
            }

            @Override
            public BoundingBox fromTargetValue(BoundingBox previousFieldValue, Number value) {
                double x = value.doubleValue();
                return previousFieldValue != null ? new BoundingBox(x, previousFieldValue.getMinY(), previousFieldValue.getMinZ(), previousFieldValue.getWidth(), previousFieldValue.getHeight(), previousFieldValue.getDepth())
                        : new BoundingBox(x, 0, 0, 0, 0, 0);
            }
        });
        getPatternVerifiers().put("Y", new NumberValuePatternVerifier<BoundingBox>() { //NON-NLS
            @Override
            public Double toTargetValue(BoundingBox fieldValue) {
                return fieldValue.getMinY();
            }

            @Override
            public BoundingBox fromTargetValue(BoundingBox previousFieldValue, Number value) {
                double y = value.doubleValue();
                return previousFieldValue != null ? new BoundingBox(previousFieldValue.getMinX(), y, previousFieldValue.getMinZ(), previousFieldValue.getWidth(), previousFieldValue.getHeight(), previousFieldValue.getDepth())
                        : new BoundingBox(0, y, 0, 0, 0, 0);
            }
        });
        getPatternVerifiers().put("Z", new NumberValuePatternVerifier<BoundingBox>() { //NON-NLS
            @Override
            public Double toTargetValue(BoundingBox fieldValue) {
                return fieldValue.getMinZ();
            }

            @Override
            public BoundingBox fromTargetValue(BoundingBox previousFieldValue, Number value) {
                double z = value.doubleValue();
                return previousFieldValue != null ? new BoundingBox(previousFieldValue.getMinX(), previousFieldValue.getMinY(), z, previousFieldValue.getWidth(), previousFieldValue.getHeight(), previousFieldValue.getDepth())
                        : new BoundingBox(0, 0, z, 0, 0, 0);
            }
        });
        getPatternVerifiers().put("Width", new NumberValuePatternVerifier<BoundingBox>(0, Double.MAX_VALUE) { //NON-NLS
            @Override
            public Double toTargetValue(BoundingBox fieldValue) {
                return fieldValue.getWidth();
            }

            @Override
            public BoundingBox fromTargetValue(BoundingBox previousFieldValue, Number value) {
                double width = value.doubleValue();
                return previousFieldValue != null ? new BoundingBox(previousFieldValue.getMinX(), previousFieldValue.getMinY(), previousFieldValue.getMinZ(), width, previousFieldValue.getHeight(), previousFieldValue.getDepth())
                        : new BoundingBox(0, 0, 0, width, 0, 0);
            }
        });
        getPatternVerifiers().put("Height", new NumberValuePatternVerifier<BoundingBox>(0, Double.MAX_VALUE) { //NON-NLS
            @Override
            public Double toTargetValue(BoundingBox fieldValue) {
                return fieldValue.getHeight();
            }

            @Override
            public BoundingBox fromTargetValue(BoundingBox previousFieldValue, Number value) {
                double height = value.doubleValue();
                return previousFieldValue != null ? new BoundingBox(previousFieldValue.getMinX(), previousFieldValue.getMinY(), previousFieldValue.getMinZ(), previousFieldValue.getWidth(), height, previousFieldValue.getDepth())
                        : new BoundingBox(0, 0, 0, 0, height, 0);
            }
        });
        getPatternVerifiers().put("Depth", new NumberValuePatternVerifier<BoundingBox>(0, Double.MAX_VALUE) { //NON-NLS
            @Override
            public Double toTargetValue(BoundingBox fieldValue) {
                return fieldValue.getDepth();
            }

            @Override
            public BoundingBox fromTargetValue(BoundingBox previousFieldValue, Number value) {
                double depth = value.doubleValue();
                return previousFieldValue != null ? new BoundingBox(previousFieldValue.getMinX(), previousFieldValue.getMinY(), previousFieldValue.getMinZ(), previousFieldValue.getWidth(), previousFieldValue.getHeight(), depth)
                        : new BoundingBox(0, 0, 0, 0, 0, depth);
            }
        });
        setPattern("X; Y; Z; Width; Height; Depth"); //NON-NLS
    }
}
