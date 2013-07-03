/*
 * @(#)StringCalendarFieldPatternVerifier.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.verifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A pattern verifier that verifies the Calendar field that is of String type.
 */
public class StringCalendarFieldPatternVerifier extends CalendarFieldPatternVerifier implements PatternVerifier.Formatter<Integer>, PatternVerifier.Parser<Integer>, PatternVerifier.Enums<String> {
    private final List<String> values;

    public StringCalendarFieldPatternVerifier(int field, String[] values) {
        super(field);
        this.values = new ArrayList<>();
        Collections.addAll(this.values, values);
    }

    @Override
    public String format(Integer value) {
        return values.get(value);
    }

    @Override
    public Integer parse(String text) {
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            if (value.startsWith(text)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Boolean call(String text) {
        for (String value : values) {
            if (value.startsWith(text)) {
                return true;
            }
        }
        return values.contains(text);
    }

    @Override
    public List<String> getValues() {
        return values;
    }
}
