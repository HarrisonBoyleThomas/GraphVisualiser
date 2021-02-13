import menu.*;
import viewport.*;
import model.*;
import model.algorithm.*;

import org.loadui.testfx.GuiTest;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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

@ExtendWith(ApplicationExtension.class)
public class MainWindowTest extends ApplicationTest{
    GraphVisualiser app;
    MainWindow mw;
    Stage stage;

    @Override
    public PointQuery point(Node node) {
        System.out.println(node);
        Point2D topLeftPoint = node.localToScreen(0, 0);
        System.out.println(topLeftPoint);
        Point2D pos = new Point2D(topLeftPoint.getX(), topLeftPoint.getY());

        return super.point(node).atOffset(pos);
    }

    @Override
    public void start(final Stage stage){
        app = new GraphVisualiser();
        mw = MainWindow.get();
        this.stage = stage;
        stage.setScene(mw.getScene());
        stage.show();

    }

    @Test
    public void getTest(){
        MainWindow mw = MainWindow.get();
        assertNotNull(mw, "mainwindow.get() returned null");
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
        try{
            GridPane view = (GridPane) mw.getCenter();
            //initialise two viewports
            while(view.getChildren().size() > 1){
                mw.deleteViewport((Viewport) view.getChildren().get(view.getChildren().size()-1));
            }
            mw.createViewport();
            sleep(1000);
            for(Node n : view.getChildren()){
                Viewport v = (Viewport) n;
                ViewportAlgorithmSelector selector = (ViewportAlgorithmSelector) v.getViewportDetails().getAlgorithmDetails().getChildren().get(0);
                System.out.println("is vis: " + selector.isVisible());
                try{
                    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return selector.isVisible();
                        }
                    });
                }
                catch(Exception timeout){

                }
                clickOn(selector, MouseButton.PRIMARY);
                Thread.sleep(1000);
                clickOn(ArrayBasedDijkstra.class.getSimpleName(), MouseButton.PRIMARY);
                Thread.sleep(1000);
            }
        }
        catch(InterruptedException e){

        }
    }

    @Test
    public void getStartNodeTest(){

    }

    @Test
    public void getClickedNodesTest(){

    }

    @Test
    public void getClickedEdgesTest(){

    }

    @Test
    public void getCameraTest(){

    }

    @Test
    public void handleMovementInputTest(){

    }

    @Test
    public void handleSingleInputTest(){

    }

    @Test
    public void updateViewportTest(){

    }

    @Test
    public void updateAppDetailsTest(){

    }

    @Test
    public void updateAlgorithmDetailsTest(){

    }

    @Test
    public void displayMessageTest(){

    }

    @Test
    public void displayWarningMessageTest(){

    }

    @Test
    public void displayErrorMessageTest(){

    }

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
