package menu;

import maths.Vector;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.application.Platform;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;

/**
*    The VGED panel displays useful information about a selected edge, and
*    allows the user to alter it's properties by calling an appropriate
*    interface method from MainWindow
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
**/
public class VisualGraphEdgeDetails extends DetailsPanel{
	VisualGraphEdge edge;

    /**
	*    @param edgeIn the edge to represent
	**/
	public VisualGraphEdgeDetails(VisualGraphEdge edgeIn){
		edge = edgeIn;
		update();
	}

    /**
	*    Set the edge this component represents
	*    @param edgeIn the edge to set
	**/
	public void setEdge(VisualGraphEdge edgeIn){
		edge = edgeIn;
		update();
	}

	public VisualGraphEdge getEdge(){
		return edge;
	}

	public void update(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
        		if(edge == null){
        			return;
        		}
        		getChildren().clear();
        		Tooltip tooltip =new Tooltip("The details panel allows you to view and edit the details of selected graph components");
        		Label title = new Label("DETAILS PANEL");
         		Tooltip.install(title, tooltip);
        		getChildren().add(title);

        		addEdgeSection();

        		addDeleteButton();

    	    	getChildren().add(new EmptyDetails());
			}
		});

	}

    /**
	*    Create and set up the edge section, which allows the user to edit the core
	*    attributes of the stored edge through text boxes
	**/
	private void addEdgeSection(){
		VBox edgeSection = new VBox();
		edgeSection.setAlignment(Pos.TOP_CENTER);
		Tooltip tooltip = new Tooltip("Details about the selected edge");


		Label title = new Label("Edge");
		Tooltip.install(title, tooltip);
		edgeSection.getChildren().add(title);


		Tooltip nameTooltip = new Tooltip("The name of the selected edge. An edge's name can contain a maximum of 15 characters");
		HBox nameSection = new HBox();
		nameSection.setAlignment(Pos.TOP_CENTER);
		Label nameTitle = new Label("  Name:");
		nameSection.getChildren().add(nameTitle);
		Tooltip.install(nameTitle, nameTooltip);
		Tooltip.install(nameSection, nameTooltip);
		TextField name = new TextField(edge.getEdge().getName());
		name.setId("name");
		Tooltip.install(name, nameTooltip);
		nameSection.getChildren().add(name);
		edgeSection.getChildren().add(nameSection);

		name.textProperty().addListener((observable, oldName, newName) -> {
			if(newName.length() < 15){
	    		edge.getEdge().setName(newName, true);
				//update();
		    	MainWindow.get().updateViewport();
			}
			else{
				name.setText(oldName);
			}
        });


		Tooltip lengthTooltip = new Tooltip("The weight of the selected edge. Edges cannot have a negative weight.");
		HBox lengthSection = new HBox();
		lengthSection.setAlignment(Pos.TOP_CENTER);
		Label lengthTitle = new Label("Weight:");
		lengthSection.getChildren().add(lengthTitle);
		Tooltip.install(lengthTitle, lengthTooltip);
		Tooltip.install(lengthSection, lengthTooltip);
		TextField length = new TextField("" + edge.getEdge().getLength());
		length.setId("length");
		Tooltip.install(length, lengthTooltip);
		lengthSection.getChildren().add(length);
		edgeSection.getChildren().add(lengthSection);

		length.textProperty().addListener((observable, oldLength, newLength) -> {
			int value = edge.getEdge().getLength();
			try{
				value = Integer.parseInt(newLength);
			}
			catch(NumberFormatException error){

			}
			if(value > 0 && value < 10000000){
			    edge.getEdge().setLength(value, true);
				//update();
			    MainWindow.get().updateViewport();
			}
			else{
				length.setText("" + edge.getEdge().getLength());
			}
		});



		VBox nodeA = new VBox(3);
		nodeA.setAlignment(Pos.TOP_CENTER);
		Tooltip nodeATooltip = new Tooltip("The origin node of the edge");
		Tooltip.install(nodeA, nodeATooltip);

		Label nodeALabel = new Label("Origin");
		Button nodeAButton = new Button(edge.getEdge().nodeA.getName());
		nodeAButton.setId("nodea");
		nodeAButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().addClickedComponent(VisualGraphNode.getNode(edge.getEdge().nodeA));
            }
        });
		Tooltip.install(nodeAButton, nodeATooltip);
		nodeA.getChildren().add(nodeALabel);
		nodeA.getChildren().add(nodeAButton);


		VBox nodeB = new VBox(3);
		nodeB.setAlignment(Pos.TOP_CENTER);
		Tooltip nodeBTooltip = new Tooltip("The end node of the edge");
		Tooltip.install(nodeB, nodeBTooltip);

		Label nodeBLabel = new Label("End");
		Button nodeBButton = new Button(edge.getEdge().nodeB.getName());
		nodeBButton.setId("nodeb");
		nodeBButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().addClickedComponent(VisualGraphNode.getNode(edge.getEdge().nodeB));
            }
        });
		Tooltip.install(nodeBButton, nodeBTooltip);
		nodeB.getChildren().add(nodeBLabel);
		nodeB.getChildren().add(nodeBButton);

		edgeSection.getChildren().add(nodeA);
		edgeSection.getChildren().add(nodeB);

		getChildren().add(edgeSection);

	}

    /**
	*    Create and set up the delete button for edges
	*    The delete button asks the mainWindow to delete the stored edge
	**/
	private void addDeleteButton(){
		Tooltip tooltip = new Tooltip("Delete the selected edge");
		Button delete = new Button("DELETE EDGE");
		delete.setId("delete");
		Tooltip.install(delete, tooltip);
		getChildren().add(delete);
		delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().deleteEdge(edge);
            }
        });
	}
}
