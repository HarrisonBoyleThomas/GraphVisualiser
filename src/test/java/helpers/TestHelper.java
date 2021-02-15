package helpers;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;

public class TestHelper{
    public static void click(Node toClick){
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, true, true, true, true, true, true, null);
        Event.fireEvent(toClick, mouseEvent);
    }

    public static void comboBoxSelect(ComboBox cb, int index){
        cb.getSelectionModel().select(index);
    }

    public static boolean containsInstanceOf(ObservableList<Node> collection, Class targetClass){
        for(Node n : collection){
            if(targetClass.isInstance(n)){
                return true;
            }
        }
        return false;
    }
}
