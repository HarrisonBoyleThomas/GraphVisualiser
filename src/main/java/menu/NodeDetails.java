package menu;

import javafx.scene.control.Label;

import model.GraphNode;
import model.GraphEdge;

public class NodeDetails extends DetailsPanel{
	GraphNode node;
	public NodeDetails(GraphNode nodeIn){
		node = nodeIn;
		update();
	}
	
	public void setNode(GraphNode nodeIn){
		node = nodeIn;
	}
	
	public void update(){
		if(node == null){
			return;
		}
		getChildren().clear();
		Label title = new Label("NODE: " + node.getName());
		getChildren().add(title);
	}
}