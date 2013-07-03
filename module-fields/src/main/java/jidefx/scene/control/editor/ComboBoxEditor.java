/*
 * @(#)TextFieldCellEditor.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.editor;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * An editor based on {@code ComboBox} for any data that can be enumerated.
 */
public class ComboBoxEditor<T> extends ComboBox<T> implements Editor<T>, LazyInitializeEditor<T> {
    @Override
    public ObservableValue<T> observableValue() {
        return valueProperty();
    }

    @Override
    public void initialize(Class<T> clazz, EditorContext context) {
        Object list = context != null ? context.getProperties().get(EditorContext.PROPERTY_OBSERVABLE_LIST) : null;
        if (list instanceof ObservableList) {
            setItems(((ObservableList<T>) list));
        }
        Object editable = context != null ? context.getProperties().get(EditorContext.PROPERTY_EDITABLE) : null;
        if (editable instanceof Boolean) {
            setEditable(((Boolean) editable));
        }
    }
}
