package helpers;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Parent;

import javafx.application.Platform;

import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class TestHelper{
    public static void click(Node toClick){
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, true, true, true, true, true, true, null);
        Event.fireEvent(toClick, mouseEvent);
    }

    public static void buttonClick(Node toClick){
        ActionEvent actionEvent = new ActionEvent();
        Event.fireEvent(toClick, actionEvent);
    }

    public static void comboBoxSelect(ComboBox cb, int index){
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
                cb.getSelectionModel().select(index);
            }
        });
    }

    /**
    *    Types the supplied string one character at a time into the textfeild
    **/
    public static void enterText(TextField tf, String textIn){
        for(int i = 0; i < textIn.length(); i++){
            char c = textIn.charAt(i);
            KeyCode code = null;
            if(("" + c).equals("\n")){
                code = KeyCode.ENTER;
            }
            else if(("" + c).equals("-")){
                code = KeyCode.MINUS;
            }
            else if(("" + c).equals(".")){
                code = KeyCode.PERIOD;
            }
            else{
                code = KeyCode.getKeyCode("" + c);
            }
            if(code != null){
                System.out.print(code + ", ");
                tf.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false));
            }
        }
    }

    public static boolean containsInstanceOf(ObservableList<Node> collection, Class targetClass){
        for(Node n : collection){
            if(targetClass.isInstance(n)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Node> getChildrenAsClass(ObservableList<Node> collection, Class targetClass){
        ArrayList<Node> matches = new ArrayList<>();
        for(Node n : collection){
            if(targetClass.isInstance(n)){
                matches.add(n);
            }
        }
        return matches;
    }

    public static Node getNodeById(ObservableList<Node> collection, String id){
        if(id == null){
            return null;
        }
        for(Node n : collection){
            if(id.equals(n.getId())){
                return n;
            }
        }
        return null;
    }

    public static Node findNodeById(Parent root, String id){
        if(id.equals(root.getId())){
            return root;
        }
        for(Node n : root.getChildrenUnmodifiable()){
            if(n instanceof Parent){
                Node found = findNodeById((Parent) n, id);
                if(found != null){
                    return found;
                }
            }
            else{
                if(id.equals(n.getId())){
                    return n;
                }
            }
        }
        return null;
    }
}
