/*
 * @(#)TextFieldCellEditor.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.editor;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * An editor based on {@code TextField} for {@code String}.
 */
public class TextFieldEditor extends TextField implements Editor<String>, LazyInitializeEditor<String> {
    @Override
    public ObservableValue<String> observableValue() {
        return textProperty();
    }

    @Override
    public void setValue(String newValue) {
        setText(newValue);
    }

    @Override
    public void initialize(Class<String> clazz, EditorContext context) {
        Object editable = context != null ? context.getProperties().get(EditorContext.PROPERTY_EDITABLE) : null;
        if (editable instanceof Boolean) {
            setEditable(((Boolean) editable));
        }
    }
}
