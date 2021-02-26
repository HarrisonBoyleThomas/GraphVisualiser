package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.ArrayList;

public class BreadthFirstSearch extends SearchAlgorithm{
    public BreadthFirstSearch(GraphNode initialStateIn){
        super(initialStateIn);
        name = "Breadth first search";
		description = "Breadth first search searches through the supplied graph from the supplied start node,";
        description += "by adding discovered nodes to a queue and expanding from the oldest element in the queue";
    }

    protected GraphNode removeNext(){
        if(openList.size() == 0){
            return null;
        }
        return openList.remove(0);
    }

    public String[] getPseudocodeLines(){
			return new String[]{"BFS(G, s)",
		            "    initialise()",
                    "    queue = []",
                    "    closedList = []",
				    "    while queue not empty",
				    "        current <- queue.dequeue()",
				    "        for each edge from current",
				    "            if edge.nodeB undiscovered",
                    "                queue.enqueue(edge.nodeB)",
                };
	}
}
