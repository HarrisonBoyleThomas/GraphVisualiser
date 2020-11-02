package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState; 

import java.util.ArrayList;

abstract class GraphAlgorithm{
	protected boolean finished;
	
	protected ArrayList<GraphNode> nodes;
	
	public GraphAlgorithm(){
		finished = false;
	}
	
	/**
	*    initialise the algorithm
	*    @param nodes: all nodes within the graph. Should contain all reachable nodes as a minimum
	**/
	public abstract void initialise(ArrayList<GraphNode> nodes);
	
	/**
	*    Run a single step in the algorithm
	*    @Return a String describing what happened in the step
	**/
	public abstract String step();
	
	/**
	*    Run the algorithm until it is finished
	*    @Return a log of what occurred during execution
	**/
	public String run(){
		String output = "";
		while(!finished){
		    output = output + step() + "\n";
		}
		return output;
	}
	
	/**
	*    Set the state of all nodes
	*    @param stateIn: the state to set all nodes to
	**/
	public void setStateForAllNodes(GraphComponentState stateIn){
		for(GraphNode node : nodes){
			node.setState(stateIn);
		}
	}
}
		