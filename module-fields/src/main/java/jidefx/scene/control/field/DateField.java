/*
 * @(#)DateField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.shape.Shape;
import javafx.util.Callback;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.decoration.PredefinedDecorators;
import jidefx.scene.control.field.popup.DatePopupContent;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.verifier.PatternVerifierUtils;
import jidefx.utils.CommonUtils;
import jidefx.utils.PredefinedShapes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A {@code PopupField} for {@link Date}.
 */
@SuppressWarnings({"UnusedDeclaration", "Convert2Lambda"})
public class DateField extends PopupField<Date> {
    public DateField() {
        this(SimpleDateFormat.getDateInstance());
    }

    public DateField(Date value) {
        this(SimpleDateFormat.getDateInstance(), value);
    }

    public DateField(String pattern) {
        this(new SimpleDateFormat(pattern));
    }

    public DateField(DateFormat format) {
        this(format, Calendar.getInstance().getTime());
    }

    public DateField(String pattern, Date value) {
        this(new SimpleDateFormat(pattern), value);
    }

    public DateField(DateFormat format, Date value) {
        setDateFormat(format);
        setValue(value);
    }

    private static final String STYLE_CLASS_DEFAULT = "date-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setPopupContentFactory(new Callback<Date, PopupContent<Date>>() {
            @Override
            public PopupContent<Date> call(Date param) {
                DatePopupContent content = new DatePopupContent();
                content.setValue(getValue());
                return content;
            }
        });
    }

    @Override
    protected void customizePopupContent(PopupContent<Date> popupContent) {
        super.customizePopupContent(popupContent);
        popupContent.valueProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {
                hide();
            }
        });
    }

    @Override
    protected String toString(Date value) {
        DateFormat format = getDateFormat();
        if (format != null) {
            try {
                return format.format(value);
            }
            catch (Exception e) {
                CommonUtils.ignoreException(e);
            }
        }
        return super.toString(value);
    }

    @Override
    protected Date fromString(String text) {
        DateFormat format = getDateFormat();
        if (format != null) {
            try {
                return format.parse(text);
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
                    PatternVerifierUtils.initializePatternVerifiersForDateFormatUsingDate(getPatternVerifiers());
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
                return new Decorator<>(createButton(shape), Pos.CENTER_RIGHT, new Point2D(-70, 0));
            }
        }.get();
    }

    /**
     * A static method which will create a DateField.
     * <p>
     * The initial value is set to the current time.
     *
     * @param dateFormat the format string as defined in {@link SimpleDateFormat}.
     * @return a FormattedTextField for Date.
     */
    public static DateField createDateField(String dateFormat) {
        return createDateField(dateFormat, Calendar.getInstance().getTime());
    }

    /**
     * A static method which will create a FormattedTextField using Date.
     *
     * @param dateFormat   the format string as defined in {@link SimpleDateFormat}.
     * @param initialValue the initial value.
     * @return a FormattedTextField for the Date or the Time.
     */
    public static DateField createDateField(String dateFormat, Date initialValue) {
        return new DateField(dateFormat, initialValue);
    }

    /**
     * A static method which will create a FormattedTextField using Date.
     *
     * @param dateFormat   the DateFormat.
     * @param initialValue the initial value.
     * @return a FormattedTextField for the Date or the Time.
     */
    public static DateField createDateField(DateFormat dateFormat, Date initialValue) {
        return new DateField(dateFormat, initialValue);
    }

    /**
     * A static method which will create a FormattedTextField for the Time in the default format.
     * <p>
     * The initial value is set to the current time.
     *
     * @return a FormattedTextField for the time.
     */
    public static DateField createTimeField() {
        return createDateField(SimpleDateFormat.getTimeInstance(), Calendar.getInstance().getTime());
    }

    /**
     * A static method which will create a FormattedTextField for the Time in the specified DateFormat dateFormatStyle.
     * <p>
     * The initial value is set to the current time.
     *
     * @param dateFormatStyle the format style as defined in DateFormat. Could DateFormat.FULL, DateFormat.LONG,
     *                        DateFormat.MEDIUM, DateFormat.SHORT.
     * @return a FormattedTextField for the Time.
     */
    public static DateField createTimeField(int dateFormatStyle) {
        return createDateField(SimpleDateFormat.getTimeInstance(dateFormatStyle), Calendar.getInstance().getTime());
    }

    /**
     * A static method which will create a FormattedTextField for the Date in the default format.
     * <p>
     * The initial value is set to the current time.
     *
     * @return a FormattedTextField for the Date.
     */
    public static DateField createDateField() {
        return createDateField(SimpleDateFormat.getDateInstance(), Calendar.getInstance().getTime());
    }

    /**
     * A static method which will create a FormattedTextField for the Date in the specified DateFormat dateFormatStyle.
     * <p>
     * The initial value is set to the current time.
     *
     * @param dateFormatStyle the format style as defined in DateFormat. Could DateFormat.FULL, DateFormat.LONG,
     *                        DateFormat.MEDIUM, DateFormat.SHORT.
     * @return a FormattedTextField for the Date.
     */
    public static DateField createDateField(int dateFormatStyle) {
        return createDateField(SimpleDateFormat.getDateInstance(dateFormatStyle), Calendar.getInstance().getTime());
    }

    /**
     * A static method which will create a FormattedTextField for the Date and Time in the default format.
     * <p>
     * The initial value is set to the current time.
     *
     * @return a FormattedTextField for the Date and Time.
     */
    public static DateField createDateTimeField() {
        return createDateField(SimpleDateFormat.getDateTimeInstance(), Calendar.getInstance().getTime());
    }

    /**
     * A static method which will create a FormattedTextField for the Date and Time in the specified dateFormatStyle and
     * timeFormatStyle.
     * <p>
     * The initial value is set to the current time.
     *
     * @param dateFormatStyle the format style as defined in DateFormat. Could DateFormat.FULL, DateFormat.LONG,
     *                        DateFormat.MEDIUM, DateFormat.SHORT.
     * @param timeFormatStyle the format style as defined in DateFormat. Could DateFormat.FULL, DateFormat.LONG,
     *                        DateFormat.MEDIUM, DateFormat.SHORT.
     * @return a FormattedTextField for the Date.
     */
    public static DateField createDateTimeField(int dateFormatStyle, int timeFormatStyle) {
        return createDateField(SimpleDateFormat.getDateTimeInstance(dateFormatStyle, timeFormatStyle), Calendar.getInstance().getTime());
    }

}
