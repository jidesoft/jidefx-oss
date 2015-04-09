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
import java.util.Date;

/**
 * A PopupContent for Date.
 */
public class DatePopupContent extends AbstractDatePopupContent<Date> {
    @Override
    protected Date fromLocalDate(LocalDate localDate) {
        Calendar cal = Calendar.getInstance();
        if (localDate != null) {
            cal.set(Calendar.YEAR, localDate.getYear());
            cal.set(Calendar.MONTH, localDate.getMonthValue() - 1);
            cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
        }
        return cal.getTime();
    }

    @Override
    protected LocalDate toLocalDate(Date value) {
        if (value != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(value);
            return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        }
        else return LocalDate.now();
    }
}
