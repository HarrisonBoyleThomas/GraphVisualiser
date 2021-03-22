package menu;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.geometry.Pos;

import javafx.application.Platform;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import model.GraphNode;
import model.GraphEdge;

import viewport.VisualGraphEdge;

/**
*    NodeDetails contains some key details about nodes, such as their
*    name and values
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
**/
public class NodeDetails extends DetailsPanel{
	GraphNode node;
	public NodeDetails(GraphNode nodeIn){
		node = nodeIn;
		update();
	}

	/**
	*    Set the node the component represents
	*    @param nodeIn the node to set to
	**/
	public void setNode(GraphNode nodeIn){
		node = nodeIn;
		update();
	}

	public GraphNode getNode(){
		return node;
	}

	public void update(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
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
	        	addEdgeButtons();
	        }
		});
	}

    /**
	*    Create and set up the name section
	*    The name section allows the user to edit the name of the stored node using
	*    a text box
	**/
	private void addNameSection(){
		HBox nameSection = new HBox();
		nameSection.setAlignment(Pos.TOP_CENTER);
		Tooltip tooltip = new Tooltip("The name of the currently selected node. You can change the name of the node by typing into the text box");
		Label title = new Label("Name:");
		Tooltip.install(title, tooltip);
		nameSection.getChildren().add(title);

		TextField name = new TextField(node.getName());
		name.setId("name");
		Tooltip.install(name, tooltip);
		nameSection.getChildren().add(name);
		getChildren().add(nameSection);

		name.textProperty().addListener((observable, oldName, newName) -> {
			node.setName(newName, true);
			MainWindow.get().updateViewport();
        });
	}

    /**
	*    Create and set up the value section of the stored node
	*    Allows the user to store edit the value associated to the node using a text box
	**/
	private void addValueSection(){
		HBox valueSection = new HBox();
		valueSection.setAlignment(Pos.TOP_CENTER);
		Tooltip tooltip = new Tooltip("The value of the currently selected node. You can change the value of the node by typing into the text box");
		Label title = new Label("Value:");
		Tooltip.install(title, tooltip);
		valueSection.getChildren().add(title);

		TextField value = new TextField("" + node.getValue());
		value.setId("value");
		Tooltip.install(value, tooltip);
		valueSection.getChildren().add(value);
		getChildren().add(valueSection);

		value.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.matches("-?\\d+")){
			    node.setValue(Integer.parseInt(newValue), true);
				MainWindow.get().updateViewport();
			}
			else{
				value.setText("" + oldValue);
			}
        });
	}

    /**
	*    Create a scorll section that lists all edges from the stored node
	*    The buttons are labelled by the name of the connected node, rather than
	*    edge name, because edge names can be ambiguous
	**/
	private void addEdgeButtons(){
		Tooltip tooltip = new Tooltip("Click to view the outgoing edge from " + node.getName() + " to the target node");
		ScrollPane pane = new ScrollPane();
		pane.setFitToWidth(true);
		VBox edges = new VBox(2);
		edges.setId("edges");
		edges.setAlignment(Pos.TOP_CENTER);

		Label title = new Label("OUTGOING EDGES");
		Tooltip.install(title, new Tooltip("Below are a list of nodes that are connected to the selected node"));
		edges.getChildren().add(title);


        for(GraphEdge edge : node.getEdges()){
            Button button = new Button(edge.nodeB.getName());
			button.setId(edge.nodeB.getName());
            button.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	                MainWindow.get().addClickedComponent(VisualGraphEdge.getEdge(edge));
	            }
	        });
			Tooltip.install(button, tooltip);
			edges.getChildren().add(button);
		}
		pane.setContent(edges);
		pane.setMaxHeight(200);
		pane.setMinHeight(200);

		getChildren().add(pane);

	}
}
