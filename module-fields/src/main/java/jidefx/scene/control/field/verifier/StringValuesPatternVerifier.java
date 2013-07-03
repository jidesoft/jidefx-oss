/*
 * @(#)StringValuesPatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

import java.util.List;

/**
 * A special verifier that restricts the input string to be in a String array. The order of the string in the array will
 * be used to implement the getNextValue and getPreviousValue methods.
 */
public abstract class StringValuesPatternVerifier<T> extends ValuesPatternVerifier<T, String> {
    public StringValuesPatternVerifier() {
    }

    public StringValuesPatternVerifier(String[] values) {
        super(values);
    }

    @Override
    public String format(String value) {
        return value;
    }

    @Override
    public String parse(String text) {
        List<String> values = getValues();
        for (String value : values) {
            if (value.startsWith(text)) {
                return value;
            }
        }
        return text;
    }

    @Override
    protected boolean matches(String current, String value) {
        return value.startsWith(current);
    }
}
