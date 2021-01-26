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
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.nio.file.Paths;

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

	private AlgorithmDetailsPanel algorithmDetails;

	public static MainWindow get(){
		return window;
	}

	private Camera camera = new Camera();

	private DetailsPanel detailsPanel;

	private boolean multiSelect;


	private MainWindowState state = MainWindowState.EDIT;

	private MainWindow(){
		cameraDetails = new CameraDetails(camera);
		GridPane viewSection = new GridPane();
		setCenter(viewSection);
		ArrayBasedDijkstra dsp = new ArrayBasedDijkstra(null, null);
		Viewport v = new Viewport(camera, dsp);
		//addViewport(v);
		ArrayList<GraphNode> nodes = new ArrayList<>();
		for(VisualGraphNode n : VisualGraphNode.getNodes()){
			nodes.add(n.getNode());
		}
		//dsp.setStartNode(nodes.get(0));
		//dsp.initialise(nodes);
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
	*    Set the start node of all viewport algorithms to the given start node
	**/
	public void setStartNode(GraphNode newNode){
	    GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			if(((Viewport) n).getAlgorithm() != null){
		        ((ShortestPathAlgorithm) ((Viewport) n).getAlgorithm()).setStartNode(newNode);
			}
		}
		updateAlgorithmDetails();
		updateViewport();
    }

    /**
	*    If the algorithms have a start node, return the start node
	*    This method will be incorrect if a non-shortest path algorithm is implemented
	**/
	public GraphNode getStartNode(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			if(((Viewport) n).getAlgorithm() != null && ((Viewport) n).getAlgorithm() instanceof ShortestPathAlgorithm){
		        return ((ShortestPathAlgorithm) ((Viewport) n).getAlgorithm()).getStartNode();
			}
		}
		return null;
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
			if(k == KeyCode.CONTROL){
				multiSelect = true;
			}
			if(k == KeyCode.ENTER){
				moved = true;
			}
		}
		if(moved){
			updateViewport();
		}
	}

	public void handleSingleInput(KeyCode k){
		if(k == KeyCode.DELETE){
			deleteAllSelected();
		}
		if(k == KeyCode.F){
			if(clickedNodes.size() > 1 && clickedEdges.size() == 0){
				addPathBetweenSelected();
			}
			else{
				createNode();
			}
		}
		if(k == KeyCode.M){
			selectAll();
		}
		if(k == KeyCode.TAB){
			selectNextNode();
		}
	}


	/**
	*    Draws all nodes on all viewports
	**/
	public void updateViewport(){
		VisualGraphNode.updateNodes(camera, 500,500);
		VisualGraphEdge.updateEdges();
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			VisualGraphNode.updateNodes(camera, 500,500);
		    Viewport v = (Viewport) n;
			v.draw();
		}
		updateCameraDetails();
		updateAlgorithmDetails();
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

		updateAlgorithmDetails();
	}

	private void updateCameraDetails(){
		cameraDetails.update();
	}

	protected void updateAlgorithmDetails(){
		if(((ScrollPane) getRight()) == null){
			return;
		}
		VBox rightDetails = (VBox) ((ScrollPane) getRight()).getContent();
		if(rightDetails.getChildren().size() > 1){
    		rightDetails.getChildren().remove(rightDetails.getChildren().get(1));
		}
		if(state == MainWindowState.EDIT){
		    algorithmDetails = new AlgorithmSetupPanel();
		}
		else{
			algorithmDetails = new AlgorithmControlPanel();
		}
		rightDetails.getChildren().add(1, algorithmDetails);
	}


    public void initialiseAlgorithms(){
    	ArrayList<GraphNode> nodes = new ArrayList<>();
    	for(VisualGraphNode n : VisualGraphNode.getNodes()){
    		nodes.add(n.getNode());
    	}
    	GridPane view = (GridPane) getCenter();
    	for(Node n : view.getChildren()){
    		((Viewport) n).getAlgorithm().initialise(nodes);
    	}
		updateAlgorithmDetails();
		//need to update viewport's algorithm details
		updateViewport();
	}

	public void stepAlgorithms(){
		if(state == MainWindowState.RUNNING){
    		GridPane view = (GridPane) getCenter();
    		for(Node n : view.getChildren()){
    			System.out.println(((Viewport) n).getAlgorithm().step());
    		}
			if(areAlgorithmsFinished()){
				state = MainWindowState.EDIT;
				updateAlgorithmDetails();
			}
			updateViewport();
		}
	}

	public void runAlgorithms(){
		state = MainWindowState.RUNNING;
		initialiseAlgorithms();
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			((Viewport) n).getAlgorithm().run();
		}
        state = MainWindowState.EDIT;
	}

	/**
	*    @Return true if all viewports have a valid algorithm
	**/
	public boolean canRunAlgorithms(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			GraphAlgorithm algorithm = ((Viewport) n).getAlgorithm();
		    if(algorithm == null || (algorithm != null && !algorithm.canRun())){
				return false;
			}
		}
		return true;
	}

	public boolean areAlgorithmsFinished(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			if(!((Viewport) n).getAlgorithm().isFinished()){
				return false;
			}
		}
		return true;
	}

    public void displayMessage(String title, String message){
        Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.setHeaderText(title);
		alert.showAndWait();
	}

	public void displayWarningMessage(String title, String message){
		Alert alert = new Alert(AlertType.WARNING, message);
		alert.setHeaderText(title);
		alert.showAndWait();
	}

	public void displayErrorMessage(String title, String message, Exception error){
        Alert alert = new Alert(AlertType.ERROR, message);
		alert.setHeaderText(title);
		alert.showAndWait();
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
		if(state == MainWindowState.EDIT){
    		if(!multiSelect || c == null){
    			clearClickedNodes();
    			clearClickedEdges();
    		}
	    	if(c instanceof VisualGraphNode){
	    		if(!clickedNodes.contains((VisualGraphNode) c)){
    	    		((VisualGraphNode) c).setSelected(true);
    		    	clickedNodes.add((VisualGraphNode) c);
    			}
				else{
					//Add the newest element to the front of the list
					clickedNodes.remove(((VisualGraphNode) c));
					clickedNodes.add(0, ((VisualGraphNode) c));
				}
    		}
    		if(c instanceof VisualGraphEdge){
    			if(!clickedEdges.contains((VisualGraphEdge) c)){
        			((VisualGraphEdge) c).setSelected(true);
    		    	clickedEdges.add((VisualGraphEdge) c);
    			}
				else{
					//ADd the newest element to the front of the list
					clickedEdges.remove((VisualGraphEdge) c);
					clickedEdges.add((VisualGraphEdge) c);
				}
    		}
    		updateDetailsPanel();
    		updateViewport();
		}
	}

    /**
	*    Add the supplied VGC to a clicked component list. This allows the user to edit a clicked component.
	*    @param overrideMultiselect = allows the user to simulate holding hte multiselect key while adding the component
	**/
	public void addClickedComponent(VisualGraphComponent c, boolean overrideMultiselect){
		boolean multiSelectBefore = multiSelect;
		multiSelect = true;
		addClickedComponent(c);
		multiSelect = multiSelectBefore;
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
	public boolean createNode(){
		if(state == MainWindowState.EDIT){
			System.out.println("New node created\n");
		    GraphNode node = new GraphNode(0);
		    node.setName("New node");
		    Vector spawnLocation = camera.getLocation().add(camera.getForwardVector().multiply(10));
		    VisualGraphNode vgn = VisualGraphNode.create(spawnLocation, node);
		    updateViewport();
			return true;
		}
		return false;
	}

	/**
	*    Create an edge between the two supplied nodes, and update the viewport
	**/
	public boolean createEdge(VisualGraphNode nodeA, VisualGraphNode nodeB){
		if(state == MainWindowState.EDIT){
			System.out.println("Edge created between " + nodeA.getNode().getName() + " and " + nodeB.getNode().getName() + "\n");
            GraphEdge edge = nodeA.getNode().addEdge(nodeB.getNode(), false);
            edge.setName("");
            VisualGraphEdge.create(edge);
            updateDetailsPanel();
            updateViewport();
			return true;
		}
		else{
			return false;
		}
	}

	/**
	*    Delete the given node from the model, and update the viewport
	**/
	public boolean deleteNode(VisualGraphNode toDelete){
		if(state == MainWindowState.EDIT){
			System.out.println("Node " + toDelete.getNode().getName() + " deleted\n");
			setStartNode(null);
		    VisualGraphNode.delete(toDelete);
		    clearClickedNodes();
		    clearClickedEdges();
		    updateDetailsPanel();
		    updateViewport();
			return true;
		}
		else{
			return false;
		}
	}

	/**
	*    Delete the given VGE from the model, and update the viewport
    **/
	public boolean deleteEdge(VisualGraphEdge toDelete){
		if(state == MainWindowState.EDIT){
			System.out.println("Edge deleted from " + toDelete.getEdge().nodeA.getName() + " to + " + toDelete.getEdge().nodeB.getName() + "\n");
		    VisualGraphEdge.delete(VisualGraphEdge.getEdge(toDelete.getEdge()));
		    clearClickedEdges();
		    updateDetailsPanel();
		    updateViewport();
			return true;
		}
		else{
			return false;
		}
	}

	/**
	*    Delete the given edge from the model, and update the viewport
	**/
	public boolean deleteEdge(GraphEdge toDelete){
		if(state == MainWindowState.EDIT){
			System.out.println("Edge deleted from " + toDelete.nodeA.getName() + " to + " + toDelete.nodeB.getName() + "\n");
		    VisualGraphEdge.delete(toDelete);
		    clearClickedEdges();
		    updateDetailsPanel();
		    updateViewport();
			return true;
		}
		else{
			return false;
		}
	}

	/**
	*    Delete all selected nodes and edges. This is triggerred by pressing the DEL keys
	**/
	private boolean deleteAllSelected(){
		if(state == MainWindowState.EDIT){
			System.out.println("deleted all selected components (" + clickedNodes.size() + "nodes, " + clickedEdges.size() + "edges)\n");
			setStartNode(null);
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
			return true;
		}
		else{
			return false;
		}
	}

    /**
	*    Create a path between each selected node, if only nodes are selected
	**/
	private boolean addPathBetweenSelected(){
		if(state == MainWindowState.EDIT){
			if(clickedNodes.size() > 1 && clickedEdges.size() == 0){
				int originIndex = 0;
				int destinationIndex = 1;
				while(destinationIndex < clickedNodes.size()){
				    GraphEdge edge = clickedNodes.get(originIndex).getNode().addEdge(clickedNodes.get(destinationIndex).getNode(), false);
					if(edge != null){
					    edge.setName("");
		                VisualGraphEdge.create(edge);
					}
					originIndex++;
					destinationIndex++;
				}
				updateDetailsPanel();
				updateViewport();
				return true;
			}
		}
		return false;
	}

	private void selectAll(){
		boolean multiSelectBefore = multiSelect;
		multiSelect = true;
		if(clickedNodes.size() == VisualGraphNode.getNodes().size() && clickedEdges.size() == VisualGraphEdge.getEdges().size()){
			clearClickedNodes();
			clearClickedEdges();
		}
		else{
			for(VisualGraphNode n : VisualGraphNode.getNodes()){
				addClickedComponent(n);
			}
			for(VisualGraphEdge e : VisualGraphEdge.getEdges()){
				addClickedComponent(e);
			}
		}
		updateDetailsPanel();
		updateViewport();
		multiSelect = multiSelectBefore;
	}

	private void selectNextNode(){
		boolean multiSelectBefore = multiSelect;
		multiSelect = false;
		if(clickedNodes.size() == 1){
            int index = (VisualGraphNode.getNodes().indexOf(clickedNodes.get(0)) + 1) % VisualGraphNode.getNodes().size();
			addClickedComponent(VisualGraphNode.getNodes().get(index));
		}
		else{
			addClickedComponent(VisualGraphNode.getNodes().get(0));
		}
	}

	public void setState(MainWindowState stateIn){
		state = stateIn;
	}

	public MainWindowState getState(){
		return state;
	}

    /**
	*    begins all algorithms, which prevents the graph from being edited
	**/
	public boolean startAlgorithms(){
		if(state != MainWindowState.RUNNING && canRunAlgorithms()){
			System.out.println("Starting algorithms\n");
			state = MainWindowState.RUNNING;
			clearClickedEdges();
			clearClickedNodes();
			initialiseAlgorithms();
			updateAlgorithmDetails();
			return true;
		}
		displayErrorMessage("Unable to run algorithms", "You must set up the algorithms before they can run", null);
		return false;
	}

    /**
	*    Begins and executes all algorithms
	**/
	public boolean executeAlgorithms(){
		if(canRunAlgorithms()){
			if(state != MainWindowState.RUNNING){
			    startAlgorithms();
			}
			runAlgorithms();
			updateAlgorithmDetails();
			updateViewport();
			return true;
		}
		displayErrorMessage("Unable to execute algorithms", "You must set up the algorithms before they can run", null);
		return false;
	}

    /**
	*    Ends the execution of all algorithms
	**/
	public boolean terminateAlgorithms(){
		if(state == MainWindowState.RUNNING){
			System.out.println("Algorithms terminated!\n\n");
			GridPane view = (GridPane) getCenter();
			for(Node n : view.getChildren()){
				((Viewport) n).getAlgorithm().terminate();
			}
			updateAlgorithmDetails();
			updateViewport();
			state = MainWindowState.EDIT;
			return true;
		}
		displayErrorMessage("Unable to terminate algorithms", "You cannot terminate algorithms that aren't running", null);
		return false;
	}


	public void saveGraph(){
        FileChooser dialog = new FileChooser();
		String initialPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/savedGraphs";
		dialog.setInitialDirectory(new File(initialPath));
		dialog.setInitialFileName("newGraph.graph");
		File saveFile = dialog.showSaveDialog(getScene().getWindow());
		if(saveFile != null){
			try{
    			FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
    			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    			objectOutputStream.writeObject(clickedNodes);
    			objectOutputStream.close();
    			fileOutputStream.close();
				displayMessage("Save successful", "Successfully saved graph to " + saveFile);
				System.out.println("Successfully saved graph to " + saveFile + "\n");
			}
			catch(Exception e){
				displayErrorMessage("Unable to save graph", "", e);
				System.out.println("Unable to save\n");
			}
		}
		else{
			System.out.println("No file selected to save to, aborting save\n");
		}
	}

	public void loadGraph(){
		FileChooser dialog = new FileChooser();
		//Restrict file search type
		FileChooser.ExtensionFilter extensions = new FileChooser.ExtensionFilter("GRAPH files (*.graph)", "*.graph");
        dialog.getExtensionFilters().add(extensions);
		File selectedFile = dialog.showOpenDialog(getScene().getWindow());
		if(selectedFile != null){
			try{
				System.out.println("Begin load operation");
                FileInputStream fileStream = new FileInputStream(selectedFile);
				ObjectInputStream objectStream = new ObjectInputStream(fileStream);
				ArrayList<VisualGraphNode> nodes = (ArrayList<VisualGraphNode>) objectStream.readObject();
				objectStream.close();
				fileStream.close();
				System.out.println("    -Successful read. Creating nodes");
				ArrayList<GraphNode> createdNodes = new ArrayList<>();
				//Create nodes in front of the camera, instead of their original position
				Vector basePosition = camera.getLocation().add(camera.getForwardVector().multiply(10));
				for(VisualGraphNode n : nodes){
					GraphNode node = n.getNode();
					createdNodes.add(n.getNode());
				    VisualGraphNode vgn = VisualGraphNode.create(n.getLocation().subtract(nodes.get(0).getLocation()).add(basePosition), node);
				}
				System.out.println("    -All nodes generated. Creating edges");
				//The serialised object is an array list of only the selected nodes, however by
				//definition of serialised, this will essentially include all reachable nodes through references
				//Only create edges for the nodes that were explicitly selected by the user. Remove the redundant edges
				for(GraphNode n : createdNodes){
					ArrayList<GraphEdge> invalidEdges = new ArrayList<>();
					for(GraphEdge e : n.getEdges()){
						if(createdNodes.contains(e.nodeB)){
			                VisualGraphEdge.create(e);
						}
						else{
							invalidEdges.add(e);
						}
					}
					for(GraphEdge e : invalidEdges){
						n.removeEdge(e, false);
					}
				}
				System.out.println("    -All edges generated");
				updateDetailsPanel();
				updateViewport();
				System.out.println("Successfully loaded " + selectedFile + "!");
			}
			catch(Exception e){
				displayErrorMessage("Failed to load graph", "Unable to load " + selectedFile + ". The file may be corrupted", e);
				System.out.println("Unable to load " + selectedFile + ". The file may be corrupted");
				//e.printStackTrace();
			}
		}
		else{
			System.out.println("No file selected to load, aborting load");
		}
		System.out.println("\n");
	}


}
