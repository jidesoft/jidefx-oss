/*
 * @(#)ConverterContextSupport.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

/**
 * The interface indicates the class who extends it can support ConverterContext.
 *
 * @see ConverterContext
 */
public interface ConverterContextSupport {

    /**
     * Sets the converter context.
     *
     * @param context converter context
     */
    void setConverterContext(ConverterContext context);

    /**
     * Gets the converter context.
     *
     * @return the converter context
     */
    ConverterContext getConverterContext();

    /**
     * Gets the type of the value.
     *
     * @return the type of the value.
     */
    Class<?> getType();

    /**
     * Sets the type of the value.
     *
     * @param clazz the type
     */
    void setType(Class<?> clazz);

}
