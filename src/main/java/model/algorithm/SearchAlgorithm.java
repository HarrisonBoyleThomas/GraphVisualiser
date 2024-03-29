package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.ArrayList;

public abstract class SearchAlgorithm extends GraphAlgorithm  implements RootNodeAlgorithm{
    protected GraphNode startNode = null;
    protected ArrayList<GraphNode> openList = new ArrayList<>();
    protected ArrayList<GraphNode> closedList = new ArrayList<>();
    protected GraphNode currentNode;
    protected ArrayList<GraphEdge> currentNodeEdges = new ArrayList<>();

    public SearchAlgorithm(GraphNode initialStateIn){
        setStartNode(initialStateIn);
        openList.clear();
        closedList.clear();
    }

    /**
	*    Set the start node of the algorithm if it is not running
	*    @param startNodeIn the new start node
	**/
    public void setStartNode(GraphNode startNodeIn){
        if(!running){
            currentPseudocodeLines = new int[] {1,2};
    		startNode = startNodeIn;
	    	currentNode = startNode;
			if(nodes != null){
    			for(GraphNode n : nodes){
    				nodeStates.put(n, GraphComponentState.UNVISITED);
    			}
			}
			nodeStates.put(startNodeIn, GraphComponentState.CURRENT);
		}
		else{
			System.out.println("cant set because running");
		}
	}

    /**
	*    @return the start node
	**/
	public GraphNode getStartNode(){
		return startNode;
	}

    /**
	*    @Return the current node being expanded
	**/
	public GraphNode getCurrentNode(){
		return currentNode;
	}

    public void initialise(ArrayList<GraphNode> nodesIn){
        currentPseudocodeLines = new int[] {1,2};
        nodeStates.clear();
        edgeStates.clear();
        currentNode = startNode;
        for(GraphNode n : nodesIn){
            nodeStates.put(n, GraphComponentState.UNVISITED);
        }
        nodeStates.put(startNode, GraphComponentState.CURRENT);
        openList.clear();
        closedList.clear();
        if(startNode != null){
            openList.add(startNode);
        }
        running = false;
        finished = false;
        currentNodeEdges.clear();

    }


    public synchronized String step(){
        running = true;
        if(currentNodeEdges.size() == 0){
            stepCount++;
            nodeStates.put(currentNode, GraphComponentState.VISITED);
            closedList.add(currentNode);
            currentNode = removeNext();
            if(currentNode == null){
                finished = true;
                running = false;
                currentPseudocodeLines = new int[] {0};
                for(GraphNode n : nodeStates.keySet()){
                    nodeStates.put(n, GraphComponentState.IN_TREE);
                }
                for(GraphEdge e : edgeStates.keySet()){
                    if(edgeStates.get(e) != GraphComponentState.IN_TREE){
                        edgeStates.put(e, GraphComponentState.UNVISITED);
                    }
                }
                nodeStates.put(startNode, GraphComponentState.CURRENT);
                return "Finished";
            }
            nodeStates.put(currentNode, GraphComponentState.CURRENT);
            currentPseudocodeLines = new int[] {5};
            currentNodeEdges = currentNode.getEdges();
            return "Choose next node " + currentNode.getName();
        }
        else{
            currentPseudocodeLines = new int[] {6,7,8};
            GraphEdge edge = currentNodeEdges.remove(0);
            GraphNode nextNode = edge.nodeB;
            if(!closedList.contains(nextNode) && !openList.contains(nextNode)){
                edgeStates.put(edge, GraphComponentState.IN_TREE);
                openList.add(nextNode);
                nodeStates.put(nextNode, GraphComponentState.IN_OPEN_LIST);
                return "Added " + nextNode.getName() + " to the open list";
            }
            else{
                edgeStates.put(edge, GraphComponentState.VISITED);
                return nextNode.getName() + " already discovered, skipping";
            }
        }
    }

    protected abstract GraphNode removeNext();

    public String[] getDetails(){
		String output = "";


        return new String[]{output};
	}

    public ArrayList<GraphNode> getOpenList(){
        return new ArrayList<GraphNode>(openList);
    }

    public ArrayList<GraphNode> getClosedList(){
        return new ArrayList<GraphNode>(closedList);
    }


    public boolean canRun(){
        return (startNode != null);
	}
}
