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
import javafx.scene.control.Label;

import maths.Vector;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GraphDetailsTest extends ApplicationTest{
    private GraphDetails details;
    @Override
    public void start(Stage stage){
        details = new GraphDetails();
        Scene scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void initialiseTest(){
        Label nodeCount = (Label) TestHelper.findNodeById(details, "nodecount");
        assertNotNull(nodeCount, "Must contain a label with id nodecount");

        Label edgeCount = (Label) TestHelper.findNodeById(details, "edgecount");
        assertNotNull(edgeCount, "Must contain a label with id edgecount");
    }

    @Test
    public void nodeCountUpdateTest(){
        VisualGraphNode node = VisualGraphNode.create(new Vector(), new GraphNode());
        details.update();
        sleep(100);
        Label nodeCount = (Label) TestHelper.findNodeById(details, "nodecount");
        assertNotNull(nodeCount, "Must contain a label with id nodecount");
        assertEquals("" + VisualGraphNode.getNodes().size(), nodeCount.getText(), "Node count displayed should match the actual node count");
        VisualGraphNode.delete(node);
    }

    @Test
    public void edgeCountUpdateTest(){
        VisualGraphNode nodeA = VisualGraphNode.create(new Vector(), new GraphNode());
        VisualGraphNode nodeB = VisualGraphNode.create(new Vector(1, 1, 1), new GraphNode());

        VisualGraphEdge.create(nodeA.getNode().addEdge(nodeB.getNode(), false));
        details.update();
        sleep(100);

        Label edgeCount = (Label) TestHelper.findNodeById(details, "edgecount");
        assertNotNull(edgeCount, "Must contain a label with id edgecount");
        assertEquals("" + VisualGraphEdge.getEdges().size(), edgeCount.getText(), "Edge count displayed should match the actual edge count");
        VisualGraphNode.delete(nodeA);
        VisualGraphNode.delete(nodeB);
    }
}
