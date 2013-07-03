/*
 * @(#)Point2DField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.geometry.Point2D;
import javafx.util.Callback;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.popup.ValuesPopupContent;
import jidefx.scene.control.field.verifier.NumberValuePatternVerifier;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.javafx.Point2DConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Point2DField} is a {@code FormattedTextField} for {@link Point2D}.
 */
public class Point2DField extends PopupField<Point2D> {
    public Point2DField() {
    }

    private static final String STYLE_CLASS_DEFAULT = "point-2d-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();
        setStringConverter(new Point2DConverter() {
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

        getPatternVerifiers().put("X", new NumberValuePatternVerifier<Point2D>() { //NON-NLS
            @Override
            public Double toTargetValue(Point2D fieldValue) {
                return fieldValue.getX();
            }

            @Override
            public Point2D fromTargetValue(Point2D previousFieldValue, Number value) {
                double x = value.doubleValue();
                return previousFieldValue != null ? new Point2D(x, previousFieldValue.getY()) : new Point2D(x, 0);
            }
        });
        getPatternVerifiers().put("Y", new NumberValuePatternVerifier<Point2D>() { //NON-NLS
            @Override
            public Double toTargetValue(Point2D fieldValue) {
                return fieldValue.getY();
            }

            @Override
            public Point2D fromTargetValue(Point2D previousFieldValue, Number value) {
                double y = value.doubleValue();
                return previousFieldValue != null ? new Point2D(previousFieldValue.getX(), y) : new Point2D(0, y);
            }
        });
        setPattern("X; Y"); //NON-NLS
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setPopupContentFactory(new Callback<Point2D, PopupContent<Point2D>>() {
            @Override
            public PopupContent<Point2D> call(Point2D param) {
                ValuesPopupContent<Point2D, Double> popupContent = new ValuesPopupContent<Point2D, Double>(new String[]{"X: ", "Y: "}) { //NON-NLS
                    @Override
                    public List<Double> toValues(Point2D value) {
                        ArrayList<Double> list = new ArrayList<>(2);
                        list.add(value.getX());
                        list.add(value.getY());
                        return list;
                    }

                    @Override
                    public Point2D fromValues(List<Double> values) {
                        return new Point2D(values.get(0), values.get(1));
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
