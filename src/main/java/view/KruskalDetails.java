package viewport;

import model.GraphEdge;
import model.algorithm.GraphAlgorithm;
import model.algorithm.KruskalsAlgorithm;

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
*    Kruskal details shows details specific to Kruskal's algorithm
**/
public class KruskalDetails extends MinimumSpanningTreeAlgorithmDetails {
    public KruskalDetails(GraphAlgorithm algorithmIn){
        super(algorithmIn);
        update(algorithmIn);
    }

    public void update(GraphAlgorithm algorithmIn){
        super.update(algorithmIn);
        if(!(algorithmIn instanceof KruskalsAlgorithm)){
            return;
        }
        KruskalsAlgorithm algorithm = (KruskalsAlgorithm) algorithmIn;
        GridPane grid = new GridPane();

        grid.add(new Label("Edge list"), 0, 0);
        int index = 1;
        for(GraphEdge e : algorithm.getSortedEdges()){
            grid.add(new Label("(" + e.nodeA + "," + e.nodeB + "):" + e.getLength()), index, 0);
            index++;
        }
        //set the padding between cells
        Insets inset = new Insets(10.0, 10.0, 10.0, 10.0);
        for(Node n : grid.getChildren()){
            grid.setMargin(n, inset);
        }
        grid.setGridLinesVisible(true);


        ((VBox) getContent()).getChildren().add(grid);
        ((VBox) getContent()).getChildren().add(new AlgorithmPseudocodePanel(algorithm));
    }

}
