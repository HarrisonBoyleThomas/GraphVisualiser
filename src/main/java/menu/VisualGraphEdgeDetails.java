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
		
		
		HBox nodeA = new HBox();
		Tooltip nodeATooltip = new Tooltip("The origin node of the edge");
		Tooltip.install(nodeA, nodeATooltip);
		
		Label nodeALabel = new Label("Origin:");
		Label nodeAName = new Label(edge.getEdge().nodeA.getName());
		nodeA.getChildren().add(nodeALabel);
		nodeA.getChildren().add(nodeAName);
		
		
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