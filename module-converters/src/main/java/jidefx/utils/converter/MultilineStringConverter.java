/*
 * @(#)MultilineStringConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

/**
 * An ObjectConverter for multi-line string. It basically escape "\r" and "\n" in the toString method so that a label
 * can display the whole string in one line instead of automatically wrapping the lines, and un-escape them in the
 * fromString method.
 */
public class MultilineStringConverter extends DefaultObjectConverter<String> {
    public static final ConverterContext CONTEXT = new ConverterContext("MultilineString"); //NON-NLS

    @Override
    public String toString(String object, ConverterContext context) {
        if (object != null) {
            return object.replaceAll("\\\\n", "\\\\\\\\n").replaceAll("\\\\r", "\\\\\\\\r").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r"); //NON-NLS
        }
        else {
            return "";
        }
    }

    @Override
    public String fromString(String string, ConverterContext context) {
        if (string != null) {
            return string.replaceAll("\\\\\\\\n", "\\\\n").replaceAll("\\\\\\\\r", "\\\\r"); //NON-NLS
        }
        else {
            return null;
        }
    }

}