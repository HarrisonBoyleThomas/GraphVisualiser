package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.ArrayList;

public class DepthFirstSearch extends SearchAlgorithm {

    public DepthFirstSearch(GraphNode initialStateIn){
        super(initialStateIn);
        name = "Depth first search";
        description = "Depth first search searches through the supplied graph from the supplied start node,";
        description += "by adding discovered nodes to a stack and expanding from the newest element in the stack";
    }

    protected GraphNode removeNext(){
        if(openList.size() == 0){
            return null;
        }
        return openList.remove(openList.size()-1);
    }


    public String[] getPseudocodeLines(){
			return new String[]{"BFS(G, s)",
		            "    initialise()",
                    "    stack = []",
                    "    closedList = []",
				    "    while stack not empty",
				    "        current <- stack.pop()",
				    "        for each edge from current",
				    "            if edge.nodeB undiscovered",
                    "                stack.push(edge.nodeB)",
                };
	}
}
