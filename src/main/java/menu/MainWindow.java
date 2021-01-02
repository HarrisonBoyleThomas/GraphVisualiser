package menu;

import viewport.Viewport;
import viewport.Camera;
import viewport.VisualGraphComponent;
import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import model.algorithm.*;
import model.GraphNode;
import model.GraphEdge;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import java.util.ArrayList;
import java.util.List;


public class MainWindow extends BorderPane{
	
	private static MainWindow window = new MainWindow();
	
	private ArrayList<VisualGraphNode> clickedNodes = new ArrayList<>();
	
	private ArrayList<VisualGraphEdge> clickedEdges = new ArrayList<>();
	
	public static MainWindow get(){
		return window;
	}
	
	private Camera camera = new Camera();
	
	private DetailsPanel detailsPanel;
	
	private boolean multiSelect;
	
	private MainWindow(){
		GridPane viewSection = new GridPane();
		setCenter(viewSection);
		DijkstraShortestPath dsp = new DijkstraShortestPath(null, null);
		Viewport v = new Viewport(camera, new DijkstraShortestPath(null, null));
		createCube();
		createNode();
		//addViewport(v);
		ArrayList<GraphNode> nodes = new ArrayList<>();
		for(VisualGraphNode n : VisualGraphNode.getNodes()){
			nodes.add(n.getNode());
		}
		dsp.setStartNode(nodes.get(0));
		dsp.initialise(nodes);
		v = new Viewport(camera, dsp);
		addViewport(v);
		updateDetailsPanel();
		
	}
	
	public boolean addViewport(Viewport viewport){
		int noOfViewports = ((GridPane) getCenter()).getChildren().size();
		GridPane view = (GridPane) getCenter();
		if(noOfViewports == 0){
		    view.add(viewport, 0, 0);
		}
		else if(noOfViewports == 1){
			view.add(viewport, 0, 1);
			System.out.println("second viewport created");
		}
		else if(noOfViewports == 2){
			view.add(viewport, 1, 0);
		}
		else if(noOfViewports == 3){
			view.add(viewport, 1, 1);
		}
		else{
			return false;
		}
		return true;
	}
	
	
	public Camera getCamera(){
		return camera;
	}
	
	public void handleMovementInput(ArrayList<KeyCode> keys){
		boolean moved = false;
		for(KeyCode k : keys){
	    	if(k == KeyCode.UP){
		    	camera.pitchRelative(-0.1);
				moved = true;
		    }
			if(k == KeyCode.DOWN){
				camera.pitchRelative(0.1);
				moved = true;
			}
			if(k == KeyCode.LEFT){
				camera.yawRelative(-0.1);
				moved = true;
			}
			if(k == KeyCode.RIGHT){
			    camera.yawRelative(0.1);
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
			if(k == KeyCode.ENTER){
				moved = true;
			}
			if(k == KeyCode.CONTROL){
				multiSelect = true;
			}
			else{
				multiSelect = false;
			}
		}
		if(moved){
			updateViewport();
		}
	}
	
	protected void updateViewport(){
		VisualGraphNode.updateNodes(camera, 500,500);
		VisualGraphEdge.updateEdges();
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			VisualGraphNode.updateNodes(camera, 500,500);
		    Viewport v = (Viewport) n;
			v.draw();
		}
	}
	
	public void stepAlgorithms(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			((Viewport) n).getAlgorithm().step();
		}
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
	
	
	
	
	public void addClickedComponent(VisualGraphComponent c){
		if(!multiSelect){
			clickedNodes.clear();
			clickedEdges.clear();
		}
		if(c instanceof VisualGraphNode){
			System.out.println("vgn clicked");
			clickedNodes.add((VisualGraphNode) c);
		}
		if(c instanceof VisualGraphEdge){
			System.out.println("vge vlicked");
			clickedEdges.add((VisualGraphEdge) c);
		}
		updateDetailsPanel();
	}
	
	private void updateDetailsPanel(){
		if(clickedNodes.size() > 0){
			if(clickedNodes.size() == 1){
				setLeft(new VisualGraphNodeDetails(clickedNodes.get(0)));
			}
		}
		else{
			setLeft(new EmptyDetails());
		}
	}
	
	public void createNode(){
		GraphNode node = new GraphNode(0);
		node.setName("New node");
		Vector spawnLocation = camera.getLocation().add(camera.getForwardVector().multiply(10));
		VisualGraphNode vgn = VisualGraphNode.create(spawnLocation, node);
		updateViewport();
	}
	
	
}