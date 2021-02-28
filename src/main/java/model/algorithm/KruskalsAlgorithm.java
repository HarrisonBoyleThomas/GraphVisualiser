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
    protected ArrayList<GraphEdge> spanningTree = new ArrayList<>();

    public KruskalsAlgorithm(ArrayList<GraphNode> nodesIn){
        name = "Kruskal";
        initialise(nodesIn);
    }

    public void initialise(ArrayList<GraphNode> nodesIn){
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
            finished = true;
            running = false;
            return "Kruskals finished";
        }
        GraphEdge currentEdge = sortedEdges.remove(0);
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
		            "    T <- EMPTYSET",
				    "    while edges.length > 0",
				    "        currentEdge <- edges.dequeue()",
				    "        if no cycle exists in the T UNION currentEdge:",
                    "            T ,_ T UNION currentEdge"};
	}

    private boolean containsCycle(GraphEdge newEdge){
        //construct an mst copy out of the current mst
        HashMap<GraphNode, GraphNode> nodeCopyMap = new HashMap<>();
        ArrayList<GraphNode> nodeCopies = new ArrayList<>();
        ArrayList<GraphEdge> newSpanningTree = new ArrayList<>(spanningTree);
        newSpanningTree.add(newEdge);
        for(GraphEdge e : newSpanningTree){
            GraphNode nodeACopy = null;
            if(nodeCopyMap.keySet().contains(e.nodeA)){
                nodeACopy = nodeCopyMap.get(e.nodeA);
            }
            else{
                nodeACopy = new GraphNode(0);
                nodeACopy.setName("copyname");
                nodeCopyMap.put(e.nodeA, nodeACopy);
                nodeCopies.add(nodeACopy);
            }
            GraphNode nodeBCopy = null;
            if(nodeCopyMap.keySet().contains(e.nodeB)){
                nodeBCopy = nodeCopyMap.get(e.nodeB);
            }
            else{
                nodeBCopy = new GraphNode(0);
                nodeBCopy.setName("copynodeb");
                nodeCopyMap.put(e.nodeB, nodeBCopy);
                nodeCopies.add(nodeBCopy);
            }
            nodeACopy.addEdge(nodeBCopy, false);
        }
        if(nodeCopies.size() == 0){
            return false;
        }
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

    public String[] getDetails(){

        return new String[]{"Distances:      "};
	}
}
