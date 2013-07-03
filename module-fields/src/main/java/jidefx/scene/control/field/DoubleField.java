/*
 * @(#)DoubleField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.util.StringConverter;
import jidefx.scene.control.field.verifier.NumberValuePatternVerifier;

/**
 * A {@code FormattedTextField} for {@link Double}.
 */
public class DoubleField extends FormattedTextField<Double> {

    public DoubleField() {
        this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public DoubleField(double min, double max) {
        getPatternVerifiers().put("double", new NumberValuePatternVerifier<Double>(min, max) { //NON-NLS
            @Override
            public Double toTargetValue(Double fieldValue) {
                return fieldValue;
            }

            @Override
            public Double fromTargetValue(Double previousFieldValue, Number value) {
                return value.doubleValue();
            }
        });
        setPattern("double"); //NON-NLS
    }

    private static final String STYLE_CLASS_DEFAULT = "double-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setSpinnersVisible(true);
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();
        setStringConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double o) {
                if (o == null) return "";
                return o.toString();
            }

            @Override
            public Double fromString(String s) {
                if (s == null || s.trim().isEmpty()) return null;
                return Double.valueOf(s);
            }
        });
    }
}
