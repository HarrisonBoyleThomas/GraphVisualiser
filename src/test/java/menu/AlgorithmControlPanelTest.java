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
import javafx.scene.control.Slider;

import maths.Vector;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlgorithmControlPanelTest extends ApplicationTest{
    AlgorithmControlPanel details;
    private Scene scene;
    @Override
    public void start(Stage stage){
        details = new AlgorithmControlPanel();
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp(){
        MainWindow.get().createNode();
        MainWindow.get().createNode();
        VisualGraphNode nodeA = VisualGraphNode.getNodes().get(0);
        VisualGraphNode nodeB = VisualGraphNode.getNodes().get(1);
        MainWindow.get().createEdge(nodeA, nodeB);

        GridPane view = (GridPane) MainWindow.get().getCenter();
        //ensure no viewports exist initially
        while(view.getChildren().size() > 0){
            MainWindow.get().deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
        }
        MainWindow.get().createViewport();
        Viewport v = (Viewport) view.getChildren().get(0);
        v.setAlgorithm(new HeapBasedDijkstra(null));

        MainWindow.get().setStartNode(nodeA.getNode());
        MainWindow.get().startAlgorithms();
    }

    @AfterEach
    public void teardown(){
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

    @Test
    public void initialiseTest(){
        details.update();
        Button stepButton = (Button) TestHelper.findNodeById(details, "step");
        assertNotNull(stepButton, "Should contain a button with id step");

        Button executeButton = (Button) TestHelper.findNodeById(details, "execute");
        assertNotNull(executeButton, "Should contain a button with id execute");

        Button terminateButton = (Button) TestHelper.findNodeById(details, "terminate");
        assertNotNull(terminateButton, "Should contain a button with id terminate");

        Slider speedDial = (Slider) TestHelper.findNodeById(details, "speed");
        assertNotNull(speedDial, "Should contain a slider with id speed");
    }

    @Test
    public void stepTest(){
        details.update();
        Button stepButton = (Button) TestHelper.findNodeById(details, "step");
        assertNotNull(stepButton, "Should contain a button with id step");

        GridPane view = (GridPane) MainWindow.get().getCenter();
        Viewport v = (Viewport) view.getChildren().get(0);
        int currentStep = v.getAlgorithm().getIterationStep();

        TestHelper.buttonClick(stepButton);
        sleep(1000);
        int newStep = v.getAlgorithm().getIterationStep();
        assertEquals(newStep, currentStep + 1, "Algorithm should be advanced one cycle after clicking on the step button");
    }

    @Test
    public void executeTest(){
        details.update();
        Button executeButton = (Button) TestHelper.findNodeById(details, "execute");
        assertNotNull(executeButton, "Should contain a button with id execute");

        GridPane view = (GridPane) MainWindow.get().getCenter();
        Viewport v = (Viewport) view.getChildren().get(0);

        int initialStep = v.getAlgorithm().getIterationStep();

        TestHelper.buttonClick(executeButton);

        sleep(2000);

        int newStep = v.getAlgorithm().getIterationStep();

        assertTrue(newStep > initialStep, "Algorithm should have advanced some steps after clicking on the execute button");
    }
}
