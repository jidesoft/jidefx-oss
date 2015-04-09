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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.shape.Shape;
import javafx.util.Callback;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.decoration.PredefinedDecorators;
import jidefx.scene.control.field.popup.CalendarPopupContent;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.verifier.PatternVerifierUtils;
import jidefx.utils.CommonUtils;
import jidefx.utils.PredefinedShapes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A {@code PopupField} for {@link Calendar}.
 */
@SuppressWarnings({"Convert2Lambda", "UnusedDeclaration"})
public class CalendarField extends PopupField<Calendar> {
    public CalendarField() {
        this(SimpleDateFormat.getDateInstance());
    }

    public CalendarField(String pattern) {
        if (pattern != null) {
            setDateFormat(new SimpleDateFormat(pattern));
        }
    }

    public CalendarField(DateFormat format) {
        setDateFormat(format);
    }

    private static final String STYLE_CLASS_DEFAULT = "calendar-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected String toString(Calendar value) {
        DateFormat format = getDateFormat();
        if (format != null) {
            try {
                return format.format(value.getTime());
            }
            catch (Exception e) {
                CommonUtils.ignoreException(e);
            }
        }
        return super.toString(value);
    }

    @Override
    protected Calendar fromString(String text) {
        DateFormat format = getDateFormat();
        if (format != null) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(format.parse(text));
                return cal;
            }
            catch (ParseException e) {
                CommonUtils.ignoreException(e);
            }
        }
        return super.fromString(text);
    }

    @Override
    protected boolean supportFromString() {
        return getDateFormat() != null || super.supportFromString();
    }

    private ObjectProperty<DateFormat> _dateFormatProperty;

    public ObjectProperty<DateFormat> dateFormatProperty() {
        if (_dateFormatProperty == null) {
            _dateFormatProperty = new SimpleObjectProperty<DateFormat>(this, "dateFormat") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    DateFormat format = get();
                    PatternVerifierUtils.initializePatternVerifiersForDateFormatUsingCalendar(getPatternVerifiers());
                    if (format instanceof SimpleDateFormat && getPattern() == null) {
                        setPattern(((SimpleDateFormat) format).toPattern());
                    }
                    setStringConverter(null);
                }
            };
        }
        return _dateFormatProperty;
    }

    /**
     * Gets the format.
     *
     * @return the format.
     */
    public DateFormat getDateFormat() {
        return dateFormatProperty().get();
    }

    /**
     * Sets the DateFormat that will format the Date.
     *
     * @param format the new DateFormat.
     */
    public void setDateFormat(DateFormat format) {
        dateFormatProperty().set(format);
    }

    @Override
    protected Decorator<Button> createPopupButtonDecorator() {
        return new PredefinedDecorators.AbstractButtonDecoratorSupplier() {
            @Override
            public Decorator<Button> get() {
                Shape calendarIcon = PredefinedShapes.getInstance().createCalendarIcon(15);
                Shape shape = PredefinedShapes.getInstance().createArrowedIcon(calendarIcon);
                return new Decorator<>(createButton(shape), Pos.CENTER_RIGHT, new Point2D(-70, 0), new Insets(0, 100, 0, 0));
            }
        }.get();
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setPopupContentFactory(new Callback<Calendar, PopupContent<Calendar>>() {
            @Override
            public PopupContent<Calendar> call(Calendar param) {
                CalendarPopupContent content = new CalendarPopupContent();
                content.setValue(getValue());
                return content;
            }
        });
    }

    @Override
    protected void customizePopupContent(PopupContent<Calendar> popupContent) {
        super.customizePopupContent(popupContent);
        popupContent.valueProperty().addListener(new ChangeListener<Calendar>() {
            @Override
            public void changed(ObservableValue<? extends Calendar> observable, Calendar oldValue, Calendar newValue) {
                hide();
            }
        });
    }

    /**
     * A static method which will create a CalendarField for Calendar.
     *
     * @param dateFormat   the format string as defined in {@link SimpleDateFormat}.
     * @param initialValue the initial value.
     * @return a CalendarField for Calendar.
     */
    public static CalendarField createCalendarField(String dateFormat, Calendar initialValue) {
        CalendarField field = new CalendarField(dateFormat);
        field.setValue(initialValue);
        return field;
    }

    /**
     * A static method which will create a CalendarField for Calendar.
     *
     * @param initialValue the initial value.
     * @return a CalendarField for Calendar.
     */
    public static CalendarField createCalendarField(Calendar initialValue) {
        CalendarField field = new CalendarField();
        field.setValue(initialValue);
        return field;
    }

    /**
     * A static method which will create a CalendarField for Calendar.
     *
     * @return a CalendarField for Calendar.
     */
    public static CalendarField createCalendarField() {
        return new CalendarField();
    }
}
