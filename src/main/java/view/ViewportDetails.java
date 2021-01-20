package viewport;

import maths.Vector;
import maths.Rotator;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ViewportDetails extends VBox{
	AlgorithmDetails algorithmDetails;
	public ViewportDetails(){
		algorithmDetails = new AlgorithmDetails();
	}
	
	public void update(Viewport viewport){
		getChildren().clear();
		
		algorithmDetails.update(viewport);
		getChildren().add(algorithmDetails);
	}
}