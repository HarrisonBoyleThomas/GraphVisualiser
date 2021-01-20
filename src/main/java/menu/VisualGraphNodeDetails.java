package menu;

import maths.Vector;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;


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
		Tooltip tooltip = new Tooltip("The details panel allows you to view and edit the details of selected graph components");
		Label title = new Label("DETAILS PANEL");
		Tooltip.install(title, tooltip);
		getChildren().add(title);
		getChildren().add(new NodeDetails(node.getNode()));
		
		addLocationSection();
		
		addDeleteButton();
		
		getChildren().add(new EmptyDetails());
		
		
		
	}
	
	private void addLocationSection(){
		HBox locationSection = new HBox();
		Tooltip tooltip = new Tooltip("The location of the node in world space. This is an x-y-z vector");
		
		Label title = new Label("Location: ");
		Tooltip.install(title, tooltip);
		locationSection.getChildren().add(title);
		
		
		HBox xSection = new HBox();
		Tooltip xTooltip = new Tooltip("The x coordinate of the node");
		
		Label xLabel = new Label("X:");
		TextField xCoord = new TextField("" + node.getLocation().x);
		xCoord.setMaxWidth(50);
		xCoord.textProperty().addListener((observable, oldCoord, newCoord) -> {
			double newX = 0.0;
			try{
			    newX = Double.valueOf(newCoord);
			}
			catch(NumberFormatException e){
				xCoord.setText("" + node.getLocation().x);
				newX = node.getLocation().x;
			}
			if(newCoord.length() > 7){
				xCoord.setText("" + node.getLocation().x);
				newX = node.getLocation().x;
			}
			
			node.setLocation(new Vector(newX, node.getLocation().y, node.getLocation().z));
			MainWindow.get().updateViewport();
        });
		
		xSection.getChildren().add(xLabel);
		xSection.getChildren().add(xCoord);
		
		locationSection.getChildren().add(xSection);
		
		
		HBox ySection = new HBox();
		Tooltip yTooltip = new Tooltip("The y coordinate of the node");
		
		Label yLabel = new Label("Y:");
		TextField yCoord = new TextField("" + node.getLocation().y);
		yCoord.setMaxWidth(50);
		yCoord.textProperty().addListener((observable, oldCoord, newCoord) -> {
			double newY = 0.0;
			try{
			    newY = Double.valueOf(newCoord);
			}
			catch(NumberFormatException e){
				yCoord.setText("" + node.getLocation().y);
				newY = node.getLocation().y;
			}
			if(newCoord.length() > 7){
				yCoord.setText("" + node.getLocation().y);
				newY = node.getLocation().y;
			}
			
			node.setLocation(new Vector(node.getLocation().x, newY, node.getLocation().z));
			MainWindow.get().updateViewport();
        });
		
		ySection.getChildren().add(yLabel);
		ySection.getChildren().add(yCoord);
		
		locationSection.getChildren().add(ySection);
		
		
		HBox zSection = new HBox();
		Tooltip zTooltip = new Tooltip("The z coordinate of the node");
		
		Label zLabel = new Label("Z:");
		TextField zCoord = new TextField("" + node.getLocation().z);
		zCoord.setMaxWidth(50);
		zCoord.textProperty().addListener((observable, oldCoord, newCoord) -> {
			double newZ = 0.0;
			try{
			    newZ = Double.valueOf(newCoord);
			}
			catch(NumberFormatException e){
				zCoord.setText("" + node.getLocation().z);
				newZ = node.getLocation().z;
			}
			if(newCoord.length() > 7){
				zCoord.setText("" + node.getLocation().z);
				newZ = node.getLocation().z;
			}
			
			node.setLocation(new Vector(node.getLocation().x, node.getLocation().y, newZ));
			MainWindow.get().updateViewport();
        });
		
		zSection.getChildren().add(zLabel);
		zSection.getChildren().add(zCoord);
		
		locationSection.getChildren().add(zSection);
		
		
		
		getChildren().add(locationSection);
	}
	
	public void addDeleteButton(){
		Tooltip tooltip = new Tooltip("Delete the selected node");
		Button delete = new Button("DELETE NODE");
		Tooltip.install(delete, tooltip);
		getChildren().add(delete);
		delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().deleteNode(node);
            }
        });
	}
}