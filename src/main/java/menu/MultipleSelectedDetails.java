package menu;

import maths.Vector;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.application.Platform;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;

/**
*    The MSD panel allows the user to view the details of multiple different
*    graph components. This is intended to be used by MainWindow when there are
*    multiple components selected
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
**/
public class MultipleSelectedDetails extends DetailsPanel{

	public MultipleSelectedDetails(){
		update();
	}

	public void update(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getChildren().clear();
				Tooltip tooltip =new Tooltip("The details panel allows you to view and edit the details of selected graph components. Perform operations on selected components");
				Label title = new Label("DETAILS PANEL");
				Tooltip.install(title, tooltip);
				getChildren().add(title);

		        //Create a scroll section listing selected nodes
				ScrollPane nodeSection = new ScrollPane();
				nodeSection.setId("nodesection");
				VBox nodes = new VBox();
				Label nodeTitle = new Label("SELECTED NODES");
				Tooltip nodeTooltip = new Tooltip("Below are the nodes that have been selected through multi-select");
				Tooltip.install(nodeTitle, nodeTooltip);
				nodes.getChildren().add(nodeTitle);
				for(VisualGraphNode n : MainWindow.get().getClickedNodes()){
					Button nodeButton = new Button(n.getNode().getName());
					nodeButton.setId(n.getNode().getName() + ":" + n.getNode().getValue());
					nodeButton.setOnAction(new EventHandler<ActionEvent>() {
		                @Override public void handle(ActionEvent e) {
		                    MainWindow.get().addClickedComponent(n);
		                }
		            });
					nodes.getChildren().add(nodeButton);
				}
				nodeSection.setContent(nodes);
				getChildren().add(nodeSection);

		        //Create a scroll section listing selected edges
				ScrollPane edgeSection = new ScrollPane();
				edgeSection.setId("edgesection");
				VBox edges = new VBox();
				Label edgeTitle = new Label("SELECTED EDGES");
				Tooltip edgeTooltip = new Tooltip("Below are the edges that have been selected through multi-select");
				Tooltip.install(edgeTitle, edgeTooltip);
				edges.getChildren().add(edgeTitle);
				int edgeCounter = 0;
				for(VisualGraphEdge edge : MainWindow.get().getClickedEdges()){
					Button edgeButton = new Button(edge.getEdge().getName() + ": " + edge.getEdge().getLength());
					edgeButton.setId(edge.getEdge().getName() + ":" + edge.getEdge().getLength() + ":" + edgeCounter);
					edgeButton.setOnAction(new EventHandler<ActionEvent>() {
		                @Override public void handle(ActionEvent e) {
		                    MainWindow.get().addClickedComponent(edge);
		                }
		            });
					edges.getChildren().add(edgeButton);
					edgeCounter++;
				}
				edgeSection.setContent(edges);
				getChildren().add(edgeSection);

				getChildren().add(new EmptyDetails());
	        }
        });

	}
}
