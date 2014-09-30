package jidefx.scene.control.validation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Node;

import java.util.Map;
import java.util.stream.Stream;

public class ValidationGroup {

    private ObservableMap<Node, ValidationStatus> validationNodes = FXCollections.observableHashMap();
    private BooleanProperty invalidProperty = new SimpleBooleanProperty();

    public ValidationGroup(Node... targetNodes) {
        for (Node node : targetNodes) {
            if (node != null) {
                validationNodes.put(node, ValidationUtils.getValidationStatus(node));
                node.addEventHandler(ValidationEvent.ANY, event -> {
                    validationNodes.put((Node) event.getTarget(), ValidationUtils.getValidationStatus((Node) event.getTarget()));
                });
            }
        }

        validationNodes.addListener((MapChangeListener<Node, ValidationStatus>) change -> {
            Stream<Map.Entry<Node, ValidationStatus>> entryStream = validationNodes.entrySet().stream().filter(entry -> entry.getValue() == ValidationStatus.VALIDATION_ERROR);

            invalidProperty.set(entryStream.count() > 0);
        });
    }

    public Boolean isInvalid() {
        return invalidProperty.get();
    }

    public ReadOnlyBooleanProperty invalidProperty() {
        return invalidProperty;
    }

    public void addValidationNode(Node targetNode) {
        validationNodes.put(targetNode, ValidationUtils.getValidationStatus(targetNode));
    }

    public void removeValidationNode(Node targetNode) {
        validationNodes.remove(targetNode);
    }
}
