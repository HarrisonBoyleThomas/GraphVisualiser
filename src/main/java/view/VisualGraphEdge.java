package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;
import model.algorithm.*;


import java.util.ArrayList;
import java.lang.NullPointerException;


import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color; 
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import javafx.scene.input.MouseEvent;

/**
*    A VGE represents a graph edge by storing a reference to an edge
*    and existing in 3D space for the renderer to draw
*    The VGE makes no use of the location inheirited from Actor, or 
*    the render location inherited from VGC. Instead, the VGE copies the 
*    render location of nodeA from the edge the VGE represents
*    
*    Author: Harrison Boyle-Thomas
*    Date: 20/11/2020
**/
public class VisualGraphEdge extends VisualGraphComponent{
	//List of all VGEs
	private static ArrayList<VisualGraphEdge> edges = new ArrayList<>();
	//The edge the VGE represents
	private final GraphEdge edge;
	
	private VisualGraphEdge(GraphEdge edgeIn){
		edge = edgeIn;
		location = VisualGraphNode.getNode(edge.nodeA).getRenderLocation();
		addEvents();
	}
	
	public VisualGraphEdge(VisualGraphEdge toCopy){
		edge = toCopy.getEdge();
		renderLocation = toCopy.getRenderLocation();
		renderScale = toCopy.renderScale;
		icon = toCopy.getIcon();
		clickEvent = toCopy.clickEvent;
		selected = toCopy.selected;
	}
	
	/**
	*    Create a VGE at the location of nodeA that represents the given edge
	*    @Return the edge
	**/
	public static VisualGraphEdge create(GraphEdge edgeIn){
		VisualGraphEdge newEdge = new VisualGraphEdge(edgeIn);
		edges.add(newEdge);
		return newEdge;
	}
	
	/**
	*    Delete the given VGE from the list of all VGEs
	*    @Return true if an edge was deleted
	**/
	public static boolean delete(VisualGraphEdge toRemove){
		toRemove.getEdge().nodeA.removeEdge(toRemove.getEdge(), false);
		return edges.remove(toRemove);
	}
	
	/**
	*    Delete the VGE that represents the given graph edge
	*    @Return true if successful
	**/
	public static boolean delete(GraphEdge edgeToDelete){
		VisualGraphEdge e = getEdge(edgeToDelete);
		return delete(e);
	}
	
	public static void delete(ArrayList<GraphEdge> toDelete){
		for(GraphEdge e : toDelete){
			delete(e);
		}
	}
	
	public static void delete(GraphNode node){
		ArrayList<GraphEdge> toDelete = new ArrayList<>();
		for(VisualGraphEdge e : edges){
			if(e.getEdge().nodeA.equals(node) || e.getEdge().nodeB.equals(node)){
				toDelete.add(e.getEdge());
			}
		}
		delete(toDelete);
	}
				
	/**
	*    @Return the VGE that represents the given edge
	*    @Return null if the given edge does not have a visual representation
	**/
	public static VisualGraphEdge getEdge(GraphEdge edgeToFind){
		for(VisualGraphEdge e : edges){
			if(e.getEdge().equals(edgeToFind)){
				System.out.println("found edge");
				return e;
			}
		}
		return null;
	}
	
	/**
	*    @Return the first VGE found that represents an edge containing the given node
	*    There should only ever be two edges that link to the same node
	**/
	public static VisualGraphEdge getEdge(GraphNode node){
		for(VisualGraphEdge e : edges){
			if(e.getEdge().nodeA.equals(node) || e.getEdge().nodeB.equals(node)){
				return e;
			}
		}
		return null;
	}
	
	public static ArrayList<VisualGraphEdge> getEdges(GraphNode node){
		ArrayList<VisualGraphEdge> found = new ArrayList<>();
		for(VisualGraphEdge e : edges){
			if(e.getEdge().nodeA.equals(node) || e.getEdge().nodeB.equals(node)){
				found.add(e);
			}
		}
		return found;
	}
	
	/**
	*    Update the positions of all icons
	**/
	public static void updateEdges(){
		ArrayList<VisualGraphEdge> invalid = new ArrayList<>();
		for(VisualGraphEdge e : edges){
			if(e.getEdge().nodeA == null || e.getEdge().nodeB == null){
				System.out.println("invalid found");
				invalid.add(e);
			}
			else{
			    e.updateRenderLocation(new Vector());
			}
		}
		edges.removeAll(invalid);
	}
	
	/**
	*    @Return a copy of all edges
	**/
	public static ArrayList<VisualGraphEdge> getEdges(){
		return new ArrayList<VisualGraphEdge>(edges);
	}
	
	public static ArrayList<VisualGraphEdge> copyEdges(){
		ArrayList<VisualGraphEdge> output = new ArrayList<>();
		for(VisualGraphEdge edge : edges){
			output.add(new VisualGraphEdge(edge));
		}
		return output;
	}
	
	/**
	*    @Return the edge the VGE represents
	**/
	public GraphEdge getEdge(){
		return edge;
	}
	
	
	/**
	*    @Return true if nodeA and nodeB are on screen
	*    //PROBLEM: IF TWO NODES ARE AT EITHER END OF THE SCREEN, THE EDGE SHOULD STILL BE VISIBLE BUT THIS METHOD WOULD RETURN FALSE
	*    //TODO: ADD A CHECK IF THE EDGE PASSES THROUGH THE SCREEN
	**/
	public boolean isOnScreen(int width, int height){
		return VisualGraphNode.getNode(edge.nodeA).isOnScreen(width, height) && VisualGraphNode.getNode(edge.nodeB).isOnScreen(width, height);
	}
	
	/**
	*    Set the location of the edge to be the location of the VGN representing nodeA
	*	 @Override
	**/
	public void updateRenderLocation(Vector newLocation){
		renderLocation = VisualGraphNode.getNode(edge.nodeA).getCenterLocation();
	}
	
	/**
	*    Draw a line from nodeA to nodeB, and add it to the VGE's icon
	**/
	public void updateIcon(GraphAlgorithm algorithm){
		Vector nodeALoc = VisualGraphNode.getNode(edge.nodeA).getRenderLocation();
		Vector nodeBLoc = VisualGraphNode.getNode(edge.nodeB).getRenderLocation();
		Vector difference = nodeBLoc.subtract(nodeALoc);
		double gradient = -(difference.x / difference.y);
		Line line = new Line(0, 0, (int) difference.x, (int) difference.y);
		Polygon arrow = new Polygon();
		double lineAngle = Math.atan2(difference.y, difference.x);
		double lineLength = 0.8*difference.length();
		arrow.getPoints().addAll(new Double[]{
			difference.x, difference.y,
			Math.cos(lineAngle+0.05)*lineLength, Math.sin(lineAngle+0.05)*lineLength,
			Math.cos(lineAngle-0.05)*lineLength, Math.sin(lineAngle-0.05)*lineLength
		});
		
		line.setStrokeWidth(4);
		
		
		Pane pane = new Pane();
		Color fillColour = Color.BLACK;
		line.setStroke(Color.BLACK);
		if(selected){
			fillColour = Color.ORANGE;
		}
		else{
    		if(algorithm.getNodeState(edge.nodeA) == GraphComponentState.CURRENT || algorithm.getNodeState(edge.nodeA) == GraphComponentState.VISITED){
    			if(algorithm.getNodeState(edge.nodeB) == GraphComponentState.VISITED || algorithm.getNodeState(edge.nodeB) == GraphComponentState.CURRENT){
    				fillColour = Color.LIME;
    			}
    			else if(algorithm.getNodeState(edge.nodeB) == GraphComponentState.IN_OPEN_LIST){
    				fillColour = Color.RED;
    			}
    			else{
    				fillColour = Color.BLACK;
    			}
    		}
    		else{
    			fillColour = Color.BLACK;
    		}
		}
		line.setStroke(fillColour);
		arrow.setStroke(fillColour);
		arrow.setFill(fillColour);
		
		pane.getChildren().add(line);
		pane.getChildren().add(arrow);
		Label label = new Label(edge.getName() + ":" + edge.getLength());
		Vector midpoint = getCenter(nodeALoc, nodeBLoc);
		label.setLayoutX((int) (Math.cos(lineAngle+0.174533)*lineLength*0.625));
        label.setLayoutY((int) (Math.sin(lineAngle+0.174533)*lineLength*0.625));
		
		BackgroundFill bg = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
		
        label.setBackground(new Background(bg));		
		pane.getChildren().add(label);
		
		icon = new Group();
		icon.getChildren().add(pane);
		icon.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent);
		Tooltip tooltip = new Tooltip("Click to edit edge");
		Tooltip.install(icon, tooltip);
	}
	
	private Vector getCenter(Vector a, Vector b){
		return Vector.multiply(b.subtract(a), 0.5);
	}
	
	/**
	*    @Return true if the other VGE represents the same edge, false otherwise
	**/
	public boolean equals(Object otherObject){
		if(otherObject instanceof VisualGraphEdge){
		    VisualGraphEdge other = (VisualGraphEdge) otherObject;
			return edge.equals(other.getEdge());
		}
		return false;
	}
		
	
}