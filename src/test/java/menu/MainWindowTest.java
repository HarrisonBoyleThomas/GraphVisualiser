package menu;
import menu.*;
import viewport.*;
import model.*;
import model.algorithm.*;
import data.*;

import maths.Vector;
import maths.Rotator;

import helpers.TestHelper;

import org.loadui.testfx.GuiTest;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import java.io.*;
import java.nio.file.Paths;

import javafx.scene.Parent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.api.FxRobot;
import org.testfx.service.query.PointQuery;

import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseButton;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainWindowTest extends ApplicationTest{
    MainWindow mw;
    Stage stage;

    @Override
    public void start(Stage stage){
        Data.CAMERA_CONTROLS.reset();
        Scene scene = MainWindow.get().getScene();
        mw = MainWindow.get();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void getTest(){
        MainWindow mw = MainWindow.get();
        assertNotNull(mw, "mainwindow.get() returned null");
    }

    @Test
    public void initialiseTest(){
        assertTrue(mw.getLeft() instanceof DetailsPanel);
        ScrollPane sp = (ScrollPane) mw.getRight();
        assertTrue(TestHelper.containsInstanceOf(((VBox) sp.getContent()).getChildren(), CameraDetails.class), "Right details does not contain CameraDetails");
        assertTrue(TestHelper.containsInstanceOf(((VBox) sp.getContent()).getChildren(), AlgorithmDetailsPanel.class), "Right details does not contains AlgorithmDetails");
        assertTrue(TestHelper.containsInstanceOf(((VBox) sp.getContent()).getChildren(), GraphDetails.class), "Right details does not contains GraphDetails");
        assertTrue(mw.getTop() instanceof MenuHeader);

        assertTrue(mw.getCenter() instanceof GridPane);
    }

    @Test
    public void detailsPanelTest(){
        Object leftPanel = mw.getLeft();
        assertTrue((leftPanel instanceof DetailsPanel), "The left panel is not a details panel. It is a " + leftPanel.getClass());
    }

    @Test
    public void createViewportTest(){
        GridPane view = (GridPane) mw.getCenter();
        //ensure no viewports exist initially
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();
        assertEquals(1, view.getChildren().size(), "MainWindow did not have correct initial viewport count");
        mw.createViewport();
        assertEquals(2, view.getChildren().size(), "MainWindow did not create a viewport");
        mw.createViewport();
        assertEquals(3, view.getChildren().size(), "MainWindow did not create a viewport");
        mw.createViewport();
        assertEquals(4, view.getChildren().size(), "MainWindow did not create a viewport");
        mw.createViewport();
        assertEquals(4, view.getChildren().size(), "MainWindow created more than 4 viewports");
        while(view.getChildren().size() > 1){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
    }

    @Test
    public void deleteViewportTest(){
        //Initialise
        GridPane view = (GridPane) mw.getCenter();
        while(view.getChildren().size() < 4){
            mw.createViewport();
        }
        mw.deleteViewport((Viewport) view.getChildren().get(3));
        assertEquals(3, view.getChildren().size(), "Viewport not deleted");
        mw.deleteViewport((Viewport) view.getChildren().get(2));
        assertEquals(2, view.getChildren().size(), "Viewport not deleted");
        mw.deleteViewport((Viewport) view.getChildren().get(1));
        assertEquals(1, view.getChildren().size(), "Viewport not deleted");
        mw.deleteViewport((Viewport) view.getChildren().get(0));
        assertEquals(0, view.getChildren().size(), "Viewport not deleted");
        mw.deleteViewport((Viewport) null);
        assertEquals(0, view.getChildren().size(), "Errors thrown when all deleted");

        //Add a single viewport back to the main window
        mw.createViewport();
    }

    @Test
    public void setStartNodeTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise two viewports
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();
        //Viewports update in their own threads, give them time to update
        sleep(1000);
        for(Node n : view.getChildren()){
            Viewport v = (Viewport) n;
            ViewportAlgorithmSelector selector = (ViewportAlgorithmSelector) v.getViewportDetails().getAlgorithmDetails().getChildren().get(0);
            TestHelper.comboBoxSelect(selector, selector.getItems().indexOf("" + HeapBasedDijkstra.class.getSimpleName()));
        }
        mw.createNode();
        GraphNode node = VisualGraphNode.getNodes().get(0).getNode();
        mw.setStartNode(node);
        Viewport v = (Viewport) view.getChildren().get(0);
        ShortestPathAlgorithm algorithm = (ShortestPathAlgorithm) v.getAlgorithm();
        assertEquals(node, algorithm.getStartNode());
        //delete the created node
        mw.deleteNode(VisualGraphNode.getNode(node));
    }

    @Test
    public void getStartNodeTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();
        assertEquals(null, mw.getStartNode(), "Start node should be null when no algorithm is set");
        //Viewports update in their own threads, give them time to update
        sleep(500);
        mw.updateViewport();
        sleep(500);
        for(Node n : view.getChildren()){
            Viewport v = (Viewport) n;
            ViewportAlgorithmSelector selector = (ViewportAlgorithmSelector) v.getViewportDetails().getAlgorithmDetails().getChildren().get(0);
            TestHelper.comboBoxSelect(selector, selector.getItems().indexOf("" + HeapBasedDijkstra.class.getSimpleName()));
        }
        mw.createNode();
        GraphNode node = VisualGraphNode.getNodes().get(0).getNode();
        mw.setStartNode(node);
        assertEquals(node, mw.getStartNode(), "start node not correctly returned/set");
        //delete the created node
        mw.deleteNode(VisualGraphNode.getNode(node));
    }

    @Test
    public void getClickedNodesTest(){
        assertNotNull(mw.getClickedNodes(), "clicked node list should never be null");
        assertEquals(0, mw.getClickedNodes().size(), "initial nodes size should be 0");
        mw.createNode();
        VisualGraphNode node = VisualGraphNode.getNodes().get(0);
        mw.addClickedComponent(node);
        assertEquals(1, mw.getClickedNodes().size());
        mw.deleteNode(node);
    }

    @Test
    public void getClickedEdgesTest(){
        assertNotNull(mw.getClickedEdges(), "clicked edge list should never be null");
        assertEquals(0, mw.getClickedEdges().size(), "initial edges size should be 0");
        mw.createNode();
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);
        mw.createEdge(nodeA, nodeB);
        VisualGraphEdge edge = VisualGraphEdge.getEdges().get(0);
        mw.addClickedComponent(edge);
        assertEquals(1, mw.getClickedEdges().size());
        mw.deleteNode(nodeA);
        mw.deleteNode(nodeB);
        //edge implicitly deleted
    }

    @Test
    public void getCameraTest(){
        assertNotNull(mw.getCamera(), "Camera should never be null");
        assertTrue(mw.getCamera() instanceof Camera, "Must return an instance of camera");
    }

    //handleMovementInputTests

    private void handleMovementInput(KeyCode key){
        ArrayList<KeyCode> keys = new ArrayList<>();
        keys.add(key);
        mw.handleMovementInput(keys);
    }

    @Test
    public void downKeyTest(){
        mw.getCamera().reset();
        Rotator originalRot = mw.getCamera().getRotation();
        handleMovementInput(KeyCode.DOWN);
        Rotator newRot = mw.getCamera().getRotation();
        //Since the pitch of the camera changes by a different amount depending on settings,
        //only test that pitch changes
        assertNotEquals(newRot.pitch, originalRot.pitch, "Pitch value should be different");
        assertEquals(newRot.roll, originalRot.roll, "Roll component should be the same");
        assertEquals(newRot.yaw, originalRot.yaw, "Yaw component should be the same");
        mw.getCamera().reset();

    }

    @Test
    public void upKeyTest(){
        mw.getCamera().reset();
        Rotator originalRot = mw.getCamera().getRotation();
        handleMovementInput(KeyCode.UP);
        Rotator newRot = mw.getCamera().getRotation();
        //Since the pitch of the camera changes by a different amount depending on settings,
        //only test that pitch changes
        assertNotEquals(newRot.pitch, originalRot.pitch, "Pitch value should be different");
        assertEquals(newRot.roll, originalRot.roll, "Roll component should be the same");
        assertEquals(newRot.yaw, originalRot.yaw, "Yaw component should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void leftKeyTest(){
        mw.getCamera().reset();
        Rotator originalRot = mw.getCamera().getRotation();
        handleMovementInput(KeyCode.LEFT);
        Rotator newRot = mw.getCamera().getRotation();
        //Since the pitch of the camera changes by a different amount depending on settings,
        //only test that pitch changes
        assertNotEquals(newRot.yaw, originalRot.yaw, "Yaw value should be different");
        assertEquals(newRot.pitch, originalRot.pitch, "Pitch component should be the same");
        assertEquals(newRot.roll, originalRot.roll, "Roll component should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void rightKeyTest(){
        mw.getCamera().reset();
        Rotator originalRot = mw.getCamera().getRotation();
        handleMovementInput(KeyCode.RIGHT);
        Rotator newRot = mw.getCamera().getRotation();
        //Since the pitch of the camera changes by a different amount depending on settings,
        //only test that pitch changes
        assertNotEquals(newRot.yaw, originalRot.yaw, "Yaw value should be different");
        assertEquals(newRot.roll, originalRot.roll, "Roll component should be the same");
        assertEquals(newRot.pitch, originalRot.pitch, "Pitch component should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void wKeyTest(){
        mw.getCamera().reset();
        Vector originalLoc = mw.getCamera().getLocation();
        handleMovementInput(KeyCode.W);
        Vector newLoc = mw.getCamera().getLocation();

        assertNotEquals(newLoc.x, originalLoc.x, "X components should be different");
        assertEquals(newLoc.y, originalLoc.y, "Y components should be the same");
        assertEquals(newLoc.z, originalLoc.z, "Z components should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void sKeyPressed(){
        mw.getCamera().reset();
        Vector originalLoc = mw.getCamera().getLocation();
        handleMovementInput(KeyCode.S);
        Vector newLoc = mw.getCamera().getLocation();

        assertNotEquals(newLoc.x, originalLoc.x, "X components should be different");
        assertEquals(newLoc.y, originalLoc.y, "Y components should be the same");
        assertEquals(newLoc.z, originalLoc.z, "Z components should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void aKeyTest(){
        mw.getCamera().reset();
        Vector originalLoc = mw.getCamera().getLocation();
        handleMovementInput(KeyCode.A);
        Vector newLoc = mw.getCamera().getLocation();

        assertNotEquals(newLoc.y, originalLoc.y, "Y components should be different");
        assertEquals(newLoc.x, originalLoc.x, "X components should be the same");
        assertEquals(newLoc.z, originalLoc.z, "Z components should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void dKeyTest(){
        mw.getCamera().reset();
        Vector originalLoc = mw.getCamera().getLocation();
        handleMovementInput(KeyCode.D);
        Vector newLoc = mw.getCamera().getLocation();

        assertNotEquals(newLoc.y, originalLoc.y, "Y components should be different");
        assertEquals(newLoc.x, originalLoc.x, "X components should be the same");
        assertEquals(newLoc.z, originalLoc.z, "Z components should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void qKeyTest(){
        mw.getCamera().reset();
        Vector originalLoc = mw.getCamera().getLocation();
        handleMovementInput(KeyCode.Q);
        Vector newLoc = mw.getCamera().getLocation();

        assertNotEquals(newLoc.z, originalLoc.z, "Z components should be different");
        assertEquals(newLoc.x, originalLoc.x, "X components should be the same");
        assertEquals(newLoc.y, originalLoc.y, "Y components should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void zKeyPressed(){
        mw.getCamera().reset();
        Vector originalLoc = mw.getCamera().getLocation();
        handleMovementInput(KeyCode.Z);
        Vector newLoc = mw.getCamera().getLocation();

        assertNotEquals(newLoc.z, originalLoc.z, "Z components should be different");
        assertEquals(newLoc.x, originalLoc.x, "X components should be the same");
        assertEquals(newLoc.y, originalLoc.y, "Y components should be the same");
        mw.getCamera().reset();
    }

    @Test
    public void commaKeyPressed(){
        Rotator originalRot = mw.getCamera().getRotation();
        handleMovementInput(KeyCode.COMMA);
        Rotator newRot = mw.getCamera().getRotation();
        //Since the pitch of the camera changes by a different amount depending on settings,
        //only test that pitch changes
        assertNotEquals(newRot.roll, originalRot.roll, "Roll value should be different");
        assertEquals(newRot.pitch, originalRot.pitch, "Pitch component should be the same");
        assertEquals(newRot.yaw, originalRot.yaw, "Yaw component should be the same");
    }

    @Test
    public void periodKeyPressed(){
        Rotator originalRot = mw.getCamera().getRotation();
        handleMovementInput(KeyCode.PERIOD);
        Rotator newRot = mw.getCamera().getRotation();
        //Since the pitch of the camera changes by a different amount depending on settings,
        //only test that pitch changes
        assertNotEquals(newRot.roll, originalRot.roll, "Roll value should be different");
        assertEquals(newRot.pitch, originalRot.pitch, "Pitch component should be the same");
        assertEquals(newRot.yaw, originalRot.yaw, "Yaw component should be the same");
    }

    @Test
    public void controlKeyPressed(){
        handleMovementInput(null);
        assertEquals(false, mw.getMultiSelect(), "Multiselect should be null initially");
        handleMovementInput(KeyCode.CONTROL);
        assertEquals(true, mw.getMultiSelect(), "Multiselect should be true when pressing the control key");
        //clear multiselect
        handleMovementInput(null);
    }


    //Handle single movement input tests
    @Test
    public void deleteKeyPressed(){
        //Delete all created nodes and edges
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        mw.createNode();
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);
        mw.createEdge(nodeA, nodeB);
        VisualGraphEdge edge = VisualGraphEdge.getEdges().get(0);
        mw.addClickedComponent(edge);
        mw.handleSingleInput(KeyCode.DELETE);
        assertEquals(0, VisualGraphEdge.getEdges().size(), "The edge should be deleted if it was selected");
        mw.addClickedComponent(nodeA, true);
        mw.addClickedComponent(nodeB, true);
        mw.handleSingleInput(KeyCode.DELETE);
        assertEquals(0, VisualGraphNode.getNodes().size(), "The nodes should be deleted if they were selected");

        //Delete all created nodes and edges
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void fKeyPressed(){
        //Delete all created nodes and edges
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        int originalCount = VisualGraphNode.getNodes().size();
        //clear multiselect
        handleMovementInput(null);
        mw.handleSingleInput(KeyCode.F);
        assertEquals(originalCount + 1, VisualGraphNode.getNodes().size(), "A node should be created by pressing the F key");
        mw.addClickedComponent(VisualGraphNode.getNodes().get(0));
        mw.handleSingleInput(KeyCode.F);
        assertEquals(originalCount + 2, VisualGraphNode.getNodes().size(), "Another node should be made if only 1 node is selected");
        mw.addClickedComponent(VisualGraphNode.getNodes().get(1), true);
        int originalEdgeCount = VisualGraphEdge.getEdges().size();
        mw.handleSingleInput(KeyCode.F);
        assertEquals(originalEdgeCount + 1, VisualGraphEdge.getEdges().size(), "An edge should have been created between the two selected nodes");
        mw.addClickedComponent(VisualGraphEdge.getEdges().get(0), true);
        mw.handleSingleInput(KeyCode.F);
        assertEquals(originalCount + 3, VisualGraphNode.getNodes().size(), "A new node should be created if any edges are selected");

        //reset mw click and select states
        handleMovementInput(null);
        mw.addClickedComponent(null);
        //Delete all created nodes and edges
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void mKeyPressed(){
        //ensure no nodes exist before the test starts
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        handleMovementInput(null);
        mw.addClickedComponent(null);
        mw.createNode();
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);
        mw.createEdge(nodeA, nodeB);
        VisualGraphEdge edge = VisualGraphEdge.getEdges().get(0);
        mw.handleSingleInput(KeyCode.M);
        sleep(1000);
        assertEquals(2, mw.getClickedNodes().size(), "Selectall not called. Not all nodes selected");
        assertEquals(1, mw.getClickedEdges().size(), "Selectall not called. Not all edges selected");

        mw.addClickedComponent(null);
        //Delete all created nodes and edges
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void tabKeyPressed(){
        //setup
        mw.addClickedComponent(null);
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.createNode();
        mw.createNode();
        mw.createNode();
        assertEquals(0, mw.getClickedNodes().size());

        mw.handleSingleInput(KeyCode.TAB);
        assertEquals(1, mw.getClickedNodes().size());

        assertEquals(VisualGraphNode.getNodes().get(0), mw.getClickedNodes().get(0));

        mw.handleSingleInput(KeyCode.TAB);
        assertEquals(VisualGraphNode.getNodes().get(1), mw.getClickedNodes().get(0));

        mw.handleSingleInput(KeyCode.TAB);
        assertEquals(VisualGraphNode.getNodes().get(2), mw.getClickedNodes().get(0));

        mw.handleSingleInput(KeyCode.TAB);
        assertEquals(VisualGraphNode.getNodes().get(0), mw.getClickedNodes().get(0));

        //teardown
        mw.addClickedComponent(null);
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void cKeyPressed(){
        //clear copy buffer initially
        handleMovementInput(KeyCode.CONTROL);
        mw.addClickedComponent(null);
        mw.handleSingleInput(KeyCode.C);

        mw.createNode();
        mw.createNode();
        mw.createNode();
        mw.addClickedComponent(VisualGraphNode.getNodes().get(0));
        mw.addClickedComponent(VisualGraphNode.getNodes().get(1), true);
        mw.addClickedComponent(VisualGraphNode.getNodes().get(2), true);

        handleMovementInput(null);
        mw.handleSingleInput(KeyCode.C);
        assertEquals(0, mw.getCopiedNodes().size(), "No nodes should have been copied if ctrl was not pressed");

        handleMovementInput(KeyCode.CONTROL);
        mw.handleSingleInput(KeyCode.C);
        assertEquals(3, mw.getCopiedNodes().size(), "Nodes should have been copied");


        //teardown
        mw.addClickedComponent(null);
        handleMovementInput(null);
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void vKeyPressed(){
        //clear copy buffer initially
        handleMovementInput(KeyCode.CONTROL);
        mw.addClickedComponent(null);
        mw.handleSingleInput(KeyCode.C);

        mw.createNode();
        mw.createNode();
        mw.createNode();
        mw.addClickedComponent(VisualGraphNode.getNodes().get(0));
        mw.addClickedComponent(VisualGraphNode.getNodes().get(1), true);
        mw.addClickedComponent(VisualGraphNode.getNodes().get(2), true);
        handleMovementInput(KeyCode.CONTROL);
        mw.handleSingleInput(KeyCode.C);
        assertEquals(3, mw.getCopiedNodes().size(), "Nodes should have been copied before pasting");

        handleMovementInput(null);
        mw.handleSingleInput(KeyCode.V);
        assertEquals(3, VisualGraphNode.getNodes().size(), "No nodes should have been pasted");

        handleMovementInput(KeyCode.CONTROL);
        mw.handleSingleInput(KeyCode.V);
        assertEquals(6, VisualGraphNode.getNodes().size(), "Nodes should have been pasted");

        //teardown
        mw.addClickedComponent(null);
        handleMovementInput(null);
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void updateViewportTest(){
        //Manually create a node
        GraphNode node = new GraphNode(0);
        node.setName("New node");
        Vector spawnLocation = mw.getCamera().getLocation().add(mw.getCamera().getForwardVector().multiply(10));
        VisualGraphNode vgn = VisualGraphNode.create(spawnLocation, node);

        GridPane view = (GridPane) mw.getCenter();
        for(Node n : view.getChildren()){
            assertEquals(2, ((Viewport) n).getChildren().size(), "New node should not be in the viewports");
        }
        mw.updateViewport();
        sleep(1000);
        //The viewports should now contain an extra child, representing the created node
        for(Node n : view.getChildren()){
            assertEquals(3, ((Viewport) n).getChildren().size(), "New node should be added to the viewports");
        }

        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }

        mw.updateViewport();
        sleep(1000);
        //The viewports should have no extra children once the node is deleted
        for(Node n : view.getChildren()){
            assertEquals(2, ((Viewport) n).getChildren().size(), "New node should be added to the viewports");
        }
    }

    /*
    Cannot be unit tested, must be tested through integration with GraphVisualiser
    @Test
    public void updateAppDetailsTest(){

    }
    */

    @Test
    public void updateAlgorithmDetailsTest(){
        mw.setState(MainWindowState.EDIT);
        mw.updateAlgorithmDetails();
        assertTrue(TestHelper.containsInstanceOf(((VBox)((ScrollPane) mw.getRight()).getContent()).getChildren(), AlgorithmSetupPanel.class), "Should be setup panel when in edit state");
        mw.setState(MainWindowState.RUNNING);
        mw.updateAlgorithmDetails();
        assertTrue(TestHelper.containsInstanceOf(((VBox)((ScrollPane) mw.getRight()).getContent()).getChildren(), AlgorithmControlPanel.class), "Should be control panel when not in edit state");
        mw.setState(MainWindowState.EDIT);
        mw.updateAlgorithmDetails();
    }

    /*
    Cannot be tested
    @Test
    public void displayMessageTest(){

    }

    @Test
    public void displayWarningMessageTest(){

    }

    @Test
    public void displayErrorMessageTest(){

    }
    */

    @Test
    public void addClickedComponentTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.createNode();
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);
        mw.createEdge(nodeA, nodeB);
        VisualGraphEdge edge = VisualGraphEdge.getEdges().get(0);

        mw.setState(MainWindowState.RUNNING);
        mw.addClickedComponent(nodeA);
        assertEquals(0, mw.getClickedNodes().size(), "Should not add if not in edit mode");

        mw.setState(MainWindowState.EDIT);

        mw.addClickedComponent(nodeA);
        assertEquals(1, mw.getClickedNodes().size(), "Node should be added if clicked");

        mw.addClickedComponent(nodeB);
        assertEquals(1, mw.getClickedNodes().size(), "Node should be replaced if clicked without multiselect");

        handleMovementInput(KeyCode.CONTROL);

        mw.addClickedComponent(nodeA);
        assertEquals(2, mw.getClickedNodes().size(), "Node should be added if multi selecting");

        handleMovementInput(null);

        mw.addClickedComponent(edge);
        assertEquals(0, mw.getClickedNodes().size(), "Node list should be cleared without multiselect");
        assertEquals(1, mw.getClickedEdges().size(), "Edge list should include the edge");

        mw.addClickedComponent(nodeA, true);
        mw.addClickedComponent(nodeB, true);
        assertEquals(1, mw.getClickedEdges().size(), "Edge list must be unaltered with multiselect override");
        assertEquals(2, mw.getClickedNodes().size(), "Node list must contain 2 nodes with multiselect override");

        mw.addClickedComponent(nodeA, true);
        assertEquals(nodeA, mw.getClickedNodes().get(0), "Node list should contain the newest element");

        //teardown
        mw.addClickedComponent(null);
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void createNodeTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        GridPane view = (GridPane) mw.getCenter();
        while(view.getChildren().size() < 1){
            mw.createViewport();
        }
        sleep(500);
        mw.updateViewport();
        sleep(500);
        //viewport should contain no nodes
        assertEquals(2, ((Viewport) view.getChildren().get(0)).getChildren().size(), "Initial size should be 2");


        mw.setState(MainWindowState.RUNNING);
        mw.createNode();
        assertEquals(0, VisualGraphNode.getNodes().size(), "Should not create node when not in EDIT mode");

        mw.setState(MainWindowState.EDIT);

        mw.createNode();
        assertEquals(1, VisualGraphNode.getNodes().size(), "New node should be created");
        //viewport should now contain a new child node
        sleep(500);
        assertEquals(3, ((Viewport) view.getChildren().get(0)).getChildren().size(), "New node should be added to viewport");
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.updateViewport();
    }

    @Test
    public void createEdgeTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }

        assertEquals(0, VisualGraphEdge.getEdges().size(), "There should be no edges initially");
        mw.createNode();
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);

        mw.setState(MainWindowState.RUNNING);
        assertFalse(mw.createEdge(nodeA, nodeB));
        assertEquals(0, VisualGraphEdge.getEdges().size(), "No edge should be created when not in EDIT mode");

        mw.setState(MainWindowState.EDIT);

        assertTrue(mw.createEdge(nodeA, nodeB));

        assertEquals(1, VisualGraphEdge.getEdges().size(), "Edge should be created");
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.updateViewport();
    }

    @Test
    public void deleteNodeTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        assertEquals(0, VisualGraphNode.getNodes().size(), "There should be no nodes initially");

        mw.createNode();
        VisualGraphNode toDelete = VisualGraphNode.getNodes().get(0);

        mw.setState(MainWindowState.RUNNING);
        mw.deleteNode(toDelete);
        assertEquals(1, VisualGraphNode.getNodes().size(), "Node should not be deleted when not in EDIT mode");

        mw.setState(MainWindowState.EDIT);

        mw.deleteNode(toDelete);
        assertEquals(0, VisualGraphNode.getNodes().size(), "Node should be deleted in EDIT mode");
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.updateViewport();
    }

    @Test
    public void deleteEdgeTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }

        mw.createNode();
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);

        mw.createEdge(nodeA,nodeB);
        mw.createEdge(nodeB, nodeA);

        VisualGraphEdge edgeAB = VisualGraphEdge.getEdge(nodeA.getNode().getEdges().get(0));
        VisualGraphEdge edgeBA = VisualGraphEdge.getEdge(nodeB.getNode().getEdges().get(0));

        mw.setState(MainWindowState.RUNNING);
        assertFalse(mw.deleteEdge(edgeAB));
        assertEquals(2, VisualGraphEdge.getEdges().size(), "Should not delete when not in EDIT mode");
        assertFalse(mw.deleteEdge(edgeAB.getEdge()));
        assertEquals(2, VisualGraphEdge.getEdges().size(), "Should not delete model.edge when not in EDIT mode");

        mw.setState(MainWindowState.EDIT);
        assertTrue(mw.deleteEdge(edgeAB));
        assertEquals(1, VisualGraphEdge.getEdges().size(), "Edge should be deleted");

        assertTrue(mw.deleteEdge(edgeBA.getEdge()));
        assertEquals(0, VisualGraphEdge.getEdges().size(), "Edge should be deleted");
        //teardown
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.updateViewport();

    }

    @Test
    public void setStateTest(){
        mw.setState(MainWindowState.EDIT);
        assertEquals(MainWindowState.EDIT, mw.getState(), "Should set to EDIT mode");

        mw.setState(MainWindowState.RUNNING);
        assertEquals(MainWindowState.RUNNING, mw.getState(), "Should set to RUNNING mode");

        //teardown, but test again just for the sake of it
        mw.setState(MainWindowState.EDIT);
        assertEquals(MainWindowState.EDIT, mw.getState(), "Should set to EDIT mode");

    }

    @Test
    public void startAlgorithmsTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();
        assertEquals(null, mw.getStartNode(), "Start node should be null when no algorithm is set");
        //Viewports update in their own threads, give them time to update
        sleep(500);

        mw.createNode();

        mw.setState(MainWindowState.RUNNING);
        assertFalse(mw.startAlgorithms(), "Shouldn't start algorithms if already in RUNNING state");
        mw.setState(MainWindowState.EDIT);
        assertFalse(mw.startAlgorithms(), "Shouldn't start algorithms when algorithms not initialised");

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        //initialise the algorithm
        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));

        assertTrue(mw.startAlgorithms(), "Algorithms should be started if conditions met");
        assertEquals(MainWindowState.RUNNING, mw.getState(), "MainWindow should be in running state after starting");

        mw.terminateAlgorithms();
        mw.deleteNode(nodeA);
        mw.setState(MainWindowState.EDIT);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
    }

    @Test
    public void executeAlgorithmsTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();
        assertEquals(null, mw.getStartNode(), "Start node should be null when no algorithm is set");
        //Viewports update in their own threads, give them time to update
        sleep(500);

        mw.createNode();

        assertFalse(mw.startAlgorithms(), "Shouldn't start algorithms when algorithms not initialised");

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        //initialise the algorithm
        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));

        assertTrue(mw.startAlgorithms(), "Algorithms should be started if conditions met");
        assertEquals(MainWindowState.RUNNING, mw.getState(), "MainWindow should be in running state after starting");

        mw.terminateAlgorithms();
        mw.deleteNode(nodeA);
        mw.setState(MainWindowState.EDIT);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
    }

    /*
    Cannot be tested because this is essentially a cascaded method call
    @Test
    public void updateAlgorithmSpeedTest(){

    }
    */

    @Test
    public void terminateAlgorithmsTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();
        assertEquals(null, mw.getStartNode(), "Start node should be null when no algorithm is set");
        //Viewports update in their own threads, give them time to update
        sleep(500);

        mw.createNode();

        assertFalse(mw.startAlgorithms(), "Shouldn't start algorithms when algorithms not initialised");

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        //initialise the algorithm
        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));

        mw.startAlgorithms();
        assertEquals(MainWindowState.RUNNING, mw.getState(), "MUST be RUNNING for the test to be correct");
        mw.terminateAlgorithms();
        assertEquals(MainWindowState.EDIT, mw.getState(), "Terminate must return the state to EDIT");

        mw.deleteNode(nodeA);
        mw.setState(MainWindowState.EDIT);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }

    }

    /*
    Cannot be tested because it is a cascaded method call
    Also, this is heavily thread dependent and cannot be accurately tested
    @Test
    public void areAlgorithmsExecutingTest(){

    }
    */

    @Test
    public void algorithmPauseTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();
        //Viewports update in their own threads, give them time to update
        sleep(500);

        mw.createNode();

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        //initialise the algorithm
        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));

        assertFalse(mw.pauseAlgorithms(), "Should not be able to pause an algorithm when in EDIT state");
        mw.executeAlgorithms();
        assertTrue(mw.pauseAlgorithms(), "Should successfully toggle pause");
        assertTrue(mw.areAlgorithmsPaused(), "Algorithms should be paused");
        assertTrue(mw.pauseAlgorithms(), "Should successfully toggle pause");
        assertFalse(mw.areAlgorithmsPaused(), "Algorithms should be unpaused");

        mw.terminateAlgorithms();
        mw.deleteNode(nodeA);
        mw.setState(MainWindowState.EDIT);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }

    }

    @Test
    public void initialiseAlgorithmsTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();

        mw.createNode();

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        //initialise the algorithm
        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));

        mw.initialiseAlgorithms();
        assertTrue(((Viewport) view.getChildren().get(0)).getAlgorithm().getNodes().size() > 0, "The algorithm's node list should not be empty");

        mw.deleteNode(nodeA);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }

    }

    @Test
    public void stepAlgorithmsTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        mw.createViewport();

        mw.createNode();

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        //initialise the algorithm
        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));

        mw.startAlgorithms();
        assertEquals(MainWindowState.RUNNING, mw.getState(), "Must be RUNNING to make the test accurate");
        mw.stepAlgorithms();
        mw.stepAlgorithms();
        assertTrue(((Viewport) view.getChildren().get(0)).getAlgorithm().isFinished(), "Algorithm should be finished");
        assertEquals(MainWindowState.EDIT, mw.getState());

        mw.terminateAlgorithms();
        mw.deleteNode(nodeA);
        mw.setState(MainWindowState.EDIT);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }

    }

    @Test
    public void canRunAlgorithmsTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        assertFalse(mw.canRunAlgorithms(), "Should return false if there are no viewports");

        mw.createNode();

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        mw.createViewport();
        assertFalse(mw.canRunAlgorithms(), "Should return false if algorithm not set up");

        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));
        assertTrue(mw.canRunAlgorithms(), "Should return true if viewport's algorithm can run");

        mw.terminateAlgorithms();
        mw.deleteNode(nodeA);
        mw.setState(MainWindowState.EDIT);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }

    }

    @Test
    public void areAlgorithmsFinishedTest(){
        GridPane view = (GridPane) mw.getCenter();
        //initialise one viewport
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        assertTrue(mw.areAlgorithmsFinished(), "SHould return true if there are no viewpoorts");

        mw.createNode();

        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);

        mw.createViewport();
        ((Viewport) view.getChildren().get(0)).setAlgorithm(new ArrayBasedDijkstra(nodeA.getNode()));

        mw.startAlgorithms();
        assertFalse(mw.areAlgorithmsFinished(), "Should be false if algorithms are running");
        mw.stepAlgorithms();
        mw.stepAlgorithms();
        assertTrue(mw.areAlgorithmsFinished(), "Should be true when finished");

        mw.terminateAlgorithms();
        mw.deleteNode(nodeA);
        mw.setState(MainWindowState.EDIT);

        //teardown
        while(view.getChildren().size() > 0){
            mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
    }


    /*
    Cannot be tested, because step() and executed algorithms handle their own
    states independently, so manually testing this method would only be possible
    in a state whose change cannot be correctly detected
    @Test
    public void updateWindowStatusTest(){

    }
    */


    /*
    Cannot be tested
    unit test implementation means that MainWindow does not have a parent, which is
    required to open a save/load dialog
    @Test
    public void saveGraphTest(){

    }

    @Test
    public void loadGraphTest(){

    }
    */

    @Test
    public void loadAndSaveTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.createNode();
        mw.createNode();
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);
        VisualGraphNode nodeC = VisualGraphNode.getNodes().get(2);
        mw.createEdge(nodeA, nodeB);
        mw.createEdge(nodeA, nodeC);
        mw.addClickedComponent(nodeA);
        mw.addClickedComponent(nodeB, true);

        File saveLoc = new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/savedGraphs/testGraph.graph");
        assertFalse(mw.save(null), "Should abort save if invalid location passed to save()");
        assertTrue(mw.save(saveLoc), "Should successfully save to the location");

        assertFalse(mw.load(null));
        assertTrue(mw.load(saveLoc), "Should successfully load the graph");

        assertEquals(5, VisualGraphNode.getNodes().size(), "Should have loaded only 2 new nodes");
        assertEquals(3, VisualGraphEdge.getEdges().size(), "Should have loaded only 1 edge");

        //teardown
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }

    }

    @Test
    public void copyAndPasteTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        mw.createNode();
        mw.createNode();
        mw.createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);
        VisualGraphNode nodeC = VisualGraphNode.getNodes().get(2);
        mw.createEdge(nodeA, nodeB);
        mw.createEdge(nodeA, nodeC);
        mw.addClickedComponent(nodeA);
        mw.addClickedComponent(nodeB, true);
        mw.copySelected();
        assertTrue(mw.getCopiedNodes().size() > 0, "Copy buffer must be more than 0 for anything to have been copied");

        mw.pasteSelected();
        assertEquals(5, VisualGraphNode.getNodes().size(), "5 nodes should exist after pasting 2 nodes");
        assertEquals(3, VisualGraphEdge.getEdges().size(), "2 edges should exist after pasting. Invalid edge from A to C should not have been duplicated");

        assertEquals(nodeA.getLocation(), VisualGraphNode.getNodes().get(3).getLocation(), "Node A should have a duplicate");
        assertEquals(nodeB.getLocation(), VisualGraphNode.getNodes().get(4).getLocation(), "Node B should have a duplicate");


        //teardown
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void setThemeTest(){
        mw.setTheme(ThemeState.LIGHT);
        assertTrue(mw.getStylesheets().get(0).contains("/themes/light.css"), "style sheet not set for light.css");
        mw.setTheme(ThemeState.DARK);
        assertTrue(mw.getStylesheets().get(0).contains("/themes/dark.css"), "style sheet not set for dark.css");
    }

    @Test
    public void getThemeStyleSheetTest(){
        mw.setTheme(ThemeState.LIGHT);
        assertTrue(mw.getThemeStyleSheet().contains("/themes/light.css"), "Style sheets does not contain light.css");
        mw.setTheme(ThemeState.DARK);
        assertTrue(mw.getThemeStyleSheet().contains("/themes/dark.css"), "Style sheets does not contain the dark.css");
    }
}
