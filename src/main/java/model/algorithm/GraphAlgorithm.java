package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState; 

import java.util.ArrayList;
import java.util.HashMap;

/**
*    An algorithm to be run on a graph
*    Include a step() method that allows the algorithm to run
*    across multiple frames
*    
*    @Author: Harrison Boyle-Thomas
*    @Date: 02/11/2020
**/
public abstract class GraphAlgorithm{
	protected boolean finished;
	
	protected ArrayList<GraphNode> nodes;
	
	protected String name;
	
	protected HashMap<GraphNode, GraphComponentState> nodeStates = new HashMap<>();
	
	protected HashMap<GraphEdge, GraphComponentState> edgeStates = new HashMap<>();
	
	protected int stepCount;
	
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
			nodeStates.put(node, stateIn);
		}
	}
	
	public GraphComponentState getNodeState(GraphNode node){
		return nodeStates.get(node);
	}
	
	public GraphComponentState getEdgeState(GraphEdge edge){
		return edgeStates.get(edge);
	}
	
	public boolean getFinished(){
		return finished;
	}
	
	public String toString(){
		return name;
	}
	
	public abstract String[] getDetails();
}
		