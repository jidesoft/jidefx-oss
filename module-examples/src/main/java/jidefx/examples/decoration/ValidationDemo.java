/*
 * @(#)ValidationDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.decoration;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.examples.demo.DemoData;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.validation.*;
import org.apache.commons.validator.routines.*;

import java.time.LocalDate;

public class ValidationDemo extends AbstractFxDemo {

    @Override
    public String getName() {
        return "Validation Demo";
    }

    @Override
    public String getDescription() {
        return "This is a demo for validations. Use Decoration to display validation info " +
                "\n" +
                "Demoed classes:\n" +
                "jidefx.scene.control.validation.*\n";
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static final String PATTERN_IP4 = "\\b(([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\b";

    @Override
    public Region getDemoPanel() {

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);

        Tab signup = new Tab("Signup Validation");
        signup.setContent(createSignUpForm());

        Tab validator = new Tab("Various Validators");
        validator.setContent(createValidatorsForm());

        tabPane.getTabs().addAll(signup, validator);

        CheckBox checkBox = new CheckBox("Using Validation.css");
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    // use a validation css for demo purpose
                    tabPane.getStylesheets().add(ValidationDemo.class.getResource("Validation.css").toExternalForm());
                } else {
                    tabPane.getStylesheets().remove(ValidationDemo.class.getResource("Validation.css").toExternalForm());
                }
            }
        });
        return new VBox(6, checkBox, tabPane);
    }

    private Region createSignUpForm() {
        Region pane = DemoData.createSignUpForm();
        return installValidatorsForSignUpForm(pane);
    }

    private Region installValidatorsForSignUpForm(final Region pane) {
        String prefix = DemoData.PREFIX_SIGNUP_FORM;

        TextField emailField = (TextField) pane.lookup("#" + prefix + "emailField");
        TextField confirmEmailField = (TextField) pane.lookup("#" + prefix + "confirmEmailField");
        SimpleValidator emailValidator = new SimpleValidator(EmailValidator.getInstance()) {
            @Override
            public ValidationEvent call(ValidationObject param) {
                ValidationEvent event = super.call(param);
                if (!ValidationEvent.VALIDATION_OK.equals(event.getEventType()) || emailField.getText().equals(confirmEmailField.getText())) {
                    return event;
                } else {
                    return new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, "Two emails are not the same!");
                }
            }
        };
        ValidationUtils.install(emailField, new SimpleValidator(EmailValidator.getInstance()));
        ValidationUtils.install(emailField, new Validator() {
            @Override
            public ValidationEvent call(ValidationObject param) {
                if (emailField.getText().trim().length() == 0) {
                    return new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, "The email cannot be empty!");
                } else return new SimpleValidator(EmailValidator.getInstance()).call(param);
            }
        }, ValidationMode.ON_DEMAND);
        ValidationUtils.install(confirmEmailField, emailValidator);

        Validator countryValidator = new Validator() {
            @Override
            public ValidationEvent call(ValidationObject param) {
                if ("United States".equals(param.getNewValue())) {
                    return ValidationEvent.OK;
                } else if (param.getNewValue() == null) {
                    return new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, "Please select a country!");
                } else {
                    return new ValidationEvent(ValidationEvent.VALIDATION_WARNING, 0, "We only support signing up in United States at the moment!");
                }
            }
        };
        ChoiceBox countryChoiceBox = (ChoiceBox) pane.lookup("#" + prefix + "countryChoiceBox");
        ValidationUtils.install(countryChoiceBox, countryValidator, ValidationMode.ON_DEMAND);
        ValidationUtils.install(countryChoiceBox, countryValidator, ValidationMode.ON_FLY);

        TextField passwordField = (TextField) pane.lookup("#" + prefix + "passwordField");
        TextField confirmPasswordField = (TextField) pane.lookup("#" + prefix + "confirmPasswordField");
        Validator passwordEmptyValidator = new Validator() {
            @Override
            public ValidationEvent call(ValidationObject param) {
                if (passwordField.getText().trim().length() == 0) {
                    return new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, "The password cannot be empty!");
                } else return ValidationEvent.OK;
            }
        };
        ValidationUtils.install(passwordField, passwordEmptyValidator, ValidationMode.ON_DEMAND);
        ValidationUtils.install(passwordField, passwordEmptyValidator, ValidationMode.ON_FLY);

        ValidationUtils.install(confirmPasswordField, new Validator() {
            @Override
            public ValidationEvent call(ValidationObject param) {
                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    return new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, "The two password are different!");
                } else return ValidationEvent.OK;
            }
        }, ValidationMode.ON_FOCUS_LOST);

        Button signUpButton = (Button) pane.lookup("#" + prefix + "signUpButton");
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ValidationUtils.validateOnDemand(pane);
            }
        });

        // add a large help icon to the sign-up button
        Label helpLabel = new Label("", new ImageView(new Image("/jidefx/examples/decoration/help.png")));
        DecorationUtils.install(signUpButton, new Decorator<>(helpLabel, Pos.CENTER_RIGHT, new Point2D(100, 0)));
        helpLabel.setTooltip(new Tooltip("This is a demo to show you how to do validation involving two fields. Both email and password fields" +
                "\ninvolves validation to ensure both value are the same. In addition to that, it also shows you how to use" +
                "\nEmailValidator to check for a valid email address. The country choice box will show a validation warning" +
                "\nif the selected country is not United States."));

        return new DecorationPane(pane);
    }

    private Region createValidatorsForm() {
        Region pane = DemoData.createValidationForm();
        return installValidatorsForValidationForm(pane);
    }

    private Region installValidatorsForValidationForm(Region pane) {
        String prefix = DemoData.PREFIX_VALIDATION_FORM;

        ValidationUtils.install(pane.lookup("#" + prefix + "emailField"), new SimpleValidator(EmailValidator.getInstance()), ValidationMode.ON_FLY);

        TextField urlField = (TextField) pane.lookup("#" + prefix + "urlField");
        ValidationUtils.install(urlField, new SimpleValidator(UrlValidator.getInstance()), ValidationMode.ON_FLY);
        urlField.addEventHandler(ValidationEvent.ANY, new EventHandler<ValidationEvent>() {
            @Override
            public void handle(ValidationEvent event) {
                pane.lookup("#" + prefix + "urlButton").setDisable(event.getEventType() != ValidationEvent.VALIDATION_OK);
            }
        });

        TextField ISBNField = (TextField) pane.lookup("#" + prefix + "ISBNField");
        ValidationUtils.install(ISBNField, new SimpleValidator(ISBNValidator.getInstance()), ValidationMode.ON_FLY);
        ISBNField.addEventHandler(ValidationEvent.ANY, new EventHandler<ValidationEvent>() {
            @Override
            public void handle(ValidationEvent event) {
                pane.lookup("#" + prefix + "amazonButton").setDisable(event.getEventType() != ValidationEvent.VALIDATION_OK);
            }
        });

        ValidationUtils.install(pane.lookup("#" + prefix + "IP4Field"), new SimpleValidator(new RegexValidator(PATTERN_IP4)), ValidationMode.ON_FLY);

        ValidationUtils.install(pane.lookup("#" + prefix + "intField"), new SimpleValidator(IntegerValidator.getInstance()), ValidationMode.ON_FLY);
        ValidationUtils.install(pane.lookup("#" + prefix + "doubleField"), new SimpleValidator(DoubleValidator.getInstance()), ValidationMode.ON_FLY);

        ValidationUtils.install(pane.lookup("#" + prefix + "currencyField"), new SimpleValidator(CurrencyValidator.getInstance()), ValidationMode.ON_FLY);
        ValidationUtils.install(pane.lookup("#" + prefix + "percentField"), new SimpleValidator(PercentValidator.getInstance()), ValidationMode.ON_FLY);
        ValidationUtils.install(pane.lookup("#" + prefix + "dateField"), new SimpleValidator(DateValidator.getInstance()), ValidationMode.ON_FLY);

        TextField cardField = (TextField) pane.lookup("#" + prefix + "cardField");
        ImageView cardImage = (ImageView) pane.lookup("#" + prefix + "cardImage");
        ValidationUtils.install(cardField, new SimpleValidator(new CreditCardValidator(CreditCardValidator.AMEX | CreditCardValidator.VISA | CreditCardValidator.MASTERCARD + CreditCardValidator.DISCOVER | CreditCardValidator.DINERS)), ValidationMode.ON_FLY);
        cardField.addEventHandler(ValidationEvent.ANY, new EventHandler<ValidationEvent>() {
            @Override
            public void handle(ValidationEvent event) {
                if (event.getEventType() == ValidationEvent.VALIDATION_OK) {
                    if (new CreditCardValidator(CreditCardValidator.VISA).isValid(cardField.getText())) {
                        cardImage.setImage(new Image("/jidefx/examples/decoration/VISA.png"));
                    } else if (new CreditCardValidator(CreditCardValidator.MASTERCARD).isValid(cardField.getText())) {
                        cardImage.setImage(new Image("/jidefx/examples/decoration/MasterCard.png"));
                    } else if (new CreditCardValidator(CreditCardValidator.AMEX).isValid(cardField.getText())) {
                        cardImage.setImage(new Image("/jidefx/examples/decoration/AMEX.png"));
                    } else if (new CreditCardValidator(CreditCardValidator.DISCOVER).isValid(cardField.getText())) {
                        cardImage.setImage(new Image("/jidefx/examples/decoration/Discover.png"));
                    } else if (new CreditCardValidator(CreditCardValidator.DINERS).isValid(cardField.getText())) {
                        cardImage.setImage(new Image("/jidefx/examples/decoration/DinersClub.png"));
                    }
                }

            }
        });

        DatePicker dpDate = (DatePicker) pane.lookup("#" + prefix + "dpDate");

        Validator validator = new Validator() {
            @Override
            public ValidationEvent call(ValidationObject param) {
                LocalDate date = LocalDate.of(2014, 8, 20);
                return dpDate.getValue().isBefore(date) ? new ValidationEvent(ValidationEvent.VALIDATION_OK) :
                        new ValidationEvent(ValidationEvent.VALIDATION_ERROR, 0, "NOT OK");
            }
        };

        ValidationUtils.install(dpDate, validator, ValidationMode.ON_FOCUS_LOST);

        return new DecorationPane(pane);
    }
}
