package viewport;

import model.GraphNode;
import model.algorithm.GraphAlgorithm;
import model.algorithm.ShortestPathAlgorithm;

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

public class ShortestPathAlgorithmDetails extends AlgorithmDetailsWindow{
    public ShortestPathAlgorithmDetails(GraphAlgorithm algorithmIn){
        update(algorithmIn);
    }

    public void update(GraphAlgorithm algorithmIn){
        if(!(algorithmIn instanceof ShortestPathAlgorithm)){
            return;
        }
        ShortestPathAlgorithm algorithm = (ShortestPathAlgorithm) algorithmIn;
        VBox pane = new VBox();

        //add title
        Tooltip titleTooltip = new Tooltip(algorithm.getDescription());
        Label title = new Label(algorithm.toString());
        Tooltip.install(title, titleTooltip);
        pane.getChildren().add(title);

        GridPane grid = new GridPane();
        int index = 1;
        Insets inset = new Insets(10.0, 10.0, 10.0, 10.0);
        grid.add(new Label("Node"), 0, 0);
        grid.add(new Label("Distance"), 0, 1);
        grid.add(new Label("Predecessor"), 0, 2);

        //Add shortest path algorithm details
        for(GraphNode n : algorithm.getDistances().keySet()){
            Label nodeName = new Label(n.getName());
            grid.add(nodeName, index, 0);
            int distance = algorithm.getDistance(n);
            String distanceString = "" + distance;
            if(distance < 0){
                distanceString = "" + '\u221e';
            }
            grid.add(new Label(distanceString), index, 1);

            String predecessorName = "";
            if(algorithm.getPredecessor(n) != null){
                predecessorName = algorithm.getPredecessor(n).getName();
            }
            grid.add(new Label(predecessorName), index, 2);
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
