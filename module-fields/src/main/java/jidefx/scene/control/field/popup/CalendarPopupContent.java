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
package jidefx.scene.control.field.popup;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * A PopupContent for Calendar.
 */
public class CalendarPopupContent extends AbstractDatePopupContent<Calendar> {
    @Override
    protected Calendar fromLocalDate(LocalDate localDate) {
        Calendar cal = Calendar.getInstance();
        if (localDate != null) {
            cal.set(Calendar.YEAR, localDate.getYear());
            cal.set(Calendar.MONTH, localDate.getMonthValue() - 1);
            cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
        }
        return cal;
    }

    @Override
    protected LocalDate toLocalDate(Calendar value) {
        if (value != null) {
            return LocalDate.of(value.get(Calendar.YEAR), value.get(Calendar.MONTH) + 1, value.get(Calendar.DAY_OF_MONTH));
        }
        else return LocalDate.now();
    }
}
