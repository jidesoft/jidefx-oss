/*
 * @(#)CalendarPopupContent.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
