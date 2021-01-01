package menu;

import javafx.scene.control.Label;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;

public class VisualGraphNodeDetails extends DetailsPanel{
	VisualGraphNode node;
	public VisualGraphNodeDetails(VisualGraphNode nodeIn){
		node = nodeIn;
		update();
	}
	
	public void setNode(VisualGraphNode nodeIn){
		node = nodeIn;
	}
	
	public void update(){
		if(node == null){
			return;
		}
		getChildren().clear();
		Label title = new Label("DETAILS PANEL");
		getChildren().add(title);
		
		getChildren().add(new NodeDetails(node.getNode()));
	}
}