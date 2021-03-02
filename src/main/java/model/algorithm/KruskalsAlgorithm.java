package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;
import model.EdgeComparator;

import java.util.HashMap;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;

public class KruskalsAlgorithm extends MinimumSpanningTreeAlgorithm {
    protected ArrayList<GraphEdge> sortedEdges = new ArrayList<>();

    public KruskalsAlgorithm(ArrayList<GraphNode> nodesIn){
        name = "Kruskal";
        description = "Kruskal's algorithm computes the edges required to form a minimum";
        description += " spanning tree from an input graph.";
        description += "Note: Kruskal's algorithm is intended for undirected graphs, so ";
        description += "the provided implementation assumes that a single edge (a,b) also";
        description += " has an edge (b,a). Please ensure all included bidirectional edges have";
        description += " the same weight";
        initialise(nodesIn);
    }

    public void initialise(ArrayList<GraphNode> nodesIn){
        currentPseudocodeLines = new int[] {1};
        if(nodesIn == null){
            return;
        }
        nodeStates.clear();
        edgeStates.clear();
        nodes = nodesIn;
        edges.clear();
        spanningTree.clear();
        sortedEdges.clear();
        stepCount = 0;
        for(GraphNode n : nodes){
            edges.addAll(n.getEdges());
            sortedEdges.addAll(n.getEdges());
        }
        Collections.sort(sortedEdges, new EdgeComparator());
        finished = false;
		running = false;
    }

    public synchronized String step(){
        if(sortedEdges.size() == 0){
            currentPseudocodeLines = new int[] {1};
            finished = true;
            running = false;
            return "Kruskals finished";
        }
        GraphEdge currentEdge = sortedEdges.remove(0);
        currentPseudocodeLines = new int[] {3,4,5};
        edgeStates.put(currentEdge, GraphComponentState.IN_OPEN_LIST);
        if(!containsCycle(currentEdge)){
            spanningTree.add(currentEdge);
            edgeStates.put(currentEdge, GraphComponentState.IN_TREE);
            nodeStates.put(currentEdge.nodeA, GraphComponentState.IN_TREE);
            nodeStates.put(currentEdge.nodeB, GraphComponentState.IN_TREE);
        }
        else{
            edgeStates.put(currentEdge, GraphComponentState.INVALID);
        }
        stepCount++;
        return "Evaluated edge from " + currentEdge.nodeA + " to " + currentEdge.nodeB;
    }

    public String[] getPseudocodeLines(){
			return new String[]{"Kruskal(G, w)",
		            "    T <- ∅",
				    "    while edges.length > 0",
				    "        currentEdge <- edges.dequeue()",
				    "        if no cycle exists in T U currentEdge:",
                    "            T <- T U currentEdge"};
	}

    /**
    *    @return true if a cycle exists in the spanning tree if the supplied edge is added to it
    *    Constructs a temporary virtual graph with only the edges from the spanning tree, and the
    *    new edge.
    **/
    private boolean containsCycle(GraphEdge newEdge){
        //construct an mst copy out of the current mst
        HashMap<GraphNode, GraphNode> nodeCopyMap = new HashMap<>();
        ArrayList<GraphEdge> newSpanningTree = new ArrayList<>(spanningTree);
        newSpanningTree.add(newEdge);
        ArrayList<GraphNode> nodeCopies = getVirtualMinimumSpanningTreeGraph(newSpanningTree);
        //If the new graph is empty, then there are no cycles
        if(nodeCopies.size() == 0){
            return false;
        }

        //Perform a BFS on the virtual graph. If a previously discovered nodes is rediscovered,
        //then there is a cycle
        GraphNode currentNode = null;
        ArrayList<GraphNode> openList = new ArrayList<>();
        ArrayList<GraphNode> closedList = new ArrayList<>();
        openList.add(nodeCopies.get(0));
        while(openList.size() > 0){
            currentNode = openList.remove(0);
            closedList.add(currentNode);
            for(GraphEdge e : currentNode.getEdges()){
                if(closedList.contains(e.nodeB)){
                    return true;
                }
                else{
                    if(!openList.contains(e.nodeB)){
                        openList.add(e.nodeB);
                    }
                }
            }
        }
        return false;
    }

    public boolean canRun(){
        return true;
    }

    public ArrayList<GraphEdge> getSortedEdges(){
        return new ArrayList<GraphEdge>(sortedEdges);
    }

    public String[] getDetails(){
        String edgeString = "";
        for(GraphEdge e : sortedEdges){
            edgeString = edgeString += "(" + e.nodeA + "," + e.nodeB + "), ";
        }

        return new String[]{"Edge list:" + edgeString};
	}
}
