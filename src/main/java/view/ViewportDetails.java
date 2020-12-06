package viewport;

import maths.Vector;
import maths.Rotator;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ViewportDetails extends VBox{
	CameraDetails cameraDetails;
	AlgorithmDetails algorithmDetails;
	public ViewportDetails(){
		cameraDetails = new CameraDetails();
		algorithmDetails = new AlgorithmDetails();
	}
	
	public void update(Viewport viewport){
		getChildren().clear();
		
		algorithmDetails.update(viewport.getAlgorithm());
		getChildren().add(algorithmDetails);
		
		cameraDetails.update(viewport.getCamera());
		getChildren().add(cameraDetails);
	}
}