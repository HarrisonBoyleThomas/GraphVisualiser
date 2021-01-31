package menu;

import maths.Vector;
import viewport.Camera;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;

/**
*    The CameraDetails simply writes properties of the camera, such as location and
*    rotation on screen for the user to see
*    @Author Harrison Boyle-Thomas
*    @Date 31/01/21
**/
public class CameraDetails extends DetailsPanel{
	Camera camera;
	public CameraDetails(Camera cameraIn){
		camera = cameraIn;
		update();
	}

	public void update(){
		if(camera == null){
			return;
		}
		getChildren().clear();
		Tooltip tooltip = new Tooltip("The details of the camera");
		Label title = new Label("CAMERA DETAILS");
		Tooltip.install(title, tooltip);
		getChildren().add(title);

		Label position = new Label("    Position: " + camera.getLocation().toStringNeat());
		getChildren().add(position);

		Label rotation = new Label("    Rotation: " + camera.getRotation().toStringNeat());
		getChildren().add(rotation);
	}
}
