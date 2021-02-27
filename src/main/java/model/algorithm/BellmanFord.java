package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.HashMap;
import java.util.AbstractCollection;
import java.util.ArrayList;


/**
*    The BF algorithm finds the shortest path from a given start node to all other nodes in the graph
*    This implementation can be run through "steps" between frames
*    To improve performance, this implementation uses a searching strategy to prune unreachable nodes, whereas
*    the original implementation explores all supplied nodes. In essence, this is the same, but the GraphVisualiser
*    allows many unconnected subgraphs to exist, so without pruning DSP may unnecessarily run on unreachable nodes
*    @Author Harrison Boyle-Thomas
*    @Date 02/11/2020
**/
public class BellmanFord extends ShortestPathAlgorithm{

    protected int nodeLoopIndex = 0;

    protected int edgeLoopIndex = 0;

    protected boolean firstStage = true;

    protected ArrayList<GraphEdge> edges = new ArrayList<>();

    public BellmanFord(GraphNode initialStateIn, ArrayList<GraphNode> nodesIn){
        name = "Bellman-Ford algorithm";
		description = "BF computes the shortest paths from a given start node, to all other nodes in a graph";
        nodes = nodesIn;
        setStartNode(initialStateIn);
		initialise(nodesIn);
    }

    public BellmanFord(GraphNode initialStateIn){
        name = "Bellman-Ford algorithm";
		description = "BF computes the shortest paths from a given start node, to all other nodes in a graph";
        setStartNode(initialStateIn);
    }

    public synchronized String step(){
        String output = "";
        if(firstStage){
            running = true;
            currentPseudocodeLines = new int[] {2, 3, 4};
            if(nodeLoopIndex < nodes.size()){
                GraphNode node = nodes.get(nodeLoopIndex);
                nodeStates.put(node, GraphComponentState.CURRENT);
                if(edgeLoopIndex < edges.size()){
                    GraphEdge edge = edges.get(edgeLoopIndex);
                    edgeStates.put(edge, GraphComponentState.CURRENT);
                    if(distances.get(edge.nodeB) > distances.get(edge.nodeA) + edge.getLength() || distances.get(edge.nodeB) == Double.POSITIVE_INFINITY){
                        distances.put(edge.nodeB, distances.get(edge.nodeA) + edge.getLength());
                        System.out.println("insert new distance: " + distances.get(edge.nodeB));
                        predecessors.put(edge.nodeB, edge.nodeA);
                        output = "Shorter path to " + edge.nodeB + " found from " + edge.nodeA + ". ";
                    }
                    edgeLoopIndex++;
                    stepCount++;
                }
                else{
                    output = output + " Evaluated all edges for " + node + ".";
                    nodeLoopIndex++;
                    edgeLoopIndex = 0;
                    stepCount++;
                    nodeStates.put(node, GraphComponentState.VISITED);
                    for(GraphEdge e : edgeStates.keySet()){
                        edgeStates.put(e, GraphComponentState.UNVISITED);
                    }
                }
            }
            else{
                output += " BF finished stage one. Begin negative cycle check";
                firstStage = false;
                edgeLoopIndex = 0;
                for(GraphEdge e : edgeStates.keySet()){
                    edgeStates.put(e, GraphComponentState.UNVISITED);
                }
                for(GraphNode n : nodeStates.keySet()){
                    nodeStates.put(n, GraphComponentState.VISITED);
                }
            }
        }
        else{
            currentPseudocodeLines = new int[] {5, 6, 7, 8};
            if(edgeLoopIndex < edges.size()){
                stepCount++;
                GraphEdge edge = edges.get(edgeLoopIndex);
                output = output + "Evaluating edge " + (edgeLoopIndex + 1) + " / " + edges.size() + ".";
                edgeStates.put(edge, GraphComponentState.IN_OPEN_LIST);
                if(distances.get(edge.nodeB) > distances.get(edge.nodeA) + edge.getLength()){
                    finished = true;
                    running = false;
                    output = "Negative cycle detected, aborting";
                    //highlight the invald cycle
                    GraphNode current = edge.nodeB;
                    boolean endHighlight = false;
                    while(predecessors.get(current) != null && !endHighlight){
                        GraphEdge currentEdge = predecessors.get(current).getEdge(current);
                        if(edgeStates.get(currentEdge) == GraphComponentState.INVALID){
                            endHighlight = true;
                        }
                        edgeStates.put(currentEdge, GraphComponentState.INVALID);
                        current = predecessors.get(current);
                    }
                }
                edgeStates.put(edge, GraphComponentState.VISITED);
                edgeLoopIndex++;
            }
            else{
                finished = true;
                running = false;
                currentPseudocodeLines = new int[] {1};
                for(GraphNode n : predecessors.keySet()){
    				if(predecessors.get(n) != null){
    				    nodeStates.put(n, GraphComponentState.IN_TREE);
    					edgeStates.put(predecessors.get(n).getEdge(n), GraphComponentState.IN_TREE);
    				}
    			}
                output = "No negative cycles detected";
            }
        }
        return output;
    }

    public void initialise(ArrayList<GraphNode> nodesIn){
        stepCount = 0;
        running = false;
        finished = false;
        edgeStates.clear();
        currentPseudocodeLines = new int[] {1};
        edgeLoopIndex = 0;
        edges.clear();
        firstStage = true;
        nodeLoopIndex = 0;
        distances.clear();
        predecessors.clear();
        nodes = nodesIn;
        currentPseudocodeLines = new int[] {1,2};
        nodeStates.clear();
        for(GraphNode n : nodesIn){
            nodeStates.put(n, GraphComponentState.UNVISITED);
            distances.put(n, Double.POSITIVE_INFINITY);
            predecessors.put(n, null);
            edges.addAll(n.getEdges());
            for(GraphEdge e : n.getEdges()){
                edgeStates.put(e, GraphComponentState.UNVISITED);
            }
        }

        nodeStates.put(startNode, GraphComponentState.CURRENT);
        distances.put(startNode, 0.0);
    }

    public String[] getPseudocodeLines(){
			return new String[]{"Bellman-Ford(G, s)",
		            "    initialise()",
				    "    for i <- 1 to |nodes|",
                    "        for each edge",
                    "            relax(edge)",
                    "    for each edge",
                    "        if(distances[edge.nodeB] > distances[edge.nodeA] + edge.weight)",
                    "            return false //negative cycle",
                    "    return true"
                };
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
						    "Main loop iterations: " + stepCount};
	}

}
