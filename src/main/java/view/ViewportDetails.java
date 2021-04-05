package viewport;

import maths.Vector;
import maths.Rotator;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.application.Platform;
/**
*    The viewportDetails displays details about the viewport
*    Currently, this just includes it's algorithm details
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public class ViewportDetails extends VBox{
	AlgorithmDetails algorithmDetails;
	public ViewportDetails(){
		algorithmDetails = new AlgorithmDetails();
	}

	public void update(Viewport viewport){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
        		getChildren().clear();

        		algorithmDetails.update(viewport);
        		if(!getChildren().contains(algorithmDetails)){
        		    getChildren().add(algorithmDetails);
        		}
			}
		});
	}

	public AlgorithmDetails getAlgorithmDetails(){
		return algorithmDetails;
	}
}
