/*
 * @(#)TextFieldsDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.fields;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.field.*;
import jidefx.scene.control.field.verifier.IntegerRangePatternVerifier;
import jidefx.scene.control.field.verifier.StringValuesPatternVerifier;
import jidefx.utils.FXUtils;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormattedTextFieldsDemo extends AbstractFxDemo {
    public FormattedTextFieldsDemo() {
    }

    public String getName() {
        return "MaskTextField/FormattedTextField Demo";
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static final String PATTERN_IP4 = "\\b(([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\b";
    public static final String PATTERN_IP6 = "^([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}$";
    public static final String PATTERN_MAC_ADDRESS = "^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$";
    public static final String PATTERN_SSN = "^\\d{3}-\\d{2}-\\d{4}$";

    public static final String PREFIX_MASK_FIELD_FORM = "FieldForm";

    public Region getDemoPanel() {
        Parent pane1 = createFormattedTextFields();
        Parent pane2 = createCustomFormattedTextFields();
        return new DecorationPane(new HBox(6, pane1, new Separator(Orientation.VERTICAL), pane2));
    }

    public static Parent createFormattedTextFields() {
        MigPane pane = new MigPane(new LC().minWidth("450px").minHeight("450px").insets("20 10 10 10"), new AC().index(0).align("right").gap("20px").index(1).fill().grow().gap("6px").fill(), new AC().gap("6px"));

        Label title = new Label("FormattedTextFields (Step by Step)");
        title.setStyle("-fx-font-size: 1.4em;");
        pane.add(title, new CC().span(2).alignX("left").wrap());

        final CheckBox showSpinnersCheckBox = new CheckBox("Show Spinners");
        showSpinnersCheckBox.setSelected(true);
        showSpinnersCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        if (c instanceof PopupField) {
                            ((FormattedTextField) c).setSpinnersVisible(newValue);
                            ((PopupField) c).setPopupButtonVisible(!newValue);
                        }
                        else {
                            ((FormattedTextField) c).setSpinnersVisible(newValue);
                        }
                    }
                });
            }
        });

        final CheckBox editableCheckBox = new CheckBox("Editable");
        editableCheckBox.setSelected(true);
        editableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
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

        final CheckBox disableCheckBox = new CheckBox("Disable");
        disableCheckBox.setSelected(false);
        disableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((FormattedTextField) c).setDisable(newValue);
                    }
                });
            }
        });

        HBox hBox = new HBox(10, showSpinnersCheckBox, editableCheckBox, disableCheckBox);

        pane.add(hBox, new CC().spanX(2).alignX("right").gapBottom("10px").wrap());

        Label label1 = new Label("new IntegerField(0, 255)");
        pane.add(label1);
        pane.add(new IntegerField(0, 255, 0), new CC().wrap());

        Label label2 = new Label("new IntegerField(0, 255).asSpinner()");
        pane.add(label2);
        pane.add(new IntegerField(0, 255, 0), new CC().wrap());

        Label label3 = new Label("new FormattedTextField<String>()");
        Label label = new Label("...");
        label.setTooltip(new Tooltip("FormattedTextField<String> twoNumberField = new FormattedTextField<>();\n" +
                "twoNumberField.setPattern(\"n.n\");\n" +
                "twoNumberField.getPatternVerifiers().put(\"n\", new IntegerRangePatternVerifier(0, 255));\n"));
        DecorationUtils.install(label3, new Decorator<>(label, Pos.BASELINE_RIGHT, new Point2D(100, 0)));
        pane.add(label3);
        FormattedTextField<String> twoNumberField = new FormattedTextField<>();
        twoNumberField.setPattern("m-n");
        twoNumberField.getPatternVerifiers().put("m", new IntegerRangePatternVerifier(10, 300, true));
        twoNumberField.getPatternVerifiers().put("n", new IntegerRangePatternVerifier(500, 2300, true));
        twoNumberField.setText("010-2000");
        pane.add(twoNumberField, new CC().wrap());

        Label amountLabel = new Label("new NumberField(NumberType.Currency)");
        pane.add(amountLabel);

        NumberField amountField = new NumberField(NumberField.NumberType.Currency);
        amountField.setValue(168.00);
        pane.add(amountField, new CC().wrap());

        Label percentLabel = new Label("new NumberField(NumberType.Percent)");
        pane.add(percentLabel);
        NumberField percentField = new NumberField(NumberField.NumberType.Percent);
        percentField.setValue(0.5);
        pane.add(percentField, new CC().wrap());

        Label IP4Label = new Label("FormattedTextField.createIPv4Field()");
        pane.add(IP4Label);

        FormattedTextField<String> IP4Field = FormattedTextField.createIPv4Field();
        IP4Field.setText("192.168.1.1");
        pane.add(IP4Field, new CC().wrap());

        Label dateLabel = new Label("DateField.createDateField()");
        pane.add(dateLabel);

        FormattedTextField<Date> dateField = DateField.createDateField();
        pane.add(dateField, new CC().wrap());

        Label dateShortLabel = new Label("DateField.createDateField(DateFormat.SHORT)");
        pane.add(dateShortLabel);

        FormattedTextField<Date> dateShortField = DateField.createDateField(DateFormat.SHORT);
        pane.add(dateShortField, new CC().wrap());

        Label timeLabel = new Label("DateField.createTimeField()");
        pane.add(timeLabel);

        FormattedTextField<Date> timeField = DateField.createTimeField();
        pane.add(timeField, new CC().wrap());

        Label dateTimeLabel = new Label("DateField.createDateTimeField()");
        pane.add(dateTimeLabel);

        FormattedTextField<Date> dateTimeField = DateField.createDateTimeField();
        pane.add(dateTimeField, new CC().wrap());

        Label localDateLabel = new Label("LocalDateField.createLocalDateField()");
        pane.add(localDateLabel);

        LocalDateField localDateField = LocalDateField.createLocalDateField();
        pane.add(localDateField, new CC().wrap());

        Label localTimeLabel = new Label("LocalTimeField.createLocalTimeField()");
        pane.add(localTimeLabel);

        LocalTimeField localTimeField = LocalTimeField.createLocalTimeField();
        pane.add(localTimeField, new CC().wrap());

        Label localDateTimeLabel = new Label("LocalDateTimeField.createLocalDateTimeField()");
        pane.add(localDateTimeLabel);

        LocalDateTimeField localDateTimeField = LocalDateTimeField.createLocalDateTimeField();
        pane.add(localDateTimeField, new CC().wrap());

        Label dateTimeCustomLabel = new Label("DateField.createDateField(\"EEEE yyyy.MMMMM.dd G hh:mm aa\")");
        pane.add(dateTimeCustomLabel, new CC().span(2).wrap());

        FormattedTextField<Date> dateTimeCustomField = DateField.createDateField("EEEE yyyy.MMMMM.dd G hh:mm aa", Calendar.getInstance().getTime());
        pane.add(dateTimeCustomField, new CC().skip().wrap());

        return pane;
    }

    public static Parent createCustomFormattedTextFields() {
        MigPane pane = new MigPane(new LC().minWidth("450px").minHeight("450px").insets("20 10 10 10"), new AC().index(0).align("right").gap("20px").index(1).fill().grow().gap("6px").fill(), new AC().gap("6px"));

        Label title = new Label("Custom FormattedTextFields (Step by Step)");
        title.setStyle("-fx-font-size: 1.4em;");
        pane.add(title, new CC().span(2).alignX("left").wrap());

        final CheckBox showSpinnersCheckBox = new CheckBox("Show Spinners");
        showSpinnersCheckBox.setSelected(true);
        showSpinnersCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        if (c instanceof PopupField) {
                            ((FormattedTextField) c).setSpinnersVisible(newValue);
                            ((PopupField) c).setPopupButtonVisible(!newValue);
                        }
                        else {
                            ((FormattedTextField) c).setSpinnersVisible(newValue);
                        }
                    }
                });
            }
        });

        final CheckBox editableCheckBox = new CheckBox("Editable");
        editableCheckBox.setSelected(true);
        editableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
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

        final CheckBox disableCheckBox = new CheckBox("Disable");
        disableCheckBox.setSelected(false);
        disableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((FormattedTextField) c).setDisable(newValue);
                    }
                });
            }
        });

        HBox hBox = new HBox(10, showSpinnersCheckBox, editableCheckBox, disableCheckBox);

        pane.add(hBox, new CC().spanX(2).alignX("right").gapBottom("10px").wrap());

        Accordion accordion = new Accordion();
        {
            FormattedTextField<String> field = new FormattedTextField<>();
            field.setPattern("n");
            field.getPatternVerifiers().put("n", new IntegerRangePatternVerifier(0, 200, false));
            field.setText("125");
            TitledPane pane1 = new TitledPane("One number, from 0 to 200", new VBox(6, new Label(
                    "FormattedTextField<String> field = new FormattedTextField<>();\n" +
                            "field.setPattern(\"n\");\n" +
                            "field.getPatternVerifiers().put(\"n\", new IntegerRangePatternVerifier(30, 200, false));\n" +
                            "field.setText(\"125\");\n"
            ), field));
            accordion.getPanes().add(pane1);
        }

        {
            FormattedTextField<String> field = new FormattedTextField<>();
            field.setPattern("m-n");
            field.getPatternVerifiers().put("m", new IntegerRangePatternVerifier(10, 300, true));
            field.getPatternVerifiers().put("n", new IntegerRangePatternVerifier(500, 2300, true));
            field.setText("010-2000");
            TitledPane pane1 = new TitledPane("Two numbers, each has its own range, fixed length", new VBox(6, new Label(
                    "FormattedTextField<String> field = new FormattedTextField<>();\n" +
                            "field.setPattern(\"m-n\");\n" +
                            "field.getPatternVerifiers().put(\"m\", new IntegerRangePatternVerifier(10, 300, true));\n" +
                            "field.getPatternVerifiers().put(\"n\", new IntegerRangePatternVerifier(500, 2300, true));\n" +
                            "field.setText(\"010-2000\");\n"
            ), field));
            accordion.getPanes().add(pane1);
        }

        {
            FormattedTextField<String> field = new FormattedTextField<>();
            field.setPattern("g");
            field.getPatternVerifiers().put("g", new StringValuesPatternVerifier<String>(new String[]{"High", "Medium", "Low"}) {
                @Override
                public String toTargetValue(String fieldValue) {
                    return fieldValue;
                }

                @Override
                public String fromTargetValue(String previousFieldValue, String targetValue) {
                    return targetValue;
                }
            });
            field.setText("010-2000");
            TitledPane pane1 = new TitledPane("Two numbers, each has its own range, fixed length", new VBox(6, new Label(
                    "FormattedTextField<String> field = new FormattedTextField<>();\n" +
                            "field.setPattern(\"m-n\");\n" +
                            "field.getPatternVerifiers().put(\"m\", new IntegerRangePatternVerifier(10, 300, true));\n" +
                            "field.getPatternVerifiers().put(\"n\", new IntegerRangePatternVerifier(500, 2300, true));\n" +
                            "field.setText(\"010-2000\");\n"
            ), field));
            accordion.getPanes().add(pane1);
        }


        pane.add(accordion, new CC().span(2).wrap());

        return pane;
    }

    @Override
    public String getDemoFolder() {
        return "src/field";
    }
}