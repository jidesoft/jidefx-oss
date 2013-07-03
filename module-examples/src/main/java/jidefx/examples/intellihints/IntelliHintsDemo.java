/*
 * @(#)IntelliHintsDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.intellihints;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.examples.demo.DemoData;
import jidefx.scene.control.hints.AbstractListIntelliHints;
import jidefx.scene.control.hints.FileIntelliHints;
import jidefx.scene.control.hints.ListDataIntelliHints;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class IntelliHintsDemo extends AbstractFxDemo {
    private static final long serialVersionUID = 4729636896685844732L;
    private BooleanProperty _applyFileFilter = new SimpleBooleanProperty(false);

    public IntelliHintsDemo() {
    }

    public String getName() {
        return "IntelliHints Demo";
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getDescription() {
        return "This is a demo of IntelliHints components. IntelliHints can display a hint popup in a text field or text area " +
                "so that user can pick a hint directly while typing.\n" +
                "\nYou can start to type in those text fields or text area to see how it works. " +
                "At any time, if you want to see whether there are hints available, you can press DOWN key " +
                "in text field or CTRL+SPACE in text area.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.hints.IntelliHints\n" +
                "com.jidesoft.hints.AbstractIntelliHints\n" +
                "com.jidesoft.hints.AbstractListIntelliHints\n" +
                "com.jidesoft.hints.FileIntelliHints\n" +
                "com.jidesoft.hints.ListDataIntelliHints";
    }


    @Override
    public Region getOptionsPanel() {
        final CheckBox applyFileFilter = new CheckBox("Show \"Program\" Folders/Files Only for FileIntelliHints");
        _applyFileFilter.bind(applyFileFilter.selectedProperty());
        return new VBox(6, applyFileFilter);
    }

    public Region getDemoPanel() {
        final String[] fontNames = DemoData.getFontNames();
        // create file text field
        List<String> urls = null;
        try {
            urls = DemoData.readUrls();
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        TextField urlTextField = new TextField("http://");
        ListDataIntelliHints intelliHints = new ListDataIntelliHints<>(urlTextField, urls);
        intelliHints.setCaseSensitive(false);

        TextField pathTextField = new TextField();
        FileIntelliHints fileIntelliHints = new FileIntelliHints(pathTextField);
        fileIntelliHints.setFilter(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return !_applyFileFilter.get() || dir.getAbsolutePath().contains("Program") || name.contains("Program");
            }
        });
        fileIntelliHints.setFolderOnly(false);
        fileIntelliHints.setShowFullPath(false);

        // create file text field
        TextField fileTextField = new TextField();
        new FileIntelliHints(fileTextField);

        // create file text field
        TextArea fileTextArea = new TextArea();
        new FileIntelliHints(fileTextArea);
        fileTextArea.setPrefRowCount(4);
//
        // create font text field
        TextField fontTextField = new TextField();
        ListDataIntelliHints fontIntelliHints = new ListDataIntelliHints<>(fontTextField, fontNames);
        fontIntelliHints.setCaseSensitive(false);

        TextField textField = new TextField();

        new AbstractListIntelliHints<Long>(textField) {
            protected Label _messageLabel;

            @Override
            public Node createHintsNode() {
                BorderPane pane = (BorderPane) super.createHintsNode();
                _messageLabel = new Label();
                pane.setTop(_messageLabel);
                pane.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-padding: 6;");
                return pane;
            }

            // update list model depending on the data in textfield
            public boolean updateHints(Object value) {
                if (value == null) {
                    return false;
                }
                String s = value.toString();
                s = s.trim();
                if (s.length() == 0) {
                    return false;
                }
                try {
                    long l = Long.parseLong(s);
                    boolean prime = isProbablePrime(l);
                    _messageLabel.setText("");
                    if (prime) {
                        return false;
                    }
                    else {
                        List<Long> list = new ArrayList<>();
                        long nextPrime = l;
                        for (int i = 0; i < 10; i++) {
                            nextPrime = nextPrime(nextPrime);
                            list.add(nextPrime);
                        }
                        setAvailableHints(FXCollections.observableArrayList(list));
                        _messageLabel.setText("Next 10 prime numbers:");
                        _messageLabel.setTextFill(Color.BLACK);
                        return true;
                    }
                }
                catch (NumberFormatException e) {
                    setAvailableHints(null);
                    _messageLabel.setText("Invalid long number");
                    _messageLabel.setTextFill(Color.RED);
                    return true;
                }
            }
        };

        VBox panel = new VBox(3);
        panel.setPadding(new Insets(10, 10, 10, 10));

        panel.getChildren().addAll(
                new Label("ListDataIntelliHints TextField for URLs: "), urlTextField, new Box(),
                new Label("FileIntelliHints TextField for paths (folders only, show partial path): "), pathTextField, new Box(),
                new Label("FileIntelliHints TextField for files (files and folders, show full path):"), fileTextField, new Box(),
                new Label("IntelliHints TextField to choose a font: "), fontTextField, new Box(),
                new Label("FileIntelliHints TextArea for files (each line is for a new file):"), fileTextArea, new Box(),
                new Label("A custom IntelliHints for prime numbers: "), textField
        );

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "src/intellihints";
    }

    public static boolean isProbablePrime(long number) {
        return new BigInteger("" + number).isProbablePrime(500);
    }

    public static long nextPrime(long lastPrime) {
        long testPrime;
        testPrime = lastPrime + 1;
        while (!isProbablePrime(testPrime)) testPrime += (testPrime % 2 == 0) ? 1 : 2;

        return testPrime;
    }

}
