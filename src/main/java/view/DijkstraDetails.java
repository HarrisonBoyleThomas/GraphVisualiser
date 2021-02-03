package viewport;

import model.GraphNode;
import model.algorithm.GraphAlgorithm;
import model.algorithm.ShortestPathAlgorithm;
import model.algorithm.DijkstraShortestPath;

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
*    DijkstraDetails displays details specific to the DSP algorithm.
*    Open list, and closed list are added in addition to inherited distance and
*    parent lists from SPAD
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public class DijkstraDetails extends ShortestPathAlgorithmDetails{
    public DijkstraDetails(GraphAlgorithm algorithmIn){
        super(algorithmIn);
        update(algorithmIn);
    }

    public void update(GraphAlgorithm algorithmIn){
        super.update(algorithmIn);
        if(!(algorithmIn instanceof DijkstraShortestPath)){
            return;
        }
        DijkstraShortestPath algorithm = (DijkstraShortestPath) algorithmIn;
        GridPane grid = new GridPane();

        grid.add(new Label("Open list"), 0, 0);
        int index = 1;
        for(GraphNode n : algorithm.getNextStates()){
            grid.add(new Label(n.getName()), index, 0);
            index++;
        }

        index = 1;
        grid.add(new Label("Closed list"), 0, 1);
        for(GraphNode n : algorithm.getVisitedNodes()){
            grid.add(new Label(n.getName()), index, 1);
            index++;
        }

        //set the padding between cells
        Insets inset = new Insets(10.0, 10.0, 10.0, 10.0);
        for(Node n : grid.getChildren()){
            grid.setMargin(n, inset);
        }
        grid.setGridLinesVisible(true);
        ((VBox) getContent()).getChildren().add(grid);
        ((VBox) getContent()).getChildren().add(new AlgorithmPseudocodePanel(algorithmIn));
    }
}
