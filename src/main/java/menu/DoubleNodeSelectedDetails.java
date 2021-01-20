package menu;

import maths.Vector;

import model.GraphEdge;
import model.GraphNode;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;


public class DoubleNodeSelectedDetails extends DetailsPanel{
	VisualGraphNode nodeA;
	VisualGraphNode nodeB;
	public DoubleNodeSelectedDetails(VisualGraphNode nodeAIn, VisualGraphNode nodeBIn){
		System.out.println("make new menu");
		nodeA = nodeAIn;
		nodeB = nodeBIn;
		update();
	}
	
	public void update(){
		if(nodeA == null || nodeB == null){
			return;
		}
		getChildren().clear();
		Tooltip tooltip = new Tooltip("The details panel allows you to view and edit the details of selected graph components");
		Label title = new Label("DETAILS PANEL");
		Tooltip.install(title, tooltip);
		getChildren().add(title);
		
		
		addCreateEdgeButtons();
		
		addDeleteButton();
		
		getChildren().add(new EmptyDetails());
		
		
	}
	
	private GraphEdge canAddEdge(GraphNode start, GraphNode end){
		for(GraphEdge e : start.getEdges()){
			if(e.nodeB.equals(end)){
				return e;
			}
		}
		return null;
	}
	
	private void addCreateEdgeButtons(){
		Tooltip createTooltip = new Tooltip("Click to create an edge between the two edges");
		Tooltip deleteTooltip = new Tooltip("Click to delete the edge between the two edges");
		Button aToB;
		GraphEdge targetEdgeOne = canAddEdge(nodeA.getNode(), nodeB.getNode());
		if(targetEdgeOne == null){
	    	aToB = new Button("CREATE EDGE FROM" + nodeA.getNode().getName() + " TO " + nodeB.getNode().getName());
	    	aToB.setOnAction(new EventHandler<ActionEvent>() {
	    		@Override public void handle(ActionEvent e){
					if(canAddEdge(nodeA.getNode(), nodeB.getNode()) == null){
	    			    MainWindow.get().createEdge(nodeA, nodeB);
					}
	    		}
	    	});
	    	Tooltip.install(aToB, createTooltip);
		}
		else{
			aToB = new Button("DELETE EDGE FROM" + nodeA.getNode().getName() + " TO " + nodeB.getNode().getName());
	    	aToB.setOnAction(new EventHandler<ActionEvent>() {
	    		@Override public void handle(ActionEvent e){
	    	    	MainWindow.get().deleteEdge(targetEdgeOne);
	    		}
	    	});
	    	Tooltip.install(aToB, deleteTooltip);
		}
			
		Button bToA;
		GraphEdge targetEdgeTwo = canAddEdge(nodeB.getNode(), nodeA.getNode());
		if(targetEdgeTwo == null){
    		bToA = new Button("CREATE EDGE FROM" + nodeB.getNode().getName() + " TO " + nodeA.getNode().getName());
    		bToA.setOnAction(new EventHandler<ActionEvent>() {
    			@Override public void handle(ActionEvent e){
					if(canAddEdge(nodeB.getNode(), nodeA.getNode()) == null){
    				    MainWindow.get().createEdge(nodeB, nodeA);
					}
    			}
    		});
    		Tooltip.install(bToA, createTooltip);
		}
		else{
			bToA = new Button("DELETE EDGE FROM" + nodeB.getNode().getName() + " TO " + nodeA.getNode().getName());
    		bToA.setOnAction(new EventHandler<ActionEvent>() {
    			@Override public void handle(ActionEvent e){
    				MainWindow.get().deleteEdge(targetEdgeTwo);
    			}
    		});
    		Tooltip.install(bToA, deleteTooltip);
		}
		getChildren().add(aToB);
		getChildren().add(bToA);
	}
		
		
	
	
	public void addDeleteButton(){
		Tooltip tooltip = new Tooltip("Delete the selected node");
		Button delete = new Button("DELETE NODE");
		Tooltip.install(delete, tooltip);
		getChildren().add(delete);
		delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().deleteNode(nodeA);
				MainWindow.get().deleteNode(nodeB);
            }
        });
	}
}