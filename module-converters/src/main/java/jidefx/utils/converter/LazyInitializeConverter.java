/*
 * @(#)LazyInitializeConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

/**
 * {@code LazyInitializeConverter} is an interface that can be implemented by any object converters to support lazy
 * initialization.
 * <p>
 * The first reason to use the lazy initialization is when the conversion logic requires the knowledge of the actual
 * data type or the converter context.
 * <p>
 * For example, for enum converter, we won't know how to convert a value until we know what the enum type is. We could
 * register a converter for each enum type, but that's way too many. So in the ObjectConverterManager, we only have one
 * entry for all enum types using registerConverter(Enum.class, new EnumConverter()). EnumConverter implements this
 * LazyInitializeConverter. ObjectConverterManager will call the initialize method with the actual enum type when it
 * sees a converter implementing LazyInitializeConverter. In the initialize method of EnumConverter, we will get all
 * enum constants and save them as an array. When toString or fromString is called, we will look up in the array to do
 * the conversion. By using this interface, we don't need to register a converter for each enum type.
 * <p>
 * Another reason to use this interface is when the the converter takes time to create. So instead of initializing the
 * logic up front in the constructor, you can put the expensive logic in this initialize method.
 */
public interface LazyInitializeConverter {
    /**
     * Initialize the converter. This method will be called by {@link ObjectConverterManager} in the getConverter
     * method.
     *
     * @param clazz            the actual data type.
     * @param converterContext the actual converter context.
     */
    void initialize(Class<?> clazz, ConverterContext converterContext);
}
