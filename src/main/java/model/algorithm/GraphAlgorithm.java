package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState; 

import java.util.ArrayList;

/**
*    An algorithm to be run on a graph
*    Include a step() method that allows the algorithm to run
*    across multiple frames
*    
*    @Author: Harrison Boyle-Thomas
*    @Date: 02/11/2020
**/
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
		