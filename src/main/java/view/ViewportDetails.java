package viewport;

import maths.Vector;
import maths.Rotator;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ViewportDetails extends VBox{
	CameraDetails cameraDetails;
	public ViewportDetails(){
		cameraDetails = new CameraDetails();
	}
	
	public void update(Viewport viewport){
		getChildren().clear();
		
		Label algorithm = new Label("Algorithm: " + viewport.getAlgorithm());
		getChildren().add(algorithm);
		
		cameraDetails.update(viewport.getCamera());
		getChildren().add(cameraDetails);
	}
}