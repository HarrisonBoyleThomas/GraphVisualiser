package viewport;

import model.algorithm.GraphAlgorithm;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class AlgorithmDetails extends VBox{
	public AlgorithmDetails(){
		
	}
	
	public void update(GraphAlgorithm algorithm){
		getChildren().clear();
		
		Label title = new Label("Algorithm: " + algorithm);
		getChildren().add(title);
		
		String[] details = algorithm.getDetails();
		
		for(String s : details){
			Label l = new Label("    " + s);
			getChildren().add(l);
		}
	}
}