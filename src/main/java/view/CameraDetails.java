package viewport;

import maths.Vector;
import maths.Rotator;


import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class CameraDetails extends VBox{
	public CameraDetails(){
		
	}
	
	public void update(Camera camera){
		getChildren().clear();
		Label title = new Label("Camera details");
		getChildren().add(title);
		
		Label position = new Label("    Position: " + camera.getLocation().toStringNeat());
		getChildren().add(position);
		
		Label rotation = new Label("    Rotation: " + camera.getRotation().toStringNeat());
		getChildren().add(rotation);
	}
}