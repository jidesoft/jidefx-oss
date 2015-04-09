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
