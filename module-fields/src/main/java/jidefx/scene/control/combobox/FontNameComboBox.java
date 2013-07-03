/*
 * @(#)FontNameComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class FontNameComboBox extends ComboBox<String> {
    private BooleanProperty _showFont;

    public FontNameComboBox() {
        getItems().addAll(Font.getFamilies());
        getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setStyle("-fx-font-family:" + getSelectionModel().getSelectedItem() + ";"); //NON-NLS
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FontComboBoxListViewSkin(this);
    }

    private class FontComboBoxListViewSkin extends ComboBoxListViewSkin<String> {

        /**
         * ************************************************************************ * Constructors *
         * ************************************************************************
         */
        public FontComboBoxListViewSkin(ComboBox<String> comboBox) {
            super(comboBox);
            setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> listView) {
                    return new ListCell<String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            updateDisplayText(this, item, empty);
                        }
                    };
                }
            });
        }

        // return a boolean to indicate that the cell is empty (and therefore not filled)
        private boolean updateDisplayText(ListCell<String> cell, String item, boolean empty) {
            Font defaultFont = Font.getDefault();
            if (empty) {
                if (cell == null) return true;
                cell.setGraphic(null);
                cell.setText(null);
                return true;
            }
            else {
                // run item through StringConverter if it isn't null
                String s = item == null ? getPromptText() : item;
                cell.setText(s);
                cell.setFont(new Font(s, defaultFont.getSize()));
                cell.setGraphic(null);
                return s == null || s.isEmpty();
            }
        }
    }
}
