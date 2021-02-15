package menu;
import menu.*;
import viewport.*;
import model.*;
import model.algorithm.*;

import maths.Vector;
import maths.Rotator;

import helpers.TestHelper;

import org.loadui.testfx.GuiTest;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

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
        sleep(1000);
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

    }

    @Test
    public void createNodeTest(){

    }

    @Test
    public void createEdgeTest(){

    }

    @Test
    public void deleteNodeTest(){

    }

    @Test
    public void deleteEdgeTest(){

    }

    @Test
    public void setStateTest(){

    }

    @Test
    public void startAlgorithmsTest(){

    }

    @Test
    public void executeAlgorithmsTest(){

    }

    @Test
    public void updateAlgorithmSpeedTest(){

    }

    @Test
    public void terminateAlgorithmsTest(){

    }

    @Test
    public void areAlgorithmsExecutingTest(){

    }

    @Test
    public void areAlgorithmsPausedTest(){

    }

    @Test
    public void pauseAlgorithmsTest(){

    }

    @Test
    public void initialiseAlgorithmsTest(){

    }

    @Test
    public void stepAlgorithmsTest(){

    }

    @Test
    public void canRunAlgorithmsTest(){

    }

    @Test
    public void areAlgorithmsFinishedTest(){

    }

    @Test
    public void updateWindowStatusTest(){

    }

    @Test
    public void saveGraphTest(){

    }

    @Test
    public void loadGraphTest(){

    }

    @Test
    public void copySelectedTest(){

    }

    @Test
    public void pasteSelectedTest(){

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
