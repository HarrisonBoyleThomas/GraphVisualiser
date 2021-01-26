package viewport;

import model.algorithm.*;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.HashMap;

public class AlgorithmDetails extends VBox{

	private static HashMap<Class, Class> algorithmDetailsMap = new HashMap<>();
	static {
		algorithmDetailsMap.put(DijkstraShortestPath.class, DijkstraDetails.class);
		algorithmDetailsMap.put(ArrayBasedDijkstra.class, DijkstraDetails.class);
		algorithmDetailsMap.put(HeapBasedDijkstra.class, DijkstraDetails.class);
	}

	private AlgorithmDetailsWindow detailsInstance;

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
            if(detailsInstance != null){
				detailsInstance.update(viewport.getAlgorithm());
			}
			else{
                Button inspect = new Button("Inspect");
	            inspect.setOnAction(new EventHandler<ActionEvent>() {
	                @Override public void handle(ActionEvent e) {
				    	try{
    					    Class windowClass = algorithmDetailsMap.get(viewport.getAlgorithm().getClass());
	                        Stage popup = new Stage();
    	    			    popup.initOwner(AlgorithmDetails.this.getScene().getWindow());
		    			    detailsInstance = (AlgorithmDetailsWindow) windowClass.getConstructor(GraphAlgorithm.class).newInstance((GraphAlgorithm) viewport.getAlgorithm());
		    			    Scene scene = new Scene(detailsInstance, 600, 150);
		    			    popup.setScene(scene);
							popup.setOnCloseRequest(closeEvent -> {
                               detailsInstance = null;
							   update(viewport);
                            });
                            popup.show();
							update(viewport);
		    			}
		    			catch(Exception error){
		    				System.out.println("Unable to load algorithm's details. This is caused by the algorithm not being defined in the algorithmDetailsMap in view/AlgorithmDetails.java");
                            error.printStackTrace();
		    			}
	                }
	            });
			    getChildren().add(inspect);
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
