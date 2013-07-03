package jidefx.scene.control.editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EnumChoiceBoxEditor<T extends Enum> extends ChoiceBoxEditor<T> implements LazyInitializeEditor<T> {
    private boolean initialized = false;

    @Override
    public void initialize(Class<T> clazz, EditorContext context) {
        super.initialize(clazz, context);
        if (Enum.class.isAssignableFrom(clazz)) {
            initializeEnums(clazz);
        }
    }

    private void initializeEnums(Class<T> clazz) {
        if (!initialized && clazz != null && clazz.isEnum()) {
            ObservableList<T> observableList = FXCollections.observableArrayList(clazz.getEnumConstants());
            setItems(observableList);
            initialized = true;
        }
    }
}
