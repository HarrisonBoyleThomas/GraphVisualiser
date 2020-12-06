package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;
import model.algorithm.GraphAlgorithm;


import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;

import javafx.animation.AnimationTimer;

public class Viewport extends Pane{
	private Camera camera;
	GraphAlgorithm algorithm;
	
	private int width = 500;
	private int height = 500;
	
	private ArrayList<KeyCode> heldDownKeys = new ArrayList<>();
	
	private ViewportDetails viewportDetails = new ViewportDetails();
	
	public Viewport(Camera cameraIn, GraphAlgorithm algorithmIn){
		setMinSize(width, height);
		setMaxSize(width, height);
		setStyle("-fx-background-color: #ffffff");
		camera = cameraIn;
		algorithm = algorithmIn;
		draw();
	}
	
	
	public void draw(){
		getChildren().clear();
		
		ArrayList<VisualGraphNode> nodes = VisualGraphNode.getNodes();
		ArrayList<VisualGraphEdge> edges = VisualGraphEdge.getEdges();
		
		Group root = new Group();
		
		for(VisualGraphEdge e : edges){
			VisualGraphEdge edge = new VisualGraphEdge(e);
			if(camera.isInFront(VisualGraphNode.getNode(edge.getEdge().nodeA))  || camera.isInFront(VisualGraphNode.getNode(edge.getEdge().nodeB))){
				edge.updateIcon(algorithm);
	    		root.getChildren().add(edge.getIcon());
	    		edge.getIcon().setLayoutX((int) edge.getRenderLocation().x);
	    		edge.getIcon().setLayoutY((int) edge.getRenderLocation().y);
			}
		}
		for(VisualGraphNode n : nodes){
			VisualGraphNode node = new VisualGraphNode(n);
			node.updateIcon(algorithm);
			root.getChildren().add(node.getIcon());
			node.getIcon().setLayoutX((int) node.getRenderLocation().x);
			node.getIcon().setLayoutY((int) node.getRenderLocation().y);
		}
		getChildren().add(root);
		
		viewportDetails.update(this);
		getChildren().add(viewportDetails);
	}
	
	public Camera getCamera(){
		return camera;
	}
	
	/**
	*    Creates a cube graph
	**/
	public void createCube(){
		ArrayList<GraphNode> nodes = new ArrayList<>();
		for(int i = 1; i <= 8; i++){
			GraphNode node = new GraphNode(i);
			node.setName("" + i);
			nodes.add(node);
		}
		
		ArrayList<GraphEdge> edges = new ArrayList<>();
		for(int i = 0; i < 4; i++){
			if(i < 3){
    			edges.add(nodes.get(i).addEdge(nodes.get(i+1), false));
			}
			else{
				edges.add(nodes.get(3).addEdge(nodes.get(0), false));
			}
		}
		
		for(int i = 4; i < 8; i++){
			if(i < 7){
    			edges.add(nodes.get(i).addEdge(nodes.get(i+1), false));
			}
			else{
				edges.add(nodes.get(7).addEdge(nodes.get(4), false));
			}
		}
		
		for(int i = 0; i < 4; i++){
    		edges.add(nodes.get(i).addEdge(nodes.get(i+4), false));
		}
		
		VisualGraphNode.create(new Vector(10, 10, -10), nodes.get(0));
		VisualGraphNode.create(new Vector(10, 10, 10), nodes.get(1));
		VisualGraphNode.create(new Vector(10, -10, 10), nodes.get(2));
		VisualGraphNode.create(new Vector(10, -10, -10), nodes.get(3));
	
		VisualGraphNode.create(new Vector(30, 10, -10), nodes.get(4));
		VisualGraphNode.create(new Vector(30, 10, 10), nodes.get(5));
		VisualGraphNode.create(new Vector(30, -10, 10), nodes.get(6));
		VisualGraphNode.create(new Vector(30, -10, -10), nodes.get(7));
		
		for(GraphEdge e : edges){
			e.setName("1");
			e.setLength(1);
			VisualGraphEdge.create(e);
		}
		
		VisualGraphNode.updateNodes(camera, 500,500);
		VisualGraphEdge.updateEdges();
	}
	
	public GraphAlgorithm getAlgorithm(){
		return algorithm;
	}
		
}