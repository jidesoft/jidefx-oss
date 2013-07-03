/*
 * @(#)PopupContent.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.popup;

import javafx.beans.value.ObservableValue;

/**
 * An interface for all the popup content of the PopupFields or the ComboBoxes. It has three methods but all are for the
 * same reason - the value that is represented by the popup. The ComboBox or the PopupField will set the value using the
 * {@link #setValue(Object)} method and also listen to the change of the value using the {@link #valueProperty()}.
 *
 * @param <T> the data type that the PopupContent represents.
 */
public interface PopupContent<T> {

    /**
     * The observable value that the owner of the popup content can listen to.
     *
     * @return the value property.
     */
    ObservableValue<T> valueProperty();

    /**
     * Gets the value.
     *
     * @return the value
     */
    T getValue();

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    void setValue(T value);
}
