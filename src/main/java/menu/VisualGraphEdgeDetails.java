package menu;

import maths.Vector;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

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

	public VisualGraphEdgeDetails(VisualGraphEdge edgeIn){
		edge = edgeIn;
		update();
	}

	public void setEdge(VisualGraphEdge edgeIn){
		edge = edgeIn;
		update();
	}

	public void update(){
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

	public void addEdgeSection(){
		VBox edgeSection = new VBox();
		Tooltip tooltip = new Tooltip("Details about the selected edge");


		Label title = new Label("Edge");
		Tooltip.install(title, tooltip);
		edgeSection.getChildren().add(title);


		Tooltip nameTooltip = new Tooltip("The name of the selected edge. An edge's name can contain a maximum of 15 characters");
		HBox nameSection = new HBox();
		Label nameTitle = new Label("Name:");
		nameSection.getChildren().add(nameTitle);
		Tooltip.install(nameTitle, nameTooltip);
		Tooltip.install(nameSection, nameTooltip);
		TextField name = new TextField(edge.getEdge().getName());
		Tooltip.install(name, nameTooltip);
		nameSection.getChildren().add(name);
		edgeSection.getChildren().add(nameSection);

		name.textProperty().addListener((observable, oldName, newName) -> {
			if(newName.length() < 15){
	    		edge.getEdge().setName(newName);
		    	MainWindow.get().updateViewport();
			}
			else{
				name.setText(oldName);
			}
        });


		Tooltip lengthTooltip = new Tooltip("The weight of the selected edge. Edges cannot have a negative weight.");
		HBox lengthSection = new HBox();
		Label lengthTitle = new Label("Weight:");
		lengthSection.getChildren().add(lengthTitle);
		Tooltip.install(lengthTitle, lengthTooltip);
		Tooltip.install(lengthSection, lengthTooltip);
		TextField length = new TextField("" + edge.getEdge().getLength());
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
			    edge.getEdge().setLength(value);
			    MainWindow.get().updateViewport();
			}
			else{
				length.setText("" + edge.getEdge().getLength());
			}
		});



		HBox nodeA = new HBox();
		Tooltip nodeATooltip = new Tooltip("The origin node of the edge");
		Tooltip.install(nodeA, nodeATooltip);

		Label nodeALabel = new Label("Origin:");
		Button nodeAButton = new Button(edge.getEdge().nodeA.getName());
		nodeAButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().addClickedComponent(VisualGraphNode.getNode(edge.getEdge().nodeA));
            }
        });
		nodeA.getChildren().add(nodeALabel);
		nodeA.getChildren().add(nodeAButton);


		HBox nodeB = new HBox();
		Tooltip nodeBTooltip = new Tooltip("The end node of the edge");
		Tooltip.install(nodeB, nodeBTooltip);

		Label nodeBLabel = new Label("End:   ");
		Label nodeBName = new Label(edge.getEdge().nodeB.getName());
		nodeB.getChildren().add(nodeBLabel);
		nodeB.getChildren().add(nodeBName);

		edgeSection.getChildren().add(nodeA);
		edgeSection.getChildren().add(nodeB);

		getChildren().add(edgeSection);

	}

	public void addDeleteButton(){
		Tooltip tooltip = new Tooltip("Delete the selected edge");
		Button delete = new Button("DELETE EDGE");
		Tooltip.install(delete, tooltip);
		getChildren().add(delete);
		delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().deleteEdge(edge);
            }
        });
	}
}
