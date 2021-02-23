package menu;
import menu.*;
import viewport.*;
import model.*;

import helpers.TestHelper;

import org.testfx.framework.junit5.ApplicationTest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;

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
@TestMethodOrder(OrderAnnotation.class)
public class DoubleNodeSelectedDetailsTest extends ApplicationTest{
    private DoubleNodeSelectedDetails details;
    private Scene scene;

    private VisualGraphNode nodeA = VisualGraphNode.create(new Vector(), new GraphNode(0));
    private VisualGraphNode nodeB = VisualGraphNode.create(new Vector(1, 1, 1), new GraphNode(0));

    @Override
    public void start(Stage stage){
        details = new DoubleNodeSelectedDetails(nodeA, nodeB);
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @Order(1)
    public void initialiseTest(){
        Button aToB = (Button) TestHelper.getNodeById(details.getChildren(), "atob");
        assertNotNull(aToB, "Should contain a button labelled aToB");

        Button bToA = (Button) TestHelper.getNodeById(details.getChildren(), "btoa");
        assertNotNull(bToA, "Should contain a button labelled bToA");

        assertTrue(TestHelper.containsInstanceOf(details.getChildren(), EmptyDetails.class), "Should contain an empty details panel");
    }

    @Test
    @Order(2)
    public void aToBButtonTest(){
        Button aToB = (Button) TestHelper.getNodeById(details.getChildren(), "atob");
        assertNotNull(aToB, "Button should exist");

        assertEquals(0, nodeA.getNode().getEdges().size(), "Should have no edges initially");

        TestHelper.buttonClick(aToB);

        assertEquals(1, nodeA.getNode().getEdges().size(), "Should have a new edge after clicking button");

        if(nodeA.getNode().getEdges().size() > 0){
            assertEquals(nodeB.getNode(), nodeA.getNode().getEdges().get(0).nodeB, "Edge should go to node b");
        }
        sleep(1000);
        aToB = (Button) TestHelper.getNodeById(details.getChildren(), "atobdelete");

        assertNotNull(aToB, "Should now contain a delete button for this edge");

        TestHelper.buttonClick(aToB);

        assertEquals(0, nodeA.getNode().getEdges().size(), "Should now no edges after delete button click");
    }

    @Test
    @Order(3)
    public void bToAButtonTest(){
        Button bToA = (Button) TestHelper.getNodeById(details.getChildren(), "btoa");
        assertNotNull(bToA, "Button should exist");

        assertEquals(0, nodeB.getNode().getEdges().size(), "Should have no edges initially");

        TestHelper.buttonClick(bToA);

        assertEquals(1, nodeB.getNode().getEdges().size(), "Should have a new edge after clicking button");
        if(nodeB.getNode().getEdges().size() > 0){
            assertEquals(nodeA.getNode(), nodeB.getNode().getEdges().get(0).nodeB, "Edge should go to node b");
        }
        sleep(1000);
        bToA = (Button) TestHelper.getNodeById(details.getChildren(), "btoadelete");

        assertNotNull(bToA, "Should now contain a delete button for this edge");

        TestHelper.buttonClick(bToA);

        assertEquals(0, nodeB.getNode().getEdges().size(), "Should now no edges after delete button click");
    }

    @AfterAll
    public void teardown(){
        VisualGraphNode.delete(nodeA);
        VisualGraphNode.delete(nodeB);
    }
}
