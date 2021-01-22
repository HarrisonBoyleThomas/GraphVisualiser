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
		getChildren().clear();
		Tooltip tooltip =new Tooltip("The details panel allows you to view and edit the details of selected graph components. Perform operations on selected components");
		Label title = new Label("DETAILS PANEL");
		Tooltip.install(title, tooltip);
		getChildren().add(title);

		ScrollPane nodeSection = new ScrollPane();
		VBox nodes = new VBox();
		Label nodeTitle = new Label("SELECTED NODES");
		Tooltip nodeTooltip = new Tooltip("Below are the nodes that have been selected through multi-select");
		Tooltip.install(nodeTitle, nodeTooltip);
		nodes.getChildren().add(nodeTitle);
		for(VisualGraphNode n : MainWindow.get().getClickedNodes()){
			Button nodeButton = new Button(n.getNode().getName());
			nodeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    MainWindow.get().addClickedComponent(n);
                }
            });
			nodes.getChildren().add(nodeButton);
		}
		nodeSection.setContent(nodes);
		getChildren().add(nodeSection);


		ScrollPane edgeSection = new ScrollPane();
		VBox edges = new VBox();
		Label edgeTitle = new Label("SELECTED EDGES");
		Tooltip edgeTooltip = new Tooltip("Below are the edges that have been selected through multi-select");
		Tooltip.install(edgeTitle, edgeTooltip);
		edges.getChildren().add(edgeTitle);
		for(VisualGraphEdge edge : MainWindow.get().getClickedEdges()){
			Button edgeButton = new Button(edge.getEdge().getName() + ": " + edge.getEdge().getLength());
			edgeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    MainWindow.get().addClickedComponent(edge);
                }
            });
			edges.getChildren().add(edgeButton);
		}
		edgeSection.setContent(edges);
		getChildren().add(edgeSection);

		getChildren().add(new EmptyDetails());

	}
}
