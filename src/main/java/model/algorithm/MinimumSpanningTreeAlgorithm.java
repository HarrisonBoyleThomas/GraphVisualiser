package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.HashMap;
import java.util.AbstractCollection;
import java.util.ArrayList;

public abstract class MinimumSpanningTreeAlgorithm extends GraphAlgorithm {
    protected ArrayList<GraphEdge> spanningTree = new ArrayList<>();
    protected ArrayList<GraphEdge> edges = new ArrayList<>();

    public ArrayList<GraphEdge> getSpanningTree(){
        return new ArrayList<GraphEdge>(spanningTree);
    }

    protected ArrayList<GraphNode> getVirtualMinimumSpanningTreeGraph(ArrayList<GraphEdge> treeIn){
        //construct an mst copy out of the current mst
        HashMap<GraphNode, GraphNode> nodeCopyMap = new HashMap<>();
        ArrayList<GraphNode> nodeCopies = new ArrayList<>();
        //Construct a virtual graph out of the spanning tree
        for(GraphEdge e : treeIn){
            GraphNode nodeACopy = null;
            if(nodeCopyMap.keySet().contains(e.nodeA)){
                nodeACopy = nodeCopyMap.get(e.nodeA);
            }
            else{
                nodeACopy = new GraphNode(0);
                nodeACopy.setName("copyname", false);
                nodeCopyMap.put(e.nodeA, nodeACopy);
                nodeCopies.add(nodeACopy);
            }
            GraphNode nodeBCopy = null;
            if(nodeCopyMap.keySet().contains(e.nodeB)){
                nodeBCopy = nodeCopyMap.get(e.nodeB);
            }
            else{
                nodeBCopy = new GraphNode(0);
                nodeBCopy.setName("copynodeb", false);
                nodeCopyMap.put(e.nodeB, nodeBCopy);
                nodeCopies.add(nodeBCopy);
            }
            nodeACopy.addEdge(nodeBCopy, true);
        }
        return nodeCopies;
    }

}
