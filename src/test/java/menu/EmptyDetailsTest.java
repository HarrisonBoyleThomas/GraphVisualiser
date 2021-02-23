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

public class EmptyDetailsTest extends ApplicationTest{

    private EmptyDetails details;
    private Scene scene;

    @Override
    public void start(Stage stage){
        details = new EmptyDetails();
        scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void initialiseTest(){
        Button button = (Button) TestHelper.getNodeById(details.getChildren(), "createButton");
        assertNotNull(button, "There should be a button for creating nodes");
    }

    @Test
    public void createNodeButtonTest(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }

        Button button = (Button) TestHelper.getNodeById(details.getChildren(), "createButton");
        TestHelper.buttonClick(button);

        assertEquals(1, VisualGraphNode.getNodes().size(), "There should now be a new node once the create button is clicked");
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }
}
