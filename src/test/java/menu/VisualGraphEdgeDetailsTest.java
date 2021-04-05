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
import javafx.scene.control.Button;

import maths.Vector;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VisualGraphEdgeDetailsTest extends ApplicationTest{

    private VisualGraphEdgeDetails details;
    private Scene scene;

    private VisualGraphNode nodeA = VisualGraphNode.create(new Vector(), new GraphNode(0));

    private VisualGraphNode nodeB = VisualGraphNode.create(new Vector(1,1,1), new GraphNode(0));

    private VisualGraphEdge edge = VisualGraphEdge.create(nodeA.getNode().addEdge(nodeB.getNode(), false));
    {
        edge.getEdge().setName("test", false);
    }
    @Override
    public void start(Stage stage){
        details = new VisualGraphEdgeDetails(edge);
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void initialiseTest(){
        TextField name = (TextField) TestHelper.findNodeById(details, "name");
        assertNotNull(name, "Should contain a text field with id name");
        if(name != null){
            assertEquals(edge.getEdge().getName(), name.getText(), "Name field should have text matching the edge name");
        }

        TextField weight = (TextField) TestHelper.findNodeById(details, "length");
        assertNotNull(weight, "Should contain a text field with id length");
        if(weight != null){
            assertEquals("" + edge.getEdge().getLength(), weight.getText(), "Weight field should have text matching the edge weight");
        }

        Button nodeAButton = (Button) TestHelper.findNodeById(details, "nodea");
        assertNotNull(nodeAButton, "Should contain a button labelled nodea");

        Button nodeBButton = (Button) TestHelper.findNodeById(details, "nodeb");
        assertNotNull(nodeBButton, "Should contain a button labelled nodeb");

        assertTrue(TestHelper.containsInstanceOf(details.getChildren(), EmptyDetails.class), "Should contain empty details as a sub panel");
    }

    @Test
    public void editNameTest(){
        TextField name = (TextField) TestHelper.findNodeById(details, "name");
        assertNotNull(name, "Should contain a text field with id name");
        assertEquals(edge.getEdge().getName(), name.getText(), "Name field should have text matching the edge name");

        name.setText("hellothere");
        sleep(1000);
        assertEquals("hellothere", edge.getEdge().getName(), "Edge name should be changed");
        name = (TextField) TestHelper.findNodeById(details, "name");

        assertEquals(edge.getEdge().getName(), name.getText(), "Text should be set to the new value");
    }

    @Test
    public void editWeightTest(){
        TextField weight = (TextField) TestHelper.findNodeById(details, "length");
        assertNotNull(weight, "Should contain a text field with id length");
        assertEquals("" + edge.getEdge().getLength(), weight.getText(), "Weight field should have text matching the edge weight");

        weight.setText("42");

        sleep(1000);
        assertEquals(42, edge.getEdge().getLength(), "Edhe weight should be changed");

        weight = (TextField) TestHelper.findNodeById(details, "length");
        assertEquals("" + edge.getEdge().getLength(), weight.getText(), "Text field should contain the new weight");
    }

    @Test
    public void originButtonTest(){
        Button nodeAButton = (Button) TestHelper.findNodeById(details, "nodea");
        assertNotNull(nodeAButton, "Should contain a button labelled nodea");

        MainWindow.get().handleMovementInput(null);

        TestHelper.buttonClick(nodeAButton);

        assertEquals(1, MainWindow.get().getClickedNodes().size(), "Should be just 1 selected node");
        assertEquals(nodeA, MainWindow.get().getClickedNodes().get(0), "Clicked nodes should contain just nodeA");
        assertEquals(0, MainWindow.get().getClickedEdges().size(), "Should be no clicked edges");
    }

    @Test
    public void destinationButtonTest(){
        Button nodeBButton = (Button) TestHelper.findNodeById(details, "nodeb");
        assertNotNull(nodeBButton, "Should contain a button labelled nodeb");

        MainWindow.get().handleMovementInput(null);

        TestHelper.buttonClick(nodeBButton);

        assertEquals(1, MainWindow.get().getClickedNodes().size(), "Should be just 1 selected node");
        assertEquals(nodeB, MainWindow.get().getClickedNodes().get(0), "Clicked nodes should contain just nodeA");
        assertEquals(0, MainWindow.get().getClickedEdges().size(), "Should be no clicked edges");
    }

    @AfterAll
    public void teardown(){
        VisualGraphNode.delete(nodeA);
        VisualGraphNode.delete(nodeB);
    }
}
