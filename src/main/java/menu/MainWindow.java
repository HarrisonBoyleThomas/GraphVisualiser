package menu;

import viewport.Viewport;
import viewport.Camera;
import viewport.VisualGraphComponent;
import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import model.algorithm.*;
import model.GraphNode;
import model.GraphEdge;
import threads.AlgorithmRunner;
import data.Data;

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

/**
*    The MainWindow provides the interface for all the components of the app
*    The MainWindow itself is just a simple border pane, but it controls the
*    communication between all components it contains
*    The class is implemented as a singleton, to prevent potential risky concurrent
*    modification of the app's state
*    @author Harrison Boyle-Thomas
*    @date 31/01/21
**/
public class MainWindow extends BorderPane{

    //single instance
	private static MainWindow window = new MainWindow();

    //list of clicked nodes. The most recently clicked node is at the front of the list
	private ArrayList<VisualGraphNode> clickedNodes = new ArrayList<>();

    //List of clicked edges. The most recently clicked edge is at the front of the list
	private ArrayList<VisualGraphEdge> clickedEdges = new ArrayList<>();

    //copy and paste implemented through a duplicate of the clickedNodeArray, otherwise
	//drag and drop overrides a shared clipboard
	private ArrayList<VisualGraphNode> copiedNodes = new ArrayList<>();

    //Display the details of the camera
	private CameraDetails cameraDetails;

    //The algorithm setup/control panel
	private AlgorithmDetailsPanel algorithmDetails;

    /**
	*    @return the single created instance
	**/
	public static MainWindow get(){
		return window;
	}
    //A single camera is shared between viewports
	private Camera camera = new Camera();
    //The current details panel that changes based on clicked component combinations
	private DetailsPanel detailsPanel;

    //If true, then the user may select multiple components at once
	private boolean multiSelect;
    //The current teme of the app
	private ThemeState theme;
    //The state of the mainWindow
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
		initialiseTheme();

	}

    /**
	*    allows an external source to create a viewport
	*    @return true if successful
	**/
	public boolean createViewport(){
		if(state == MainWindowState.EDIT){
		    Viewport v = new Viewport(camera, null);
		    return addViewport(v);
		}
		else{
			displayWarningMessage("Unable to create viewport", "Viewports cannot be created while algorithms are running");
			return false;
		}
	}

    /**
	*    @param v the viewport to delete
	**/
	public void deleteViewport(Viewport v){
		((GridPane) getCenter()).getChildren().remove(v);
	}


	/**
	*    Add a viewport to the center of the window
	*    @param viewport the viewport to add to the window
	*    @return true if successful
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
	*    @param newNode the node to set as start node for shortest path algorithms
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
	*    @return a copy of all clicked nodes
	**/
	public ArrayList<VisualGraphNode> getClickedNodes(){
		return new ArrayList<VisualGraphNode>(clickedNodes);
	}

	/**
	*    @return a copy of all clicked edges
	**/
	public ArrayList<VisualGraphEdge> getClickedEdges(){
		return new ArrayList<VisualGraphEdge>(clickedEdges);
	}

	/**
	*    @return the camera
	**/
	public Camera getCamera(){
		return camera;
	}

    //KeyBoard input section

	/**
	*    Handle all movement input controls that the user can enter
	*    @param keys the keys that have been pressed by the user
	*
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

    /**
	*    Handle "semi-automatic" key presses. This would only apply the input once, even
	*    if the key is held down
	*    @param k the key that was pressed
	**/
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
		if(multiSelect){
			//copy
			if(k == KeyCode.C){
                copySelected();
			}
			//paste
			if(k == KeyCode.V){
                pasteSelected();
			}
		}
	}

    //Window component refresh section

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

    /**
	*    Update the cameraDetails box in the right panel, to display correct
	*    data about the camera
	**/
	private void updateCameraDetails(){
		cameraDetails.update();
	}

    /**
	*    Refresh the algorithmDetails panel, so that it displays valid operations
	**/
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

	/**
	*    Update the details panel to allow valid operations on a particular combination of selected
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

    //ALert message section

    /**
	*    Display an alert message with given title and message
	*    @param title the title of the message
	*    @param message the message of the alert
	**/
    public void displayMessage(String title, String message){
        Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.getDialogPane().getStylesheets().add(getStylesheets().get(0));
		alert.setHeaderText(title);
		alert.showAndWait();
	}

    /**
	*    Display a warning message with given title and message
	*    @param title the title of the message
	*    @param message the message of the alert
	**/
	public void displayWarningMessage(String title, String message){
		Alert alert = new Alert(AlertType.WARNING, message);
		alert.getDialogPane().getStylesheets().add(getStylesheets().get(0));
		alert.setHeaderText(title);
		alert.showAndWait();
	}

    /**
	*    Display an error message to the user
	*    @param title the title of the message
	*    @param message the message of the alert
	*    @param error the error that caused the message(skipped if null)
	**/
	public void displayErrorMessage(String title, String message, Exception error){
        Alert alert = new Alert(AlertType.ERROR, message);
		alert.getDialogPane().getStylesheets().add(getStylesheets().get(0));
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

    //Graph editing controls section

	/**
	*    Set all clicked nodes to unselected, and clear the clicked node list
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
	*    @param c the clicked component. Can be null to simulate clicking on nothing
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
	*    Add the supplied VGC to a clicked component list. This allows the user to edit a clicked component without
	*    deselecting other components
	*    @param c the component that was clicked. Can be null
	*    @param overrideMultiselect allows the user to simulate holding hte multiselect key while adding the component
	**/
	public void addClickedComponent(VisualGraphComponent c, boolean overrideMultiselect){
		boolean multiSelectBefore = multiSelect;
		multiSelect = true;
		addClickedComponent(c);
		multiSelect = multiSelectBefore;
	}


	/**
	*    Create a node 10 units infront of the camera, and update the viewport to render the new node
	*    Nodes can only be created if the mainWindow is in EDIT Mode
	*    @return true if a node was created
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
	*    edges can only be added if the mainWindow is in EDIT mode
	*    @param nodeA the origin of the edge
	*    @param nodeB the end point of the edge
	*    return true if an edge was created
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
	*    Nodes can only be deleted if the mainWindow is in EDIT mode
	*    @param toDelete the node to delete
	*    @return true if successful
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
	*    Edges can only be deleted if the mainWindow is in EDIT mode
	*    @param toDelete the edge to delete
	*    @return true if successful
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
	*    Edges can only be deleted if the mainWIndow is in EDIT mode
	*    @param toDelete the edge to delete
	*    @return true if successful
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
	*    Delete all selected nodes and edges. This is triggerred by pressing the DEL key
	*    Components can only be deleted if the mainWindow is in EDIT mode
	*    @return true if successful
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
	*    For example, if three nodes A B C were selected in that order, then a path
	*    is created like: A->B->C
	*    Duplicate nodes cannot appear in the clicked list, so cycles are not possible
	*    Only available if the mainWindow is in EDIT mode
	*    @return true if successful
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

    /**
	*    Select all nodes and edges that exist within the application
	**/
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

    /**
	*    Shuffle through the clickedNode list, so that the next appearing node is at the front of the
	*    clickedNode list
	**/
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

	//Window state section

    /**
	*    Set the mainWindow state
	*    @param stateIn the new state
	**/
	public void setState(MainWindowState stateIn){
		state = stateIn;
	}

    /**
	*    @return the current state of the mainWindow
	**/
	public MainWindowState getState(){
		return state;
	}

	//Algorithm controls section

    /**
	*    begins all algorithms, which prevents the graph from being edited
	*    @return true if successful
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
	*    @return true if successful
	**/
	public boolean executeAlgorithms(){
		if(canRunAlgorithms()){
			if(state != MainWindowState.RUNNING){
			    startAlgorithms();
			}
			state = MainWindowState.RUNNING;
			initialiseAlgorithms();
			GridPane view = (GridPane) getCenter();
			for(Node n : view.getChildren()){
				((Viewport) n).executeAlgorithm();
				//((Viewport) n).getAlgorithm().run();
			}
			updateAlgorithmDetails();
			updateViewport();
			return true;
		}
		displayErrorMessage("Unable to execute algorithms", "You must set up the algorithms before they can run", null);
		return false;
	}

	public void updateAlgorithmSpeed(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			((Viewport) n).updateAlgorithmSpeed();
		}
	}

    /**
	*    Ends the execution of all algorithms
	*    @return true if successful
	**/
	public boolean terminateAlgorithms(){
		if(state == MainWindowState.RUNNING){
			System.out.println("Algorithms terminated!\n\n");
			GridPane view = (GridPane) getCenter();
			for(Node n : view.getChildren()){
				((Viewport) n).terminateAlgorithm();
				//((Viewport) n).getAlgorithm().terminate();
			}
			updateViewport();
			state = MainWindowState.EDIT;
			updateAlgorithmDetails();
			return true;
		}
		displayErrorMessage("Unable to terminate algorithms", "You cannot terminate algorithms that aren't running", null);
		return false;
	}

	public boolean areAlgorithmsExecuting(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			if(((Viewport) n).isExecutingAlgorithm()){
				return true;
			}
		}
		return false;
	}

	public boolean areAlgorithmsPaused(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			if(((Viewport) n).isAlgorithmPaused()){
				return true;
			}
		}
		return false;
	}

	public boolean pauseAlgorithms(){
		if(state == MainWindowState.RUNNING){
			boolean paused = false;
			GridPane view = (GridPane) getCenter();
			for(Node n : view.getChildren()){
				paused = ((Viewport) n).pauseAlgorithm();
			}
			if(paused){
				System.out.println("Algorithms paused!");
			}
			else{
				System.out.println("Algorithms unpaused!");
			}
			updateAlgorithmDetails();
			return true;
		}
		return false;
	}

	/**
	*    Start all algorithms in each viewport
	**/
    public void initialiseAlgorithms(){
    	ArrayList<GraphNode> nodes = new ArrayList<>();
    	for(VisualGraphNode n : VisualGraphNode.getNodes()){
    		nodes.add(n.getNode());
    	}
    	GridPane view = (GridPane) getCenter();
    	for(Node n : view.getChildren()){
    		((Viewport) n).getAlgorithm().initialise(nodes);
			((Viewport) n).getAlgorithm().start();
    	}
		updateAlgorithmDetails();
		//need to update viewport's algorithm details
		updateViewport();
	}

    /**
	*    Advance all algorithms one step
	**/
	public void stepAlgorithms(){
		if(state == MainWindowState.RUNNING){
    		GridPane view = (GridPane) getCenter();
    		for(Node n : view.getChildren()){
    			System.out.println(((Viewport) n).getAlgorithm().step());
    		}
			if(areAlgorithmsFinished()){
				state = MainWindowState.EDIT;
				updateAlgorithmDetails();
				for(Node n : view.getChildren()){
					((Viewport) n).getAlgorithm().stop();
				}
			}
			updateViewport();
		}
	}


	/**
	*    @return true if all viewports have a valid algorithm
	**/
	public boolean canRunAlgorithms(){
		GridPane view = (GridPane) getCenter();
		if(view.getChildren().size() == 0){
			return false;
		}
		for(Node n : view.getChildren()){
			GraphAlgorithm algorithm = ((Viewport) n).getAlgorithm();
		    if(algorithm == null || (algorithm != null && !algorithm.canRun())){
				return false;
			}
		}
		return true;
	}

    /**
	*    @return true if all algorithms are marked as finished
	**/
	public boolean areAlgorithmsFinished(){
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			if(!((Viewport) n).getAlgorithm().isFinished()){
				return false;
			}
		}
		return true;
	}

	public MainWindowState updateWindowStatus(){
	    if(areAlgorithmsFinished()){
			if(state == MainWindowState.RUNNING){
				state = MainWindowState.EDIT;
				updateAlgorithmDetails();
			}
		}
		return state;
	}

    //Utility controls

    /**
	*    Save the selected nodes and edges into a file of the user's choice
	*    The operation saves all edges that a node has, including edges that end at
	*    nodes that will not be saved. The means that when loading a graph, invalid edges may exist
	*    which need to be culled when loading
	**/
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

    /**
	*    Load a graph from a file of the user's choice
	*    To prevent errors and illegal states, each created edge is validated, so that only
	*    legal edges are created
	**/
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


    /**
	*    Copy the clickedNode list into the copiedNodes list
	**/
	protected void copySelected(){
        copiedNodes = new ArrayList<VisualGraphNode>(clickedNodes);
		System.out.println("Copied " + copiedNodes.size() + " nodes!");
	}

    /**
	*    Paste the copied node list at the original location of the copied subgraph
	*    First, copy the nodes. Then copy the edges between them.
	*    Copied nodes and edges do not share the name of their original node, to avoid ambiguity
	**/
	protected void pasteSelected(){
		clearClickedNodes();
		clearClickedEdges();
		if(copiedNodes == null){
			System.out.println("No nodes copied - aborting paste operation");
			return;
		}
		//Create the nodes first, storing a list of nodes that have edges between copied nodes
		ArrayList<VisualGraphNode> newVisualGraphNodes = new ArrayList<>();
		ArrayList<GraphNode> validNodes = new ArrayList<>();
        for(VisualGraphNode node : copiedNodes){
			GraphNode n = new GraphNode(0);
			n.setName(node.getNode().getName() + "(copy)");
			n.setValue(node.getNode().getValue());
			VisualGraphNode newNode = VisualGraphNode.create(node.getLocation(), n);
			newVisualGraphNodes.add(newNode);
			validNodes.add(node.getNode());
			addClickedComponent(newNode, true);
		}
		//Iterate back through the copied nodes, but create a duplicate edge only for the edges that link to copied
		//nodes
		int index = 0;
		for(VisualGraphNode copiedNode : copiedNodes){
			for(GraphEdge copiedEdge : copiedNode.getNode().getEdges()){
                if(validNodes.contains(copiedEdge.nodeB)){
					GraphNode targetNode = newVisualGraphNodes.get(validNodes.indexOf(copiedEdge.nodeB)).getNode();
					GraphEdge e = newVisualGraphNodes.get(index).getNode().addEdge(targetNode, false);
					e.setName(copiedEdge.getName() + "(copy)");
					e.setLength(copiedEdge.getLength());
					VisualGraphEdge.create(e);
				}
			}
			index++;
		}
		System.out.println("Successfully pasted " + copiedNodes.size() + " nodes!");
	}

	//Theme section

    /**
	*    Attempt to load the theme setting from the config folder.
	*    If this fails, set the default theme to LIGHT, and create the
	*    missing file
	**/
	private void initialiseTheme(){
		ThemeState themeIn = ThemeState.LIGHT;
		try{
    		FileInputStream fileStream = new FileInputStream(Data.THEME_CONFIG_PATH);
    		ObjectInputStream objectStream = new ObjectInputStream(fileStream);
    		themeIn = (ThemeState) objectStream.readObject();
    		objectStream.close();
	    	fileStream.close();
			setTheme(themeIn);
		}
		catch(Exception e){
            setTheme(ThemeState.LIGHT);
		}
	}

    /**
	*    Set the theme of the app, and save this to the theme file in the config
	*    folder
	**/
	protected void setTheme(ThemeState stateIn){
        theme = stateIn;
		getStylesheets().clear();
		getStylesheets().add(getClass().getResource("/themes/" + theme.name().toLowerCase() + ".css").toExternalForm());
		try{
		    FileOutputStream fileOutputStream = new FileOutputStream(Data.THEME_CONFIG_PATH);
    		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    		objectOutputStream.writeObject(theme);
    		objectOutputStream.close();
    		fileOutputStream.close();
		}
		catch(Exception e){
            displayErrorMessage("Theme set error", "Unable to save the theme of the app", e);
		}
		GridPane view = (GridPane) getCenter();
		for(Node n : view.getChildren()){
			((Viewport) n).setStyleSheet(getStylesheets().get(0));
		}
	}


}
