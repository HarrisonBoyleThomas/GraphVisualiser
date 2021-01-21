package viewport;

import model.algorithm.GraphAlgorithm;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class AlgorithmDetails extends VBox{

	public AlgorithmDetails(){

	}

	public void update(Viewport viewport){
		GraphAlgorithm algorithm = viewport.getAlgorithm();
		getChildren().clear();
		if(viewport.getAlgorithm() == null || (viewport.getAlgorithm() != null && !viewport.getAlgorithm().isRunning())){
    		ViewportAlgorithmSelector selection = new ViewportAlgorithmSelector(viewport);
	    	getChildren().add(selection);
		}

		if(viewport.getAlgorithm() != null && (viewport.getAlgorithm().isRunning() || viewport.getAlgorithm().isFinished())){
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
		else{
			Label details = new Label("State: Not running");
			Tooltip tooltip = new Tooltip("Not running! Start the algorithm using the algorithm control panel!");
			Tooltip.install(details, tooltip);
			getChildren().add(details);
		}
	}
}
