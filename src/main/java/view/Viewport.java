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

import threads.AlgorithmRunner;


import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.control.Button;
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

import java.io.*;
import java.nio.file.Paths;

import javafx.animation.AnimationTimer;
/**
*    The Viewport is an intergal part of GraphVisualiser
*    The viewport makes duplicates of the base VisualGraphComponents, and alters
*    their colours based on the viewport's algorithm state
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public class Viewport extends Pane{
	private Camera camera;
	GraphAlgorithm algorithm;

	private int width = 500;
	private int height = 500;

	private ArrayList<KeyCode> heldDownKeys = new ArrayList<>();

	private ViewportDetails viewportDetails = new ViewportDetails();

	private AlgorithmRunner algorithmRunner;

	public Viewport(Camera cameraIn, GraphAlgorithm algorithmIn){
		setMinSize(width, height);
		setMaxSize(width, height);
		setId("viewport");
		//setStyle("-fx-background-color: #ffffff");
		camera = cameraIn;
		algorithm = algorithmIn;
		draw();
		setOnMouseClicked(e -> {requestFocus(); MainWindow.get().addClickedComponent(null, false);});

		//set the clipping rectangle to hide VGCs that are off screen
		Rectangle clip = new Rectangle(width, height);
        setClip(clip);
		addDragAndDropEvents();
		createCloseButton();
	}
    /**
	*    Add drag and drop events so that nodes can be dragged onto the viewport
	**/
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
    /**
	*    Set the algorithm of the viewport. If the algorithm is a shortest path algorithm, try
	*    to copy the start node from other existing shortest path algorithms to save the user time
	**/
	public void setAlgorithm(GraphAlgorithm algorithmIn){
		//Copy the old start node to be the start node of the replacement algorithm
		if(algorithm != null){
		    ((ShortestPathAlgorithm) algorithmIn).setStartNode(((ShortestPathAlgorithm) algorithm).getStartNode());
		}
		algorithm = algorithmIn;
		viewportDetails.update(this);
	}

    /**
	*    Draw all VGCs to the viewport
	*    Note: VGC's renderLocations MUST be updated before draw, to ensure their
	*    locations are correct
	**/
	public void draw(){
		//clear the old frame
		getChildren().clear();
		//add the close button
		createCloseButton();
        //get copies of VGCs
		ArrayList<VisualGraphNode> nodes = VisualGraphNode.getNodes();
		ArrayList<VisualGraphEdge> edges = VisualGraphEdge.getEdges();

		Group root = new Group();
        //draw the edges
		for(VisualGraphEdge e : edges){
			VisualGraphEdge edge = new VisualGraphEdge(e);
			if(camera.isInFront(VisualGraphNode.getNode(edge.getEdge().nodeA))  || camera.isInFront(VisualGraphNode.getNode(edge.getEdge().nodeB))){
				edge.updateIcon(algorithm);
	    		root.getChildren().add(edge.getIcon());
	    		edge.getIcon().setLayoutX((int) edge.getRenderLocation().x);
	    		edge.getIcon().setLayoutY((int) edge.getRenderLocation().y);
			}
		}
		//Draw the nodes
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

	public void setStyleSheet(String sheetIn){
        getStylesheets().clear();
		getStylesheets().add(sheetIn);
	}

    //Algorithm execution section

    /**
	*    Create a button in the top right corner that deletes the viewport from the main window
	**/
	private void createCloseButton(){
		Button close = new Button("X");
		close.setId("closeButton");
		close.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().deleteViewport(Viewport.this);
				terminateAlgorithm();
            }
        });
		getChildren().add(close);
		//Position in the top right corner
		close.setLayoutX(width - 30);
	}

    /**
	*    Create an AlgorithmRunner which runs the algorithm in a separate thread.
	*    The viewport automatically redraws the graph to reflect the changes within
	*    the algorithm
	**/
	public void executeAlgorithm(){
		terminateAlgorithm();
		algorithmRunner = new AlgorithmRunner(algorithm, 100);
		//Extract the algorithm speed
		updateAlgorithmSpeed();
		algorithmRunner.start();
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				draw();
				if(algorithmRunner == null || !algorithmRunner.isRunning()){
					System.out.println("end draw");
					stop();
					draw();
					MainWindow.get().updateWindowStatus();
				}
			}
		}.start();
	}

	public void updateAlgorithmSpeed(){
		if(algorithmRunner == null){
			return;
		}
		algorithmRunner.updateSleepDelay();
	}
    /**
	*    toggle the pause value of the algorithm runner, if it exists
	*    @return the value of pause, or false on failure
	**/
	public boolean pauseAlgorithm(){
		if(algorithmRunner == null){
			return false;
		}
		return algorithmRunner.togglePause();
	}
    /**
	*    @return true if the algorithmRunner exists
	**/
	public boolean isExecutingAlgorithm(){
		return algorithmRunner != null;
	}
    /**
	*    @return true if the algorithmRunner's pause flag is true, or false if the runner doesn't exist
	**/
	public boolean isAlgorithmPaused(){
		if(algorithmRunner == null){
			return false;
		}
		return algorithmRunner.isPaused();
	}
    /**
	*    Ends the AlgorithmRunner thread if it existed, which safely terminates the
	*    running algorithm
	**/
	public void terminateAlgorithm(){
		if(algorithmRunner != null){
			algorithmRunner.stop();
			try{
				algorithmRunner.join();
			}
			catch(Exception e){
				System.out.println("Failed to join with runner thread");
			    e.printStackTrace();
			}
			System.out.println("Algorithm terminated!");
			algorithmRunner = null;
		}
	}

}
