package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.HashMap;
import java.util.AbstractCollection;
import java.util.ArrayList;


/**
*    The DSP algorithm finds the shortest path from a given start node to all other nodes in the graph
*    This implementation can be run through "steps" between frames
*    To improve performance, this implementation uses a searching strategy to prune unreachable nodes, whereas
*    the original implementation explores all supplied nodes. In essence, this is the same, but the GraphVisualiser
*    allows many unconnected subgraphs to exist, so without pruning DSP may unnecessarily run on unreachable nodes
*    @Author Harrison Boyle-Thomas
*    @Date 02/11/2020
**/
public abstract class DijkstraShortestPath extends ShortestPathAlgorithm{


	//The open list of nodes discovered by the algorithm
	protected AbstractCollection<GraphNode> nextStates;

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
		name = "Dijkstra shortest path";
		description = "Dijkstra shortest path computes the shortest paths from a given start node, to all other nodes in a graph";
		startNode = initialStateIn;
		initialise(nodesIn);
	}

	public DijkstraShortestPath(GraphNode initialStateIn){
		name = "Dijkstra shortest path";
		description = "Dijkstra shortest path computes the shortest paths from a given start node, to all other nodes in a graph";
		startNode = initialStateIn;
	}

	/**
	*    Runs a single step of DSP. If the open list(nextStates) is empty, DSP has searched all nodes -> STOP
	*    if all edges from the current nodes have been explored, pick the next node from the open list
	*    else pick the next edge, and work out if the connecting node's distance is shorter
	*    update distance tables if it is indeed better than the current distance stored for this new node
	*
	*    @Return A String describing what DSP did in the current cycle
	**/
	public synchronized String step(){
		if(finished){
			return "";
		}
		String outputString = "";

       //stop when there is nothing else to explore
		if(nextStates.isEmpty() && currentNodeEdges.isEmpty()){
			finished = true;
			outputString = "Dijkstra has finished";
			running = false;
			visitedNodes.add(currentNode);
			for(GraphNode n : predecessors.keySet()){
				if(predecessors.get(n) != null){
				    nodeStates.put(n, GraphComponentState.IN_TREE);
					edgeStates.put(predecessors.get(n).getEdge(n), GraphComponentState.IN_TREE);
				}
			}
			currentPseudocodeLines = new int[0];
		}
		else{
			running = true;
			if(currentNodeEdges.size() == 0){
				if(currentNode != null){
				    outputString = "All edges from the current state (" + currentNode.getName() + ")";
				    nodeStates.put(currentNode, GraphComponentState.VISITED);
				    visitedNodes.add(currentNode);
				}
				else{
					outputString = "Selecting initial node: " + getNextNode() + " ";
				}
				currentPseudocodeLines = new int[] {3,4};
				currentNode = removeMinimum();
				stepCount++;
				nodeStates.put(currentNode, GraphComponentState.CURRENT);
				currentNodeEdges = new ArrayList<>(currentNode.getEdges());
				outputString += " have been considered. Choosing new state (" + currentNode.getName() + ")";
			}
			else{
				currentPseudocodeLines = new int[] {6};
				GraphEdge edge = currentNodeEdges.remove(0);
				edgeStates.put(edge, GraphComponentState.VISITED);
				//If the current node does not exist in the list of expected nodes,
				//add the node to the distance list
				if(distances.get(currentNode) == null){
					nodes.add(currentNode);
					distances.put(currentNode, -1.0);
					predecessors.put(currentNode, startNode);
				}
				double newLength = edge.getLength() + distances.get(currentNode);
				if((newLength < (distances.get(edge.nodeB))) || (distances.get(edge.nodeB) == -1)){
					distances.put(edge.nodeB, newLength);
					predecessors.put(edge.nodeB, currentNode);
					outputString = outputString + "Shorter path to " + edge.nodeB + " found from " + currentNode + ". ";
				}
				if(!visitedNodes.contains(edge.nodeB) && !nextStates.contains(edge.nodeB)){
					addDiscoveredNode(edge.nodeB);
					nodeStates.put(edge.nodeB, GraphComponentState.IN_OPEN_LIST);
					outputString += "New state discovered (" + edge.nodeB.getName() + ")- adding to open list";
				}
			}
		}
		return outputString;

	}

	/**
	*    Initialise variables
	**/
	public void initialise(ArrayList<GraphNode> nodesIn){
		stepCount = 0;
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
				distances.put(node, 0.0);
			}
			else{
			    distances.put(node, -1.0);
			}
			predecessors.put(node, null);
			nodeStates.put(node, GraphComponentState.UNVISITED);
		}
		edgeStates.clear();

		//current node is initially the start node
		currentNode = null;//startNode;
		nodeStates.put(currentNode, GraphComponentState.CURRENT);

		//nodes initially in the open list are nodes connected to the start node
		initialiseNextStates();
		addDiscoveredNode(startNode);
		//copy the array so that unwanted edits are not made to the model
		currentNodeEdges = new ArrayList<>();//new ArrayList<>(currentNode.getEdges());

		//list of nodes visited by DSP
		visitedNodes = new ArrayList<>();
		//visitedNodes.add(currentNode);

		finished = false;
		running = false;
		currentPseudocodeLines = new int[] {2};
	}

    /**
	*    Creates the next states data structure to vary/improve performance
	**/
	protected abstract void initialiseNextStates();

    /**
	*    @Return the next node to be expanded"
	**/
	protected abstract GraphNode getNextNode();

    /**
	*    Remove and return the next node from the open list
	*    @Return the next node from the open list
	**/
	protected abstract GraphNode removeMinimum();

    /**
	*    Add a newly-discovered node to the open list
	*    @Param the discovered node
	**/
	protected abstract void addDiscoveredNode(GraphNode toAdd);

	public ArrayList<GraphNode> getNextStates(){
		return new ArrayList<GraphNode>(nextStates);
	}

	public ArrayList<GraphNode> getVisitedNodes(){
		return new ArrayList<GraphNode>(visitedNodes);
	}

	public String[] getDetails(){
		String start = "Not set";
		if(startNode != null){
			start = startNode.getName();
		}

		String current = "Not set";
		if(currentNode != null){
			current = currentNode.getName();
		}
		String state = "";
		if(stepCount == 1){
			state = "Not started";
		}
		else{
	    	if(finished){
    			state = "Finished";
    		}
    		else{
    			state = "Not finished";
    		}
		}
		String distanceString = "{";
		for(GraphNode n : distances.keySet()){
			distanceString += (n.getName() + "=");
			double distance = distances.get(n);
			if(distance < 0){
			    distanceString += '\u221e';
			}
			else{
				distanceString += distance;
			}
			distanceString += ", ";
		}
		distanceString += "}";


        return new String[]{"Distances:      " + distanceString,
			                "Predecessors: " + predecessors,
						    "OpenList:  " + nextStates,
						    "ClosedList:" + visitedNodes,
						    "Main loop iterations: " + stepCount};
	}

	public String[] getPseudocodeLines(){
			return new String[]{"Dijkstra(G, w, s)",
		            "    initialise()",
				    "    while nextStates.length > 0",
				    "        current <- EXTRACTMIN(nextStates)",
				    "        visited.add(current)",
				    "        for each edge from current",
				    "            RELAX(edge)"};
	}

}
