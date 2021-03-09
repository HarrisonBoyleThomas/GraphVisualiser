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
/**
*    AlgorithmDetails displays information about the alhorithm in a Viewport.
*    A static map of algorithms to AlgorithmWindows is used to add
*    varied data about a particular algorithm instance
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public class AlgorithmDetails extends VBox{
    //A map from algorithm to detailsWindow for this particular algorithm. I intended to
	//keep algorithms independent of it's corresponding details component
	//To correctly display details, an algorithm MUST be added to this map
	private static HashMap<Class, Class> algorithmDetailsMap = new HashMap<>();
	static {
		algorithmDetailsMap.put(DijkstraShortestPath.class, DijkstraDetails.class);
		algorithmDetailsMap.put(ArrayBasedDijkstra.class, DijkstraDetails.class);
		algorithmDetailsMap.put(HeapBasedDijkstra.class, DijkstraDetails.class);
		algorithmDetailsMap.put(BreadthFirstSearch.class, SearchAlgorithmDetails.class);
		algorithmDetailsMap.put(DepthFirstSearch.class, SearchAlgorithmDetails.class);
		algorithmDetailsMap.put(BellmanFord.class, ShortestPathAlgorithmDetails.class);
		algorithmDetailsMap.put(KruskalsAlgorithm.class, KruskalDetails.class);
		algorithmDetailsMap.put(PrimsAlgorithm.class, PrimDetails.class);
	}
    //Popup window that displays information about a particular algorithm
	private AlgorithmDetailsWindow detailsInstance;

	public AlgorithmDetails(){

	}
    /**
	*    Refresh the details component
	*    @param viewport to extract an algorithm from/represent
	**/
	public void update(Viewport viewport){
		GraphAlgorithm algorithm = viewport.getAlgorithm();
		getChildren().clear();
		if(viewport.getAlgorithm() == null || (viewport.getAlgorithm() != null && !viewport.getAlgorithm().isRunning())){
			//Add a selection box to choose an algorithm for the viewport
    		ViewportAlgorithmSelector selection = new ViewportAlgorithmSelector(viewport);
	    	getChildren().add(selection);
		}

		if(viewport.getAlgorithm() != null && (viewport.getAlgorithm().isRunning() || viewport.getAlgorithm().isFinished())){
			//Display this if the algorithm is runnin/has run
		    Label title = new Label("Algorithm: " + algorithm);
		    getChildren().add(title);
            if(detailsInstance != null){
				detailsInstance.update(viewport.getAlgorithm());
			}
			else{
				//The inspect button opens up a popup window that displays pseudocode/state variables for the algorithm
                Button inspect = new Button("Inspect");
	            inspect.setOnAction(new EventHandler<ActionEvent>() {
	                @Override public void handle(ActionEvent e) {
				    	try{
    					    Class windowClass = algorithmDetailsMap.get(viewport.getAlgorithm().getClass());
	                        Stage popup = new Stage();
							popup.setTitle(viewport.getAlgorithm().toString() + " details");
    	    			    popup.initOwner(AlgorithmDetails.this.getScene().getWindow());
		    			    detailsInstance = (AlgorithmDetailsWindow) windowClass.getConstructor(GraphAlgorithm.class).newInstance((GraphAlgorithm) viewport.getAlgorithm());
		    			    Scene scene = new Scene(detailsInstance, 600, 400);
		    			    popup.setScene(scene);
							popup.setOnCloseRequest(closeEvent -> {
                               detailsInstance = null;
							   update(viewport);
                            });
                            popup.show();
							update(viewport);
		    			}
		    			catch(Exception error){
		    				System.out.println("Unable to load algorithm's details. This is caused by the algorithm not being defined in the algorithmDetailsMap in view/AlgorithmDetails.java, or if there is an error in the panel for this algorithm");
                            error.printStackTrace();
		    			}
	                }
	            });
			    getChildren().add(inspect);
			}
			Label iterations = new Label("Iteration number: "+ viewport.getAlgorithm().getIterationStep());
			getChildren().add(iterations);
		}
		else{
			//Display this if the algorithm isn't/has not run
			Label details = new Label("State: Not running");
			Tooltip tooltip = new Tooltip("Not running! Start the algorithm using the algorithm control panel!");
			Tooltip.install(details, tooltip);
			getChildren().add(details);
		}
	}
}
