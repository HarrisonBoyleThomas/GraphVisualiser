package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState; 

import java.util.HashMap;
import java.util.ArrayList;

class DijkstraShortestPath extends GraphAlgorithm{
	
	private final GraphNode startNode;
	
	private HashMap<GraphNode, Integer> distances;
	
	private HashMap<GraphNode, GraphNode> predecessors;
	
	protected GraphNode currentNode;
	
	protected ArrayList<GraphNode> nextStates;
	
	protected ArrayList<GraphEdge> currentNodeEdges;
	
	protected ArrayList<GraphNode> visitedNodes;
	
	
	public DijkstraShortestPath(GraphNode initialStateIn, ArrayList<GraphNode> nodesIn){
		startNode = initialStateIn;
		initialise(nodesIn);
	}
	
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
				
				currentNodeEdges = currentNode.getEdges();
				outputString = "All edges from the current state have been considered. Choosing new state";
			}
			else{
				GraphEdge edge = currentNodeEdges.remove(0);
				edge.setState(GraphComponentState.VISITED);
				int newLength = edge.getLength() + distances.get(predecessors.get(edge.nodeB));
				if((newLength < (distances.get(edge.nodeB))) || (distances.get(edge.nodeB) == -1)){
					distances.put(edge.nodeB, newLength);
					predecessors.put(edge.nodeB, currentNode);
					outputString = outputString + "Shorter path to " + edge.nodeB + " found. ";
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
	
	public void initialise(ArrayList<GraphNode> nodesIn){
		distances = new HashMap<>();
		predecessors = new HashMap<>();
		nodes = nodesIn;
		
		for(GraphNode node: nodes){
			if(node.equals(startNode)){
				distances.put(node, 0);
			}
			else{
			    distances.put(node, -1);
			}
			predecessors.put(node, startNode);
		}
		
		currentNode = startNode;
		currentNode.setState(GraphComponentState.CURRENT);
		
		nextStates = currentNode.getConnectedNodes();
		
		currentNodeEdges = currentNode.getEdges();
		
		visitedNodes = new ArrayList<>();
		
		finished = false;
	}
	
	public HashMap<GraphNode, Integer> getDistances(){
		return distances;
	}
	
	public HashMap<GraphNode, GraphNode> getPredecessors(){
		return predecessors;
	}
	
	public GraphNode getCurrentNode(){
		return currentNode;
	}
		
			
		
}