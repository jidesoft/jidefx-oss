/*
 * @(#)AbstractFxDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.demo;

//import com.aquafx_project.AquaFx;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.scenicview.ScenicView;
import org.scenicview.utils.ScenicViewBooter;

import java.util.Arrays;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * A template to create additional demo module.
 */
abstract public class AbstractFxDemo extends Application implements FxDemo {
    private BooleanProperty _traceFocusProperty;

    public BooleanProperty tranceFocusProperty() {
        if (_traceFocusProperty == null) {
            _traceFocusProperty = new SimpleBooleanProperty(false);
        }
        return _traceFocusProperty;
    }

    public boolean isTraceFocus() {
        return tranceFocusProperty().get();
    }

    public void setTraceFocus(boolean traceFocus) {
        tranceFocusProperty().set(traceFocus);
    }

    public AbstractFxDemo() {
    }

    public String getDescription() {
        return null;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }

    public Region getOptionsPanel() {
        return null;
    }

    public boolean isCommonOptionsPaneVisible() {
        return true;
    }

    public void dispose() {
    }

    @Override
    public void start(Stage stage) {
        String stylesheet = Preferences.userRoot().get("JideFXDemo.UserAgentStylesheet", STYLESHEET_MODENA);
        setUserAgentStylesheet(stylesheet);

        String locale = Preferences.userRoot().get("JideFXDemo.Locale", Locale.getDefault().toLanguageTag());
        Locale.setDefault(Locale.forLanguageTag(locale));
        System.out.println("Setting locale to " + Locale.getDefault().toString());

        Scene scene = new Scene(createDemo(this));

        ScenicViewBooter.show(scene);

        stage.setTitle(getName() + " - JIDE "/* + " on JDK " + SystemInfo.getJavaVersion()*/);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(true);
        stage.show();

        scene.focusOwnerProperty().addListener((observable, oldValue, newValue) -> {
            if (isTraceFocus()) {
                System.out.println(newValue);
            }
        });
    }

    public Pane createDemo(final FxDemo demo) {
        Region demoPanel = demo.getDemoPanel();

        demoPanel.setPadding(createInsets());
        Region optionsPanel = createOptionsPanel(demo, demoPanel);
        optionsPanel.setPadding(createInsets());

        HBox.setHgrow(demoPanel, Priority.ALWAYS);
        return new HBox(10, optionsPanel, demoPanel);
    }

    protected Region createOptionsPanel(FxDemo demo, Node demoPanel) {
        Region optionsPanel = demo.getOptionsPanel();
        Accordion accordion = new Accordion();
        if (optionsPanel != null) {
            optionsPanel.setPadding(createInsets());
            accordion.getPanes().add(new TitledPane("Options", optionsPanel));
        }

        if (demo.isCommonOptionsPaneVisible()) {
            Region commonOptionsPanel = createCommonOptions(demoPanel);
            commonOptionsPanel.setPadding(createInsets());
            accordion.getPanes().add(new TitledPane("Common Options", commonOptionsPanel));
        }

        String description = demo.getDescription();
        if (description != null && description.trim().length() > 0) {
            Label label = new Label(description);
            label.setWrapText(true);
            label.setPrefWidth(300);
            VBox descPanel = new VBox();
            VBox.setVgrow(label, Priority.ALWAYS);
            descPanel.getChildren().add(label);
            descPanel.setPadding(createInsets());
            accordion.getPanes().add(new TitledPane("Description", descPanel));
        }

        if (!accordion.getPanes().isEmpty()) {
            accordion.setExpandedPane(accordion.getPanes().get(0));
        }

        VBox box = new VBox(10);
        box.getChildren().addAll(accordion);
        return box;
    }

    protected Insets createInsets() {
        return new Insets(10);
    }

    private Region createCommonOptions(final Node demoPanel) {
        CheckBox toggleLTR = new CheckBox("_Toggle Left-to-Right/Right-to-Left");
        toggleLTR.selectedProperty().addListener((property, oldValue, newValue) -> {
            demoPanel.setNodeOrientation(newValue ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
        });
        toggleLTR.setSelected(true);

        CheckBox traceFocus = new CheckBox("Trace _Focus");
        traceFocus.selectedProperty().bindBidirectional(tranceFocusProperty());

        Label message = new Label();
        message.setTextFill(Color.RED);
        ChoiceBox<String> toggleStyle = new ChoiceBox<>(FXCollections.observableArrayList(STYLESHEET_MODENA, STYLESHEET_CASPIAN));
        toggleStyle.setValue(getUserAgentStylesheet());
        toggleStyle.valueProperty().addListener((property, oldValue, newValue) -> {
            Preferences.userRoot().put("JideFXDemo.UserAgentStylesheet", newValue);
            message.setText("Restart the app required");
            message.setVisible(true);
        });
        Label styleLabel = new Label("_Style: ");
        styleLabel.setMnemonicParsing(true);
        styleLabel.setLabelFor(toggleStyle);

/*
        CheckBox aquafxStyle = new CheckBox("AquaFX");
        aquafxStyle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (aBoolean2) {
                    AquaFx.style();
                }
                else {
                    PlatformImpl.setDefaultPlatformUserAgentStylesheet();
                }
            }
        });

        HBox stylePanel = new HBox(4, styleLabel, toggleStyle, aquafxStyle);
*/

        HBox stylePanel = new HBox(4, styleLabel, toggleStyle);
        stylePanel.setAlignment(Pos.BASELINE_LEFT);

        Locale[] locales = Locale.getAvailableLocales();
        Arrays.sort(locales, (o1, o2) -> {
            if (o1 instanceof Locale && o2 instanceof Locale) {
                return o1.toString().compareTo(o2.toString());
            }
            return 0;
        });
        ChoiceBox<Locale> locale = new ChoiceBox<>(FXCollections.observableArrayList(locales));
        locale.getSelectionModel().select(Locale.getDefault());
        locale.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Locale>() {
            @Override
            public void changed(ObservableValue<? extends Locale> property, Locale oldValue, Locale newValue) {
                Preferences.userRoot().put("JideFXDemo.Locale", newValue.toLanguageTag());
                message.setText("Restart the app required");
                message.setVisible(true);
            }
        });


        Label label = new Label("_Change Locale: ");
        label.setMnemonicParsing(true);
        label.setLabelFor(locale);

        HBox localePanel = new HBox(4, label, locale);
        localePanel.setAlignment(Pos.BASELINE_LEFT);

        message.setVisible(false);
        return new VBox(4, stylePanel, toggleLTR, traceFocus, localePanel, message);
    }


    public String[] getDemoSource() {
        return new String[]{getClass().getName() + ".java"};
    }

    public String getDemoFolder() {
        return "";
    }

    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    @Override
    public Product getProduct() {
        return Product.COMMON;
    }
}


