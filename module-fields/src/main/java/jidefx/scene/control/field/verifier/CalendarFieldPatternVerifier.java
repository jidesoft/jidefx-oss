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

import java.util.Calendar;

/**
 * {@code BaseCalendarFieldPatternVerifier} is an abstract implementation of the pattern verifier for the Date
 * type. It can verify a Calendar field.
 */
public abstract class CalendarFieldPatternVerifier extends PatternVerifier<Calendar> implements PatternVerifier.Value<Calendar, Calendar>,
        PatternVerifier.Adjustable<Integer>, PatternVerifier.Range<Integer> {
    private int field;
    private Calendar value;
    private int min;
    private int max;
    private boolean minMaxSet = true;

    public CalendarFieldPatternVerifier(int field) {
        this(field, Calendar.getInstance().getMinimum(field), Calendar.getInstance().getMaximum(field));
        minMaxSet = false;
    }

    CalendarFieldPatternVerifier(int field, int min, int max) {
        this.field = field;
        this.min = min;
        this.max = max;
        value = Calendar.getInstance();
    }

    @Override
    public Integer getMin() {
        return !minMaxSet && value != null ? value.getActualMinimum(field) : min;
    }

    @Override
    public Integer getMax() {
        return !minMaxSet && value != null ? value.getActualMaximum(field) : max;
    }

    @Override
    public Integer getEnd(Integer current) {
        if (value != null) {
            value.set(field, getMax());
        }
        return null;
    }

    @Override
    public Integer getHome(Integer current) {
        if (value != null) {
            value.set(field, getMin());
        }
        return null;
    }

    @Override
    public Integer getPreviousPage(Integer current, boolean restart) {
        if (value != null) {
            if (restart) {
                value.add(field, -10);
            }
            else {
                if (value.get(field) - getMin() > 10) {
                    value.add(field, -10);
                }
                else {
                    value.set(field, getMin());
                }
            }
        }
        return null;
    }

    @Override
    public Integer getNextPage(Integer current, boolean restart) {
        if (value != null) {
            if (restart) {
                value.add(field, 10);
            }
            else {
                if (getMax() - value.get(field) > 10) {
                    value.add(field, 10);
                }
                else {
                    value.set(field, getMax());
                }
            }
        }
        return null;
    }

    @Override
    public Integer getNextValue(Integer current, boolean restart) {
        if (value != null) {
            if (restart) {
                value.add(field, 1);
            }
            else {
                if (value.get(field) != getMax()) {
                    value.add(field, 1);
                }
            }
        }
        return null;
    }

    @Override
    public Integer getPreviousValue(Integer current, boolean restart) {
        if (value != null) {
            if (restart) {
                value.add(field, -1);
            }
            else {
                if (value.get(field) != getMin()) {
                    value.add(field, -1);
                }
            }
        }
        return null;
    }

    @Override
    public void setFieldValue(Calendar fieldValue) {
        if (fieldValue != null) {
            value = fieldValue;
        }
    }

    @Override
    public Calendar getFieldValue() {
        return value;
    }

    @Override
    public Calendar toTargetValue(Calendar fieldValue) {
        return fieldValue;
    }

    @Override
    public Calendar fromTargetValue(Calendar existingValue, Calendar value) {
        return value;
    }
}
