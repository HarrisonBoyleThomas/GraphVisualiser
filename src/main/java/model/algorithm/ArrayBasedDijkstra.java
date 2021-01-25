package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.HashMap;
import java.util.ArrayList;

/**
*    ArrayBasedDijkstra implements DSP by using a FIFO queue
**/
public class ArrayBasedDijkstra extends DijkstraShortestPath{
    public ArrayBasedDijkstra(GraphNode initialStateIn, ArrayList<GraphNode> nodesIn){
        super(initialStateIn, nodesIn);
		name += " (array)";
        description += "This implementation uses an array-based FIFO queue with no priority sorting to expand nodes. This is the most ";
        description += "inefficient provided implementation of DSP";
	}

	public ArrayBasedDijkstra(GraphNode initialStateIn){
        super(initialStateIn);
        name += " (array)";
        description += "This implementation uses an array-based queue with no priority sorting to expand nodes. This is the most ";
        description += "inefficient provided implementation of DSP";
	}

    protected void initialiseNextStates(){
        nextStates = new ArrayList<>();//currentNode.getConnectedNodes();
    }

    protected GraphNode getNextNode(){
        return (GraphNode) ((ArrayList) nextStates).get(0);
    }
    protected GraphNode removeMinimum(){
        return (GraphNode) ((ArrayList) nextStates).remove(0);
    }

    protected void addDiscoveredNode(GraphNode toAdd){
        ((ArrayList) nextStates).add(toAdd);
    }
}
