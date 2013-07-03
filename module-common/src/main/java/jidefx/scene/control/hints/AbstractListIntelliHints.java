/*
 * @(#)AbstractListIntelliHints.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.scene.control.hints;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;


/**
 * {@code AbstractListIntelliHints} extends AbstractIntelliHints and further implement most of the methods in
 * interface {@link IntelliHints}. In this class, it assumes the hints can be represented as
 * a ListView, so it used ListView in the hints popup.
 */
public abstract class AbstractListIntelliHints<T> extends AbstractIntelliHints<T> {
    private ListView<T> _listView;

    /**
     * Creates a Completion for a TextInputControl
     *
     * @param textInputControl the control where the intelliHints will be installed to.
     */
    public AbstractListIntelliHints(TextInputControl textInputControl) {
        super(textInputControl);
    }

    public Node createHintsNode() {
        BorderPane pane = new BorderPane();

        _listView = createListView();
        pane.setCenter(_listView);

        return pane;
    }

    /**
     * Creates the list to display the hints. By default, we create a ListView using the code below.
     *
     * @return the list.
     */
    protected ListView<T> createListView() {
        ListView<T> listView = new ListView<>();
        listView.setPrefHeight(215);
        return listView;
    }

    /**
     * Gets the list.
     *
     * @return the list.
     */
    protected ListView<T> getListView() {
        return _listView;
    }

    /**
     * Sets the available hints.
     *
     * @param hints the list of hints
     */
    protected void setAvailableHints(ObservableList<T> hints) {
        if (getListView() == null) {
            return;
        }
        resetSelection();
        getListView().setItems(hints);
    }

    private void resetSelection() {
        if (getListView() == null) {
            return;
        }
        getListView().getSelectionModel().clearSelection();
    }

    public T getSelectedHint() {
        return getListView() == null ? null : getListView().getSelectionModel().getSelectedItem();
    }

    @Override
    public Node getDelegateNode() {
        return getListView();
    }

    /**
     * Gets the delegate keystrokes. Since we know the hints popup is a ListView, we return eight keystrokes so that
     * they can be delegate to the ListView. Those keystrokes are DOWN, UP, PAGE_DOWN, PAGE_UP, HOME and END.
     *
     * @return the keystrokes that will be delegated to the ListView when hints popup is visible.
     */
    @Override
    public KeyCombination[] getDelegateKeyCombination() {
        return new KeyCombination[]{
                KeyCombination.keyCombination("Down"), //NON-NLS
                KeyCombination.keyCombination("Up"), //NON-NLS
                KeyCombination.keyCombination("Page Down"), //NON-NLS
                KeyCombination.keyCombination("Page Up"), //NON-NLS
                KeyCombination.keyCombination("Ctrl+Page Up"), //NON-NLS
                KeyCombination.keyCombination("Ctrl+Page Down"), //NON-NLS
                KeyCombination.keyCombination("Ctrl+Home"), //NON-NLS
                KeyCombination.keyCombination("Ctrl+End"), //NON-NLS

        };
    }
}