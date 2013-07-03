/*
 * @(#)ComboBoxSearchable.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.searchable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * {@code ComboBoxSearchable} is an concrete implementation of {@link Searchable} that enables the search function
 * on non-editable ComboBox. <p>It's very simple to use it. Assuming you have a ComboBox, all you need to do is to call
 * <pre>{@code
 * ComboBox comboBox = ....;
 * ComboBoxSearchable searchable = new ComboBoxSearchable(comboBox);
 * }</pre>
 * Now the ComboBox will have the search function.
 * <p>
 * There is very little customization you need to do to ComboBoxSearchable. The only thing you might need is when the
 * element in the ComboBox needs a special conversion to convert to string. If so, you can override
 * convertElementToString() to provide you own algorithm to do the conversion.
 * <pre>{@code
 * ComboBox comboBox = ....;
 * ComboBoxSearchable searchable = new ComboBoxSearchable(comboBox) {
 *      protected String convertElementToString(Object object) {
 *          ...
 *      }
 * };
 * }</pre>
 * <p>
 *
 * @param <T> the element type in the ComboBox.
 */
@SuppressWarnings({"Convert2Lambda", "unchecked"})
public class ComboBoxSearchable<T> extends Searchable<T> {

    private ListChangeListener<T> _listChangeListener;
    private ChangeListener<ObservableList<T>> _itemsChangeListener;

    public ComboBoxSearchable(ComboBox<T> comboBox) {
        super(comboBox);
    }

    @Override
    public void installListeners() {
        super.installListeners();
        if (_listChangeListener == null) {
            _listChangeListener = new ListChangeListener<T>() {
                @Override
                public void onChanged(Change<? extends T> c) {
                    hidePopup();
                }
            };
        }
        ((ComboBox<T>) _node).getItems().addListener(_listChangeListener);
        if (_itemsChangeListener == null) {
            _itemsChangeListener = new ChangeListener<ObservableList<T>>() {
                @Override
                public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldValue, ObservableList<T> newValue) {
                    hidePopup();
                }
            };
        }
        ((ComboBox<T>) _node).itemsProperty().addListener(_itemsChangeListener);
    }

    @Override
    public void uninstallListeners() {
        if (_listChangeListener != null) {
            ((ComboBox<T>) _node).getItems().removeListener(_listChangeListener);
            _listChangeListener = null;
        }
        if (_itemsChangeListener != null) {
            ((ComboBox<T>) _node).itemsProperty().removeListener(_itemsChangeListener);
            _itemsChangeListener = null;
        }
        super.uninstallListeners();
    }

    private BooleanProperty _showPopupProperty;

    public BooleanProperty showPopupProperty() {
        if (_showPopupProperty == null) {
            _showPopupProperty = new SimpleBooleanProperty(this, "showPopup", false); //NON-NLS
        }
        return _showPopupProperty;
    }

    public boolean isShowPopup() {
        return showPopupProperty().get();
    }

    public void setShowPopup(boolean showPopup) {
        showPopupProperty().set(showPopup);
    }

    @Override
    protected int getSelectedIndex() {
        return ((ComboBox<T>) _node).getSelectionModel().getSelectedIndex();
    }

    @Override
    protected void setSelectedIndex(int index, boolean incremental) {
        ComboBox<T> comboBox = (ComboBox<T>) _node;
        comboBox.getSelectionModel().select(getElementAt(index));
        if (isShowPopup()) {
            comboBox.show();
        }
    }

    @Override
    protected int getElementCount() {
        return ((ComboBox<T>) _node).getItems().size();
    }

    @Override
    protected T getElementAt(int index) {
        return ((ComboBox<T>) _node).getItems().get(index);
    }

    @Override
    protected String convertElementToString(T element) {
        ComboBox<T> comboBox = (ComboBox<T>) _node;
        StringConverter<T> converter = comboBox.getConverter();
        return converter != null ? converter.toString(element) : (element != null ? element.toString() : "");
    }
}
