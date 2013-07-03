/*
 * @(#)TextFieldsDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.fields;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.field.DateField;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.SpinnerStyle;
import jidefx.utils.FXUtils;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.Calendar;
import java.util.Date;

public class SpinnerStyleDemo extends AbstractFxDemo {

    private DecorationPane _decorationPane;
    private FormattedTextField<Date> _noBorderField;
    private FormattedTextField<Date> _borderedField;
    private FormattedTextField<Date> _customizedField;
    private final static String DATE_FORMAT = "EEEE yyyy.MMMM.dd G";

    public SpinnerStyleDemo() {
    }

    public String getName() {
        return "Spinner Style Demo";
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Region getDemoPanel() {
        MigPane pane = new MigPane(new LC().width("450px").minHeight("450px").insets("20 10 10 10"), new AC().index(0).align("right").gap("30px").index(1).fill().grow().gap("6px").fill(), new AC().gap("50px"));

        Label title = new Label("Customizing Spinner Buttons");
        title.setStyle("-fx-font-size: 1.4em;");
        pane.add(title, new CC().span(2).alignX("left").wrap());

        Label noBorderLabel = new Label("Using FormattedText itself:");
        _noBorderField = DateField.createDateField(DATE_FORMAT, Calendar.getInstance().getTime());
        pane.add(noBorderLabel);
        pane.add(_noBorderField, new CC().wrap());

        addDecorator(noBorderLabel, _noBorderField);

        Label borderedLabel = new Label("Using FormattedText.asSpinner:");
        _borderedField = new DateField(DATE_FORMAT, Calendar.getInstance().getTime()) {
            @Override
            protected Button createDecreaseSpinnerButton() {
                Button decreaseSpinnerButton = super.createDecreaseSpinnerButton();
                decreaseSpinnerButton.setId("decreaseSpinnerButton");
                return decreaseSpinnerButton;
            }
        };
        pane.add(borderedLabel);
        Node borderedDecorationPane = _borderedField.asSpinner();
        pane.add(borderedDecorationPane, new CC().wrap());

        addDecorator(borderedLabel, _borderedField);

        Label customizedLabel = new Label("Customized Buttons using CSS:");
        _customizedField = new DateField(DATE_FORMAT, Calendar.getInstance().getTime());
        Node node = _customizedField.asSpinner();
        node.setId("customField");
        pane.add(customizedLabel);
        pane.add(node, new CC().wrap());
        pane.getStylesheets().add(SpinnerStyleDemo.class.getResource("CustomFields.css").toExternalForm());

        addDecorator(customizedLabel, _customizedField);

        HBox.setHgrow(pane, Priority.ALWAYS);
        HBox hBox = new HBox(6, pane);
        hBox.setPadding(new Insets(0, 20, 0, 0));

        // demo code to create field with all the styles for screenshot purpose
/*
        for (SpinnerStyle style : SpinnerStyle.values()) {
            FormattedTextField field1 = new LocalDateField(DATE_FORMAT, LocalDate.now());
            pane.add(new Label(style.name()));
            pane.add(field1.asSpinner(style), new CC().wrap());
            FormattedTextField field2 = new LocalDateField(DATE_FORMAT, LocalDate.now());
            pane.add(new Label(style.name() + " (non-editable)"));
            pane.add(field2.asSpinner(style), new CC().wrap());
            field2.setEditable(false);
        }
*/

        _decorationPane = new DecorationPane(hBox);
        return _decorationPane;
    }

    private static void addDecorator(Label label, FormattedTextField<?> field) {
        field.setSpinnersVisible(true);
        field.installAdjustmentMouseHandler(label, 1);
    }

    @Override
    public Region getOptionsPanel() {
        VBox optionPane = new VBox();
        optionPane.setSpacing(10);

        ListView<SpinnerStyle> styleListView = new ListView<>();
        styleListView.getItems().addAll(SpinnerStyle.values());

        CheckBox editableCheckBox = new CheckBox("Editable");
        editableCheckBox.setSelected(true);

        styleListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SpinnerStyle>() {
            @Override
            public void changed(ObservableValue<? extends SpinnerStyle> observable, SpinnerStyle oldValue, SpinnerStyle newValue) {
                _noBorderField.setSpinnerStyle(newValue);
                _borderedField.setSpinnerStyle(newValue);
                _customizedField.setSpinnerStyle(newValue);
            }
        });

        editableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(_decorationPane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((FormattedTextField) c).setEditable(newValue);
                    }
                });
            }
        });

        optionPane.getChildren().addAll(editableCheckBox, new Label("Pre-defined Styles"), styleListView);

        return optionPane;
    }

    @Override
    public String getDemoFolder() {
        return "src/field";
    }
}