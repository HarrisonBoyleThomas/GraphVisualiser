package menu;

import maths.Vector;
import viewport.Camera;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;

import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
        		if(camera == null){
        			return;
        		}
        		getChildren().clear();
        		Tooltip tooltip = new Tooltip("The details of the camera");
        		Label title = new Label("CAMERA DETAILS");
        		Tooltip.install(title, tooltip);
        		getChildren().add(title);

        		Label position = new Label("    Position: " + camera.getLocation().toStringNeat());
                position.setId("position");
        		getChildren().add(position);

        		Label rotation = new Label("    Rotation: " + camera.getRotation().toStringNeat());
                rotation.setId("rotation");
        		getChildren().add(rotation);

                //New rendering method does not use this feature
    	    	//addViewDistanceDial();
			}
		});
	}

    /**
	*    Add a dial that allows the user to edit the view distance of the camera
	**/
	private void addViewDistanceDial(){
		VBox section = new VBox();
		Tooltip tooltip = new Tooltip("Set maximum view distance of the camera(metres)");
		Label title = new Label("Max draw distance");
		Tooltip.install(title, tooltip);
		section.getChildren().add(title);

		Slider slider = new Slider(10, 200, camera.getMaxDrawDistance());
		slider.setFocusTraversable(false);
		Tooltip.install(slider, tooltip);

		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> value, Number oldValue, Number newValue) {
				camera.setMaxDrawDistance((double) newValue);
            }
        });
		slider.setMinorTickCount(2);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
        section.getChildren().add(slider);
		getChildren().add(section);
	}
}
