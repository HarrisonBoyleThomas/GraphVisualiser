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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

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
	
	/**
	*    @Return the VGE that represents the given edge
	*    @Return null if the given edge does not have a visual representation
	**/
	public static VisualGraphEdge getEdge(GraphEdge edgeToFind){
		for(VisualGraphEdge e : edges){
			if(e.getEdge().equals(edgeToFind)){
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
	
	/**
	*    Update the positions of all icons
	**/
	public static void updateEdges(){
		for(VisualGraphEdge e : edges){
			e.updateRenderLocation(new Vector());
			e.updateIcon();
		}
	}
	
	/**
	*    @Return a copy of all edges
	**/
	public static ArrayList<VisualGraphEdge> getEdges(){
		return new ArrayList<VisualGraphEdge>(edges);
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
		renderLocation = VisualGraphNode.getNode(edge.nodeA).getRenderLocation();
	}
	
	/**
	*    Draw a line from nodeA to nodeB, and add it to the VGE's icon
	**/
	public void updateIcon(){
		Vector nodeBLoc = VisualGraphNode.getNode(edge.nodeB).getRenderLocation();
		Line line = new Line((int) renderLocation.x, (int) renderLocation.y, (int)nodeBLoc.x, (int) nodeBLoc.y);
		
		
		StackPane pane = new StackPane();
		Circle background = new Circle(26);
		line.setStroke(Color.BLACK);
		if(edge.getState() == GraphComponentState.UNVISITED){
			line.setStroke(Color.BLACK);
		}
		else if(edge.getState() == GraphComponentState.VISITED){
			line.setStroke(Color.GREEN);
		}
		else if(edge.getState() == GraphComponentState.IN_OPEN_LIST){
			line.setStroke(Color.RED);
		}
		else if(edge.getState() == GraphComponentState.CURRENT){
			line.setStroke(Color.BLUE);
		}
		else{
			line.setStroke(Color.BLACK);
		}
		pane.getChildren().add(line);
		Text label = new Text(edge.getName());
		//pane.getChildren().add(label);
		
		icon = new Group();
		icon.getChildren().add(pane);
	}
	
	/**
	*    @Return true if the other VGE represents the same edge, false otherwise
	**/
	public boolean equals(Object otherObject){
		if(otherObject instanceof VisualGraphEdge){
		    VisualGraphEdge other = (VisualGraphEdge) otherObject;
			return edge.equals(other);
		}
		return false;
	}
		
	
}