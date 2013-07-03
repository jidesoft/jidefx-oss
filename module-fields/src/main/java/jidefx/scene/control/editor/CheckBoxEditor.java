/*
 * @(#)TextFieldCellEditor.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.editor;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

/**
 * An editor based on {@code CheckBox} for {@code Boolean}.
 */
public class CheckBoxEditor extends CheckBox implements Editor<Boolean> {
    @Override
    public ObservableValue<Boolean> observableValue() {
        return selectedProperty();
    }

    @Override
    public void setValue(Boolean value) {
        setSelected(value);
    }
}
