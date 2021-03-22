package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;
import model.EdgeComparator;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;

public class PrimsAlgorithm extends MinimumSpanningTreeAlgorithm implements RootNodeAlgorithm{
    protected PriorityQueue<GraphEdge> edgeQueue;
    protected ArrayList<GraphEdge> evaluatedEdges = new ArrayList<>();
    protected GraphNode startNode;

    public PrimsAlgorithm(ArrayList<GraphNode> nodesIn){
        System.out.println("constructor called");
        int initialSize = 1;
        if(nodesIn != null){
            initialSize = nodesIn.size();
        }
        edgeQueue = new PriorityQueue<GraphEdge>(initialSize, new EdgeComparator());
        name = "Prims";
        description = "Prim's algorithm computes the edges required to form a minimum";
        description += " spanning tree from an input graph.";
        description += "Note: Prim's algorithm is intended for undirected graphs, so ";
        description += "the provided implementation assumes that a single edge (a,b) also";
        description += " has an edge (b,a). Please ensure all included bidirectional edges have";
        description += " the same weight";
        initialise(nodesIn);
    }

    public void setStartNode(GraphNode nodeIn){
        startNode = nodeIn;
        initialise(null);
    }

    public GraphNode getStartNode(){
        return startNode;
    }

    public PriorityQueue<GraphEdge> getEdges(){
        return edgeQueue;
    }

    public void initialise(ArrayList<GraphNode> nodesIn){
        currentPseudocodeLines = new int[] {1, 2, 3, 4};
        nodeStates.clear();
        edgeStates.clear();
        spanningTree.clear();
        nodes = nodesIn;
        stepCount = 0;
        finished = false;
		running = false;
        if(startNode != null){
            edgeQueue.clear();
            edgeQueue.addAll(startNode.getEdges());
            evaluatedEdges.clear();
            nodeStates.put(startNode, GraphComponentState.CURRENT);
        }
    }

    public synchronized String step(){
        if(edgeQueue.size() == 0){
            currentPseudocodeLines = new int[] {1, 2, 3, 4};
            running = false;
            finished = true;
            return "Prims finished";
        }
        String output = "";
        currentPseudocodeLines = new int[] {6, 7, 8, 9, 10};
        GraphEdge currentEdge = edgeQueue.peek();
        edgeStates.put(currentEdge, GraphComponentState.IN_OPEN_LIST);
        edgeQueue.remove(currentEdge);
        if(!areConnected(currentEdge.nodeA, currentEdge.nodeB)){
            spanningTree.add(currentEdge);
            edgeStates.put(currentEdge, GraphComponentState.IN_TREE);
            nodeStates.put(currentEdge.nodeA, GraphComponentState.IN_TREE);
            nodeStates.put(currentEdge.nodeB, GraphComponentState.IN_TREE);
            for(GraphEdge e : currentEdge.nodeA.getEdges()){
                if(!e.equals(currentEdge) && !evaluatedEdges.contains(e) && !edgeQueue.contains(e)){
                    edgeQueue.add(e);
                }
            }
            for(GraphEdge e : currentEdge.nodeB.getEdges()){
                if(!evaluatedEdges.contains(e) && !edgeQueue.contains(e)){
                    edgeQueue.add(e);
                }
            }
            output = "Add (" + currentEdge.nodeA + ", " + currentEdge.nodeB + ") to spanning tree";
        }
        else{
            edgeStates.put(currentEdge, GraphComponentState.INVALID);
            output = "Evaluated, but did not add (" + currentEdge.nodeA + ", " + currentEdge.nodeB + ") to spanning tree";
        }
        evaluatedEdges.add(currentEdge);
        stepCount++;
        return output;
    }

    private boolean areConnected(GraphNode a, GraphNode b){
        //construct an mst copy out of the current mst
        HashMap<GraphNode, GraphNode> nodeCopyMap = new HashMap<>();
        ArrayList<GraphNode> nodeCopies = new ArrayList<>();
        //Construct a virtual graph out of the spanning tree
        for(GraphEdge e : spanningTree){
            GraphNode nodeACopy = null;
            if(nodeCopyMap.keySet().contains(e.nodeA)){
                nodeACopy = nodeCopyMap.get(e.nodeA);
            }
            else{
                nodeACopy = new GraphNode(0);
                nodeACopy.setName("copynode" + e.nodeA.getName(), false);
                nodeCopyMap.put(e.nodeA, nodeACopy);
                nodeCopies.add(nodeACopy);
            }
            GraphNode nodeBCopy = null;
            if(nodeCopyMap.keySet().contains(e.nodeB)){
                nodeBCopy = nodeCopyMap.get(e.nodeB);
            }
            else{
                nodeBCopy = new GraphNode(0);
                nodeBCopy.setName("copynode:" + e.nodeB.getName(), false);
                nodeCopyMap.put(e.nodeB, nodeBCopy);
                nodeCopies.add(nodeBCopy);
            }
            nodeACopy.addEdge(nodeBCopy, true);
        }

        ArrayList<GraphNode> tempTree = nodeCopies;

        String s = "St: ";
        if(tempTree.size() == 0){
            return false;
        }
        GraphNode nodeA = nodeCopyMap.get(a);
        GraphNode nodeB = nodeCopyMap.get(b);
        if(nodeA == null || nodeB == null){
            return false;
        }
        return (find(nodeA, nodeB) || find(nodeB, nodeA));
    }

    private boolean find(GraphNode startNode, GraphNode targetNode){
        if(startNode.equals(targetNode)){
            return true;
        }
        ArrayList<GraphNode> openList = new ArrayList<>();
        ArrayList<GraphNode> closedList = new ArrayList<>();
        openList.add(startNode);
        while(openList.size() > 0){
            GraphNode currentNode = openList.remove(0);
            for(GraphEdge e : currentNode.getEdges()){
                if(e.nodeB.equals(targetNode)){
                    return true;
                }
                if(!openList.contains(e.nodeB) && !closedList.contains(e.nodeB)){
                    openList.add(e.nodeB);
                }
            }
            closedList.add(currentNode);
        }
        return false;
    }


    public String[] getPseudocodeLines(){
			return new String[]{"Prims(G, w, startNode)",
		            "    T <- ∅",
                    "    Q <- priorityQueue()",
                    "    for each edge from startNode",
                    "        Q.add(edge)",
				    "    while Q.length > 0",
				    "        edge <- Q.dequeue()",
				    "        if edge.nodeA and edge.nodeB disconnected in T",
                    "            T <- T U edge",
                    "            Q.addUnique(a.edges())",
                    "            Q.addUnique(b.edges())"
                };
	}

    public boolean canRun(){
        return startNode != null;
    }

    public String[] getDetails(){
        String edgeString = "";
        for(GraphEdge e : edgeQueue){
            edgeString = edgeString += "(" + e.nodeA + "," + e.nodeB + "), ";
        }

        return new String[]{"Edge list:" + edgeString};
	}
}
