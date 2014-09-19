package jidefx.scene.control.validation;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import jidefx.animation.AnimationType;
import jidefx.animation.AnimationUtils;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;

import java.util.Arrays;
import java.util.function.Function;

public class ValidationDecorators {

    private static final Double DEFAULT_TOOLTIP_OFFSET_X = 15.0;
    private static final Double DEFAULT_TOOLTIP_OFFSET_Y = 15.0;

    public static final String PROPERTY_VALIDATION_DECORATOR = "validation-decorator"; //NON-NLS
    public static final String PROPERTY_REQUIRED_VALIDATION_DECORATOR = "required-validation-decorator"; //NON-NLS

    public static Decorator<Label> graphicDecoratorCreator(Node targetNode, Decorator oldDecorator, ValidationEvent event) {
        Label label;
        ImageView graphic = new ImageView(ValidationIcons.getInstance().getValidationResultIcon(event.getEventType()));

        if (oldDecorator != null && exists(targetNode, oldDecorator)) {
            label = (Label) oldDecorator.getNode();
            label.setGraphic(graphic);
            label.setTooltip(createTooltip(event.getMessage(), label));
            DecorationUtils.setAnimationPlayed(label, false);

            return oldDecorator;
        } else {
            return createAndInstallValidationDecorator(targetNode, event, ValidationDecorationType.GRAPHIC);
        }
    }

    public static Decorator<Label> fontAwesomeDecoratorCreator(Node targetNode, Decorator oldDecorator, ValidationEvent event) {
        Label label;
        String fontAwesomeIcon = ValidationFontAwesomeIcons.getInstance().getValidationFontAwesomeIcon(event.getEventType());

        if (oldDecorator != null && exists(targetNode, oldDecorator)) {
            label = (Label) oldDecorator.getNode();
            label.setText(fontAwesomeIcon);
            label.setTooltip(createTooltip(event.getMessage(), label));
            DecorationUtils.setAnimationPlayed(label, false);

            return oldDecorator;
        } else {
            return createAndInstallValidationDecorator(targetNode, event, ValidationDecorationType.FONT_AWESOME);
        }
    }

    public static void defaultDecorationClickBehavior(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() != null && mouseEvent.getTarget() instanceof Label) {
            Label label = (Label) mouseEvent.getTarget();
            if (label.getTooltip() != null) {
                Point2D point = label.localToScene(DEFAULT_TOOLTIP_OFFSET_X, DEFAULT_TOOLTIP_OFFSET_Y);

                label.getTooltip().setAutoHide(true);
                label.getTooltip().show(label, point.getX()
                        + label.getScene().getX() + label.getScene().getWindow().getX(), point.getY()
                        + label.getScene().getY() + label.getScene().getWindow().getY());
            }
        }
    }

    public static boolean exists(Node targetNode, Decorator resultDecorator) {
        Object o = DecorationUtils.getDecorators(targetNode);
        if (o != null) {
            if (o.equals(resultDecorator) || (o instanceof Decorator[] && Arrays.asList((Decorator[]) o).contains(resultDecorator))) {
                return true;
            }
        }
        return false;
    }

    public static ValidationUtils.TooltipFix createTooltip(String message, Label label) {
        ValidationUtils.TooltipFix tooltip = null;

        if (message != null && message.trim().length() > 0) {
            tooltip = new ValidationUtils.TooltipFix(label);
            tooltip.setText(message);
            tooltip.setAutoHide(true);
            tooltip.setConsumeAutoHidingEvents(false);
        }

        return tooltip;
    }

    public static Decorator<Label> createAndInstallValidationDecorator(Node targetNode, ValidationEvent event, ValidationDecorationType type) {
        Label label = new Label();
        label.getStyleClass().add(PROPERTY_VALIDATION_DECORATOR);
        label.setTooltip(createTooltip(event.getMessage(), label));

        switch (type) {
            case GRAPHIC:
                label.setGraphic(new ImageView(ValidationIcons.getInstance().getValidationResultIcon(event.getEventType())));
                break;
            case FONT_AWESOME:
                label.setText(ValidationFontAwesomeIcons.getInstance().getValidationFontAwesomeIcon(event.getEventType()));
                label.setStyle("-fx-font-family: FontAwesome;");
                break;
        }

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, ValidationDecorators::defaultDecorationClickBehavior);

        Decorator<Label> newDecorator = null;
        if (targetNode instanceof Cell) {
            newDecorator = new Decorator<>(label, Pos.CENTER_RIGHT, new Point2D(-60, 0), new Insets(0, 100, 0, 0), AnimationType.TADA);
        } else {
            newDecorator = new Decorator<>(label, Pos.BOTTOM_LEFT, new Point2D(0, 0), new Insets(0, 0, 0, 0), AnimationType.TADA);
        }
        DecorationUtils.install(targetNode, newDecorator);

        targetNode.getParent().requestLayout();

        return newDecorator;
    }

    public static Decorator<Label> graphicRequiredCreator(Node targetNode) {
        Image image = new Image(ValidationIcons.ICON_REQUIRED);
        Label lblRequired = new Label("", new ImageView(image));
        lblRequired.getStyleClass().add(PROPERTY_REQUIRED_VALIDATION_DECORATOR);

        Decorator<Label> requiredDecorator = new Decorator<>(lblRequired, Pos.TOP_LEFT, new Point2D(image.getWidth()/2, image.getHeight()/2),
                new Insets(0, 0, 0, 0), false, AnimationUtils.createTransition(targetNode, AnimationType.NONE));

        return requiredDecorator;
    }

    public static Decorator<Label> fontAwesomeRequiredCreator(Node targetNode) {
        Label lblRequired = new Label();
        lblRequired.setText("\uf005");
        lblRequired.setStyle("-fx-font-family: FontAwesome;");

        lblRequired.getStyleClass().add(PROPERTY_REQUIRED_VALIDATION_DECORATOR);
        Decorator<Label> requiredDecorator = new Decorator<>(lblRequired, Pos.TOP_LEFT, new Point2D(0, 0), new Insets(0, 0, 0, 0));

        return requiredDecorator;
    }

    public static Decorator<Label> installRequiredDecorator(Node targetNode, Function<Node, Decorator<Label>> requiredDecoratorCreator) {
        Decorator<Label> decorator = requiredDecoratorCreator.apply(targetNode);
        DecorationUtils.install(targetNode, decorator);
        targetNode.getParent().requestLayout();

        return decorator;
    }

}
