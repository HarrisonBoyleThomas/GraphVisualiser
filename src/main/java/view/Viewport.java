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
		setOnMouseClicked(e -> {requestFocus(); MainWindow.get().addClickedComponent(null);});

		//set the clipping rectangle to hide VGCs that are off screen
		Rectangle clip = new Rectangle(width, height);
        setClip(clip);
		addDragAndDropEvents();
	}

	private void addDragAndDropEvents(){
		System.out.println("Drop events added");

		setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
                /* data is dragged over the target */
                System.out.println("onDragOver");

                e.acceptTransferModes(TransferMode.MOVE);
				System.out.println(e.getTransferMode());

                e.consume();
            }
        });

		setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */

                e.consume();
            }
        });

		setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
                e.consume();
            }
        });

		setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent e) {
                /* data dropped */
                System.out.println("onDragDropped");
				Object o = e.getDragboard().getContent(VisualGraphNode.FORMAT);
				System.out.println(o);
				if(o instanceof VisualGraphNode){
					VisualGraphNode vgn = (VisualGraphNode) o;
					Point mouse =java.awt.MouseInfo.getPointerInfo().getLocation();
					Vector mousePosition = new Vector(mouse.x, mouse.y, 0);
					if(MainWindow.get().getClickedNodes().size() > 0){
    					Vector difference = mousePosition.subtract(MainWindow.get().getClickedNodes().get(0).getRenderLocation());
						System.out.println(mousePosition);
    					double xChange = difference.x / width;
    					double yChange = difference.y / height;
    					System.out.println("xchange = " + xChange);
    					double deltaYaw = xChange*180;
    					double deltaPitch = yChange*180;
    					for(VisualGraphNode node : MainWindow.get().getClickedNodes()){
    		    			System.out.println("ex: " + (VisualGraphNode.getNodes().get(0).getNode() == vgn.getNode()));
    			    		System.out.println(vgn.getNode());
    				    	node.setLocation(Functions.rotateVector(camera.getLocation(), node.getLocation(), new Rotator(0, deltaPitch, deltaYaw)));
	    				}
					}
					e.setDropCompleted(true);
				    MainWindow.get().updateViewport();
				}
				else{
					e.setDropCompleted(false);
				}
				//clear the clipboard
				ClipboardContent clipboard = new ClipboardContent();
				e.getDragboard().setContent(clipboard);
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
