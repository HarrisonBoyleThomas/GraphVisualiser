package viewport;

import model.GraphNode;
import model.algorithm.GraphAlgorithm;
import model.algorithm.SearchAlgorithm;

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
*    SADetails displays information about a search algorithm, such as it's open
*    and closed lists
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public class SearchAlgorithmDetails extends AlgorithmDetailsWindow{
    public SearchAlgorithmDetails(GraphAlgorithm algorithmIn){
        update(algorithmIn);
    }

    public void update(GraphAlgorithm algorithmIn){
        VBox pane = new VBox();

        SearchAlgorithm algorithm = (SearchAlgorithm) algorithmIn;
        if(!(algorithm instanceof SearchAlgorithm)){
            return;
        }

        //add title
        Tooltip titleTooltip = new Tooltip(algorithm.getDescription());
        Label title = new Label(algorithm.toString());
        Tooltip.install(title, titleTooltip);
        pane.getChildren().add(title);

        GridPane grid = new GridPane();

        grid.add(new Label("Open list"), 0, 0);
        int index = 1;
        for(GraphNode n : algorithm.getOpenList()){
            grid.add(new Label(n.getName()), index, 0);
            index++;
        }

        index = 1;
        grid.add(new Label("Closed list"), 0, 1);
        for(GraphNode n : algorithm.getClosedList()){
            grid.add(new Label("" + n), index, 1);
            index++;
        }

        //set the padding between cells
        Insets inset = new Insets(10.0, 10.0, 10.0, 10.0);
        for(Node n : grid.getChildren()){
            grid.setMargin(n, inset);
        }
        grid.setGridLinesVisible(true);
        pane.getChildren().add(grid);
        pane.getChildren().add(new AlgorithmPseudocodePanel(algorithm));
        setContent(pane);
    }
}
