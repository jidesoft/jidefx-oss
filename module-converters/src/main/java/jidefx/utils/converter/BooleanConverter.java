/*
 * @(#)BooleanConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.util.Locale;

/**
 * {@link ObjectConverter} implementation for {@link Boolean} (and boolean primitive) values. The {@link
 * #toString(Boolean, ConverterContext)} will return a localized boolean text. The {@link #fromString(String,
 * ConverterContext)} will return true or false for both localized boolean text as well as the English version text
 * (such as "true" or "false").
 */
public class BooleanConverter extends DefaultObjectConverter<Boolean> {

    public BooleanConverter() {
    }

    @Override
    public String toString(Boolean object, ConverterContext context) {
        if (Boolean.FALSE.equals(object)) {
            return getFalse();
        }
        else if (Boolean.TRUE.equals(object)) {
            return getTrue();
        }
        else {
            return getNull();
        }
    }

    @Override
    public Boolean fromString(String string, ConverterContext context) {
        if (string.equalsIgnoreCase(getTrue())) {
            return Boolean.TRUE;
        }
        else if (string.equalsIgnoreCase("true")) { // in case the application runs under different locale, we still consider "true" is true. NON-NLS
            return Boolean.TRUE;
        }
        else if (string.equalsIgnoreCase(getFalse())) {
            return Boolean.FALSE;
        }
        else if (string.equalsIgnoreCase("false")) { // in case the application runs under different locale, we still consider "false" is false. NON-NLS
            return Boolean.FALSE;
        }
        else {
            return null;
        }
    }

    /**
     * Get the string to represent the true value. By default, it's "true". You could override this method to customize
     * the string.
     *
     * @return the string to represent the true value.
     */
    protected String getTrue() {
        String s = ConverterResource.getResourceBundle(Locale.getDefault()).getString("Boolean.true");
        return s == null ? getNull() : s.trim();
    }

    /**
     * Get the string to represent the false value. By default, it's "false". You could override this method to
     * customize the string.
     *
     * @return the string to represent the false value.
     */
    protected String getFalse() {
        String s = ConverterResource.getResourceBundle(Locale.getDefault()).getString("Boolean.false");
        return s == null ? getNull() : s.trim();
    }

    /**
     * Get the string to represent the null value. By default, it's "". You could override this method to customize the
     * string.
     *
     * @return the string to represent the null value.
     */
    protected String getNull() {
        return "";
    }
}
