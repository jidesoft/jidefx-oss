/*
 * @(#)ListViewSearchable.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.searchable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * {@code ListViewSearchable} is an concrete implementation of {@link Searchable} that enables the search function
 * on ListView. <p>It's very simple to use it. Assuming you have a ListView, all you need to do is to call
 * <pre>{@code
 * ListView list = ....;
 * ListViewSearchable searchable = new ListViewSearchable(list);
 * }</pre>
 * Now the ListView will have the search function.
 * <p>
 * There is very little customization you need to do to ListViewSearchable. The only thing you might need is when the
 * element in the ListView needs a special conversion to convert to string. If so, you can override
 * convertElementToString() to provide you own algorithm to do the conversion.
 * <pre>{@code
 * ListView list = ....;
 * ListViewSearchable searchable = new ListViewSearchable(list) {
 *      protected String convertElementToString(Object object) {
 *          ...
 *      }
 * };
 * }</pre>
 * <p>
 *
 * @param <T> the element type in the ListView.
 */
@SuppressWarnings({"Convert2Lambda", "unchecked"})
public class ListViewSearchable<T> extends Searchable<T> {

    private ListChangeListener<T> _listChangeListener;
    private ChangeListener<ObservableList<T>> _itemsChangeListener;

    public ListViewSearchable(ListView<T> listView) {
        super(listView);
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
        ((ListView<T>) _node).getItems().addListener(_listChangeListener);
        if (_itemsChangeListener == null) {
            _itemsChangeListener = new ChangeListener<ObservableList<T>>() {
                @Override
                public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldValue, ObservableList<T> newValue) {
                    hidePopup();
                }
            };
        }
        ((ListView<T>) _node).itemsProperty().addListener(_itemsChangeListener);
    }

    @Override
    public void uninstallListeners() {
        if (_listChangeListener != null) {
            ((ListView<T>) _node).getItems().removeListener(_listChangeListener);
            _listChangeListener = null;
        }
        if (_itemsChangeListener != null) {
            ((ListView<T>) _node).itemsProperty().removeListener(_itemsChangeListener);
            _itemsChangeListener = null;
        }
        super.uninstallListeners();
    }

    @Override
    protected int getSelectedIndex() {
        return ((ListView<T>) _node).getSelectionModel().getSelectedIndex();
    }

    @Override
    protected void setSelectedIndex(int index, boolean incremental) {
        ListView<T> listView = (ListView<T>) _node;
        if (incremental) {
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listView.getSelectionModel().select(index);
            listView.scrollTo(index);
        }
        else {
            listView.getSelectionModel().clearSelection(index);
            listView.getSelectionModel().select(index);
            listView.scrollTo(index);
        }
    }

    @Override
    protected int getElementCount() {
        return ((ListView<T>) _node).getItems().size();
    }

    @Override
    protected T getElementAt(int index) {
        return ((ListView<T>) _node).getItems().get(index);
    }

    @Override
    protected String convertElementToString(T element) {
        return element != null ? element.toString() : "";
    }
}
