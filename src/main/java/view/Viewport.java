package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;
import model.algorithm.GraphAlgorithm;

import menu.MainWindow;


import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

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
		setOnMouseClicked(e -> {requestFocus(); MainWindow.get().addClickedComponent(null);});
		
		//set the clipping rectangle to hide VGCs that are off screen
		Rectangle clip = new Rectangle(width, height);
        setClip(clip);
	}
	
	public void setAlgorithm(GraphAlgorithm algorithmIn){
		algorithm = algorithmIn;
		viewportDetails.update(this);
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
	
	
	public GraphAlgorithm getAlgorithm(){
		return algorithm;
	}
		
}