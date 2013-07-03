/*
 * @(#)SimpleValidator.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.decoration;

import jidefx.scene.control.validation.ValidationEvent;
import jidefx.scene.control.validation.ValidationObject;
import jidefx.scene.control.validation.Validator;
import jidefx.utils.ReflectionUtils;

public class SimpleValidator implements Validator {
    private Object _validator;

    public SimpleValidator(Object validator) {
        _validator = validator;
    }

    @Override
    public ValidationEvent call(ValidationObject param) {
        try {
            Object valid = ReflectionUtils.callAny(_validator, "isValid", new Class[]{String.class}, new Object[]{param.getNewValue().toString()});
            String name = _validator.getClass().getSimpleName();
            int index = name.indexOf("Validator");
            return Boolean.FALSE.equals(valid) ? new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, ValidationEvent.FailBehavior.PERSIST, "Invalid " + name.substring(0, index) + "!")
                    : ValidationEvent.OK;
        }
        catch (Exception e) {
            return ValidationEvent.UNKNOWN;
        }
    }
}
