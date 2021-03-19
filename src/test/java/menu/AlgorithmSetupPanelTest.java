package menu;
import menu.*;
import viewport.*;
import model.*;
import model.algorithm.*;

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
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;

import maths.Vector;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class AlgorithmSetupPanelTest extends ApplicationTest{
    private AlgorithmSetupPanel details;
    private Scene scene;
    @Override
    public void start(Stage stage){
        details = new AlgorithmSetupPanel();
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @Order(1)
    public void initialiseTest(){
        GridPane view = (GridPane) MainWindow.get().getCenter();
        //ensure no viewports exist initially
        while(view.getChildren().size() > 0){
            MainWindow.get().deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        MainWindow.get().createViewport();
        Viewport v = (Viewport) view.getChildren().get(0);
        v.setAlgorithm(new KruskalsAlgorithm(null));
        sleep(1000);
        details.update();
        sleep(1000);
        ComboBox selection = (ComboBox) TestHelper.findNodeById(details, "selection");
        assertNull(selection, "Should not contain a combo box with id selection if the algorithm doesn't need it");

        v.setAlgorithm(new HeapBasedDijkstra(null));
        details.update();
        sleep(1000);
        selection = (ComboBox) TestHelper.findNodeById(details, "selection");
        assertNotNull(selection, "Should contain a combo box with id selection");

        Button runButton = (Button) TestHelper.findNodeById(details, "run");
        assertNotNull(runButton, "There should be a button with id run");
    }

    @Test
    @Order(2)
    public void setStartNodeTest(){

        MainWindow.get().createNode();
        GraphNode node = VisualGraphNode.getNodes().get(0).getNode();
        //node.setName("test name");
        VisualGraphNode.create(new Vector(), node);

        details.update();
        sleep(1000);

        ComboBox selection = (ComboBox) TestHelper.findNodeById(details, "selection");
        assertNotNull(selection, "Selection box not present when it should be");


        //selection.setValue(node.getName());
        System.out.println("index fround: " + selection.getItems().indexOf(node.getName()));
        TestHelper.comboBoxSelect(selection, selection.getItems().indexOf(node.getName()));
        sleep(1000);

        GridPane view = (GridPane) MainWindow.get().getCenter();
        Viewport v = (Viewport) view.getChildren().get(0);
        HeapBasedDijkstra algorithm = (HeapBasedDijkstra) v.getAlgorithm();
        assertEquals(node, algorithm.getStartNode(), "Start node should be set after selecting it");
    }

    @Test
    @Order(3)
    public void runButtonTest(){
        details.update();
        sleep(1000);
        MainWindow.get().setStartNode(VisualGraphNode.getNodes().get(0).getNode());
        Button runButton = (Button) TestHelper.findNodeById(details, "run");
        assertNotNull(runButton, "Run button should exist");
        assertEquals(MainWindowState.EDIT, MainWindow.get().getState(), "Main window should be in EDIT state initially");
        System.out.println(MainWindow.get().canRunAlgorithms());
        TestHelper.buttonClick(runButton);
        sleep(1000);
        assertEquals(MainWindowState.RUNNING, MainWindow.get().getState(), "Main window should be in RUNNING state to signal algorithms are running");

        MainWindow.get().terminateAlgorithms();
        details.update();
        assertEquals(MainWindowState.EDIT, MainWindow.get().getState(), "Ensure algorithms correctly terminated to improve test accuracy");
        GridPane view = (GridPane) MainWindow.get().getCenter();
        Viewport v = (Viewport) view.getChildren().get(0);
        v.setAlgorithm(null);
        TestHelper.buttonClick(runButton);
        assertEquals(MainWindowState.EDIT, MainWindow.get().getState(), "Should not run if no algorithm selected");
    }

    @AfterAll
    public void tearDown(){
        MainWindow.get().terminateAlgorithms();
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
        GridPane view = (GridPane) MainWindow.get().getCenter();
        //ensure no viewports exist initially
        while(view.getChildren().size() > 0){
            MainWindow.get().deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
    }


}
