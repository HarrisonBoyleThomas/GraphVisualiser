package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;
import data.Data;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;
import model.algorithm.*;

import menu.MainWindow;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collections;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.SnapshotParameters;

import javafx.scene.input.MouseEvent;

/**
*    A VGN represents a graph node by storing a reference to a node
*    and existing in 3D space for the renderer to draw
*
*    Author: Harrison Boyle-Thomas
*    Date: 20/11/2020
**/
public class VisualGraphNode extends VisualGraphComponent{
	//List of all created icons
	private static ArrayList<VisualGraphNode> icons = new ArrayList<>();

	//The node the GNI represents
	private final GraphNode node;

	public static final DataFormat FORMAT = new DataFormat("VisualGraphNode");

	/**
	*    Create a VisualGraphNode at the given location that represents the given node
	*    Only the create() function should be able to create GCNs
	**/
	private VisualGraphNode(Vector locationIn, GraphNode nodeIn){
		location = locationIn;
		node = nodeIn;
		addEvents();
	}

	/**
	*    Copy constructor
	*    Copies of the "concrete" VGNs created by the create() function are supplied
	*    to each viewport
	**/
	public VisualGraphNode(VisualGraphNode toCopy){
		location = toCopy.getLocation();
		node = toCopy.getNode();
		renderLocation = toCopy.getRenderLocation();
		renderScale = toCopy.renderScale;
		icon = toCopy.getIcon();
		//clickEvent = toCopy.clickEvent;
		addEvents();
		selected = toCopy.selected;
	}

	/**
	*    Create a node at the given location that represents the given node
	*    @Param location to spawn the node
	*    @Param node the VGN represents
	*    @Return the created node
	**/
	public static synchronized VisualGraphNode create(Vector location, GraphNode node){
		VisualGraphNode newIcon = new VisualGraphNode(location, node);
		icons.add(newIcon);
		return newIcon;
	}

	/**
	*    Delete the given node from the node list
	*    To ensure the node is deleted, all references to the
	*    given node must further be deleted
	**/
	public static synchronized boolean delete(VisualGraphNode toDelete){
		VisualGraphEdge.delete(toDelete.getNode());
	    return icons.remove(toDelete);
	}

	/**
	*    Delete the VGN with the given node from the node list
	**/
	public static synchronized boolean delete(GraphNode toDelete){
		VisualGraphNode iconToDelete = getNode(toDelete);
		return delete((VisualGraphNode) iconToDelete);
	}

	/**
	*    @Return the icon that represents the given node
	**/
	public static VisualGraphNode getNode(GraphNode node){
		for(VisualGraphNode icon : icons){
			if(icon.getNode() == node){
				//VisualGraphEdge.delete(VisualGraphEdge.getEdge(node));
				//VisualGraphEdge.delete(VisualGraphEdge.getEdge(node));
				return icon;
			}
		}
		return null;
	}

	/**
	*    Update the positions of all icons
	**/
	public static synchronized void updateNodes(Camera camera, int width, int height){
		for(VisualGraphNode icon : icons){
			Vector loc = camera.project(icon.getLocation(), width, height);
			icon.updateRenderLocation(loc);
			icon.updateRenderScale(1 - (Vector.distance(camera.getLocation(), icon.getLocation())/50.0));
		}
	}

	/**
	*    @Return a copy of the icons
	**/
	public static ArrayList<VisualGraphNode> getNodes(){
		return new ArrayList<VisualGraphNode>(icons);
	}

	public static ArrayList<VisualGraphNode> copyNodes(){
		ArrayList<VisualGraphNode> output = new ArrayList<>();
		for(VisualGraphNode node : icons){
			output.add(new VisualGraphNode(node));
		}
		return output;
	}

	/**
	*    @Return the node the VGN represents
	**/
	public GraphNode getNode(){
		return node;
	}

	/**
	*    Update the visual representation of the node
	**/
	public void updateIcon(GraphAlgorithm algorithm){
		StackPane pane = new StackPane();
		Circle background = new Circle(26);// * renderScale);
		//background.setStroke(Color.BLACK);
		background.setStrokeWidth(3);
		if(selected){
			String colourString = Data.formatColourToRGBA(Data.COLOUR_CODE_DATA.getColourForState(GraphComponentState.SELECTED));
			//background.setFill(Color.ORANGE);
			background.setStyle("-fillColour: " + colourString);
			setColour = Color.web(colourString);
		}
		else{
			if(algorithm == null){

			}
			else{
				Color colour = Data.COLOUR_CODE_DATA.getColourForState(algorithm.getNodeState(node));
				if(colour != GraphComponentState.UNVISITED){
		    		String colourString = Data.formatColourToRGBA(colour);
                    if(colourString != null){
			    		background.setStyle("-fillColour: " + colourString);
				    	setColour = Color.web(colourString);
				    }
				}
				/*
				if(algorithm.getNodeState(node) == GraphComponentState.VISITED){
					//LIME colour
					background.setStyle("-fillColour: rgba(0, 255, 0, 1)");
					setColour = Color.web("rgba(0, 255, 0, 1)");
        		}
        		else if(algorithm.getNodeState(node) == GraphComponentState.IN_OPEN_LIST){
        			//RED colour
					background.setStyle("-fillColour: rgba(255, 0, 0, 1)");
					setColour = Color.web("rgba(255, 0, 0, 1)");
	        	}
	        	else if(algorithm.getNodeState(node) == GraphComponentState.CURRENT){
	        		//CYAN colour
					background.setStyle("-fillColour: rgba(0, 255, 255, 1)");
					setColour = Color.web("rgba(0, 255, 255, 1)");
	        	}
				else if(algorithm.getNodeState(node) == GraphComponentState.IN_TREE){
					//CORNFLOWERBLUE colour
					background.setStyle("-fillColour: rgba(100, 149, 237, 1)");
					setColour = Color.web("rgba(100, 149, 237, 1)");
				}
				*/
			}
		}
		pane.getChildren().add(background);

	    Label label;
	    if(node.getName().length() < 4){
		   	label = new Label(node.getName());
		}
	    else{
	    	label = new Label(node.getName().substring(0, 4));
	    }
	    label.setId("nodeLabel");
    	pane.getChildren().add(label);

		icon = new Group();
		icon.getChildren().add(pane);
		icon.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent);
		if(renderScale >= 0.5){
    		if(algorithm instanceof ShortestPathAlgorithm){
                addEstimatedDistance((ShortestPathAlgorithm) algorithm);
		    }
		}
		//Tooltip tooltip = new Tooltip("Click to edit node");
		//Tooltip.install(icon, tooltip);
		addDragEvents();
	}

    /**
	*    Add a small label above the node to represent the current estimated distance
	*    from the start node if the running algorithm is a SPA
	**/
	private void addEstimatedDistance(ShortestPathAlgorithm algorithm){
        if(icon == null || algorithm == null){
			return;
		}
        double distance = algorithm.getDistance(node);
		String number = "" + distance;
		if(distance < 0 || distance == Double.POSITIVE_INFINITY){
            number = "" + '\u221e';
		}

        //create and add the label to the node
		Label label = new Label(number);
		label.setId("nodeLabel");
		label.setLayoutX(40*renderScale);
		label.setLayoutY(-20*renderScale);
		icon.getChildren().add(label);
		icon.setPickOnBounds(true);
	}

	private void addDragEvents(){
		icon.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = icon.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
				content.put(FORMAT, VisualGraphNode.this);
                db.setContent(content);
				SnapshotParameters parameters = new SnapshotParameters();
				parameters.setFill(Color.TRANSPARENT);
				db.setDragView(icon.snapshot(parameters, null));
				MainWindow.get().addClickedComponentDragged(VisualGraphNode.getNode(getNode()));

                event.consume();
            }
        });


		icon.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {

                event.consume();
            }
        });
	}

	public void updatePosition(){

	}

	/**
	*    @Return A Vector representing the render location of the middle of the node
	**/
	public Vector getCenterLocation(){
		return new Vector(renderLocation.x + (26.0 * renderScale), renderLocation.y + (26.0 * renderScale), 0);
	}


	/**
	*    @Return true if the other icon represents the same node, false otherwise
	**/
	public boolean equals(Object otherObject){
		if(otherObject instanceof VisualGraphNode){
		    VisualGraphNode other = (VisualGraphNode) otherObject;
			return node.equals(other.getNode());
		}
		return false;
	}

	public boolean iconsEqual(Group other){
		if(other == null || icon == null){
			return false;
		}
		StackPane root = (StackPane) other.getChildren().get(0);
		StackPane myRoot = (StackPane) icon.getChildren().get(0);
		//System.out.println(((Circle) root.getChildren().get(0)).getFill());
		//System.out.println(setColour);
		Color circleCol = (Color) ((Circle) root.getChildren().get(0)).getFill();
		Boolean blankColourCheck = (setColour == null && ((Color.web("rgba(37,37,37, 255)").equals(circleCol) || Color.web("rgba(255, 255, 255, 255)").equals(circleCol))));
		if(blankColourCheck){

		}
		else if((! circleCol.equals(setColour))){
			return false;
		}
		if(! ((Label) root.getChildren().get(1)).getText().equals(((Label) myRoot.getChildren().get(1)).getText())){
			return false;
		}
		//if(((Label) other.getChildren().get(1)).getText() != ((Label) icon.getChildren().get(1)).getText()){
		//	return false;
		//}
		return true;
	}

	public Color getSetColour(){
		return setColour;
	}

	/**
	*    Since the click list MUST contain a VGC from the static VGC list, search for
	*   the corresponding VGC from this list by comparing the node the VGN represents
	**/
	protected void handleClick(VisualGraphComponent toAdd, boolean doubleClick){
		if(toAdd == null){
		    super.handleClick(VisualGraphNode.getNode(this.getNode()), doubleClick);
		}
	}
}
