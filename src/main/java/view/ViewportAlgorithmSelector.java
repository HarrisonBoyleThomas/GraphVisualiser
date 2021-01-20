package viewport;

import model.GraphNode;
import model.algorithm.*;


import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewportAlgorithmSelector extends ComboBox{
	private static Class[] algorithmList = {DijkstraShortestPath.class};

	private Viewport viewport;

	public ViewportAlgorithmSelector(Viewport viewportIn){
		viewport = viewportIn;
		for(Class algorithm : algorithmList){
			getItems().add(algorithm.getSimpleName());
		}

		if(viewport.getAlgorithm() != null){
			getSelectionModel().select((new ArrayList<Class>(Arrays.asList(algorithmList))).indexOf(viewport.getAlgorithm().getClass()));
		}

		setOnAction((event) -> {
            Class selectedClass = algorithmList[getSelectionModel().getSelectedIndex()];
			try{
			    GraphAlgorithm instance = (GraphAlgorithm) selectedClass.getConstructor(GraphNode.class).newInstance((Object) null);
				viewport.setAlgorithm(instance);
			}
			catch(Exception e){
				System.out.println(e);
			}
        });
	}

}
