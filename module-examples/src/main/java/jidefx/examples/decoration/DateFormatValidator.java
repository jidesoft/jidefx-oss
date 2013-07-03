/*
 * @(#)DateFormatValidator.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.decoration;

import jidefx.scene.control.validation.ValidationEvent;
import jidefx.scene.control.validation.ValidationObject;
import jidefx.scene.control.validation.Validator;
import jidefx.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatValidator implements Validator {
    private String _dateFormatPattern;
    private SimpleDateFormat _dateFormat;

    public DateFormatValidator(String dateFormatPattern) {
        _dateFormatPattern = dateFormatPattern;
        try {
            _dateFormat = new SimpleDateFormat(_dateFormatPattern);
        }
        catch (Exception e) {
            CommonUtils.ignoreException(e);
        }
    }

    @Override
    public ValidationEvent call(ValidationObject param) {
        if (_dateFormat != null) {
            try {
                _dateFormat.parse("" + param.getNewValue());
                return ValidationEvent.OK;
            }
            catch (ParseException e) {
                CommonUtils.ignoreException(e);
            }
        }
        return new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, "Invalid time!");
    }
}
