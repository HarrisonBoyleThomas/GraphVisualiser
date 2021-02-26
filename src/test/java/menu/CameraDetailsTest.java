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
import javafx.scene.control.Label;

import maths.Vector;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CameraDetailsTest extends ApplicationTest{
    CameraDetails details;
    Camera camera;
    @Override
    public void start(Stage stage){
        camera = new Camera();
        details = new CameraDetails(camera);
        Scene scene = new Scene(details, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void initialiseTest(){
        Label position = (Label) TestHelper.findNodeById(details, "position");
        assertNotNull(position, "Must contain a label with id position");
        assertTrue(position.getText().contains(camera.getLocation().toStringNeat()), "Must contain position as a string");

        Label rotation = (Label) TestHelper.findNodeById(details, "rotation");
        assertNotNull(rotation, "Must contain a label with id rotation");
        assertTrue(rotation.getText().contains(camera.getRotation().toStringNeat()), "Must contain rotation as a string");
    }

    @Test
    public void movementUpdateTest(){
        camera.moveUpwards(1.0);
        details.update();
        sleep(1000);
        Label position = (Label) TestHelper.findNodeById(details, "position");
        assertNotNull(position, "Must contain a label with id position");
        assertTrue(position.getText().contains(camera.getLocation().toStringNeat()), "Must contain the new position as a string");
    }

    @Test
    public void rotationUpdateTest(){
        camera.pitch(1.0);
        details.update();
        sleep(1000);
        Label rotation = (Label) TestHelper.findNodeById(details, "rotation");
        assertNotNull(rotation, "Must contain a label with id rotation");
        assertTrue(rotation.getText().contains(camera.getRotation().toStringNeat()), "Must contain the new rotation as a string");
    }
}
