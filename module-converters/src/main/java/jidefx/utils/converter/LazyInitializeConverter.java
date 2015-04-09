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
