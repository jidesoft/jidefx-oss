package jidefx.scene.control.editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EnumComboBoxEditor<T extends Enum> extends ComboBoxEditor<T> implements LazyInitializeEditor<T> {
    private boolean initialized = false;

    public EnumComboBoxEditor() {
        setEditable(false);
    }

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
