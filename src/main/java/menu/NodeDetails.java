package menu;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

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
		Tooltip tooltip = new Tooltip("The details panel now contains the details of the currently selected node");
		Label title = new Label("NODE DETAILS");
		Tooltip.install(title, tooltip);
		getChildren().add(title);
		
		addNameSection();
		addValueSection();
	}
	
	private void addNameSection(){
		HBox nameSection = new HBox();
		Tooltip tooltip = new Tooltip("The name of the currently selected node. You can change the name of the node by typing into the text box");
		Label title = new Label("Name:");
		Tooltip.install(title, tooltip);
		nameSection.getChildren().add(title);
		
		TextField name = new TextField(node.getName());
		Tooltip.install(name, tooltip);
		nameSection.getChildren().add(name);
		getChildren().add(nameSection);
		
		name.textProperty().addListener((observable, oldName, newName) -> {
			node.setName(newName);
			MainWindow.get().updateViewport();
        });
	}
	
	private void addValueSection(){
		HBox valueSection = new HBox();
		Tooltip tooltip = new Tooltip("The value of the currently selected node. You can change the value of the node by typing into the text box");
		Label title = new Label("Value:");
		Tooltip.install(title, tooltip);
		valueSection.getChildren().add(title);
		
		TextField value = new TextField("" + node.getValue());
		Tooltip.install(value, tooltip);
		valueSection.getChildren().add(value);
		getChildren().add(valueSection);
		
		value.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.matches("-?\\d+")){
			    node.setValue(Integer.parseInt(newValue));
				MainWindow.get().updateViewport();
			}
			else{
				value.setText("" + oldValue);
			}
        });
	}
}