package viewport;

import model.algorithm.GraphAlgorithm;

import java.util.Arrays;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.Node;

/**
*    The pseudocode panel extracts the pseudocode lines from an algorithm,
*    and colour codes them according to the algorithm's state.
*    The lines are added to the panel's contents
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public class AlgorithmPseudocodePanel extends AlgorithmDetailsWindow{

    public AlgorithmPseudocodePanel(GraphAlgorithm algorithm){
        update(algorithm);
    }

    public void update(GraphAlgorithm algorithm){
        VBox content = new VBox();

        //Create an array of the current lines of the algorithm, by reading the algorithm's
        //current line index list, then extracting the corresponding line from the lines list
        int index = 0;
        String[] currentLines = new String[algorithm.getCurrentPseudocodeLines().length];
        for(int i : algorithm.getCurrentPseudocodeLines()){
            currentLines[index] = algorithm.getPseudocodeLines()[i];
            index++;
        }

        //Add the lines of the algorithm to the panel
        GridPane grid = new GridPane();
        index = 0;
        for(String line : algorithm.getPseudocodeLines()){
            Label newLine = new Label(line);
            //Colour the line green if it is a current line
            if(Arrays.asList(currentLines).contains(line)){
                newLine.setTextFill(Color.LIME);
            }
            grid.add(newLine, 0, index);
            index++;
        }

        content.getChildren().add(grid);

        setContent(content);
    }
}
