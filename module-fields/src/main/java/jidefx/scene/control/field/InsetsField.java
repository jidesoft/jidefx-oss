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

import javafx.geometry.Insets;
import javafx.util.Callback;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.popup.ValuesPopupContent;
import jidefx.scene.control.field.verifier.NumberValuePatternVerifier;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.javafx.InsetsConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code InsetsField} is a {@code FormattedTextField} for {@link Insets}.
 */
public class InsetsField extends PopupField<Insets> {
    public InsetsField() {
    }

    private static final String STYLE_CLASS_DEFAULT = "insets-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();
        setStringConverter(new InsetsConverter() {
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

        getPatternVerifiers().put("Top", new NumberValuePatternVerifier<Insets>() { //NON-NLS
            @Override
            public Double toTargetValue(Insets fieldValue) {
                return fieldValue.getTop();
            }

            @Override
            public Insets fromTargetValue(Insets previousFieldValue, Number value) {
                double top = value.doubleValue();
                return previousFieldValue != null ? new Insets(top, previousFieldValue.getRight(), previousFieldValue.getBottom(), previousFieldValue.getLeft())
                        : new Insets(top, 0, 0, 0);
            }
        });
        getPatternVerifiers().put("Right", new NumberValuePatternVerifier<Insets>() { //NON-NLS
            @Override
            public Double toTargetValue(Insets fieldValue) {
                return fieldValue.getRight();
            }

            @Override
            public Insets fromTargetValue(Insets previousFieldValue, Number value) {
                double right = value.doubleValue();
                return previousFieldValue != null ? new Insets(previousFieldValue.getTop(), right, previousFieldValue.getBottom(), previousFieldValue.getLeft())
                        : new Insets(0, right, 0, 0);
            }
        });
        getPatternVerifiers().put("Bottom", new NumberValuePatternVerifier<Insets>() { //NON-NLS
            @Override
            public Double toTargetValue(Insets fieldValue) {
                return fieldValue.getBottom();
            }

            @Override
            public Insets fromTargetValue(Insets previousFieldValue, Number value) {
                double bottom = value.doubleValue();
                return previousFieldValue != null ? new Insets(previousFieldValue.getTop(), previousFieldValue.getRight(), bottom, previousFieldValue.getLeft())
                        : new Insets(0, 0, bottom, 0);
            }
        });
        getPatternVerifiers().put("Left", new NumberValuePatternVerifier<Insets>() { //NON-NLS
            @Override
            public Double toTargetValue(Insets fieldValue) {
                return fieldValue.getLeft();
            }

            @Override
            public Insets fromTargetValue(Insets previousFieldValue, Number value) {
                double left = value.doubleValue();
                return previousFieldValue != null ? new Insets(previousFieldValue.getTop(), previousFieldValue.getRight(), previousFieldValue.getBottom(), left)
                        : new Insets(0, 0, 0, left);
            }
        });
        setPattern("Top; Right; Bottom; Left"); //NON-NLS
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setPopupContentFactory(new Callback<Insets, PopupContent<Insets>>() {
            @Override
            public PopupContent<Insets> call(Insets param) {
                ValuesPopupContent<Insets, Double> popupContent = new ValuesPopupContent<Insets, Double>(new String[]{getResourceString("top"), getResourceString("right"), getResourceString("bottom"), getResourceString("left")}) {
                    @Override
                    public List<Double> toValues(Insets value) {
                        ArrayList<Double> list = new ArrayList<>(4);
                        list.add(value.getTop());
                        list.add(value.getRight());
                        list.add(value.getBottom());
                        list.add(value.getLeft());
                        return list;
                    }

                    @Override
                    public Insets fromValues(List<Double> values) {
                        return new Insets(values.get(0), values.get(1), values.get(2), values.get(3));
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
