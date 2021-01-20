package model;

/**
*    Graph components can be in many different states
*    while being executed by an algorithm
*    The available states should be self-explanatory
**/
public enum GraphComponentState{
	UNVISITED,
	VISITED,
	IN_OPEN_LIST,
	CURRENT
}