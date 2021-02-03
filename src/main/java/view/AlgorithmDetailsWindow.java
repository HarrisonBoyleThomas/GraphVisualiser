package viewport;

import model.algorithm.GraphAlgorithm;

import javafx.scene.control.ScrollPane;

/**
*    An AlgorithmDetails window displays information about a given
*    GraphALgorithm. The details are described within subclasses, which are
*    referenced to by the AlgorithmDetails class
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public abstract class AlgorithmDetailsWindow extends ScrollPane{
    public abstract void update(GraphAlgorithm algorithmIn);
}
