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
    //State of discovered nodes during the algorithm's execution
	protected HashMap<GraphNode, GraphComponentState> nodeStates = new HashMap<>();
    //State of discovered edges during the algorithm's execution
	protected HashMap<GraphEdge, GraphComponentState> edgeStates = new HashMap<>();
    //number of iterations of the main loop
	protected int stepCount = 1;

	protected boolean running = false;
    //The lines that are being considered by the algorithm. A list of line is required, because
	//Algorithm execution can effectively execute multiple lines in one loop(too quick for the user to see)
	protected int[] currentPseudocodeLines = new int[0];

	public GraphAlgorithm(){
		finished = false;
	}
    /**
	*    Mark the algorithm as running
	**/
	public void start(){
		running = true;
	}
    /**
	*    Mark the algorithm as not running
	**/
	public void stop(){
		running = false;
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
    /**
	*    @param node the node to get the state for
	*    @return the state of the node stored by the algorithm
	**/
	public GraphComponentState getNodeState(GraphNode node){
		return nodeStates.get(node);
	}
    /**
	*    @param edge the edge to get the state for
	*    @return the state of the edge stored by the algorithm
	**/
	public GraphComponentState getEdgeState(GraphEdge edge){
		return edgeStates.get(edge);
	}
    /**
	*     @return the name of the algorithm
	**/
	public String toString(){
		return name;
	}
    /**
	*    @return the description of the algorithm
	**/
	public String getDescription(){
		return description;
	}

	public String getName(){
		return name;
	}
    /**
	*    @return true if the algorithm is marked as running
	**/
	public boolean isRunning(){
		return running;
	}
    /**
	*    @return true if the algorithm is marked as finished
	**/
	public boolean isFinished(){
		return finished;
	}

    /**
	*    @return an array containing a representation of each component of the algorithm
	**/
	public abstract String[] getDetails();

    /**
	*    @return true if the algorithm is in a legal running state
	**/
	public abstract boolean canRun();

    /**
	*    Ends the algorithm so no more execution steps are possible
	**/
	public void terminate(){
		finished = true;
		running = false;
	}

    /**
	*    @return the pseudocode of the algorithm, where each element of the array is one line
	**/
	public abstract String[] getPseudocodeLines();

    /**
	*    @return an array of lines that are being considered by the algorithm
	**/
	public int[] getCurrentPseudocodeLines(){
		return currentPseudocodeLines;
	}
}
