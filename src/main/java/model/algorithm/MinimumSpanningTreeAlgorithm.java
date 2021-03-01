package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.HashMap;
import java.util.AbstractCollection;
import java.util.ArrayList;

public abstract class MinimumSpanningTreeAlgorithm extends GraphAlgorithm {
    protected ArrayList<GraphEdge> spanningTree = new ArrayList<>();
    protected ArrayList<GraphEdge> edges = new ArrayList<>();

    public ArrayList<GraphEdge> getSpanningTree(){
        return new ArrayList<GraphEdge>(spanningTree);
    }

}
