package menu;
import menu.*;
import model.*;

import helpers.TestHelper;

import org.testfx.framework.junit5.ApplicationTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.fail;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import maths.Vector;
import maths.Rotator;

public class NodeDetailsTest extends ApplicationTest{
    private NodeDetails details;
    private Scene scene;

    @Override
    public void start(Stage stage){
        details = new NodeDetails(new GraphNode(0));
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void setNodeTest(){
        GraphNode oldNode = details.getNode();
        GraphNode newNode = new GraphNode(0);
        details.setNode(newNode);
        assertEquals(newNode, details.getNode(), "Should be set to the new node");
        details.setNode(oldNode);
        assertEquals(oldNode, details.getNode(), "Should be set to the old node");
    }

    @Test
    public void initialiseTest(){
        Node name = scene.lookup("#name");
        assertTrue(name instanceof TextField, "Should contain a textfield for editing the name of the node");
        Node value = scene.lookup("#value");
        assertTrue(value instanceof TextField, "Should contain a textfield for editing the value of the node");
        Node edges = scene.lookup("#edges");
        assertTrue(edges instanceof VBox, "Should contain a vbox for edge buttons");
    }

    @Test
    public void editNameTest(){
        try{
            TextField name = (TextField) scene.lookup("#name");
            String oldName = details.getNode().getName();;
            name.setText("testName");
            assertEquals("testName", details.getNode().getName(), "Name should be changed by typing into the text box");
            name.setText(oldName);
            assertEquals(oldName, details.getNode().getName(), "Name should be set back to the old value");
        }
        catch(ClassCastException e){
            fail("Did not contain a textfield for the name");
        }
    }

    @Test
    public void editValueTest(){
        try{
            TextField value = (TextField) scene.lookup("#value");
            int oldValue = details.getNode().getValue();;
            value.setText("42");
            assertEquals(42, details.getNode().getValue(), "Value should be changed by typing into the text box");
            value.setText("blah");
            assertEquals(42, details.getNode().getValue(), "Value should be unchanged when entering invalid values");
            value.setText("" + oldValue);
            assertEquals(oldValue, details.getNode().getValue(), "Value should be set back to the old value");
        }
        catch(ClassCastException e){
            fail("Did not contain a textfield for the value");
        }
    }
}
