package viewport;

import model.GraphNode;
import model.algorithm.*;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.Arrays;

/**
*    The ViewportAlgorithmSelector is a drop down selection box
*    which assings a viewport's algorith based on it's selection
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public class ViewportAlgorithmSelector extends ComboBox{
	//A list of algorithms to choose from. If you wish to add more algorithms,
	//It MUST be added here to be choosabkle within the app
	private static Class[] algorithmList = {ArrayBasedDijkstra.class,
	                                        HeapBasedDijkstra.class,
										    BreadthFirstSearch.class,
										    DepthFirstSearch.class};

	private Viewport viewport;

	public ViewportAlgorithmSelector(Viewport viewportIn){
		Tooltip tooltip = new Tooltip("Select an algorithm to run in this viewport");
		Tooltip.install(this, tooltip);
		viewport = viewportIn;
		for(Class algorithm : algorithmList){
			getItems().add(algorithm.getSimpleName());
		}

		if(viewport.getAlgorithm() != null){
			getSelectionModel().select((new ArrayList<Class>(Arrays.asList(algorithmList))).indexOf(viewport.getAlgorithm().getClass()));
		}

		setOnAction((event) -> {
			if(viewport.getAlgorithm() == null || (viewport.getAlgorithm() != null && !viewport.getAlgorithm().isRunning())){
				//get the algorithm class chosen by the user
                Class selectedClass = algorithmList[getSelectionModel().getSelectedIndex()];
    			try{
					//Crate an instance of the chosen algorithm
    			    GraphAlgorithm instance = (GraphAlgorithm) selectedClass.getConstructor(GraphNode.class).newInstance((Object) null);
					System.out.println("try set algorithm");
					//Add the algorithm to the viewport
    				viewport.setAlgorithm(instance);
    			}
    			catch(Exception e){
    				System.out.println(e);
    			}
			}
		});
	}

}
