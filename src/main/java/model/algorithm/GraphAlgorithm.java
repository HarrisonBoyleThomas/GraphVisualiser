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

	protected String description;

	protected HashMap<GraphNode, GraphComponentState> nodeStates = new HashMap<>();

	protected HashMap<GraphEdge, GraphComponentState> edgeStates = new HashMap<>();

	protected int stepCount = 1;

	protected boolean running = false;

	protected int[] currentPseudocodeLines = new int[0];

	public GraphAlgorithm(){
		finished = false;
	}

	public void start(){
		running = true;
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

    public int getIterationStep(){
		return stepCount;
	}

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

	public String toString(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public boolean isRunning(){
		return running;
	}

	public boolean isFinished(){
		return finished;
	}

	public abstract String[] getDetails();

	public abstract boolean canRun();

    /**
	*    Ends the algorithm so no more execution steps are possible
	**/
	public void terminate(){
		finished = true;
	}

	public abstract String[] getPseudocodeLines();

	public int[] getCurrentPseudocodeLines(){
		return currentPseudocodeLines;
	}
}
