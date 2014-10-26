/*
 * @(#)TextFieldsDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.fields;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import jidefx.examples.decoration.DateFormatValidator;
import jidefx.examples.decoration.SimpleValidator;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.field.*;
import jidefx.scene.control.field.verifier.IntegerRangePatternVerifier;
import jidefx.scene.control.validation.ValidationMode;
import jidefx.scene.control.validation.ValidationUtils;
import jidefx.utils.FXUtils;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.apache.commons.validator.routines.RegexValidator;
import org.tbee.javafx.scene.layout.MigPane;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

public class TextFieldsDemo extends AbstractFxDemo {
    public TextFieldsDemo() {
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
        Parent pane1 = createMaskTextFields();
        Parent pane2 = createFormattedTextFields();
        HBox.setHgrow(pane1, Priority.ALWAYS);
        HBox.setHgrow(pane2, Priority.ALWAYS);
        HBox pane = new HBox(6, pane1, new Separator(Orientation.VERTICAL), pane2);

        ValidationUtils.install(pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "IP4Field"), new SimpleValidator(new RegexValidator(PATTERN_IP4)), ValidationMode.ON_FLY);
        ValidationUtils.install(pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "IP6Field"), new SimpleValidator(new RegexValidator(PATTERN_IP6)), ValidationMode.ON_FLY);
        ValidationUtils.install(pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "MacAddressField"), new SimpleValidator(new RegexValidator(PATTERN_MAC_ADDRESS)), ValidationMode.ON_FLY);
        ValidationUtils.install(pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "SSNField"), new SimpleValidator(new RegexValidator(PATTERN_SSN)), ValidationMode.ON_FLY);

        Node dateField = pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "dateField");
        ValidationUtils.install(dateField, new DateFormatValidator(((FormattedTextField) dateField).getPattern()), ValidationMode.ON_FLY);

        Node timeField = pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "timeField");
        ValidationUtils.install(timeField, new DateFormatValidator(((FormattedTextField) timeField).getPattern()), ValidationMode.ON_FLY);

        Node dateShortField = pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "dateShortField");
        ValidationUtils.install(dateShortField, new DateFormatValidator(((FormattedTextField) dateShortField).getPattern()), ValidationMode.ON_FLY);

        Node dateTimeField = pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "dateTimeField");
        ValidationUtils.install(dateTimeField, new DateFormatValidator(((FormattedTextField) dateTimeField).getPattern()), ValidationMode.ON_FLY);

        Node localTimeField = pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "localTimeField");
        ValidationUtils.install(localTimeField, new DateFormatValidator(((FormattedTextField) localTimeField).getPattern()), ValidationMode.ON_FLY);

        Node dateTimeCustomField = pane.lookup("#" + PREFIX_MASK_FIELD_FORM + "dateTimeCustomField");
        ValidationUtils.install(dateTimeCustomField, new DateFormatValidator(((FormattedTextField) dateTimeCustomField).getPattern()), ValidationMode.ON_FLY);

        return new DecorationPane(pane);
    }

    public static Parent createMaskTextFields() {

        MigPane pane = new MigPane(new LC().minWidth("450px").minHeight("450px").insets("20 10 10 10"), new AC().index(0).align("right").gap("20px").index(1).fill().grow().gap("6px").fill(), new AC().gap("6px"));

        Label title = new Label("Mask TextFields");
        title.setStyle("-fx-font-size: 1.4em;");
        pane.add(title, new CC().span(2).alignX("left").wrap());

        final CheckBox showClearButtonCheckBox = new CheckBox("Show Clear Button");
        showClearButtonCheckBox.setSelected(true);
        showClearButtonCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof MaskTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((MaskTextField) c).setClearButtonVisible(newValue);
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
                        return c instanceof MaskTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((MaskTextField) c).setEditable(newValue);
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
                        return c instanceof MaskTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((MaskTextField) c).setDisable(newValue);
                    }
                });
            }
        });

        HBox hBox = new HBox(10, showClearButtonCheckBox, editableCheckBox, disableCheckBox);

        pane.add(hBox, new CC().spanX(2).alignX("right").gapBottom("10px").wrap());

        Label digitsLabel = new Label("Number (5-digits):");
        digitsLabel.setId(PREFIX_MASK_FIELD_FORM + "DigitsLabel");
        MaskTextField digitsField = new MaskTextField();
        digitsField.setInputMask("99999");
        digitsField.setInputMask(null);
        digitsField.setPlaceholderCharacter('0');
        digitsField.setId(PREFIX_MASK_FIELD_FORM + "DigitsField");
        pane.add(digitsLabel);
        pane.add(digitsField, new CC().wrap());

        addDecoratorForMaskTextField(digitsLabel, digitsField);

        Label lettersLabel = new Label("Letters (All required):");
        lettersLabel.setId(PREFIX_MASK_FIELD_FORM + "LettersLabel");
        MaskTextField lettersField = new MaskTextField();
        lettersField.setInputMask("AAAAAA");
        lettersField.setConversionMask("UUUUUU");
        lettersField.setPlaceholderCharacter('_');
        lettersField.setId(PREFIX_MASK_FIELD_FORM + "LettersField");
        pane.add(lettersLabel);
        pane.add(lettersField, new CC().wrap());

        addDecoratorForMaskTextField(lettersLabel, lettersField);

        Label optionalLettersLabel = new Label("Letters (Optional except 1st letter):");
        optionalLettersLabel.setId(PREFIX_MASK_FIELD_FORM + "OptionalLettersLabel");
        MaskTextField optionalLettersField = new MaskTextField();
        optionalLettersField.setInputMask("AAAAAA");
        optionalLettersField.setConversionMask("UUUUUU");
        optionalLettersField.setRequiredMask("R_____");
        optionalLettersField.setPlaceholderCharacter('_');
        optionalLettersField.setId(PREFIX_MASK_FIELD_FORM + "OptionalLettersField");
        pane.add(optionalLettersLabel);
        pane.add(optionalLettersField, new CC().wrap());

        addDecoratorForMaskTextField(optionalLettersLabel, optionalLettersField);

        Label serialLabel = new Label("Serial:");
        serialLabel.setId(PREFIX_MASK_FIELD_FORM + "SerialLabel");
        MaskTextField serialField = MaskTextField.createSerialNumberField();
        serialField.setPlaceholderCharacter('_');
        serialField.setId(PREFIX_MASK_FIELD_FORM + "SerialField");
        pane.add(serialLabel);
        pane.add(serialField, new CC().wrap());

        addDecoratorForMaskTextField(serialLabel, serialField);

        Label IP6Label = new Label("IPv6:");
        IP6Label.setId(PREFIX_MASK_FIELD_FORM + "IP6Label");
        MaskTextField IP6Field = MaskTextField.createIPv6Field();
        IP6Field.setId(PREFIX_MASK_FIELD_FORM + "IP6Field");
        pane.add(IP6Label);
        pane.add(IP6Field, new CC().wrap());

        addDecoratorForMaskTextField(IP6Label, IP6Field);

        Label macLabel = new Label("MAC Address:");
        macLabel.setId(PREFIX_MASK_FIELD_FORM + "MacAddressLabel");
        MaskTextField macField = MaskTextField.createMacAddressField();
        macField.setId(PREFIX_MASK_FIELD_FORM + "MacAddressField");
        pane.add(macLabel);
        pane.add(macField, new CC().wrap());

        addDecoratorForMaskTextField(macLabel, macField);

        Label SSNLabel = new Label("SSN:");
        SSNLabel.setId(PREFIX_MASK_FIELD_FORM + "SSNLabel");
        MaskTextField SSNField = MaskTextField.createSSNField();

// 5/10/2013 decided to remove this example as MaskFormatter is a Swing class. Don't want to introduce any unnecessary dependency on Swing
//        // This is just a demo to configure MaskTextField from a MaskFormatter
//        try {
//            MaskFormatter maskFormatter = new MaskFormatter("###-##-####");
//            maskFormatter.setPlaceholderCharacter('#');
//            SSNField.fromMaskFormatter(maskFormatter);
//        }
//        catch (ParseException e) {
//            // ignore
//        }

        SSNField.setId(PREFIX_MASK_FIELD_FORM + "SSNField");
        pane.add(SSNLabel);
        pane.add(SSNField, new CC().wrap());

        addDecoratorForMaskTextField(SSNLabel, SSNField);

        Label phoneNumberLabel = new Label("Phone Number:");
        phoneNumberLabel.setId(PREFIX_MASK_FIELD_FORM + "PhoneNumberLabel");
        MaskTextField phoneNumberField = MaskTextField.createPhoneNumberField();
        phoneNumberField.setPlaceholderCharacter('_');
        phoneNumberField.setId(PREFIX_MASK_FIELD_FORM + "PhoneNumberField");
        pane.add(phoneNumberLabel);
        pane.add(phoneNumberField, new CC().wrap());

        addDecoratorForMaskTextField(phoneNumberLabel, phoneNumberField);

        Label zipCodeLabel = new Label("Zip Code + 4 (US):");
        zipCodeLabel.setId(PREFIX_MASK_FIELD_FORM + "ZipCodeLabel");
        MaskTextField zipCodeField = MaskTextField.createZipCodePlus4Field();
        zipCodeField.setPlaceholderCharacter('_');
        zipCodeField.setId(PREFIX_MASK_FIELD_FORM + "ZipCodeField");
        pane.add(zipCodeLabel);
        pane.add(zipCodeField, new CC().wrap());

        addDecoratorForMaskTextField(zipCodeLabel, zipCodeField);

        return pane;
    }

    public static Parent createFormattedTextFields() {
        MigPane pane = new MigPane(new LC().minWidth("450px").minHeight("450px").insets("20 10 10 10"), new AC().index(0).align("right").gap("20px").index(1).fill().grow().gap("6px").fill(), new AC().gap("6px"));

        Label title = new Label("Formatted TextFields");
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

        Label digitsLabel = new Label("Positive Integer (with Grouping):");
        digitsLabel.setId(PREFIX_MASK_FIELD_FORM + "DigitsLabel");
        NumberField digitsField = new NumberField();
        DecimalFormat integerFormat = (DecimalFormat) DecimalFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(true);
        digitsField.setDecimalFormat(integerFormat);
        digitsField.setAutoReformat(true);
        digitsField.setValue(12345);
        digitsField.setPositiveOnly(true);
        digitsField.setId(PREFIX_MASK_FIELD_FORM + "DigitsField");
        pane.add(digitsLabel);
        pane.add(digitsField, new CC().wrap());

        addDecoratorForGroupBasedField(digitsLabel, digitsField);

        Label amountLabel = new Label("Amount ($####.##):");
        amountLabel.setId(PREFIX_MASK_FIELD_FORM + "AmountLabel");
        NumberField amountField = new NumberField();
        DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance();
        currencyFormat.setMaximumIntegerDigits(4);
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(2);
        amountField.setDecimalFormat(currencyFormat);
        amountField.setPositiveOnly(true);
        amountField.setValue(168.00);
        amountField.setId(PREFIX_MASK_FIELD_FORM + "AmountField");
        pane.add(amountLabel);
        pane.add(amountField, new CC().wrap());

        addDecoratorForGroupBasedField(amountLabel, amountField);

        Label percentLabel = new Label("Percentage (.##%):");
        percentLabel.setId(PREFIX_MASK_FIELD_FORM + "PercentLabel");
        NumberField percentField = new NumberField();
        DecimalFormat percentFormat = (DecimalFormat) NumberFormat.getPercentInstance();
        percentFormat.setGroupingUsed(false);
        percentFormat.setMaximumIntegerDigits(4);
        percentFormat.setMaximumFractionDigits(3);
        percentField.setDecimalFormat(percentFormat);
        percentField.setValue(0.5);
        percentField.setId(PREFIX_MASK_FIELD_FORM + "PercentField");
        pane.add(percentLabel);
        pane.add(percentField, new CC().wrap());

        addDecoratorForGroupBasedField(percentLabel, percentField);

        Label IP4Label = new Label("IPv4:");
        IP4Label.setId(PREFIX_MASK_FIELD_FORM + "IP4Label");
        FormattedTextField<String> IP4Field = FormattedTextField.createIPv4Field();
        FormattedTextField<String> field = new FormattedTextField<>();
        field.getPatternVerifiers().put("a", new IntegerRangePatternVerifier(0, 999, true));
        field.getPatternVerifiers().put("b", new IntegerRangePatternVerifier(0, 999, true));
        field.getPatternVerifiers().put("c", new IntegerRangePatternVerifier(0, 9999, true));
        field.setPattern("a-b-c");
        field.setId(PREFIX_MASK_FIELD_FORM + "IP4Field");
        pane.add(IP4Label);
        pane.add(field, new CC().wrap());

        addDecoratorForGroupBasedField(IP4Label, IP4Field);

        Label dateLabel = new Label("Date (Default Format):");
        dateLabel.setId(PREFIX_MASK_FIELD_FORM + "dateLabel");
        FormattedTextField<Date> dateField = DateField.createDateField();
        dateField.setId(PREFIX_MASK_FIELD_FORM + "dateField");
        pane.add(dateLabel);
        pane.add(dateField, new CC().wrap());

        addDecoratorForGroupBasedField(dateLabel, dateField);

        Label dateShortLabel = new Label("Date (Short Format):");
        dateShortLabel.setId(PREFIX_MASK_FIELD_FORM + "dateShortLabel");
        FormattedTextField<Date> dateShortField = DateField.createDateField(DateFormat.SHORT);
        dateShortField.setId(PREFIX_MASK_FIELD_FORM + "dateShortField");
        pane.add(dateShortLabel);
        pane.add(dateShortField, new CC().wrap());

        addDecoratorForGroupBasedField(dateShortLabel, dateShortField);

        Label timeLabel = new Label("Time (Default Format):");
        timeLabel.setId(PREFIX_MASK_FIELD_FORM + "timeLabel");
        FormattedTextField<Date> timeField = DateField.createTimeField();
        timeField.setId(PREFIX_MASK_FIELD_FORM + "timeField");
        pane.add(timeLabel);
        pane.add(timeField, new CC().wrap());

        addDecoratorForGroupBasedField(timeLabel, timeField);

        Label dateTimeLabel = new Label("Date/Time (Default):");
        dateTimeLabel.setId(PREFIX_MASK_FIELD_FORM + "dateTimeLabel");
        FormattedTextField<Date> dateTimeField = DateField.createDateTimeField();
        dateTimeField.setId(PREFIX_MASK_FIELD_FORM + "dateTimeField");
        pane.add(dateTimeLabel);
        pane.add(dateTimeField, new CC().wrap());

        addDecoratorForGroupBasedField(dateTimeLabel, dateTimeField);

        Label dateTimeCustomLabel = new Label("Date/Time (Custom):");
        dateTimeCustomLabel.setId(PREFIX_MASK_FIELD_FORM + "dateTimeCustomLabel");
        FormattedTextField<Date> dateTimeCustomField = DateField.createDateField("EEEE yyyyy.MMMMM.dd G hh:mm aa", Calendar.getInstance().getTime());
        dateTimeCustomField.setId(PREFIX_MASK_FIELD_FORM + "dateTimeCustomField");
        pane.add(dateTimeCustomLabel);
        pane.add(dateTimeCustomField, new CC().wrap());

        addDecoratorForGroupBasedField(dateTimeCustomLabel, dateTimeCustomField);

        Label localTimeLabel = new Label("Time (LocalTime):");
        localTimeLabel.setId(PREFIX_MASK_FIELD_FORM + "localTimeLabel");
        LocalTimeField localTimeField = LocalTimeField.createLocalTimeField();
        localTimeField.setId(PREFIX_MASK_FIELD_FORM + "localTimeField");
        pane.add(localTimeLabel);
        pane.add(localTimeField, new CC().wrap());

        addDecoratorForGroupBasedField(localTimeLabel, localTimeField);

        return pane;
    }

    private static void addDecoratorForMaskTextField(Label label, MaskTextField field) {
        field.setClearButtonVisible(true);

        ImageView tip = new ImageView(new Image("/jidefx/examples/fields/mask.png"));
        Tooltip tooltip = new Tooltip("InputMask: " + field.getInputMask()
                + "\n" + "ConversionMask: " + field.getConversionMask()
                + "\n" + "RequiredMask: " + field.getRequiredMask()
                + "\n" + "PlaceholderCharacter: " + field.getPlaceholderCharacter()
        );
        Tooltip.install(tip, tooltip);
        DecorationUtils.install(label, new Decorator<Node>(tip, Pos.CENTER_RIGHT, new Point2D(80, 0), new Insets(0)));
    }

    private static void addDecoratorForGroupBasedField(Label label, FormattedTextField<?> field) {
        field.setSpinnersVisible(true);

        ImageView tip = new ImageView(new Image("/jidefx/examples/fields/formatted.png"));
        Tooltip tooltip = new Tooltip("Pattern: " + field.getPattern());
        Tooltip.install(tip, tooltip);
        DecorationUtils.install(label, new Decorator<Node>(tip, Pos.CENTER_RIGHT, new Point2D(80, 0), new Insets(0)));

        field.installAdjustmentMouseHandler(label, 1);
    }

    @Override
    public String getDemoFolder() {
        return "src/field";
    }
}