package jidefx.scene.control.decoration;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import jidefx.utils.PredefinedShapes;

import java.util.function.Supplier;

/**
 * {@code PredefinedDecorators} defines several decorator supplier that can be used in the UI. We used them inside
 * JideFX.
 * <p>
 * You can change those factories by calling {@link #setInstance(PredefinedDecorators)} to a new default instance.
 */
@SuppressWarnings("UnusedDeclaration")
public class PredefinedDecorators {
    private static PredefinedDecorators _instance = new PredefinedDecorators();

    /**
     * Gets the default instance of PredefinedDecorators.
     *
     * @return the default instance of PredefinedDecorators.
     * @see #setInstance(PredefinedDecorators)
     */
    public static PredefinedDecorators getInstance() {
        return _instance;
    }

    /**
     * Sets your own instance of PredefinedDecorators if you want to customize the predefined decorators.
     *
     * @param instance a new instance.
     */
    public static void setInstance(PredefinedDecorators instance) {
        _instance = instance;
    }

    /**
     * Gets the decorator supplier for the increase button used as the spinner buttons (in FormattedTextField).
     *
     * @return the decorator supplier for the increase button.
     */
    public Supplier<Decorator<Button>> getIncreaseButtonDecoratorSupplier() {
        return new IncreaseButtonDecoratorSupplier();
    }

    /**
     * Gets the decorator supplier for the decrease button used as the spinner buttons (in FormattedTextField).
     *
     * @return the decorator supplier for the decrease button.
     */
    public Supplier<Decorator<Button>> getDecreaseButtonDecoratorSupplier() {
        return new DecreaseButtonDecoratorSupplier();
    }

    /**
     * Gets the decorator supplier for the clear button which can be used to clear the text in a TextField.
     *
     * @return the decorator supplier for the clear button.
     */
    public Supplier<Decorator<Button>> getClearButtonDecoratorSupplier() {
        return new ClearButtonDecoratorSupplier();
    }

    /**
     * Gets the decorator supplier for the popup button which can be used to show a popup.
     *
     * @return the decorator supplier for popup clear button.
     */
    public Supplier<Decorator<Button>> getPopupButtonDecoratorSupplier() {
        return new PopupButtonDecoratorSupplier();
    }

    protected static class IncreaseButtonDecoratorSupplier implements Supplier<Decorator<Button>> {

        private String STYLE_CLASS_DEFAULT = "increase-button"; //NON-NLS
        public static final double DEFAULT_WIDTH = 10;
        public static final double DEFAULT_HEIGHT = 5;

        @Override
        public Decorator<Button> get() {
            return new Decorator<>(createButton(), Pos.CENTER_RIGHT, new Point2D(-100, -80), new Insets(0, 100, 0, 0));
        }

        public Button createButton() {
            Button button = new Button();
            button.setPrefWidth(DEFAULT_WIDTH);
            button.setPrefHeight(DEFAULT_HEIGHT);
            button.getStyleClass().addAll(STYLE_CLASS_DEFAULT);
            button.setFocusTraversable(false);
            return button;
        }
    }

    protected static class DecreaseButtonDecoratorSupplier implements Supplier<Decorator<Button>> {

        private String STYLE_CLASS_DEFAULT = "decrease-button"; //NON-NLS
        public static final double DEFAULT_WIDTH = 10;
        public static final double DEFAULT_HEIGHT = 5;

        @Override
        public Decorator<Button> get() {
            return new Decorator<>(createButton(), Pos.CENTER_RIGHT, new Point2D(-100, 80), new Insets(0, 100, 0, 0));
        }

        public Button createButton() {
            Button button = new Button();
            button.setPrefWidth(DEFAULT_WIDTH);
            button.setPrefHeight(DEFAULT_HEIGHT);
            button.getStyleClass().addAll(STYLE_CLASS_DEFAULT);
            button.setFocusTraversable(false);
            button.setPickOnBounds(true);
            return button;
        }
    }

    protected static class ClearButtonDecoratorSupplier extends AbstractButtonDecoratorSupplier {
        public static final double DEFAULT_SIZE = 12;

        @Override
        public Decorator<Button> get() {
            return create(PredefinedShapes.getInstance().createClearIcon(DEFAULT_SIZE));
        }
    }

    protected static class PopupButtonDecoratorSupplier extends AbstractButtonDecoratorSupplier {
        public static final double DEFAULT_WIDTH = 12;
        public static final double DEFAULT_HEIGHT = 16;

        @Override
        public Decorator<Button> get() {
            return create(PredefinedShapes.getInstance().createArrowIcon(10), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    /**
     * A convenient class that can be subclassed to create a button decorator supplier using any node. The button has no
     * background. The graphic is set to the provided node.
     */
    public static abstract class AbstractButtonDecoratorSupplier implements Supplier<Decorator<Button>> {

        private String STYLE_CLASS_DEFAULT = "no-background-button"; //NON-NLS

        public Decorator<Button> create(Node shape) {
            return new Decorator<>(createButton(shape), Pos.CENTER_RIGHT, new Point2D(-100, 0), new Insets(0, 100, 0, 0));
        }

        public Decorator<Button> create(Node shape, double width, double height) {
            return new Decorator<>(createButton(shape, width, height), Pos.CENTER_RIGHT, new Point2D(-100, 0), new Insets(0, 100, 0, 0));
        }

        protected Button createButton(Node shape) {
            return createButton(shape, shape.prefWidth(-1), shape.prefHeight(-1));
        }

        protected Button createButton(Node shape, double width, double height) {
            Button button = new Button();
            button.setPrefWidth(width);
            button.setPrefHeight(height);
            button.getStyleClass().addAll(STYLE_CLASS_DEFAULT);
            button.setFocusTraversable(false);
            button.setPickOnBounds(true);
            button.setCursor(Cursor.DEFAULT);

            shape.setFocusTraversable(false);
            button.setGraphic(shape);

            return button;
        }
    }
}
