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

public class PrimsAlgorithm extends MinimumSpanningTreeAlgorithm {
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
        description += " spanning tree from an input graph";
        initialise(nodesIn);
    }

    public void setStartNode(GraphNode nodeIn){
        startNode = nodeIn;
        initialise(null);
    }

    public GraphNode getStartNode(){
        return startNode;
    }

    public void initialise(ArrayList<GraphNode> nodesIn){
        currentPseudocodeLines = new int[] {1};
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
            running = false;
            finished = true;
            return "Prims finished";
        }
        String output = "";
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

        ArrayList<GraphNode> tempTree = nodeCopies;

        String s = "St: ";
        for(GraphNode n : tempTree){
            System.out.print(n + ", ");
        }
        if(tempTree.size() == 0){
            return false;
        }
        GraphNode nodeA = nodeCopyMap.get(a);
        GraphNode nodeB = nodeCopyMap.get(b);
        if(nodeA == null || nodeB == null){
            return false;
        }
        return (tempTree.contains(nodeA) || tempTree.contains(nodeB));
        //return (find(tempTree, nodeA, nodeB) || find(tempTree, nodeB, nodeA));
    }

    private boolean find(ArrayList<GraphNode> tempTree, GraphNode startNode, GraphNode targetNode){
        if(tempTree.size() == 0){
            return false;
        }
        if(startNode.equals(targetNode)){
            return true;
        }
        ArrayList<GraphNode> openList = new ArrayList<>();
        ArrayList<GraphNode> closedList = new ArrayList<>();
        openList.add(startNode);
        while(openList.size() > 0){
            GraphNode currentNode = openList.remove(0);
            System.out.print(currentNode + ", ");
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
			return new String[]{"Prims(G, w)",
		            "    T <- ∅",
				    "    while edges.length > 0",
				    "        currentEdge <- edges.dequeue()",
				    "        if no cycle exists in T U currentEdge:",
                    "            T <- T U currentEdge"};
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
