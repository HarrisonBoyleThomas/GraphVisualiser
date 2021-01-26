package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;
import model.algorithm.*;

import menu.MainWindow;


import java.util.ArrayList;


import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

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
		clickEvent = toCopy.clickEvent;
		selected = toCopy.selected;
	}

	/**
	*    Create a node at the given location that represents the given node
	*    @Param location to spawn the node
	*    @Param node the VGN represents
	*    @Return the created node
	**/
	public static VisualGraphNode create(Vector location, GraphNode node){
		VisualGraphNode newIcon = new VisualGraphNode(location, node);
		icons.add(newIcon);
		return newIcon;
	}

	/**
	*    Delete the given node from the node list
	*    To ensure the node is deleted, all references to the
	*    given node must further be deleted
	**/
	public static boolean delete(VisualGraphNode toDelete){
		VisualGraphEdge.delete(toDelete.getNode());
	    return icons.remove(toDelete);
	}

	/**
	*    Delete the VGN with the given node from the node list
	**/
	public static boolean delete(GraphNode toDelete){
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
	public static void updateNodes(Camera camera, int width, int height){
		for(VisualGraphNode icon : icons){
			icon.updateRenderLocation(camera.project(icon.getLocation(), width, height));
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
		Circle background = new Circle(26 * renderScale);
		background.setStroke(Color.BLACK);
		background.setStrokeWidth(3);
		if(selected){
			background.setFill(Color.ORANGE);
		}
		else{
			if(algorithm == null){
				background.setFill(Color.WHITE);
			}
			else{
        		if(algorithm.getNodeState(node) == GraphComponentState.UNVISITED){
        			background.setFill(Color.WHITE);
        		}
        		else if(algorithm.getNodeState(node) == GraphComponentState.VISITED){
        			background.setFill(Color.LIME);
        		}
        		else if(algorithm.getNodeState(node) == GraphComponentState.IN_OPEN_LIST){
        			background.setFill(Color.RED);
	        	}
	        	else if(algorithm.getNodeState(node) == GraphComponentState.CURRENT){
	        		background.setFill(Color.CYAN);
	        	}
				else if(algorithm.getNodeState(node) == GraphComponentState.IN_TREE){
					background.setFill(Color.CORNFLOWERBLUE);
				}
	        	else{
	        		background.setFill(Color.WHITE);
		        }
			}
		}
		pane.getChildren().add(background);
		Text label = new Text(node.getName());
		pane.getChildren().add(label);
		icon = new Group();
		icon.getChildren().add(pane);
		icon.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent);
		if(algorithm instanceof ShortestPathAlgorithm){
            addEstimatedDistance((ShortestPathAlgorithm) algorithm);
		}
		Tooltip tooltip = new Tooltip("Click to edit node");
		Tooltip.install(icon, tooltip);
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
        int distance = algorithm.getDistance(node);
		String number = "" + distance;
		if(distance < 0){
            number = "" + '\u221e';
		}

        //create and add the label to the node
		Label label = new Label(number);
		label.setLayoutX(30*renderScale);
		label.setLayoutY(-15*renderScale);
		BackgroundFill bg = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
        label.setBackground(new Background(bg));
		icon.getChildren().add(label);
	}

	private void addDragEvents(){
		icon.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = icon.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
				content.put(FORMAT, VisualGraphNode.this);
                db.setContent(content);
				db.setDragView(icon.snapshot(null, null));
				MainWindow.get().addClickedComponent(VisualGraphNode.getNode(getNode()), true);

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
}
