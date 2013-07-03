/*
 * @(#)ValidationMode.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.validation;

/**
 * {@code ValidationMode} defines when the validation will be triggered.
 */
public enum ValidationMode {
    /**
     * Validation will be triggered when user types.
     */
    ON_FLY,

    /**
     * Validation will be triggered when the field loses focus.
     */
    ON_FOCUS_LOST,

    /**
     * Validation will be triggered when called explicitly.
     */
    ON_DEMAND
}