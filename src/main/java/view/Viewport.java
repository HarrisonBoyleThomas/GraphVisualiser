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

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.*;
import java.awt.Point;

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
		setOnMouseClicked(e -> {requestFocus(); MainWindow.get().addClickedComponent(null, false);});

		//set the clipping rectangle to hide VGCs that are off screen
		Rectangle clip = new Rectangle(width, height);
        setClip(clip);
		addDragAndDropEvents();
	}

	private void addDragAndDropEvents(){
		setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
                if(e.getDragboard().getContent(VisualGraphNode.FORMAT) != null){
                    e.acceptTransferModes(TransferMode.MOVE);
				}

                e.consume();
            }
        });

		setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
				if(e.getDragboard().getContent(VisualGraphNode.FORMAT) != null){

				}
                e.consume();
            }
        });

		setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
                e.consume();
            }
        });

		/**
		*    Move the most recently selected node such that it's new location is mapped to approximately
		*    the same renderLocation. Move all other nodes by simple translation, so the graph's 3D
		*    structure is preserved
		**/
		setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
				Object o = e.getDragboard().getContent(VisualGraphNode.FORMAT);
				boolean success = false;
				if(o instanceof VisualGraphNode){
					//The vgn from the drahboard(a copy) can't be used because extracting the "original" from the
					//"real" VGN list is impossible, so assume the most recently added VGC is the original node
					Point mouse =java.awt.MouseInfo.getPointerInfo().getLocation();
					Vector mousePosition = new Vector(e.getX(), e.getY(), 0);
					if(MainWindow.get().getClickedNodes().size() > 0){
						System.out.println("Move selected subgraph (" + MainWindow.get().getClickedNodes().size() + " nodes) about "+ MainWindow.get().getClickedNodes().get(0).getNode().getName());
    					Vector differenceFromMouse = mousePosition.subtract(MainWindow.get().getClickedNodes().get(0).getRenderLocation());
    					double xChange = differenceFromMouse.x / width;
    					double yChange = differenceFromMouse.y / height;
    					double deltaYaw = xChange*180;
    					double deltaPitch = yChange*180;
						Vector originalPosition = MainWindow.get().getClickedNodes().get(0).getLocation();
						//Move the dragged node by rotating about the camera to keep pit's erspective render location
						MainWindow.get().getClickedNodes().get(0).setLocation(Functions.rotateVector(camera.getLocation(), MainWindow.get().getClickedNodes().get(0).getLocation(), new Rotator(0, deltaPitch, deltaYaw)));
                        //Translate all other nodes to preserve their position
						for(VisualGraphNode node : MainWindow.get().getClickedNodes()){
							if(node != MainWindow.get().getClickedNodes().get(0)){
			    				Vector relative = Vector.subtract(MainWindow.get().getClickedNodes().get(0).getLocation(), originalPosition);
							    node.setLocation(node.getLocation().add(relative));
							}
	    				}
						success = true;
					}
				    MainWindow.get().updateViewport();
				}
				e.setDropCompleted(success);
                e.consume();
            }
        });

	}

	public void setAlgorithm(GraphAlgorithm algorithmIn){
		//Copy the old start node to be the start node of the replacement algorithm
		if(algorithm != null){
		    ((DijkstraShortestPath) algorithmIn).setStartNode(((DijkstraShortestPath) algorithm).getStartNode());
		}
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
