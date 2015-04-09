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
package jidefx.scene.control.combobox;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.IntegerField;

/**
 * A {@code FormattedComboBox} for Integers. It allows you to specify the min and max value. The slider is used as
 * the popup to choose an integer value.
 */
public class IntegerComboBox extends FormattedComboBox<Integer> {
    private static final String STYLE_CLASS_DEFAULT = "integer-combo-box"; //NON-NLS

    private IntegerProperty _maxProperty;
    private IntegerProperty _minProperty;

    /**
     * Creates an IntegerComboBox which allows an integer from 0 to 100. The initial value is 0.
     */
    public IntegerComboBox() {
        this(0, 100, 0);
    }

    /**
     * Creates an IntegerComboBox with the specified min, max and value.
     *
     * @param min   the min value
     * @param max   the max value
     * @param value the value
     */
    public IntegerComboBox(int min, int max, int value) {
        super(value);
        setMin(min);
        setMax(max);
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
    }

    public IntegerProperty maxProperty() {
        if (_maxProperty == null) {
            _maxProperty = new SimpleIntegerProperty(this, "max"); //NON-NLS
        }
        return _maxProperty;
    }

    public int getMax() {
        return maxProperty().get();
    }

    public void setMax(int maxValue) {
        maxProperty().set(maxValue);
    }

    public IntegerProperty minProperty() {
        if (_minProperty == null) {
            _minProperty = new SimpleIntegerProperty(this, "min"); //NON-NLS
        }
        return _minProperty;
    }

    public int getMin() {
        return minProperty().get();
    }

    public void setMin(int min) {
        minProperty().set(min);
    }

    @Override
    protected FormattedTextField<Integer> createFormattedTextField() {
        IntegerField field = new IntegerField(Integer.MIN_VALUE, Integer.MAX_VALUE);
        field.maxProperty().bind(maxProperty());
        field.minProperty().bind(minProperty());
        return field;
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((IntegerField) getEditor()).getPopupContentFactory());
    }
}
