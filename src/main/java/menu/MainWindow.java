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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
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
	
	private CameraDetails cameraDetails;
	
	public static MainWindow get(){
		return window;
	}
	
	private Camera camera = new Camera();
	
	private DetailsPanel detailsPanel;
	
	private boolean multiSelect;
	
	private MainWindow(){
		cameraDetails = new CameraDetails(camera);
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
		
		initialiseRightDetailsPanel();
		setTop(new MenuHeader());
		
	}
	
	public boolean createViewport(){
		Viewport v = new Viewport(camera, null);
		return addViewport(v);
	}
		
	
	/**
	*     Add a viewport to the center of the window
	**/
	private boolean addViewport(Viewport viewport){
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
	
	/**
	*    @Return a copy of all clicked nodes
	**/
	public ArrayList<VisualGraphNode> getClickedNodes(){
		return new ArrayList<VisualGraphNode>(clickedNodes);
	}
	
	/**
	*    @Return a copy of all clicked e3dges
	**/
	public ArrayList<VisualGraphEdge> getClickedEdges(){
		return new ArrayList<VisualGraphEdge>(clickedEdges);
	}
	
	/**
	*    @Return the camera
	**/
	public Camera getCamera(){
		return camera;
	}
	
	/**
	*    Handle all movement input cpntrols that the user can enter
	**/
	public void handleMovementInput(ArrayList<KeyCode> keys){
		boolean moved = false;
		multiSelect = false;
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
			if(k == KeyCode.DELETE){
				deleteAllSelected();
			}
		}
		if(moved){
			updateViewport();
		}
	}
	
	
	/**
	*    Draws all nodes on all viewports
	**/
	protected void updateViewport(){
		VisualGraphNode.updateNodes(camera, 500,500);
		VisualGraphEdge.updateEdges();
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			VisualGraphNode.updateNodes(camera, 500,500);
		    Viewport v = (Viewport) n;
			v.draw();
		}
		updateCameraDetails();
	}
	
	/**
	*    Initialise the components of the right details panel
	**/
	private void initialiseRightDetailsPanel(){
		ScrollPane sp = new ScrollPane();
		VBox rightDetails = new VBox();
		rightDetails.setMaxWidth(300);
		rightDetails.setMinWidth(300);
		sp.setContent(rightDetails);
		rightDetails.getChildren().add(cameraDetails);
		
		setRight(sp);
	}
	
	private void updateCameraDetails(){
		cameraDetails.update();
	}
	
	public void stepAlgorithms(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			((Viewport) n).getAlgorithm().step();
		}
	}
	
	/**
	*    @Return true if all viewports have a valid algorithm
	**/
	public boolean canRunAlgorithms(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
		    if(((Viewport) n).getAlgorithm() == null){
				return false;
			}
		}
		return true;
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
	
	
	/**
	*    Set all clicked nodes to unselected, and cleat the clicked node list
	**/
	private void clearClickedNodes(){
		for(VisualGraphNode n : clickedNodes){
			n.setSelected(false);
		}
		clickedNodes.clear();
	}
	
	/**
	*    Set all clicked edges to unselected, and clear the clicked edge list
	**/
	private void clearClickedEdges(){
		for(VisualGraphEdge e : clickedEdges){
			e.setSelected(false);
		}
		clickedEdges.clear();
	}
	
	/**
	*    Add the supplied VGC to a clicked component list. This allows the user to edit a clicked component
	**/
	public void addClickedComponent(VisualGraphComponent c){
		if(!multiSelect || c == null){
			clearClickedNodes();
			clearClickedEdges();
		}
		System.out.println(multiSelect);
		if(c instanceof VisualGraphNode){
			if(!clickedNodes.contains((VisualGraphNode) c)){
	    		((VisualGraphNode) c).setSelected(true);
		    	clickedNodes.add((VisualGraphNode) c);
			}
		}
		if(c instanceof VisualGraphEdge){
			if(!clickedEdges.contains((VisualGraphEdge) c)){
    			((VisualGraphEdge) c).setSelected(true);
	    		System.out.println("vge vlicked");
		    	clickedEdges.add((VisualGraphEdge) c);
			}
		}
		updateDetailsPanel();
		updateViewport();
	}
	
	/**
	*    Update the details panel to allow operations on a particular combination of selected
	*    graph components
	**/
	private void updateDetailsPanel(){
		if(clickedNodes.size() > 0){
			if(clickedEdges.size() > 0){
				setLeft(new MultipleSelectedDetails());
			}
			else{
    			if(clickedNodes.size() == 1){
    				setLeft(new VisualGraphNodeDetails(clickedNodes.get(0)));
    			}
    			else if(clickedNodes.size() == 2){
    				setLeft(new DoubleNodeSelectedDetails(clickedNodes.get(0), clickedNodes.get(1)));
     			}
    			else{
	    			setLeft(new MultipleSelectedDetails());
		    	}
			}
		}
		else{
			if(clickedEdges.size() > 0){
			    if(clickedEdges.size() == 1){
				    setLeft(new VisualGraphEdgeDetails(clickedEdges.get(0)));
			    }
				else{
					setLeft(new MultipleSelectedDetails());
				}
				
		    }
	    	else{
				setLeft(new EmptyDetails());
		    }
		}
	}
	
	/**
	*    Create a node 10 units infront of the camera, and update the viewport to render the new node
	**/
	public void createNode(){
		GraphNode node = new GraphNode(0);
		node.setName("New node");
		Vector spawnLocation = camera.getLocation().add(camera.getForwardVector().multiply(10));
		VisualGraphNode vgn = VisualGraphNode.create(spawnLocation, node);
		updateViewport();
	}
	
	/**
	*    Create an edge between the two supplied nodes, and update the viewport
	**/
	public void createEdge(VisualGraphNode nodeA, VisualGraphNode nodeB){
        GraphEdge edge = nodeA.getNode().addEdge(nodeB.getNode(), false);
        edge.setName("New edge");
        VisualGraphEdge.create(edge);
        updateDetailsPanel();
        updateViewport();		
	}
	
	/**
	*    Delete the given node from the model, and update the viewport
	**/
	public void deleteNode(VisualGraphNode toDelete){
		VisualGraphNode.delete(toDelete);
		clearClickedNodes();
		clearClickedEdges();
		updateDetailsPanel();
		updateViewport();
	}
	
	/**
	*    Delete the given VGE from the model, and update the viewport
    **/	
	public void deleteEdge(VisualGraphEdge toDelete){
		VisualGraphEdge.delete(VisualGraphEdge.getEdge(toDelete.getEdge()));
		clearClickedEdges();
		updateDetailsPanel();
		updateViewport();
	}
	
	/**
	*    Delete the given edge from the model, and update the viewport
	**/
	public void deleteEdge(GraphEdge toDelete){
		VisualGraphEdge.delete(toDelete);
		clearClickedEdges();
		updateDetailsPanel();
		updateViewport();
	}
	
	/**
	*    Delete all selected nodes and edges. This is triggerred by pressing the DEL keys
	**/
	private void deleteAllSelected(){
		for(VisualGraphNode n : clickedNodes){
			VisualGraphNode.delete(n);
		}
		for(VisualGraphEdge e : clickedEdges){
			VisualGraphEdge.delete(e);
		}
		clearClickedNodes();
		clearClickedEdges();
		updateViewport();
		updateDetailsPanel();
	}
	
	
}