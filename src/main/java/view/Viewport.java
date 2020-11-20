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

public class Viewport extends Application{
	private Camera camera;
	private Pane sp;
	
	private int width = 500;
	private int height = 500;
	
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
			root.getChildren().add(edge.getIcon());
			edge.getIcon().setLayoutX((int) edge.getRenderLocation().x);
			edge.getIcon().setLayoutY((int) edge.getRenderLocation().y);
		}
		for(VisualGraphNode node : nodes){
			root.getChildren().add(node.getIcon());
			node.getIcon().setLayoutX((int) node.getRenderLocation().x);
			node.getIcon().setLayoutY((int) node.getRenderLocation().y);
		}
		sp.getChildren().add(root);
	}
		
	public void start(Stage primaryStage) throws Exception{
		camera = new Camera();
		
		sp = new Pane();
		
		GraphNode node = new GraphNode(5);
		node.setName("A");
		VisualGraphNode.create(new Vector(10, 0, 0), node);
		
		GraphNode node2 = new GraphNode(4);
		GraphEdge edge = node.addEdge(node2, false);
		node2.setName("B");
		VisualGraphNode.create(new Vector(10, 0, -5), node2);
		VisualGraphEdge.create(edge);
		
		GraphNode node3 = new GraphNode(4);
		//VisualGraphNode.create(new Vector(10, 10, 5), node3);
		
		draw();
		
		
		
		
		primaryStage.setTitle("Viewport");
		Scene scene = new Scene(sp, width, height);
		addEvents(scene);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void addEvents(Scene scene){
		//Pitch sensor
		scene.setOnKeyPressed(new EventHandler<KeyEvent> (){
			public void handle(KeyEvent e){
				if(e.getCode() == KeyCode.UP){
					camera.pitch(-1.0);
					draw();
				}
				if(e.getCode() == KeyCode.DOWN){
					camera.pitch(1.0);
					draw();
				}
				if(e.getCode() == KeyCode.LEFT){
					camera.yaw(-1.0);
					draw();
				}
				if(e.getCode() == KeyCode.RIGHT){
					camera.yaw(1.0);
					draw();
				}
				if(e.getCode() == KeyCode.W){
					camera.moveForward(1.0);
					draw();
				}
				if(e.getCode() == KeyCode.S){
					camera.moveForward(-1.0);
					draw();
				}
				if(e.getCode() == KeyCode.A){
					camera.moveSideways(1.0);
					draw();
				}
				if(e.getCode() == KeyCode.D){
					camera.moveSideways(-1.0);
					draw();
				}
			}
		});
	}
}