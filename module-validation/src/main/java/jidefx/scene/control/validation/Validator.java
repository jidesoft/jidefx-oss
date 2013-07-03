/*
 * @(#)Validator.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.validation;

import javafx.util.Callback;

/**
 * {@code Validator} is a callback that takes an {@code ValidationObject} and returns a {@code ValidationEvent}.
 */
public interface Validator extends Callback<ValidationObject, ValidationEvent> {
}
