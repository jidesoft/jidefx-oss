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
