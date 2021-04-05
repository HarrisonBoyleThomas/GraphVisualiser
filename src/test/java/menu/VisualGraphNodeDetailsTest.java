package menu;
import menu.*;
import viewport.*;
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
import javafx.scene.layout.HBox;

import maths.Vector;
import maths.Rotator;

/**
*    Test the VGND panel
**/
public class VisualGraphNodeDetailsTest extends ApplicationTest{
    private VisualGraphNodeDetails details;
    private Scene scene;

    @Override
    public void start(Stage stage){
        details = new VisualGraphNodeDetails(VisualGraphNode.create(new Vector(), new GraphNode(0)));
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
    *    The node the panel represents should be changed to a new node, then set back again
    **/
    @Test
    public void setNodeTest(){
        VisualGraphNode oldNode = details.getNode();
        VisualGraphNode node = VisualGraphNode.create(new Vector(), new GraphNode(0));
        node.getNode().setName("testNode", false);
        details.setNode(node);
        assertEquals(node, details.getNode(), "Should be set to the new node");

        details.setNode(oldNode);
        VisualGraphNode.delete(node);
        assertEquals(oldNode, details.getNode(), "Should be set to the old node");

    }

    /**
    *    The panel should contain sections for x y z coordinates, a delet button
    *    and an EmptyDetails panel
    **/
    @Test
    public void initialiseTest(){
        Node location = scene.lookup("#location");
        assertNotNull(location, "Should contain child component with location section");
        Node xSection = scene.lookup("#xCoord");
        Node ySection = scene.lookup("#yCoord");
        Node zSection = scene.lookup("#zCoord");
        assertTrue(xSection instanceof TextField, "Should contain a text field to edit the x coordinate");
        assertTrue(ySection instanceof TextField, "Should contain a text field to edit the y coordinate");
        assertTrue(zSection instanceof TextField, "Should contain a text field to edit the z coordinate");
        Node delete = scene.lookup("#delete");
        assertNotNull(delete, "Should contain a delete button");
        assertTrue(TestHelper.containsInstanceOf(details.getChildren(), NodeDetails.class), "Should contain a NodeDetails instance");
        assertTrue(TestHelper.containsInstanceOf(details.getChildren(), EmptyDetails.class), "Should contains an empty details instance");
    }

    /**
    *    The location of the node should be changed by typing into each
    *    component's textfield
    **/
    @Test
    public void editLocationTest(){
        try{
            TextField xSection = (TextField) scene.lookup("#xCoord");
            assertEquals(0, details.getNode().getLocation().x, "Should be at position (0,0,0) initially");
            xSection.setText("-1.0");
            assertEquals(-1.0, details.getNode().getLocation().x, "x coord should be changed");
            xSection.setText("--+7");
            assertEquals(-1.0, details.getNode().getLocation().x, "x coord should revert to old value if invalid text typed");
        }
        catch(ClassCastException e){
            fail("xSection is not an editable TextField, or did not exist");
        }

        try{
            TextField ySection = (TextField) scene.lookup("#yCoord");
            assertEquals(0, details.getNode().getLocation().y, "Should be at position (0,0,0) initially");
            ySection.setText("-1.0");
            assertEquals(-1.0, details.getNode().getLocation().y, "y coord should be changed");
            ySection.setText("--+7");
            assertEquals(-1.0, details.getNode().getLocation().y, "y coord should revert to old value if invalid text typed");
        }
        catch(ClassCastException e){
            fail("ySection is not an editable TextField, or did not exist");
        }

        try{
            TextField zSection = (TextField) scene.lookup("#zCoord");
            assertEquals(0, details.getNode().getLocation().z, "Should be at position (0,0,0) initially");
            zSection.setText("-1.0");
            assertEquals(-1.0, details.getNode().getLocation().z, "z coord should be changed");
            zSection.setText("--+7");
            assertEquals(-1.0, details.getNode().getLocation().z, "z coord should revert to old value if invalid text typed");
        }
        catch(ClassCastException e){
            fail("zSection is not an editable TextField, or did not exist");
        }
        details.getNode().setLocation(new Vector());
    }

    /**
    *    A node should be deleted by clicking on the delete button
    **/
    @Test
    public void deleteTest(){
        assertTrue(VisualGraphNode.getNodes().contains(details.getNode()), "Node should be valid in the world initially");
        Node delete = scene.lookup("#delete");
        TestHelper.buttonClick(delete);
        assertFalse(VisualGraphNode.getNodes().contains(details.getNode()), "Node should be deleted after clicking the delete button");
        VisualGraphNode.delete(details.getNode());
    }
}
