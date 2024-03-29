package menu;

import maths.Vector;
import data.UndoRedoController;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

import javafx.application.Platform;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;

/**
*    The VGND panel displays useful information about a selected node, and
*    allows the user to edit some of it's properties
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
**/
public class VisualGraphNodeDetails extends DetailsPanel{
	VisualGraphNode node;
	/**
	*    @param nodeIn the node to represent
	**/
	public VisualGraphNodeDetails(VisualGraphNode nodeIn){
		node = nodeIn;
		update();
	}

    /**
	*    Set the stored node to the supplied node
	*    @param nodeIn the node to set
	**/
	public void setNode(VisualGraphNode nodeIn){
		node = nodeIn;
		update();
	}

	public VisualGraphNode getNode(){
		return node;
	}
	public void highlightFirstAttribute(){
		Platform.runLater(new Runnable() {
			@Override
			public void run(){
				if(getChildren().size() > 0){
					((DetailsPanel) getChildren().get(1)).highlightFirstAttribute();
				}
			}
		});
	}

	public void update(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
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
		});
	}

    /**
	*    Create and set up the location section, which allows the
	*    user to edit the location of the node by manually typing in the values
	*    of it's vector components
	**/
	private void addLocationSection(){
		HBox locationSection = new HBox();
		locationSection.setId("location");
		locationSection.setAlignment(Pos.TOP_CENTER);
		Tooltip tooltip = new Tooltip("The location of the node in world space. This is an x-y-z vector");

		Label title = new Label("Location: ");
		Tooltip.install(title, tooltip);
		locationSection.getChildren().add(title);

        //Create a small section for editing the x coordinate of the node
		HBox xSection = new HBox();
		Tooltip xTooltip = new Tooltip("The x coordinate of the node");

		Label xLabel = new Label("X:");
		TextField xCoord = new TextField("" + node.getLocation().x);
		xCoord.setId("xCoord");
		xCoord.setMaxWidth(50);
		xCoord.textProperty().addListener((observable, oldCoord, newCoord) -> {
			double newX = 0.0;
			//if there are any errors, just reset to the node's current position
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
            UndoRedoController.pushToUndoStack();
			node.setLocation(new Vector(newX, node.getLocation().y, node.getLocation().z));
			MainWindow.get().updateViewport();
        });

		xSection.getChildren().add(xLabel);
		xSection.getChildren().add(xCoord);

		locationSection.getChildren().add(xSection);

        //create a small section for editing the y coordinate of the node
		HBox ySection = new HBox();
		Tooltip yTooltip = new Tooltip("The y coordinate of the node");

		Label yLabel = new Label("Y:");
		TextField yCoord = new TextField("" + node.getLocation().y);
		yCoord.setId("yCoord");
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
            UndoRedoController.pushToUndoStack();
			node.setLocation(new Vector(node.getLocation().x, newY, node.getLocation().z));
			MainWindow.get().updateViewport();
        });

		ySection.getChildren().add(yLabel);
		ySection.getChildren().add(yCoord);

		locationSection.getChildren().add(ySection);

        //create a small section for editing the z coordinate of the node
		HBox zSection = new HBox();
		Tooltip zTooltip = new Tooltip("The z coordinate of the node");

		Label zLabel = new Label("Z:");
		TextField zCoord = new TextField("" + node.getLocation().z);
		zCoord.setId("zCoord");
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
            UndoRedoController.pushToUndoStack();
			node.setLocation(new Vector(node.getLocation().x, node.getLocation().y, newZ));
			MainWindow.get().updateViewport();
        });

		zSection.getChildren().add(zLabel);
		zSection.getChildren().add(zCoord);

		locationSection.getChildren().add(zSection);



		getChildren().add(locationSection);
	}

    /**
	*    Create and set up the delete button.
	*    The delete button asks the mainWindow to delete the stored node
	**/
	private void addDeleteButton(){
		Tooltip tooltip = new Tooltip("Delete the selected node");
		Button delete = new Button("DELETE NODE");
		delete.setId("delete");
		Tooltip.install(delete, tooltip);
		getChildren().add(delete);
		delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().deleteNode(node);
            }
        });
	}
}
