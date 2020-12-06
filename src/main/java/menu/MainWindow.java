package menu;

import viewport.Viewport;
import viewport.Camera;
import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import model.algorithm.*;
import model.GraphNode;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;


public class MainWindow extends BorderPane{
	
	private Camera camera = new Camera();
	
	public MainWindow(){
		GridPane viewSection = new GridPane();
		setCenter(viewSection);
		DijkstraShortestPath dsp = new DijkstraShortestPath(null, null);
		Viewport v = new Viewport(camera, new DijkstraShortestPath(null, null));
		v.createCube();
		//addViewport(v);
		ArrayList<GraphNode> nodes = new ArrayList<>();
		for(VisualGraphNode n : VisualGraphNode.getNodes()){
			nodes.add(n.getNode());
		}
		dsp.setStartNode(nodes.get(0));
		dsp.initialise(nodes);
		v = new Viewport(camera, dsp);
		addViewport(v);
		
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
		}
		if(moved){
			VisualGraphNode.updateNodes(camera, 500,500);
		    VisualGraphEdge.updateEdges();
			GridPane view = (GridPane) getCenter();
			for(Node n : view.getChildren()){
				VisualGraphNode.updateNodes(camera, 500,500);
			    Viewport v = (Viewport) n;
				v.draw();
			}
		}
	}
	
	public void stepAlgorithms(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			((Viewport) n).getAlgorithm().step();
		}
	}
}