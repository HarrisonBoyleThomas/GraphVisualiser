package model;
import data.UndoRedoController;

import java.util.ArrayList;


/**
*    A GraphNode is a node in a graph. It can have connections to other nodes
*    through the GraphEdge class. Nodes can have an int value assigned to it
*    Edges are all directed. To have an undirected edge between nodes, create
*    two directed edges from either edge

*    @Author Harrison Boyle-Thomas
*    @Date 30/10/2020
**/
public class GraphNode extends GraphComponent{
	private int value;
	private ArrayList<GraphEdge> edges;
	private int uniqueId = hashCode();

	public GraphNode(){
		value = 0;
		edges = new ArrayList<>();
		setName("New node", false);
	}

	public GraphNode(int valueIn){
		value = valueIn;
		edges = new ArrayList<>();
		setName("New node", false);
	}

	public int getUniqueId(){
		return uniqueId;
	}

    public void updateId(){
		uniqueId = hashCode();
	}

	/**
	*    update the value of the node to the new value
	*    @valueIn: the new value
	*    @return the new value
	**/
	public int setValue(int valueIn, boolean saveToUndoStack){
		if(saveToUndoStack){
	    	UndoRedoController.pushToUndoStack();
		}
		value = valueIn;
		System.out.println("set value push");
		return value;
	}

	/**
	*    @return the value of the node
	**/
	public int getValue(){
		return value;
	}

	/**
	*    add an edge between the supplied graph node. If bidirectional,
	*    add an edge from the suppied graph node to this graph node
	*    Ensures only 1 edge from A to B exists at a time
	*    @return the created edge, or null if params invalid or an edge already exists
	**/
	public GraphEdge addEdge(GraphNode other, boolean biDirectional){
		if (other == null){
			return null;
		}
		if(biDirectional){
			other.addEdge(this, false);
		}
		GraphEdge newEdge = new GraphEdge(this, other, 1);
		//if there is not already an edge between nodes, add the edge
		if(!edges.contains(newEdge)){
		    edges.add(newEdge);
			return newEdge;
		}
		return null;
	}

	/**
	*    Remove the supplied edge from the edge list
	*    @param toRemove: the edge to remove
	*    @param biDirectional: remove an edge that is from the opposite direction to the supplied edge
	*    @return true if successful, false if the given element was not in the list
	**/
	public boolean removeEdge(GraphEdge toRemove, boolean biDirectional){
		if(toRemove != null){
		    if(biDirectional){
	        	toRemove.nodeB.removeEdge(this, false);
		    }
		}
		return edges.remove(toRemove);
	}

	/**
	*    Remove an edge from this node to the supplied node
	**/
	public boolean removeEdge(GraphNode toRemove, boolean biDirectional){
		GraphEdge edge = getEdge(toRemove);
		if(edge != null){
			if(biDirectional){
				toRemove.removeEdge(this, false);
			}
		}
		return edges.remove(edge);
	}

	/**
	*    @return the edge list
	**/
	public ArrayList<GraphEdge> getEdges(){
		return new ArrayList<>(edges);
	}


	/**
	*    @param target the node to get the edge to
	*    @return an edge from this node to the supplied node or null if no edge exists
	**/
	public GraphEdge getEdge(GraphNode target){
		for(GraphEdge edge : edges){
			if(edge.nodeB.equals(target)){
				return edge;
			}
		}
		return null;
	}


	/**
	*    @param name of the edge to find
	*    @return an edge from this node that has the given name, or null if not found
	**/
	public GraphEdge getEdgeByName(String name){
	    for(GraphEdge edge : edges){
			if(edge.getName().equals(name)){
				return edge;
			}
		}
		return null;
    }

	/**
	*    @return a list of nodes this node has edges within
	**/
    public ArrayList<GraphNode> getConnectedNodes(){
		ArrayList<GraphNode> output = new ArrayList<>();
	    for(GraphEdge edge: edges){
			output.add(edge.nodeB);
		}
		return output;
	}

	/**
	*    Removes all references to this node within the graph model
	**/
	public void delete(){
		for(GraphEdge edge : edges){
			edge.nodeB.removeEdge(this, false);
		}
		edges.clear();
	}

    @Override
	public boolean equals(Object other){
		if(other instanceof GraphNode){
			return uniqueId == ((GraphNode) other).getUniqueId();
		}
		return false;
	}
}
