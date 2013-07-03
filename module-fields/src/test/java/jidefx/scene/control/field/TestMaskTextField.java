package jidefx.scene.control.field;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestMaskTextField extends Application {

    private FormattedTextField<String> _formattedTextField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Region demo = createDemo();
        root.getChildren().add(demo);

        Scene scene = new Scene(root);

        demo.prefWidthProperty().bind(scene.widthProperty());
        demo.prefHeightProperty().bind(scene.heightProperty());

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(true);
        stage.show();

    }

    private Region createDemo() {
        testSSNField();
        testDateField();
        System.exit(0);
        return null;
    }

    public void testSSNField() {
        MaskTextField field = MaskTextField.createSSNField();
        field.setText("123-45-6789");
        TestCase.assertEquals("123-45-6789", field.getText());
        field.clear();
        TestCase.assertEquals("###-##-####", field.getText());
        field.setPlaceholderCharacter(' ');
        TestCase.assertEquals("   -  -    ", field.getText());
    }

    public void testDateField() {
        FormattedTextField<Date> field = DateField.createDateField("MM/dd/yyyy"); //NON-NLS
        Date time = Calendar.getInstance().getTime();
        field.setValue(time);
        TestCase.assertEquals(new SimpleDateFormat(field.getPattern()).format(time), field.getText());
        field.clear();
        TestCase.assertEquals("//", field.getText());
        TestCase.assertEquals(null, field.getValue());
        field.setText("12/3/2013");
        field.commitEdit();
        TestCase.assertEquals("12/03/2013", field.getText());
        field.setText("12//2013");
        field.commitEdit();
        TestCase.assertEquals("12//2013", field.getText());
        field.cancelEdit();
        TestCase.assertEquals("12/03/2013", field.getText());
        field.processKeyCode(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false));
        TestCase.assertEquals("01/03/2014", field.getText());
        field.nextWord();
        field.increaseValue();
        TestCase.assertEquals("01/04/2014", field.getText());
        field.decreaseValue();
        TestCase.assertEquals("01/03/2014", field.getText());
        field.previousWord();
        field.previousWord();
        field.processKeyCode(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, false, false, false, false));
        TestCase.assertEquals("12/03/2013", field.getText());

        field.setText("02/29/2000");
        field.commitEdit();
        TestCase.assertEquals("02/29/2000", field.getText());
        field.selectRange(6, 6);
        field.increaseValue();
        TestCase.assertEquals("02/28/2001", field.getText());
    }
}
