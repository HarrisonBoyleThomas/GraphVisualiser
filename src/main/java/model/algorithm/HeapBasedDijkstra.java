package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.ArrayList;

/**
*    ArrayBasedDijkstra implements DSP by using a Heap-based prority queue
*    @Author Harrison Boyle-Thomas
*    @Date 01/02/21
**/
public class HeapBasedDijkstra extends DijkstraShortestPath{
    public HeapBasedDijkstra(GraphNode initialStateIn, ArrayList<GraphNode> nodesIn){
        super(initialStateIn, nodesIn);
		name += " (heap)";
        description += "This implementation uses a sorted heap to expand nodes. This is the standard implementation.";
	}

	public HeapBasedDijkstra(GraphNode initialStateIn){
        super(initialStateIn);
        name += " (heap)";
        description += "This implementation uses a sorted heap to expand nodes. This is the standard implementation.";
	}

    protected void initialiseNextStates(){
        nextStates = new PriorityQueue<>(distances.size(), new DijkstraComparator(distances));
    }

    protected GraphNode getNextNode(){
        return (GraphNode) ((PriorityQueue) nextStates).peek();
    }
    protected GraphNode removeMinimum(){
        GraphNode returnValue = getNextNode();
        ((PriorityQueue) nextStates).remove(returnValue);
        return returnValue;
    }

    protected void addDiscoveredNode(GraphNode toAdd){
        ((PriorityQueue) nextStates).add(toAdd);
    }
}
