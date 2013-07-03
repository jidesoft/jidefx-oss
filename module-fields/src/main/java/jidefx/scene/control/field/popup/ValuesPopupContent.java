/*
 * @(#)ValuesPopupContent.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.popup;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.field.FormattedTextField;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.ArrayList;
import java.util.List;

/**
 * An editor pane for any type that can be represented by a value arrays, such as Insets which is basically an four
 * double value array, Point2D is a two double value array.
 *
 * @param <S> the type the pane is editing. In the two examples above, it would be the Insets or the Point2D
 * @param <T> the element type of the array. In both examples above, it would be Double.
 */
public abstract class ValuesPopupContent<S, T> extends DecorationPane implements PopupContent<S> {
    private List<FormattedTextField<T>> _fields;
    private static final String STYLE_CLASS_DEFAULT = "popup-content"; //NON-NLS
    private String[] _labels;

    /**
     * The labels of all the fields.
     *
     * @param labels the labels of all the fields
     */
    public ValuesPopupContent(String[] labels) {
        super(new MigPane(new LC().minWidth("100px").insets("10 10 10 10"), //NON-NLS
                new AC().align("right", 0).size("pref", 0).size("100px", 1).fill(1).grow(1).gap("10px"), //NON-NLS
                new AC().gap("6px"))); //NON-NLS
        getStylesheets().add(PopupContent.class.getResource("PopupContent.css").toExternalForm()); //NON-NLS
        getStyleClass().add(STYLE_CLASS_DEFAULT);
        initializeComponents(labels);
    }

    /**
     * Coverts from the editor data type to the elements.
     *
     * @param value the value for the editor
     * @return the list of each element values.
     */
    public abstract List<T> toValues(S value);

    /**
     * Coverts from the elements to the editor value.
     *
     * @param values the element values
     * @return the editor value.
     */
    public abstract S fromValues(List<T> values);

    /**
     * Creates a text field for the element.
     *
     * @return the text field
     */
    public abstract FormattedTextField<T> createTextField(String label);

    private void initializeComponents(String[] labels) {
        _labels = labels;
        _fields = new ArrayList<>();
        MigPane content = (MigPane) getContent();
        ChangeListener<T> changeListener = new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                List<T> list = getValues();
                if (list.size() == _fields.size()) {  // make sure all values are there
                    S value = fromValues(list);
                    if (value != null) {
                        setValue(value);
                    }
                }
            }
        };
        for (String text : labels) {
            Label label = new Label(text);
            content.add(label);
            FormattedTextField<T> field = createTextField(text);
            field.valueProperty().addListener(changeListener);
            _fields.add(field);
            field.installAdjustmentMouseHandler(label);
            content.add(field, new CC().wrap());
        }
    }

    protected int getFieldIndex(String label) {
        for (int i = 0; i < _labels.length; i++) {
            if (_labels[i].equals(label)) {
                return i;
            }
        }
        return -1;
    }

    private ObjectProperty<S> _valueProperty = null;

    @Override
    public final ObjectProperty<S> valueProperty() {
        if (_valueProperty == null) {
            _valueProperty = new SimpleObjectProperty<S>(this, "value") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    updateFields(get());
                }
            };
        }
        return _valueProperty;
    }

    @Override
    public final S getValue() {
        return valueProperty().get();
    }

    @Override
    public final void setValue(S value) {
        valueProperty().set(value);
    }

    protected void updateFields(S value) {
        List<T> values = toValues(value);
        if (values.size() != _fields.size()) {
            throw new IllegalStateException("The number of the values return from toValues method must be the same as the number of labels passed in to ValuePopupContent's constructor.");
        }
        for (int i = 0; i < _fields.size(); i++) {
            _fields.get(i).setValue(values.get(i));
        }
    }

    protected List<T> getValues() {
        List<T> values = new ArrayList<>();
        for (FormattedTextField<T> field : _fields) {
            T value = field.getValue();
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }
}
