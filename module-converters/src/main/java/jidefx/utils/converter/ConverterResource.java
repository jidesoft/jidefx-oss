/*
 * @(#)ConverterResource.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Resource for the converters.
 */
public class ConverterResource {
    static final String BASENAME = "jidefx.utils.converter.converter"; //NON-NLS

    static final ResourceBundle RB = ResourceBundle.getBundle(BASENAME);

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(BASENAME, locale);
    }
}
