package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;
import model.dataStructures.BucketQueue;

import java.lang.Math;

import java.util.HashMap;
import java.util.ArrayList;

/**
*    BucketQueueDijkstra implements DSP by using a bucket queue to store the open list
*    @Author Harrison Boyle-Thomas
*    @Date 01/02/21
**/
public class BucketQueueDijkstra extends DijkstraShortestPath{
    public BucketQueueDijkstra(GraphNode initialStateIn, ArrayList<GraphNode> nodesIn){
        super(initialStateIn, nodesIn);
		name += " (bucket queue)";
        description += "This implementation uses a bucket queue to efficiently add and remove nodes.";
	}

	public BucketQueueDijkstra(GraphNode initialStateIn){
        super(initialStateIn);
        name += " (bucket queue)";
        description += "This implementation uses a bucket queue to efficiently add and remove nodes.";
	}

    protected void initialiseNextStates(){
        nextStates = new BucketQueue<>(distances.size());
    }

    protected GraphNode getNextNode(){
        int index = ((BucketQueue) nextStates).getMinIndex();
        return (GraphNode) ((BucketQueue) nextStates).get(index);
    }
    protected GraphNode removeMinimum(){
        GraphNode returnValue = getNextNode();
        int index = ((BucketQueue) nextStates).getMinIndex();
        ((BucketQueue) nextStates).remove(index);
        return returnValue;
    }

    protected void addDiscoveredNode(GraphNode toAdd){
        ((BucketQueue) nextStates).add(toAdd, (int) Math.round(distances.get(toAdd)));
    }

    protected void handleDistances(GraphNode node, double newDistance){
        ((BucketQueue) nextStates).updateIndex(node, (int) Math.round(newDistance));
    }
}
