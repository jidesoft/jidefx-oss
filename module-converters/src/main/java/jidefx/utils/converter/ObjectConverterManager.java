/*
 * @(#)ObjectConverterManager.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import jidefx.utils.converter.javafx.*;
import jidefx.utils.converter.time.LocalDateConverter;
import jidefx.utils.converter.time.LocalDateTimeConverter;
import jidefx.utils.converter.time.LocalTimeConverter;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class ObjectConverterManager {

    public static final String PROPERTY_OBJECT_CONVERTER_MANAGER = "ObjectConverterManager"; //NON-NLS

    /**
     * Multi Manager Support
     */
    private static final ObjectConverterManager _instance = createInstance();

    /**
     * Creates a new instance of the ObjectConverterManager.
     *
     * @return a new instance of the ObjectConverterManager.
     */
    public static ObjectConverterManager createInstance() {
        return new ObjectConverterManager();
    }

    /**
     * Gets the default instance of the ObjectConverterManager.
     *
     * @return the default instance of the ObjectConverterManager.
     */
    public static ObjectConverterManager getInstance() {
        return _instance;
    }

    /**
     * Gets the ObjectConverterManager from the node if the node has an ObjectConverterManager defined on the
     * Properties. If not there, return the default instance.
     *
     * @param node the node
     * @return an ObjectConverterManager.
     */
    public static ObjectConverterManager getInstance(Node node) {
        if (node != null && node.getProperties().get(PROPERTY_OBJECT_CONVERTER_MANAGER) instanceof ObjectConverterManager) {
            return (ObjectConverterManager) node.getProperties().get(PROPERTY_OBJECT_CONVERTER_MANAGER);
        }
        else {
            return getInstance();
        }
    }

    /**
     * Instance individual
     */
    private boolean _inited = false;
    private boolean _initing = false;
    private boolean _autoInit = true;

    private CacheMap<ObjectConverter, ConverterContext> _cache = new CacheMap<>(ConverterContext.CONTEXT_DEFAULT);

    private ObjectConverter _defaultConverter = new DefaultObjectConverter();

    public void resetInit() {
        _inited = false;
    }

    public void clear() {
        resetInit();
        _cache.clear();
    }

    public void initDefaultConverters() {
        if (_inited) {
            return;
        }

        _initing = true;

        try {
            // Default String Converters
            registerConverter(String.class, new DefaultObjectConverter());
            registerConverter(String.class, new MultilineStringConverter(), MultilineStringConverter.CONTEXT);

            // Default NUMBER Converters
            IntegerConverter integerConverter = new IntegerConverter();
            registerConverter(int.class, integerConverter);
            registerConverter(Integer.class, integerConverter);

            NaturalNumberConverter naturalNumberConverter = new NaturalNumberConverter();
            registerConverter(int.class, naturalNumberConverter, NaturalNumberConverter.CONTEXT);
            registerConverter(Integer.class, naturalNumberConverter, NaturalNumberConverter.CONTEXT);

            ShortConverter shortConverter = new ShortConverter();
            registerConverter(Short.class, shortConverter);
            registerConverter(short.class, shortConverter);

            LongConverter longConverter = new LongConverter();
            registerConverter(Long.class, longConverter);
            registerConverter(long.class, longConverter);

            DoubleConverter doubleConverter = new DoubleConverter();
            registerConverter(Double.class, doubleConverter);
            registerConverter(double.class, doubleConverter);

            NumberFormat format1DigitInstance = NumberFormat.getNumberInstance();
            format1DigitInstance.setMinimumFractionDigits(1);
            format1DigitInstance.setMaximumFractionDigits(1);
            DoubleConverter fraction1DigitConverter = new DoubleConverter(format1DigitInstance);
            registerConverter(Number.class, fraction1DigitConverter, AbstractNumberConverter.CONTEXT_FIXED_1_DIGIT_FRACTION);

            NumberFormat format2Digit = NumberFormat.getNumberInstance();
            format2Digit.setMinimumFractionDigits(2);
            format2Digit.setMaximumFractionDigits(2);
            DoubleConverter fraction2DigitConverter = new DoubleConverter(format2Digit);
            registerConverter(Number.class, fraction2DigitConverter, AbstractNumberConverter.CONTEXT_FIXED_2_DIGIT_FRACTION);

            NumberFormat format4Digit = NumberFormat.getNumberInstance();
            format4Digit.setMinimumFractionDigits(4);
            format4Digit.setMaximumFractionDigits(4);
            DoubleConverter fraction4DigitConverter = new DoubleConverter(format4Digit);
            registerConverter(Number.class, fraction4DigitConverter, AbstractNumberConverter.CONTEXT_FIXED_4_DIGIT_FRACTION);

            FloatConverter floatConverter = new FloatConverter();
            registerConverter(Float.class, floatConverter);
            registerConverter(float.class, floatConverter);

            registerConverter(BigDecimal.class, new BigDecimalConverter());

            ByteConverter byteConverter = new ByteConverter();
            registerConverter(Byte.class, byteConverter);
            registerConverter(byte.class, byteConverter);

            BooleanConverter booleanConverter = new BooleanConverter();
            registerConverter(Boolean.class, booleanConverter);
            registerConverter(boolean.class, booleanConverter);

            registerConverter(File.class, new FileConverter());

            QuarterNameConverter quarterNameConverter = new QuarterNameConverter();
            registerConverter(int.class, quarterNameConverter, QuarterNameConverter.CONTEXT);
            registerConverter(Integer.class, quarterNameConverter, QuarterNameConverter.CONTEXT);

            CurrencyConverter currencyConverter = new CurrencyConverter();
            registerConverter(Float.class, currencyConverter, CurrencyConverter.CONTEXT);
            registerConverter(float.class, currencyConverter, CurrencyConverter.CONTEXT);
            registerConverter(Double.class, currencyConverter, CurrencyConverter.CONTEXT);
            registerConverter(double.class, currencyConverter, CurrencyConverter.CONTEXT);

            PercentConverter percentConverter = new PercentConverter();
            registerConverter(Float.class, percentConverter, PercentConverter.CONTEXT);
            registerConverter(float.class, percentConverter, PercentConverter.CONTEXT);
            registerConverter(Double.class, percentConverter, PercentConverter.CONTEXT);
            registerConverter(double.class, percentConverter, PercentConverter.CONTEXT);

            DateConverter dateConverter = new DateConverter();
            registerConverter(Date.class, dateConverter);
            registerConverter(Date.class, dateConverter, DateConverter.CONTEXT_DATETIME);
            registerConverter(Date.class, dateConverter, DateConverter.CONTEXT_TIME);

            CalendarConverter calendarConverter = new CalendarConverter();
            registerConverter(Calendar.class, calendarConverter);
            registerConverter(Calendar.class, calendarConverter, DateConverter.CONTEXT_DATETIME);
            registerConverter(Calendar.class, calendarConverter, DateConverter.CONTEXT_TIME);
            registerConverter(Calendar.class, new YearMonthConverter(), YearMonthConverter.CONTEXT_YEAR_MONTH);

            // For java.time package available on JDK8
            registerConverter(LocalDate.class, new LocalDateConverter());
            registerConverter(LocalDateTime.class, new LocalDateTimeConverter());
            registerConverter(LocalTime.class, new LocalTimeConverter());
            registerConverter(YearMonth.class, new YearMonthConverter());

            MonthNameConverter monthNameConverter = new MonthNameConverter();
            registerConverter(Integer.class, monthNameConverter, MonthNameConverter.CONTEXT);
            registerConverter(int.class, monthNameConverter, MonthNameConverter.CONTEXT);

            registerConverter(Color.class, new RgbColorConverter());
            registerConverter(Color.class, new HexColorConverter(), ColorConverter.CONTEXT_HEX);
            registerConverter(Color.class, new RgbColorConverter(true), ColorConverter.CONTEXT_RGBA);
            registerConverter(Color.class, new HexColorConverter(true), ColorConverter.CONTEXT_HEX_WITH_ALPHA);
            registerConverter(Color.class, new WebColorConverter(), ColorConverter.CONTEXT_WEB);

            registerConverter(Point2D.class, new Point2DConverter());
            registerConverter(Point3D.class, new Point3DConverter());
            registerConverter(Rectangle2D.class, new Rectangle2DConverter());
            registerConverter(Dimension2D.class, new Dimension2DConverter());
            registerConverter(Insets.class, new InsetsConverter());
            registerConverter(BoundingBox.class, new BoundingBoxConverter());

            registerConverter(Font.class, new FontConverter());

            // enums
            registerConverter(Enum.class, new EnumConverter());

            // Default Array Converters
            registerConverter(String[].class, new StringArrayConverter());
            registerConverter(int[].class, new DefaultValuesConverter<Integer>("; ", int.class));
            registerConverter(float[].class, new DefaultValuesConverter<Float>("; ", float.class));
            registerConverter(double[].class, new DefaultValuesConverter<Double>("; ", double.class));
            registerConverter(long[].class, new DefaultValuesConverter<Long>("; ", long.class));
            registerConverter(short[].class, new DefaultValuesConverter<Short>("; ", short.class));
            registerConverter(Object[].class, new DefaultValuesConverter<>("; ", Object.class));
            registerConverter(String[].class, new DefaultValuesConverter<String>("; ", String.class));
            registerConverter(Date[].class, new DefaultValuesConverter<Date>("; ", Date.class));
            registerConverter(Calendar[].class, new DefaultValuesConverter<Calendar>("; ", Calendar.class));
            registerConverter(Number[].class, new DefaultValuesConverter<Number>("; ", Number.class));
            registerConverter(Integer[].class, new DefaultValuesConverter<Integer>("; ", Integer.class));
            registerConverter(Float[].class, new DefaultValuesConverter<Float>("; ", Float.class));
            registerConverter(Double[].class, new DefaultValuesConverter<Double>("; ", Double.class));
            registerConverter(Long[].class, new DefaultValuesConverter<Long>("; ", Long.class));
            registerConverter(Short[].class, new DefaultValuesConverter<Short>("; ", Short.class));
        }
        finally {
            _initing = false;
            _inited = true;
        }
    }

    public void registerConverter(Class<?> clazz, ObjectConverter converter, ConverterContext context) {
        if (clazz == null) {
            throw new IllegalArgumentException("Parameter class cannot be null");
        }

        if (context == null) {
            context = ConverterContext.CONTEXT_DEFAULT;
        }

        if (isAutoInit() && !_inited && !_initing) {
            initDefaultConverters();
        }

        _cache.register(clazz, converter, context);
    }

    public void registerConverter(Class<?> clazz, ObjectConverter converter) {
        registerConverter(clazz, converter, ConverterContext.CONTEXT_DEFAULT);
    }

    public void unregisterConverter(Class<?> clazz, ConverterContext context) {
        if (context == null) {
            context = ConverterContext.CONTEXT_DEFAULT;
        }

        if (isAutoInit() && !_inited && !_initing) {
            initDefaultConverters();
        }

        _cache.unregister(clazz, context);
    }

    public void unregisterConverter(Class<?> clazz) {
        unregisterConverter(clazz, ConverterContext.CONTEXT_DEFAULT);
    }

    public void unregisterAllConverters(Class<?> clazz) {
        _cache.remove(clazz);
    }

    public void unregisterAllConverters() {
        _cache.clear();
    }

    public <T> ObjectConverter<T> getConverter(Class<?> clazz) {
        return getConverter(clazz, ConverterContext.CONTEXT_DEFAULT);
    }

    public <T> ObjectConverter<T> getConverter(Class<?> clazz, ConverterContext context) {
        if (isAutoInit() && !_inited && !_initing) {
            initDefaultConverters();
        }

        if (context == null) {
            context = ConverterContext.CONTEXT_DEFAULT;
        }

        ObjectConverter converter = _cache.getRegisteredObject(clazz, context);
        if (converter != null) {
            if (converter instanceof LazyInitializeConverter) {
                ((LazyInitializeConverter) converter).initialize(clazz, context);
            }
            return converter;
        }
        else {
            if (clazz != null && clazz.isArray()) {
                DefaultValuesConverter defaultArrayConverter = new DefaultValuesConverter("; ", clazz.getComponentType());
                registerConverter(clazz, defaultArrayConverter);
                return defaultArrayConverter;
            }
            return _defaultConverter;
        }
    }

    public String toString(Object object) {
        if (object != null) {
            return toString(object, object.getClass(), ConverterContext.CONTEXT_DEFAULT);
        }
        else {
            return "";
        }
    }

    public String toString(Object object, Class<?> clazz) {
        return toString(object, clazz, ConverterContext.CONTEXT_DEFAULT);
    }

    public String toString(Object object, Class<?> clazz, ConverterContext context) {
        ObjectConverter converter = getConverter(clazz, context);
        if (converter != null) {
            if (converter instanceof RequiringConverterManager) {
                context.getProperties().put(ConverterContext.PROPERTY_OBJECT_CONVERTER_MANAGER, this);
            }
            String s;
            try {
                s = converter.toString(object, context);
            }
            finally {
                if (converter instanceof RequiringConverterManager) {
                    context.getProperties().remove(ConverterContext.PROPERTY_OBJECT_CONVERTER_MANAGER);
                }
            }
            return s;
        }
        else if (object == null) {
            return "";
        }
        else {
            return object.toString();
        }
    }

    public Object fromString(String string, Class<?> clazz) {
        return fromString(string, clazz, ConverterContext.CONTEXT_DEFAULT);
    }

    public Object fromString(String string, Class<?> clazz, ConverterContext context) {
        ObjectConverter converter = getConverter(clazz, context);
        if (converter != null) {
            Object value;
            try {
                if (converter instanceof RequiringConverterManager) {
                    context.getProperties().put(ConverterContext.PROPERTY_OBJECT_CONVERTER_MANAGER, this);
                }
                value = converter.fromString(string, context);
            }
            finally {
                if (converter instanceof RequiringConverterManager) {
                    context.getProperties().remove(ConverterContext.PROPERTY_OBJECT_CONVERTER_MANAGER);
                }
            }
            if (value != null && clazz != null && !clazz.isAssignableFrom(value.getClass())) {
                if (TypeUtils.isNumericType(clazz) && value instanceof Number) {
                    clazz = TypeUtils.convertPrimitiveToWrapperType(clazz);
                    if (clazz == Double.class) {
                        return ((Number) value).doubleValue();
                    }
                    if (clazz == Byte.class) {
                        return ((Number) value).byteValue();
                    }
                    if (clazz == Short.class) {
                        return ((Number) value).shortValue();
                    }
                    if (clazz == Integer.class) {
                        return ((Number) value).intValue();
                    }
                    if (clazz == Long.class) {
                        return ((Number) value).longValue();
                    }
                    if (clazz == Float.class) {
                        return ((Number) value).floatValue();
                    }
                }
            }
            return value;
        }
        else {
            return null;
        }
    }

    public boolean isAutoInit() {
        return _autoInit;
    }

    public void setAutoInit(boolean autoInit) {
        _autoInit = autoInit;
    }

    public ConverterContext[] getConverterContexts(Class<?> clazz) {
        return _cache.getKeys(clazz, new ConverterContext[0]);
    }
}