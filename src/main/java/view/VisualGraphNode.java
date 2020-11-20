package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;


import java.util.ArrayList;


import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color; 
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

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
	
	
	/**
	*    Create a VisualGraphNode at the given location that represents the given node
	*    Only the create() function should be able to create GCNs
	**/
	private VisualGraphNode(Vector locationIn, GraphNode nodeIn){
		location = locationIn;
		node = nodeIn;
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
			if(icon.getNode().equals(node)){
				VisualGraphEdge.delete(VisualGraphEdge.getEdge(node));
				VisualGraphEdge.delete(VisualGraphEdge.getEdge(node));
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
			System.out.println(camera);
			icon.updateRenderLocation(camera.project(icon.getLocation(), width, height));
			icon.updateRenderScale(1 - (Vector.distance(camera.getLocation(), icon.getLocation())/50.0));
			icon.updateIcon();
		}
	}
	
	/**
	*    @Return a copy of the icons
	**/
	public static ArrayList<VisualGraphNode> getNodes(){
		return new ArrayList<VisualGraphNode>(icons);
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
	public void updateIcon(){
		StackPane pane = new StackPane();
		Circle background = new Circle(26 * renderScale);
		background.setStroke(Color.BLACK);
		background.setStrokeWidth(3);
		if(node.getState() == GraphComponentState.UNVISITED){
			background.setFill(Color.WHITE);
		}
		else if(node.getState() == GraphComponentState.VISITED){
			background.setFill(Color.GREEN);
		}
		else if(node.getState() == GraphComponentState.IN_OPEN_LIST){
			background.setFill(Color.RED);
		}
		else if(node.getState() == GraphComponentState.CURRENT){
			background.setFill(Color.BLUE);
		}
		else{
			background.setFill(Color.WHITE);
		}
		pane.getChildren().add(background);
		Text label = new Text(node.getName());
		pane.getChildren().add(label);
		
		icon = new Group();
		icon.getChildren().add(pane);
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