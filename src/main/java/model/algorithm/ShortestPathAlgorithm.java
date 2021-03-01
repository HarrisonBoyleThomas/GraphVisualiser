package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.HashMap;
import java.util.ArrayList;

/**
*     A shortest path algorithm finds the shortest path from a given start node
*    to other nodes in a graph
*    @Author Harrison Boyle-Thomas
*    @Date 01/02/21
**/
public abstract class ShortestPathAlgorithm extends GraphAlgorithm implements RootNodeAlgorithm{
    //the node to find the distance from
	protected GraphNode startNode;

	//maps nodes to their distances from the start node
	protected HashMap<GraphNode, Double> distances = new HashMap<>();

	//maps nodes to their predecessor along the route for their current shortest path
	//e.g: A->B->C->D, the predecessor for D is C, predecessor for C is B, and so on
	protected HashMap<GraphNode, GraphNode> predecessors = new HashMap<>();

	//the current node the algorithm is expanding
	protected GraphNode currentNode;

    /**
	*    Set the start node of the algorithm if it is not running
	*    @param startNodeIn the new start node
	**/
    public void setStartNode(GraphNode startNodeIn){
        if(!running){
    		startNode = startNodeIn;
	    	currentNode = startNode;
			if(nodes != null){
    			for(GraphNode n : nodes){
    				nodeStates.put(n, GraphComponentState.UNVISITED);
    			}
			}
			nodeStates.put(startNodeIn, GraphComponentState.CURRENT);
		}
		else{
			System.out.println("cant set because running");
		}
	}
    /**
	*    @return the start node
	**/
	public GraphNode getStartNode(){
		return startNode;
	}

    /**
	*    @Return a map of distances
	**/
	public HashMap<GraphNode, Double> getDistances(){
		return distances;
	}

	/**
	*    @Return a map of predecessors
	**/
	public HashMap<GraphNode, GraphNode> getPredecessors(){
		return predecessors;
	}

	/**
	*    @Return the current node DSP is expanding
	**/
	public GraphNode getCurrentNode(){
		return currentNode;
	}

	/**
	*    @Param node: the node to find the distance for
	*    @Return the distance DSP has found from the start node to the given node so far
	*    @Return -1 if the distance list is not initialised(ie distance between nodes is unknown)
	*    If called when DSP.finished == true, this will return the shortest distance
	**/
	public double getDistance(GraphNode node){
		if(distances == null){
			return -1;
		}
	    if(distances.containsKey(node)){
			return distances.get(node);
		}
		return -1;
	}

	/**
	*    @Param node: the node to find the predecessor for
	*    @Return: the predecessor node for the route from the start node to the current node
	*    For example, say the route was A->C->D->Z
	*    predecessor(z) -> D
	*    predecessor(d) -> C
	*
	*    predecessor(startNode) is ALWAYS itself
	**/
	public GraphNode getPredecessor(GraphNode node){
		if(predecessors.containsKey(node)){
			return predecessors.get(node);
		}
		return null;
	}


	/**
	*    @Param: the node to get a path for
	*    @Return the shortest path found so far from the start node to the given node
	*    Will return the shortest path if DSP.finished
	*    Format: StartNode, intermediate path, node
	**/
	public ArrayList<GraphNode> getShortestPath(GraphNode node){
		ArrayList<GraphNode> reversedPathList = new ArrayList<>();
		reversedPathList.add(node);
		GraphNode current = getPredecessor(node);
		while(current != startNode){
			reversedPathList.add(current);
			current = getPredecessor(current);
		}
		//If node = start node, do not repeat the start node in the list
		if(!reversedPathList.contains(startNode)){
		    reversedPathList.add(startNode);
		}

		ArrayList output = new ArrayList<>();
		for(int i = reversedPathList.size()-1; i>=0; i--){
			output.add(reversedPathList.get(i));
		}
		return output;
	}

    public boolean canRun(){
        return (startNode != null);
	}

}
