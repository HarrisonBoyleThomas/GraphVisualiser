package model.algorithm;

import model.GraphNode;

/**
*    Root node algorithms are graph algorithms that
*    require a start node to be supplied to it in order to run
*    examples of this are various search algorithms, and prim's algorithm
*    @author Harrison Boyle-Thomas
*    @date 01/03/21
**/
public interface RootNodeAlgorithm {
    public void setStartNode(GraphNode nodeIn);

    public GraphNode getStartNode();
}
