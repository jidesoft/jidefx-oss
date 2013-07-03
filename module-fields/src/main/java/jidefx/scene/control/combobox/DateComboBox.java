/*
 * @(#)DateComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import jidefx.scene.control.field.DateField;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.popup.PopupContent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A {@code FormattedComboBox} for java.util.Date.
 */
public class DateComboBox extends FormattedComboBox<Date> {
    private static final String STYLE_CLASS_DEFAULT = "date-combo-box"; //NON-NLS
    private StringProperty _patternProperty;

    /**
     * Creates a {@code DateComboBox} with a default pattern and today's date.
     */
    public DateComboBox() {
        this(((SimpleDateFormat) SimpleDateFormat.getDateInstance()).toPattern(), Calendar.getInstance().getTime());
    }

    /**
     * Creates a {@code DateComboBox} with the specified pattern and the today's date.
     *
     * @param pattern  the pattern of the format
     */
    public DateComboBox(String pattern) {
        this(pattern, Calendar.getInstance().getTime());
    }

    /**
     * Creates a {@code DateComboBox} with a default pattern and the specified value.
     *
     * @param date the initial value
     */
    public DateComboBox(Date date) {
        this(((SimpleDateFormat) SimpleDateFormat.getDateInstance()).toPattern(), date);
    }

    /**
     * Creates a {@code DateComboBox} with the specified pattern and the specified value.
     *
     * @param pattern  the pattern of the format
     * @param date the initial value
     */
    public DateComboBox(String pattern, Date date) {
        super(date);
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
    protected FormattedTextField<Date> createFormattedTextField() {
        DateField field = new DateField(getPattern());
        field.patternProperty().bind(patternProperty());
        return field;
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(new Callback<Date, PopupContent<Date>>() {
            @Override
            public PopupContent<Date> call(Date param) {
                PopupContent<Date> popupContent = ((DateField) getEditor()).getPopupContentFactory().call(param);
                popupContent.valueProperty().addListener(new ChangeListener<Date>() {
                    @Override
                    public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {
                        hide();
                    }
                });
                return popupContent;
            }
        });
    }
}
