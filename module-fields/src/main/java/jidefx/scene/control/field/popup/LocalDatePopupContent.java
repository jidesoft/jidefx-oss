/*
 * @(#)LocalDatePopupContent.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.popup;

import java.time.LocalDate;

/**
 * A PopupContent for LocalDateTime.
 */
public class LocalDatePopupContent extends AbstractDatePopupContent<LocalDate> {
    @Override
    public LocalDate fromLocalDate(LocalDate localDate) {
        return localDate;
    }

    @Override
    public LocalDate toLocalDate(LocalDate value) {
        return value;
    }
}
