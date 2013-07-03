/*
 * @(#)LocalDatePopupContent.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.popup;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A PopupContent for LocalDateTime.
 */
public class LocalDateTimePopupContent extends AbstractDatePopupContent<LocalDateTime> {
    private LocalDateTime _oldValue = null;

    @Override
    public LocalDateTime fromLocalDate(LocalDate localDate) {
        if (_oldValue != null) {
            return _oldValue.with(localDate);
        }
        return null;
    }

    @Override
    public LocalDate toLocalDate(LocalDateTime value) {
        _oldValue = value;
        return value == null ? null : LocalDate.from(value);
    }
}
