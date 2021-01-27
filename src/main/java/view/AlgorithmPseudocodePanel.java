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


public class AlgorithmPseudocodePanel extends AlgorithmDetailsWindow{

    public AlgorithmPseudocodePanel(GraphAlgorithm algorithm){
        update(algorithm);
    }

    public void update(GraphAlgorithm algorithm){
        VBox content = new VBox();

        int index = 0;
        String[] currentLines = new String[algorithm.getCurrentPseudocodeLines().length];
        for(int i : algorithm.getCurrentPseudocodeLines()){
            currentLines[index] = algorithm.getPseudocodeLines()[i];
            index++;
        }
        GridPane grid = new GridPane();
        index = 0;
        for(String line : algorithm.getPseudocodeLines()){
            Label newLine = new Label(line);
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
