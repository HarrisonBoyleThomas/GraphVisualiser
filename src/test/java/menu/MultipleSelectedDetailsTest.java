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
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;

import java.util.ArrayList;

import javafx.scene.input.KeyCode;

import maths.Vector;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MultipleSelectedDetailsTest extends ApplicationTest{

    private MultipleSelectedDetails details;
    private Scene scene;

    private ArrayList<VisualGraphNode> nodes = new ArrayList<>();

    @Override
    public void start(Stage stage){
        for(int i = 0; i < 10; i++){
            GraphNode node = new GraphNode(0);
            node.setName("node:" + i);
            nodes.add(VisualGraphNode.create(new Vector(i, i, i), node));
        }
        MainWindow.get().createEdge(nodes.get(0), nodes.get(1));
        //select all components
        MainWindow.get().handleSingleInput(KeyCode.M);
        details = new MultipleSelectedDetails();
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void initialiseTest(){
        VBox nodeSection = (VBox) ((ScrollPane) TestHelper.getNodeById(details.getChildren(), "nodesection")).getContent();
        assertNotNull(nodeSection, "Should contain a vbox of node buttons");

        for(VisualGraphNode n : nodes){
            Button b = (Button) TestHelper.getNodeById(nodeSection.getChildren(), "" + n.getNode().getName() + ":" + n.getNode().getValue());
            assertNotNull(b, "should contain a button for each created node. Missing: " + n.getNode().getName());
        }

        VBox edgeSection = (VBox) ((ScrollPane) TestHelper.getNodeById(details.getChildren(), "edgesection")).getContent();
        assertNotNull(edgeSection, "Should contain a vbox of edge buttons");

        Button edgeButton = (Button) TestHelper.getNodeById(edgeSection.getChildren(), ":1:0");
        assertNotNull(edgeButton, "Should contain a button for the new edge");
    }

    @Test
    public void nodeButtonTests(){
        VBox nodeSection = (VBox) ((ScrollPane) TestHelper.getNodeById(details.getChildren(), "nodesection")).getContent();
        assertNotNull(nodeSection, "Should contain a vbox of node buttons");
        for(VisualGraphNode n : nodes){
            MainWindow.get().handleSingleInput(KeyCode.M);
            MainWindow.get().handleMovementInput(null);
            details.update();
            sleep(1000);
            Button b = (Button) TestHelper.getNodeById(nodeSection.getChildren(), "" + n.getNode().getName() + ":" + n.getNode().getValue());
            assertNotNull(b, "Should contain a button for each created node. Missing: " + n.getNode().getName());
            TestHelper.buttonClick(b);


            assertEquals(1, MainWindow.get().getClickedNodes().size(), "After clicking a node, the node should be the only selected component");
            assertEquals(0, MainWindow.get().getClickedEdges().size(), "After clicking a node, there should be no selected edges");
        }
    }

    @Test
    public void edgeButtonTests(){
        VBox edgeSection = (VBox) ((ScrollPane) TestHelper.getNodeById(details.getChildren(), "edgesection")).getContent();
        assertNotNull(edgeSection, "Should contain a vbox of edge buttons");
        VisualGraphEdge edge = VisualGraphEdge.getEdge(nodes.get(0).getNode().getEdges().get(0));
        MainWindow.get().handleSingleInput(KeyCode.M);
        MainWindow.get().handleMovementInput(null);
        details.update();
        sleep(1000);
        Button b = (Button) TestHelper.getNodeById(edgeSection.getChildren(), edge.getEdge().getName() + ":" + edge.getEdge().getLength() + ":0");
        TestHelper.buttonClick(b);
        assertEquals(0, MainWindow.get().getClickedNodes().size(), "After clicking an edge, no nodes should be selected");
        assertEquals(1, MainWindow.get().getClickedEdges().size(), "After clicking an edge, only 1 edge should be selected");

    }

    @AfterAll
    public void teardown(){
        for(VisualGraphNode node : nodes){
            VisualGraphNode.delete(node);
        }
    }


}
