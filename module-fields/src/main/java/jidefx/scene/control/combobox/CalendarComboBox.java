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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import jidefx.scene.control.field.CalendarField;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.popup.PopupContent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A {@code FormattedComboBox} for java.util.Calendar.
 */
public class CalendarComboBox extends FormattedComboBox<Calendar> {
    private static final String STYLE_CLASS_DEFAULT = "calendar-combo-box"; //NON-NLS
    private StringProperty _patternProperty;

    /**
     * Creates a {@code CalendarComboBox} with the a default pattern and today's date.
     */
    public CalendarComboBox() {
        this(((SimpleDateFormat) SimpleDateFormat.getDateInstance()).toPattern(), Calendar.getInstance());
    }

    /**
     * Creates a {@code CalendarComboBox} with the specified pattern and the today's date.
     *
     * @param pattern the pattern of the format
     */
    public CalendarComboBox(String pattern) {
        this(pattern, Calendar.getInstance());
    }

    /**
     * Creates a {@code CalendarComboBox} with a default pattern and the specified value.
     *
     * @param calendar the initial value
     */
    public CalendarComboBox(Calendar calendar) {
        this(((SimpleDateFormat) SimpleDateFormat.getDateInstance()).toPattern(), calendar);
    }

    /**
     * Creates a {@code CalendarComboBox} with the specified pattern and the specified value.
     *
     * @param pattern  the pattern of the format
     * @param calendar the initial value
     */
    public CalendarComboBox(String pattern, Calendar calendar) {
        super(calendar);
        setPattern(pattern);
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
    }

    public StringProperty patternProperty() {
        if (_patternProperty == null) {
            _patternProperty = new SimpleStringProperty();
        }
        return _patternProperty;
    }

    /**
     * Gets the pattern.
     *
     * @return the pattern.
     */
    public String getPattern() {
        return patternProperty().get();
    }

    /**
     * Sets the pattern.
     *
     * @param pattern a new pattern
     */
    public void setPattern(String pattern) {
        patternProperty().set(pattern);
    }

    @Override
    protected FormattedTextField<Calendar> createFormattedTextField() {
        CalendarField field = new CalendarField(getPattern());
        field.patternProperty().bind(patternProperty());
        return field;
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(new Callback<Calendar, PopupContent<Calendar>>() {
            @Override
            public PopupContent<Calendar> call(Calendar param) {
                PopupContent<Calendar> popupContent = ((CalendarField) getEditor()).getPopupContentFactory().call(param);
                popupContent.valueProperty().addListener(new ChangeListener<Calendar>() {
                    @Override
                    public void changed(ObservableValue<? extends Calendar> observable, Calendar oldValue, Calendar newValue) {
                        hide();
                    }
                });
                return popupContent;
            }
        });
    }
}
