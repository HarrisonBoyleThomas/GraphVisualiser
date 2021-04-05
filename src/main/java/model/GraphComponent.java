package model;
import data.UndoRedoController;

import java.io.*;

/**
*    A GraphComponent represents a part of a graph
*    All GraphComponents have a name and an algorithm state
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
abstract class GraphComponent implements Serializable{
	private GraphComponentState state;
	private String name;
	static final long serialVersionUID = 21L;

	public GraphComponent(){
		initialiseState();
		name = "";
	}

	/**
	*    @return the state of the component
	**/
	public GraphComponentState getState(){
		return state;
	}

	/**
	*    update the state of the component
	*    @param stateIn: the new state
	*    @return the new state
	**/
	public GraphComponentState setState(GraphComponentState stateIn){
		state = stateIn;
		return state;
	}

	/**
	*    set the state to unvisited
	**/
	public void initialiseState(){
		state = GraphComponentState.UNVISITED;
	}

	/**
	*    @return the name of the component
	**/
	public String getName(){
		return name;
	}

	/**
	*    update the name of the component
	*    @param nameIn the new name of the component
	*    @return the new name
	**/
	public String setName(String nameIn, boolean saveToUndoStack){
		if(saveToUndoStack){
		    UndoRedoController.pushToUndoStack();
		}
		name = nameIn;
		return name;
	}

	public String toString(){
		return name;
	}
}
