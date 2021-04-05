package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import data.Data;
import data.UndoRedoController;

import model.GraphNode;
import model.GraphEdge;
import model.GraphComponentState;
import model.algorithm.*;

import menu.MainWindow;

import threads.AlgorithmRunner;


import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.*;
import java.awt.Point;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.scene.transform.Scale;
import javafx.collections.FXCollections;

import javafx.scene.image.Image;

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

	private HashMap<VisualGraphComponent, Group> drawnComponents = new HashMap<>();

	Service<Void> renderTask;
	ArrayList<Service<Void>> renderTasks = new ArrayList<>();

	public Viewport(Camera cameraIn, GraphAlgorithm algorithmIn){
		setId(this.toString());
		setMinSize(width, height);
		setMaxSize(width, height);
		setId("viewport");
		//setStyle("-fx-background-color: #ffffff");
		camera = cameraIn;
		algorithm = algorithmIn;
		draw();

		setPickOnBounds(false);
		setOnMouseClicked(e -> {handleClick();
		});

		//set the clipping rectangle to hide VGCs that are off screen
		Rectangle clip = new Rectangle(width, height);
        setClip(clip);
		addDragAndDropEvents();
		createCloseButton();
	}

	private void handleClick(){
		requestFocus();
		MainWindow.get().addClickedComponent(null, false);

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
						UndoRedoController.pushToUndoStack();
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
		GraphNode oldStartNode = null;
		if(algorithm != null){
			if(algorithm instanceof ShortestPathAlgorithm){
				oldStartNode = ((ShortestPathAlgorithm) algorithm).getStartNode();
			}
			else if(algorithm instanceof SearchAlgorithm){
				oldStartNode = ((SearchAlgorithm) algorithm).getStartNode();
			}
			else if(algorithm instanceof PrimsAlgorithm){
				oldStartNode = ((PrimsAlgorithm) algorithm).getStartNode();
			}
		}
		//Copy the old start node to be the start node of the replacement algorithm
		if(oldStartNode != null){
			if(algorithmIn instanceof ShortestPathAlgorithm){
		        ((ShortestPathAlgorithm) algorithmIn).setStartNode(oldStartNode);
			}
			else if(algorithmIn instanceof SearchAlgorithm){
		        ((SearchAlgorithm) algorithmIn).setStartNode(oldStartNode);
			}
			else if(algorithmIn instanceof PrimsAlgorithm){
				((PrimsAlgorithm) algorithmIn).setStartNode(oldStartNode);
			}
		}
		algorithm = algorithmIn;
		viewportDetails.update(this);
		MainWindow.get().updateAlgorithmDetails();
	}

    /**
	*    Removes all drawn graphs from the screen. This helps to reduce the chance of errors.
	**/
	public void reset(){
		//Apply changes in the JavaFX Application thread
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Remove any previous services to prevent deadlock
				while(renderTasks.size() > 0){
					Service<Void> task = renderTasks.remove(0);
					task.cancel();
				}
				drawnComponents.clear();
        		ArrayList<Node> foreignComponents = new ArrayList<>(getChildren());
        		foreignComponents.remove(viewportDetails);
				for(Node n : foreignComponents){
					getChildren().remove(n);
				}
	      	    createCloseButton();
				draw();
			}
		});
	}

    /**
	*    Draw all VGCs to the viewport
	*    Note: VGC's renderLocations MUST be updated before draw, to ensure their
	*    locations are correct. The Viewport simply renders what it is given
	*
	*    To improve performance, only new icons are added to the viewport.
	*    The outer Task identifies VGC icons that are "out of date", and adds them to
	*    an invalid icon list. The viewport also stores a hashmap of VGCs mapped to the
	*    current valid icon for the component.
	**/
	public void draw(){
		renderTask = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
    			return new Task<Void>() {
	    			@Override
                    protected Void call() throws Exception {
                        CountDownLatch delay = new CountDownLatch(1);
						VisualGraphNode.updateNodes(camera, 500,500);
						//get copies of VGCs that need to be rendered
						ArrayList<VisualGraphComponent> components = new ArrayList<>(VisualGraphComponent.getComponents());
						//create a list of icons that were removed from the viewport
						ArrayList<Group> invalidIcons = new ArrayList<>();
						//Create a list of VGCs that are invalid. ic[i] -> ii[i], implicitly
						ArrayList<VisualGraphComponent> invalidComponents = new ArrayList<>();
						//Invalidate all VGCs that were deleted by the user in the last frame,
						//and invalidate the icons of edges(the arrows for edges must be redrawn to reflect
						//new node positions)
						for(VisualGraphComponent c : drawnComponents.keySet()){
							if(isCancelled()){
								throw new InterruptedException();
							}
							Boolean containsComp = components.contains(c);
							if(!containsComp){
								invalidIcons.add(drawnComponents.get(c));
								invalidComponents.add(c);
							}
							else{
								if(c instanceof VisualGraphEdge){
									invalidIcons.add(drawnComponents.get(c));
									invalidComponents.add(c);
								}
							}
						}
						//list of icons to add to the viewport
						ArrayList<Group> newIcons = new ArrayList<>();

                        //hashmap contains() method will not work here
						ArrayList<VisualGraphComponent> keys = new ArrayList<>();
						for(VisualGraphComponent c : drawnComponents.keySet()){
							keys.add(c);
						}
						//Go through each no component, and add icons to the newIcons list
					    //if the component's appearance has changed since the last frame
						//or if the component was just created by the user
						for(VisualGraphComponent c : components){
							if(isCancelled()){
								throw new InterruptedException();
							}
							//Create a duplicate of the component
							VisualGraphComponent newComp;
							if(c instanceof VisualGraphEdge){
								newComp = new VisualGraphEdge((VisualGraphEdge) c);
							}
							else{
								newComp = new VisualGraphNode((VisualGraphNode) c);
							}
							//update the icon of the duplicate
							newComp.updateIcon(algorithm);
							//Add the edge's icon to newIcon list
							if(c instanceof VisualGraphEdge){
								drawnComponents.put(c, newComp.getIcon());
								newIcons.add(newComp.getIcon());
							}
							else{
								//Add newly detected node
								if(!keys.contains(c)){
									drawnComponents.put(c, newComp.getIcon());
									newIcons.add(newComp.getIcon());
								}
								else{
									VisualGraphComponent comp = keys.get(keys.indexOf(c));
									//Update the icon if the VGN's appearance has changed
									if(!newComp.iconsEqual(drawnComponents.get(comp))){
										Group old = drawnComponents.put(c, newComp.getIcon());
										newIcons.add(newComp.getIcon());
										invalidIcons.add(old);
									}
								}
							}
						}
						//Create a list of components that should not be included in the viewport
						//Due to the nature of the thread, it is possible that a node is missed when
						//Removing invalid icons
						ArrayList<Node> foreignComponents = new ArrayList<>(getChildren());
						foreignComponents.removeAll(drawnComponents.values());
						//foreignComponents.removeAll(invalidIcons);
						foreignComponents.remove(viewportDetails);

						HashMap<Node, VisualGraphComponent> iconMap = new HashMap<>();
						for(VisualGraphComponent c : drawnComponents.keySet()){
							if(isCancelled()){
								throw new InterruptedException();
							}
							iconMap.put(drawnComponents.get(c), c);
						}

						//Apply changes in the JavaFX Application thread
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
								//Sometimes the render task fails to render components in the correct position,
								//which causes icons to hover at around (0,0)
								//This seems to be a threading issue, so render components off-screen initially
								//so that they can be correctly rendered later on. This prevents
								for(Node n : newIcons){
									n.setLayoutX(-10000);
									n.setLayoutY(-10000);
								}
								//Clear foreign elements from the screen, to ensure only up-t-date components
								//are rendered
								for(Node n : foreignComponents){
									getChildren().remove(n);
								}
								//add the close button
								createCloseButton();


								//hashmap contains() method will not work here
								keys.clear();
								for(VisualGraphComponent c : drawnComponents.keySet()){
									keys.add(c);
								}
								//Update the drawn position of all VGC icons
								//Unfortunately, this MUST be done here in the main thread
								for(VisualGraphComponent c : components){
									if(keys.contains(c)){
										VisualGraphComponent comp = keys.get(keys.indexOf(c));
										if(c instanceof VisualGraphEdge){
											VisualGraphEdge edge = (VisualGraphEdge) c;
											VisualGraphNode nodeA = VisualGraphNode.getNode(edge.getEdge().nodeA);
											VisualGraphNode nodeB = VisualGraphNode.getNode(edge.getEdge().nodeB);
											if(edge != null && edge.getRenderLocation() != null){
									    		if(nodeA !=  null && nodeB != null){
										    	    if(camera.isInFront(nodeA)  || camera.isInFront(nodeB)){
														if(drawnComponents.get(comp) != null){
										    	        	drawnComponents.get(comp).setLayoutX((int) edge.getRenderLocation().x);
										    	    	    drawnComponents.get(comp).setLayoutY((int) edge.getRenderLocation().y);
	                                                    }
													}
											    }
											}
										}
										else if (c instanceof VisualGraphNode){
											VisualGraphNode node = (VisualGraphNode) c;
											drawnComponents.get(comp).setLayoutX((int) node.getRenderLocation().x);
											drawnComponents.get(comp).setLayoutY((int) node.getRenderLocation().y);
											//Scale about the top right corner of the node, instead of it's default scale center
											Scale scale = new Scale();
											scale.setPivotX(0.0);
											scale.setPivotY(0.0);
											scale.setX(node.getRenderScale());
											scale.setY(node.getRenderScale());
											drawnComponents.get(comp).getTransforms().clear();
											drawnComponents.get(comp).getTransforms().add(scale);
										}
									}
								}
								//Clear invalid icons from the viewport, and
								//remove all references to deleted components
								if(invalidIcons.size() > 0){
									getChildren().removeAll(invalidIcons);
							    	for(Group icon : invalidIcons){
							    		getChildren().remove(icon);
								    }
									for(VisualGraphComponent c : invalidComponents){
										drawnComponents.remove(c);
									}
								}
								//Draw all new VGC icons to the screen
								if(newIcons.size() > 0){
								    getChildren().addAll(newIcons);
								}
								if(iconMap.size() > 0){
							    	FXCollections.sort(getChildren(), new ComponentDistanceComparator(camera, iconMap));
								}
								//Update viewport details
								viewportDetails.update(Viewport.this);
								//Threading can sometimes cause this component to not be erased at this stage
								//So check before re adding it
								if(viewportDetails.getParent() != Viewport.this){
					    			getChildren().add(viewportDetails);
								}
								//Remove any previous services to prevent deadlock
								while(renderTasks.size() > 0){
									Service<Void> task = renderTasks.remove(0);
									if(!isCancelled()){
								    	task.cancel();
									}
								}
							    delay.countDown();
                            }
                        });
                        delay.await();
                        return null;
                    }
                };
            }
	    };
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
		        //Remove any previous services to prevent deadlock
        		while(renderTasks.size() > 0){
	        		Service<Void> task = renderTasks.remove(0);
					if(task != null){
		        	    task.cancel();
					}
		        }
		    }
		});
		renderTask.start();
		renderTasks.add(renderTask);
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
		close.setFocusTraversable(false);
		close.setId("closeButton");
		close.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().deleteViewport(Viewport.this);
				if(renderTask != null){
					renderTask.cancel();
				}
				terminateAlgorithm();
            }
        });
		if(!getChildren().contains(close)){
		    getChildren().add(close);
		}
		//Position in the top right corner
		close.setLayoutX(width - 30);
	}

    /**
	*    Create an AlgorithmRunner which runs the algorithm in a separate thread.
	*    The viewport automatically redraws the graph to reflect the changes within
	*    the algorithm
	**/
	public void executeAlgorithm(){
		//terminateAlgorithm();
		algorithmRunner = new AlgorithmRunner(algorithm, 100);
		//Extract the algorithm speed
		updateAlgorithmSpeed();
		algorithmRunner.start();
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				draw();
				if(algorithmRunner == null || !algorithmRunner.isRunning()){
					stop();
					draw();
					//Draw twice just incase the draw thread failed to clear parts of the screen
					draw();
					MainWindow.get().updateWindowStatus();
					terminateAlgorithm();
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
			if(algorithmRunner.isAlive()){
		    	algorithmRunner.stopThread();
			}
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
		else{
			if(algorithm != null){
			    algorithm.terminate();
			}
		}
	}

	public ViewportDetails getViewportDetails(){
		return viewportDetails;
	}

}
