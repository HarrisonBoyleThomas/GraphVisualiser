package viewport;

import model.algorithm.GraphAlgorithm;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class AlgorithmDetails extends VBox{
	
	public AlgorithmDetails(){
		
	}
	
	public void update(Viewport viewport){
		GraphAlgorithm algorithm = viewport.getAlgorithm();
		getChildren().clear();
		ViewportAlgorithmSelector selection = new ViewportAlgorithmSelector(viewport);
		getChildren().add(selection);
		Label title = new Label("Algorithm: " + algorithm);
		getChildren().add(title);
		
		String[] details = {};
		if(algorithm != null){
			details = algorithm.getDetails();
		}
		
		for(String s : details){
			Label l = new Label("    " + s);
			getChildren().add(l);
		}
	}
}