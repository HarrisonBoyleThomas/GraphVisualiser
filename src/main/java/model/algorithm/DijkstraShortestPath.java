package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState; 

import java.util.HashMap;
import java.util.ArrayList;


/**
*    The DSP algorithm finds the shortest path from a given start node to all other nodes in the graph
*    This implementation can be run through "steps" between frames
*    @Author Harrison Boyle-Thomas
*    @Date 02.11.2020
**/
public class DijkstraShortestPath extends GraphAlgorithm{
	
	
	
	//the node to find the distance from
	private final GraphNode startNode;
	
	//maps nodes to their distances from the start node
	private HashMap<GraphNode, Integer> distances;
	
	//maps nodes to their predecessor along the route for their current shortest path
	//e.g: A->B->C->D, the predecessor for D is C, predecessor for C is B, and so on
	private HashMap<GraphNode, GraphNode> predecessors;
	
	//the current node the algorithm is expanding
	protected GraphNode currentNode;
	
	//The open list of nodes discovered by the algorithm
	protected ArrayList<GraphNode> nextStates;
	
	//A list of edges from the current node
	//used to find new nodes
	protected ArrayList<GraphEdge> currentNodeEdges;
	
	//A list of nodes that have been visited/discovered
	//Because visited nodes are removed from the open list, a separate list must be
	//maintained to correctly update node states. For example, say node A has been VISITED already
	//If a new path to A is found from node G, then A's state should not be set to DISCOVERED, and should
	//stay set to VISITED. Without this variable, this would not be possible
	protected ArrayList<GraphNode> visitedNodes;
	
	
	/**
	*    Takes a start node and a list of nodes in the graph
	*    @param initialStateIn: the node to find distances from
	*    @param nodesIn: a list of all nodes in the graph. The list should contain all reachable nodes as a minimum.
	*                   Failure to include reachable nodes in this list will result in the step() function having to add
	*                   unexpected nodes to it's lists, which can impact performance
	**/
	public DijkstraShortestPath(GraphNode initialStateIn, ArrayList<GraphNode> nodesIn){
		name = "Dijkstra shortest path (array)";
		startNode = initialStateIn;
		initialise(nodesIn);
	}
	
	/**
	*    Runs a single step of DSP. If the open list(nextStates) is empty, DSP has searched all nodes -> STOP
	*    if all edges from the current nodes have been explored, pick the next node from the open list
	*    else pick the next edge, and work out if the connecting node's distance is shorter
	*    update distance tables if it is indeed better than the current distance stored for this new node
	*
	*    @Return A String describing what DSP did in the current cycle
	**/
	public String step(){
		String outputString = "";
		
		if(nextStates.isEmpty()){
			finished = true;
			outputString = "Dijkstra has finished";
		}
		else{
			if(currentNodeEdges.size() == 0){
				currentNode.setState(GraphComponentState.VISITED);
				currentNode = nextStates.remove(0);
				currentNode.setState(GraphComponentState.CURRENT);
				
				currentNodeEdges = new ArrayList<>(currentNode.getEdges());
				outputString = "All edges from the current state have been considered. Choosing new state";
			}
			else{
				GraphEdge edge = currentNodeEdges.remove(0);
				edge.setState(GraphComponentState.VISITED);
				//If the current node does not exist in the list of expected nodes,
				//add the node to the distance list
				if(distances.get(currentNode) == null){
					nodes.add(currentNode);
					distances.put(currentNode, -1);
					predecessors.put(currentNode, startNode);
				}
				int newLength = edge.getLength() + distances.get(currentNode);
				if((newLength < (distances.get(edge.nodeB))) || (distances.get(edge.nodeB) == -1)){
					distances.put(edge.nodeB, newLength);
					predecessors.put(edge.nodeB, currentNode);
					outputString = outputString + "Shorter path to " + edge.nodeB + " found from " + currentNode + ". ";
				}
				if(!visitedNodes.contains(edge.nodeB)){
					visitedNodes.add(edge.nodeB);
					nextStates.add(edge.nodeB);
					edge.nodeB.setState(GraphComponentState.IN_OPEN_LIST);
					outputString += "New state discovered- adding to open list";
				}
			}
		}
		return outputString;
					
	}
	
	/**
	*    Initialise variables
	**/
	public void initialise(ArrayList<GraphNode> nodesIn){
		if(nodesIn == null){
			return;
		}
		distances = new HashMap<>();
		predecessors = new HashMap<>();
		nodes = nodesIn;
		
		//initialise distances to be -1 for all nodes except startNode, which is set to 0
		//(distance from x to x is 0)
		//the initial predecessors for all nodes is the start node
		for(GraphNode node: nodes){
			if(node.equals(startNode)){
				distances.put(node, 0);
			}
			else{
			    distances.put(node, -1);
			}
			predecessors.put(node, startNode);
		}
		
		//current node is initially the start node
		currentNode = startNode;
		currentNode.setState(GraphComponentState.CURRENT);
		
		//nodes initially in the open list are nodes connected to the start node
		nextStates = currentNode.getConnectedNodes();
		
		//copy the array so that unwanted edits are not made to the model
		currentNodeEdges = new ArrayList<>(currentNode.getEdges());
		
		//list of nodes visited by DSP
		visitedNodes = new ArrayList<>();
		visitedNodes.add(currentNode);
		
		finished = false;
	}
	
	/**
	*    @Return a map of distances
	**/
	public HashMap<GraphNode, Integer> getDistances(){
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
	*    If called when DSP.finished == true, this will return the shortest distance
	**/
	public int getDistance(GraphNode node){
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
			
			
		
			
		
}