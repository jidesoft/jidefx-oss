/*
 * @(#)DatePopupContent.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
