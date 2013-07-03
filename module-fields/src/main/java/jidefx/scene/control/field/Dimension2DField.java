/*
 * @(#)Dimension2DField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.geometry.Dimension2D;
import javafx.util.Callback;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.popup.ValuesPopupContent;
import jidefx.scene.control.field.verifier.NumberValuePatternVerifier;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.javafx.Dimension2DConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Dimension2DField} is a {@code FormattedTextField} for {@link Dimension2D}.
 */
public class Dimension2DField extends PopupField<Dimension2D> {
    public Dimension2DField() {
    }

    private static final String STYLE_CLASS_DEFAULT = "dimension-2d-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();
        setStringConverter(new Dimension2DConverter() {
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

        getPatternVerifiers().put("Width", new NumberValuePatternVerifier<Dimension2D>(0, Double.MAX_VALUE) { //NON-NLS
            @Override
            public Double toTargetValue(Dimension2D fieldValue) {
                return fieldValue.getWidth();
            }

            @Override
            public Dimension2D fromTargetValue(Dimension2D previousFieldValue, Number value) {
                double x = value.doubleValue();
                return previousFieldValue != null ? new Dimension2D(x, previousFieldValue.getHeight()) : new Dimension2D(x, 0);
            }
        });
        getPatternVerifiers().put("Height", new NumberValuePatternVerifier<Dimension2D>(0, Double.MAX_VALUE) { //NON-NLS
            @Override
            public Double toTargetValue(Dimension2D fieldValue) {
                return fieldValue.getHeight();
            }

            @Override
            public Dimension2D fromTargetValue(Dimension2D previousFieldValue, Number value) {
                double y = value.doubleValue();
                return previousFieldValue != null ? new Dimension2D(previousFieldValue.getWidth(), y) : new Dimension2D(0, y);
            }
        });

        setPattern("Width; Height"); //NON-NLS
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();

        setPopupContentFactory(new Callback<Dimension2D, PopupContent<Dimension2D>>() {
            @Override
            public PopupContent<Dimension2D> call(Dimension2D param) {
                ValuesPopupContent<Dimension2D, Double> popupContent = new ValuesPopupContent<Dimension2D, Double>(new String[]{getResourceString("width"), getResourceString("height")}) {
                    @Override
                    public List<Double> toValues(Dimension2D value) {
                        ArrayList<Double> list = new ArrayList<>(2);
                        list.add(value.getWidth());
                        list.add(value.getHeight());
                        return list;
                    }

                    @Override
                    public Dimension2D fromValues(List<Double> values) {
                        return new Dimension2D(values.get(0), values.get(1));
                    }

                    @Override
                    public FormattedTextField<Double> createTextField(String label) {
                        return new DoubleField(0, Double.MAX_VALUE);
                    }
                };
                popupContent.setValue(getValue());
                return popupContent;
            }
        });
    }
}
