package menu;

import maths.Functions;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;


import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;

import java.lang.instrument.Instrumentation;

/**
*    The GraphDetails box displays useful information about the current graph,
*    such as it's node count
*    @author Harrison Boyle-Thomas
*    @date 07/02/21
**/
public class GraphDetails extends DetailsPanel{
    public GraphDetails(){
        update();
    }

    public void update(){
        getChildren().clear();
        Label title = new Label("GRAPH DETAILS");
        getChildren().add(title);
        createNodeCountSection();
        createEdgeCountSection();
        createTotalMemorySection();
    }

    private void createNodeCountSection(){
        Tooltip tooltip = new Tooltip("The total number of nodes in the current graph");
        HBox section = new HBox();
        Tooltip.install(section, tooltip);
        Label title = new Label("Node count: ");
        Label count = new Label("" + VisualGraphNode.getNodes().size());
        section.getChildren().add(title);
        section.getChildren().add(count);
        section.setAlignment(Pos.TOP_CENTER);
        getChildren().add(section);
    }

    private void createEdgeCountSection(){
        Tooltip tooltip = new Tooltip("The total number of edges in the current graph");
        HBox section = new HBox();
        Tooltip.install(section, tooltip);
        Label title = new Label("Edge count: ");
        Label count = new Label("" + VisualGraphEdge.getEdges().size());
        section.getChildren().add(title);
        section.getChildren().add(count);
        section.setAlignment(Pos.TOP_CENTER);
        getChildren().add(section);
    }

    private void createTotalMemorySection(){
        Tooltip tooltip = new Tooltip("The total memory usage of GraphVisualiser. If you choose to save a graph, the graph will likely use less memory than the displayed value");
        HBox section = new HBox();
        Tooltip.install(section, tooltip);
        Label title = new Label("Est. mem: ");
        Label count = new Label("" + Functions.round(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024.0/1024.0), 3) + "MB");
        section.getChildren().add(title);
        section.getChildren().add(count);
        section.setAlignment(Pos.TOP_CENTER);
        getChildren().add(section);
    }
}
