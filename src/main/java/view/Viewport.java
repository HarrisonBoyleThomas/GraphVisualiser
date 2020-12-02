package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;


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

public class Viewport extends Application{
	private Camera camera;
	private Pane sp;
	
	private int width = 500;
	private int height = 500;
	
	private ArrayList<KeyCode> heldDownKeys = new ArrayList<>();
	
	private ViewportDetails viewportDetails = new ViewportDetails();
	
	public static void main(String[] args){
		launch(args);
	}
	
	public void draw(){
		sp.getChildren().clear();
		
		VisualGraphNode.updateNodes(camera, width, height);
		VisualGraphEdge.updateEdges();
		
		ArrayList<VisualGraphNode> nodes = VisualGraphNode.getNodes();
		ArrayList<VisualGraphEdge> edges = VisualGraphEdge.getEdges();
		
		Group root = new Group();
		
		for(VisualGraphEdge edge : edges){
			if(camera.isInFront(VisualGraphNode.getNode(edge.getEdge().nodeA))  || camera.isInFront(VisualGraphNode.getNode(edge.getEdge().nodeB))){
	    		root.getChildren().add(edge.getIcon());
	    		edge.getIcon().setLayoutX((int) edge.getRenderLocation().x);
	    		edge.getIcon().setLayoutY((int) edge.getRenderLocation().y);
			}
		}
		for(VisualGraphNode node : nodes){
			root.getChildren().add(node.getIcon());
			node.getIcon().setLayoutX((int) node.getRenderLocation().x);
			node.getIcon().setLayoutY((int) node.getRenderLocation().y);
		}
		sp.getChildren().add(root);
		
		viewportDetails.update(this);
		sp.getChildren().add(viewportDetails);
	}
	
	public Camera getCamera(){
		return camera;
	}
	
	/**
	*    Creates a cube graph
	**/
	private void createCube(){
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
			VisualGraphEdge.create(e);
		}
	}
		
	
	public void start(Stage primaryStage) throws Exception{
		camera = new Camera();
		
		sp = new Pane();
		
		createCube();
		draw();
		
		
		
		
		primaryStage.setTitle("Viewport");
		Scene scene = new Scene(sp, width, height);
		addEvents(scene);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		//Mainloop
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				handleMovementInput();
			    try{
				    Thread.sleep(1);
			    }
			    catch(InterruptedException  error){
			        
			    }
			}
		}.start();
		
	}
	
	private void handleMovementInput(){
		boolean moved = false;
		for(KeyCode k : heldDownKeys){
	    	if(k == KeyCode.UP){
		    	camera.pitch(-0.1);
				moved = true;
		    }
			if(k == KeyCode.DOWN){
				camera.pitch(0.1);
				moved = true;
			}
			if(k == KeyCode.LEFT){
				camera.yaw(-0.1);
				moved = true;
			}
			if(k == KeyCode.RIGHT){
			    camera.yaw(0.1);
				moved = true;
	    	}
			if(k == KeyCode.W){
				camera.moveForward(0.1);
				moved = true;
			}
			if(k == KeyCode.S){
				camera.moveForward(-0.1);
				moved = true;
			}
			if(k == KeyCode.A){
				camera.moveSideways(0.1);
				moved = true;
			}
			if(k == KeyCode.D){
				camera.moveSideways(-0.1);
				moved = true;
			}
			if(k == KeyCode.Q){
				camera.moveUpwards(-0.1);
				moved = true;
			}
			if(k == KeyCode.Z){
				camera.moveUpwards(0.1);
				moved = true;
			}
			if(k == KeyCode.COMMA){
				camera.roll(-0.1);
				moved = true;
			}
			if(k == KeyCode.PERIOD){
				camera.roll(0.1);
				moved = true;
			}
		}
		if(moved){
			draw();
		}
	}
		
	private void addEvents(Scene scene){
		//Pitch sensor
		scene.setOnKeyPressed(new EventHandler<KeyEvent> (){
			public void handle(KeyEvent e){
				if(!heldDownKeys.contains(e.getCode())){
					heldDownKeys.add(e.getCode());
				}
			}
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent> () {
			public void handle(KeyEvent e){
				heldDownKeys.remove(e.getCode());
			}
		});
	}
}