/*
 * @(#)SearchableDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.searchable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.examples.demo.DemoData;
import jidefx.scene.control.searchable.*;

import java.util.ArrayList;
import java.util.List;

public class SearchableDemo extends AbstractFxDemo {
    public SearchableDemo() {
    }

    public String getName() {
        return "Searchable Demo";
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getDescription() {
        return "Searchable is a collection of several classes that enable quick search feature on ListView, ComboBox, ChoiceBox, TreeView, TableView or TextInputControl.\n" +
                "1. Press any letter key to start the search\n" +
                "2. Press up/down arrow key to navigation to next or previous matching occurrence\n" +
                "3. Hold CTRL key while pressing up/down arrow key to to multiple selection\n" +
                "4. Press CTRL+A to select all matching occurrences\n" +
                "5. Use '?' to match any character or '*' to match several characters (except in TextInputControl) \n" +
                "6. A lot of customization options using the API\n" +
                "\n" +
                "Demoed classes:\n" +
                "jidefx.utils.searchable.Searchable\n" +
                "jidefx.utils.searchable.ComboBoxSearchable\n" +
                "jidefx.utils.searchable.ChoiceBoxSearchable\n" +
                "jidefx.utils.searchable.ListViewSearchable\n" +
                "jidefx.utils.searchable.TreeViewSearchable\n" +
                "jidefx.utils.searchable.TextInputControlSearchable\n" +
                "jidefx.utils.searchable.TableViewSearchable\n";
    }

    List<Searchable> _searchables = new ArrayList<>();

    @Override
    public Region getOptionsPanel() {

        CheckBox wildcard = new CheckBox("Enable Wildcard");
        wildcard.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                for (Searchable searchable : _searchables) {
                    searchable.setWildcardEnabled(newValue);
                }
            }
        });
        CheckBox repeats = new CheckBox("Repeats");
        repeats.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                for (Searchable searchable : _searchables) {
                    searchable.setRepeats(newValue);
                }
            }
        });

        wildcard.setSelected(true);
        repeats.setSelected(true);

        return new VBox(4, wildcard, repeats);
    }

    public Region getDemoPanel() {
        ObservableList<String> fontList = DemoData.createFontList();
        ObservableList<String> stateList = DemoData.createStateList();
        ObservableList<String> countryList = DemoData.createCountryList();

        ListView<String> listView = new ListView<>(countryList);
        _searchables.add(new ListViewSearchable<>(listView));

        ComboBox<String> comboBox = new ComboBox<>(fontList);
        comboBox.setPrefWidth(200);
        ComboBoxSearchable<String> comboBoxSearchable = new ComboBoxSearchable<>(comboBox);
        _searchables.add(comboBoxSearchable);
        CheckBox checkBoxForComboBox = new CheckBox("Show popup when searching");
        checkBoxForComboBox.setSelected(comboBoxSearchable.isShowPopup());
        checkBoxForComboBox.selectedProperty().bindBidirectional(comboBoxSearchable.showPopupProperty());

        ChoiceBox<String> choiceBox = new ChoiceBox<>(stateList);
        choiceBox.setPrefWidth(200);
        ChoiceBoxSearchable<String> choiceBoxSearchable = new ChoiceBoxSearchable<>(choiceBox);
        _searchables.add(choiceBoxSearchable);
        CheckBox checkBoxForChoiceBox = new CheckBox("Show popup when searching");
        checkBoxForChoiceBox.setSelected(choiceBoxSearchable.isShowPopup());
        checkBoxForChoiceBox.selectedProperty().bindBidirectional(choiceBoxSearchable.showPopupProperty());

        ObservableList<DemoData.Song> items = DemoData.createSongList();

        TableView<DemoData.Song> tableView = new TableView<>(items);

        String[] columnNames = {"Name", "Album", "Artist", "Genre", "Time", "Year"};
        for (String columnName : columnNames) {
            TableColumn<DemoData.Song, String> tableColumn = new TableColumn<>(columnName);
            tableColumn.setSortable(true);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(columnName));
            tableView.getColumns().add(tableColumn);
        }

        _searchables.add(new TableViewSearchable<>(tableView));
        CheckBox checkBoxForTableView = new CheckBox("Enable table cell selection");
        checkBoxForTableView.setSelected(tableView.getSelectionModel().isCellSelectionEnabled());
        checkBoxForTableView.selectedProperty().bindBidirectional(tableView.getSelectionModel().cellSelectionEnabledProperty());

        TreeView<String> treeView = new TreeView<>(DemoData.createTaskTreeItem());
        TreeViewSearchable<String> treeViewSearchable = new TreeViewSearchable<>(treeView);
        _searchables.add(treeViewSearchable);
        CheckBox checkBoxForTreeView = new CheckBox("Enable deep search");
        checkBoxForTreeView.setSelected(tableView.getSelectionModel().isCellSelectionEnabled());
        checkBoxForTreeView.selectedProperty().bindBidirectional(treeViewSearchable.recursiveProperty());

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(200);
        StringBuilder buf = new StringBuilder();
        for (String name : countryList) {
            buf.append(name);
            buf.append("\n");
        }
        textArea.setText(buf.toString());
//        textArea.setEditable(false);
        TextInputControlSearchable textInputControlSearchable = new TextInputControlSearchable(textArea);
        _searchables.add(textInputControlSearchable);

        VBox panel1 = new VBox(3);

        panel1.getChildren().addAll(
                new Label("Searchable ListView:"), listView, new Box(),
                new Label("Searchable ComboBox (non-editable only):"), comboBox, checkBoxForComboBox, new Box(),
                new Label("Searchable ChoiceBox:"), choiceBox, checkBoxForChoiceBox, new Box()
        );

        VBox panel2 = new VBox(4);

        panel2.getChildren().addAll(new Label("Searchable TableView:"), tableView, checkBoxForTableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        VBox panel3 = new VBox(4);

        panel3.getChildren().addAll(new Label("Searchable TreeView:"), treeView, checkBoxForTreeView);
        VBox.setVgrow(treeView, Priority.ALWAYS);

        VBox panel4 = new VBox(4);

        panel4.getChildren().addAll(new Label("Searchable TextArea: (Press Control+F or Command+F to start searching"), textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);

        HBox box = new HBox(10);
        box.getChildren().addAll(panel1, panel2, panel3, panel4);

        return box;
    }

    @Override
    public String getDemoFolder() {
        return "src/searchable";
    }
}