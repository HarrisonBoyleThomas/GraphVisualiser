package model;

/**
*    A GraphEdge represents an edge between two nodes
*    An edge must have an assigned length
*
*    @Author Harrison Boyle-Thomas
*    @Date 30/10/2020
**/
public class GraphEdge extends GraphComponent{
	
	public final GraphNode nodeA;
	public final GraphNode nodeB;
	private int length;
	
	public GraphEdge(GraphNode nodeAIn, GraphNode nodeBIn, int lengthIn){
		nodeA = nodeAIn;
		nodeB = nodeBIn;
		length = setLength(lengthIn);
	}
	
	/**
	*    update the length of the edge. Clamped between 0 and 100000
	*    @param lengthIn: the new length
	*    @return the new length
	**/
	public int setLength(int lengthIn){
		if(lengthIn < 0){
			length = 0;
		}
		else if(lengthIn > 100000){
			length = 100000;
		}
		else{
			length = lengthIn;
		}
		return length;
	}
    
	/**
	*    @return the length of the edge
	**/
    public int getLength(){
        return  length;
	}
	
	/**
	*    @return a GraphNode[] of the endpoints of the edge
	**/
	public GraphNode[] getNodes(){
		return new GraphNode[]{nodeA, nodeB};
	}
	
	/**
	*    Only 1 edge is allowed between nodes
	*    @Return true if the endpoints of the edge are the same, false otherwise
	**/
	public boolean equals(Object otherObject){
		if(otherObject instanceof GraphEdge){
		    GraphEdge edge = (GraphEdge) otherObject;
			return (edge.nodeA.equals(nodeA) && edge.nodeB.equals(nodeB));
		}
		return false;
	}
		
}	