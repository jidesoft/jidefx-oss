/*
 * @(#)CalendarComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
