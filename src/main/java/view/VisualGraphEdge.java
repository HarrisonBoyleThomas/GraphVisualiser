package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;
import data.Data;
import data.UndoRedoController;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;
import model.algorithm.*;


import java.util.ArrayList;
import java.lang.NullPointerException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collections;


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

import javafx.scene.Node;

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
		//clickEvent = toCopy.clickEvent;
		addEvents();
		selected = toCopy.selected;
		location = toCopy.renderLocation;
	}

	/**
	*    Create a VGE at the location of nodeA that represents the given edge
	*    @Return the edge
	**/
	public static synchronized VisualGraphEdge create(GraphEdge edgeIn){
		VisualGraphEdge newEdge = new VisualGraphEdge(edgeIn);
		edges.add(newEdge);
		return newEdge;
	}

	/**
	*    Delete the given VGE from the list of all VGEs
	*    @Return true if an edge was deleted
	**/
	public static synchronized boolean delete(VisualGraphEdge toRemove){
		toRemove.getEdge().nodeA.removeEdge(toRemove.getEdge(), false);
		return edges.remove(toRemove);
	}

	/**
	*    Delete the VGE that represents the given graph edge
	*    @Return true if successful
	**/
	public static synchronized boolean delete(GraphEdge edgeToDelete){
		VisualGraphEdge e = getEdge(edgeToDelete);
		return delete(e);
	}

	public static synchronized void delete(ArrayList<GraphEdge> toDelete){
		for(GraphEdge e : toDelete){
			delete(e);
		}
	}

	public static synchronized void delete(GraphNode node){
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
	*    Used for undo and redo
	**/
	public static synchronized void setEdges(ArrayList<VisualGraphEdge> edgesIn){
		edges = new ArrayList<VisualGraphEdge>(edgesIn);
	}

	/**
	*    Update the positions of all icons
	**/
	public static synchronized void updateEdges(){
		ArrayList<VisualGraphEdge> invalid = new ArrayList<>();
		for(VisualGraphEdge e : edges){
			if(e.getEdge().nodeA == null || e.getEdge().nodeB == null){
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
	*    The line is drawn by using the relative position of nodeB from nodeA,
	**/
	public void updateIcon(GraphAlgorithm algorithm){
		VisualGraphNode nodeA = VisualGraphNode.getNode(edge.nodeA);
		Vector nodeALoc = nodeA.getRenderLocation();
		VisualGraphNode nodeB = VisualGraphNode.getNode(edge.nodeB);
		Vector nodeBLoc = nodeB.getRenderLocation();
		Vector difference = nodeBLoc.subtract(nodeALoc);
		Vector nodeASize = difference.normalise().multiply(nodeA.getRenderScale()*26);
		//How much space nodeB's circle radius takes up along the edge line
		Vector nodeBSize = difference.normalise().multiply(nodeB.getRenderScale()*23);
		//Subtract nodeB's circle radius, so that the arrow head ends at the boundary of the node
		difference = difference.subtract(nodeBSize);
		double gradient = -(difference.x / difference.y);
		Line line = new Line(nodeASize.x, nodeBSize.y, (int) difference.x, (int) difference.y);
		Polygon arrow = new Polygon();
		double lineAngle = Math.atan2(difference.y, difference.x);
		//How far up the line the arrow head ends
		double lineLength = 0.8*difference.length();
		//The two trigonometric points are a distance along the line, and slightly offset perpendicular to the line
		//(the edges of the arrows)
		arrow.getPoints().addAll(new Double[]{
			difference.x, difference.y,
			Math.cos(lineAngle+0.1)*lineLength, Math.sin(lineAngle+0.1)*lineLength,
			Math.cos(lineAngle-0.1)*lineLength, Math.sin(lineAngle-0.1)*lineLength
		});
		line.setStrokeWidth(4);


		Pane pane = new Pane();
		Color fillColour = null;//Color.BLACK;
		//line.setStroke(Color.BLACK);
		if(selected){
			fillColour = Color.ORANGE;
		}
		else{
			if(algorithm != null){
				String colourString = Data.formatColourToRGBA(Data.COLOUR_CODE_DATA.getColourForState(algorithm.getEdgeState(edge)));
				if(colourString != null){
					fillColour = Color.web(colourString);
				}
			}
		}
		//Override the css color property if the edge must be coloured differently
		if(fillColour != null){
			setColour = fillColour;
			String selectedColour = String.format("rgba(%d, %d, %d, %f)", ((int) (255*fillColour.getRed())), ((int) (255*fillColour.getGreen())), ((int) (255*fillColour.getBlue())), fillColour.getOpacity());
			line.setStyle("-colour: " + selectedColour + ";");
		    line.setStroke(fillColour);
			arrow.setStyle("-colour: " + selectedColour + ";");
			arrow.setStroke(fillColour);
			arrow.setFill(fillColour);
		}

		pane.getChildren().add(line);
		pane.getChildren().add(arrow);
		Label label = new Label(edge.getName() + ":" + edge.getLength());
		label.setId("edgeLabel");
		Vector midpoint = getCenter(nodeALoc, nodeBLoc);
		label.setLayoutX((int) (Math.cos(lineAngle+0.174533)*lineLength*0.625));
        label.setLayoutY((int) (Math.sin(lineAngle+0.174533)*lineLength*0.625));

		BackgroundFill bg = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);

        label.setBackground(new Background(bg));
		pane.getChildren().add(label);

		icon = new Group();
		icon.getChildren().add(pane);
		icon.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent);
		icon.setPickOnBounds(false);
		for(Node n : pane.getChildren()){
			n.setPickOnBounds(false);
		}
		pane.setPickOnBounds(false);
		//Tooltip tooltip = new Tooltip("Click to edit edge");
		//Tooltip.install(icon, tooltip);
	}

	private Vector getCenter(Vector a, Vector b){
		return Vector.multiply(b.subtract(a), 0.5);
	}
    /**
	*    @return the midpoint of the edge
	*    @return null if one of the nodes is invalid
	**/
	public Vector getWorldMidpoint(){
		VisualGraphNode nodeA = VisualGraphNode.getNode(edge.nodeA);
		VisualGraphNode nodeB = VisualGraphNode.getNode(edge.nodeB);
		if(nodeA == null || nodeB == null){
			return null;
		}
		Vector a = nodeA.getLocation();
		Vector b = nodeB.getLocation();
		return new Vector((a.x + b.x) /2, (a.y + b.y) / 2, (a.z + b.z)/2);
	}

	/**
	*    @Return true if the other VGE represents the same edge, false otherwise
	**/
	@Override
	public boolean equals(Object otherObject){
		if(otherObject instanceof VisualGraphEdge){
		    VisualGraphEdge other = (VisualGraphEdge) otherObject;
			return edge.equals(other.getEdge());
		}
		return false;
	}

	public boolean iconsEqual(Group other){
		return false;
	}

    /**
	*    Since the click list MUST contain a VGC from the static VGC list, search for
	*   the corresponding VGC from this list by comparing the edge the VGE represents
	**/
	protected void handleClick(VisualGraphComponent toAdd, boolean doubleClick){
		if(toAdd == null){
		    super.handleClick(VisualGraphEdge.getEdge(this.getEdge()), doubleClick);
		}
	}


}
