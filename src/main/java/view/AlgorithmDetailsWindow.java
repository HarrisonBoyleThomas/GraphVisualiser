package viewport;

import model.algorithm.GraphAlgorithm;

import javafx.scene.control.ScrollPane;


public abstract class AlgorithmDetailsWindow extends ScrollPane{
    public abstract void update(GraphAlgorithm algorithmIn);
}
