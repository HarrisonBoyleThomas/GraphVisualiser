package viewport;

import model.GraphEdge;
import model.algorithm.GraphAlgorithm;
import model.algorithm.MinimumSpanningTreeAlgorithm;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.geometry.Insets;
import javafx.scene.Node;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
*    MSTADetails displays details specific to MST algorithms
**/
public class MinimumSpanningTreeAlgorithmDetails extends AlgorithmDetailsWindow {
    public MinimumSpanningTreeAlgorithmDetails(GraphAlgorithm algorithmIn){
        update(algorithmIn);
    }

    public void update(GraphAlgorithm algorithmIn){
        if(!(algorithmIn instanceof MinimumSpanningTreeAlgorithm)){
            return;
        }
        MinimumSpanningTreeAlgorithm algorithm = (MinimumSpanningTreeAlgorithm) algorithmIn;
        VBox pane = new VBox();

        //add title
        Tooltip titleTooltip = new Tooltip(algorithm.getDescription());
        Label title = new Label(algorithm.toString());
        Tooltip.install(title, titleTooltip);
        pane.getChildren().add(title);
        Label stepCount = new Label("Step count: " + algorithm.getIterationStep());
        pane.getChildren().add(stepCount);

        GridPane grid = new GridPane();
        int index = 1;
        Insets inset = new Insets(10.0, 10.0, 10.0, 10.0);
        grid.add(new Label("Spanning tree"), 0, 0);

        //Add shortest path algorithm details
        for(GraphEdge e : algorithm.getSpanningTree()){
            Label edgeName = new Label("(" + e.nodeA + "," + e.nodeB + "):" + e.getLength());
            grid.add(edgeName, index, 0);
            index++;
        }
        grid.setGridLinesVisible(true);
        pane.getChildren().add(grid);

        //set the padding between cells
        for(Node n : grid.getChildren()){
            grid.setMargin(n, inset);
        }
        setContent(pane);
    }
}
