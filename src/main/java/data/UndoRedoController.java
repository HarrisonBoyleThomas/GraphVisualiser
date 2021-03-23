package data;
import model.GraphNode;
import model.GraphEdge;
import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;

import java.util.ArrayList;

public class UndoRedoController{
    private static final ArrayList<ArrayList<VisualGraphNode>> nodeUndoStack = new ArrayList<>();
    private static final ArrayList<ArrayList<VisualGraphNode>> nodeRedoStack = new ArrayList<>();

    public static boolean undo(){
        boolean output = true;
        if(nodeUndoStack.size() == 0){
            return false;
		}
        nodeRedoStack.add(copy(VisualGraphNode.getNodes()));
        ArrayList<VisualGraphNode> nodeToUndo = nodeUndoStack.remove(nodeUndoStack.size()-1);
        VisualGraphNode.setNodes(nodeToUndo);

        VisualGraphEdge.setEdges(new ArrayList<>());
        int edgeCount = 0;
        for(VisualGraphNode n : nodeToUndo){
            n.addEvents();
            ArrayList<GraphEdge> oldEdges = n.getNode().getEdges();
            while(n.getNode().getEdges().size() > 0){
                GraphEdge e = n.getNode().getEdges().get(0);
                n.getNode().removeEdge(e, false);
            }

            for(GraphEdge e : oldEdges){
                GraphNode nodeB = null;
                for(VisualGraphNode node : nodeToUndo){
                    if(node.getNode().equals(e.nodeB)){
                        nodeB = node.getNode();
                        break;
                    }
                }
                GraphEdge newEdge = n.getNode().addEdge(nodeB, false);
                VisualGraphEdge.create(newEdge).addEvents();
                edgeCount++;
            }
        }

        return true;
	}

	public static boolean redo(){
		if(nodeRedoStack.size() == 0){
			return false;
		}
		ArrayList<VisualGraphNode> nodeToRedo = nodeRedoStack.remove(nodeRedoStack.size()-1);
        nodeUndoStack.add(copy(VisualGraphNode.getNodes()));
        VisualGraphNode.setNodes(nodeToRedo);

        VisualGraphEdge.setEdges(new ArrayList<>());
        int edgeCount = 0;
        for(VisualGraphNode n : nodeToRedo){
            n.addEvents();
            ArrayList<GraphEdge> oldEdges = n.getNode().getEdges();
            while(n.getNode().getEdges().size() > 0){
                GraphEdge e = n.getNode().getEdges().get(0);
                n.getNode().removeEdge(e, false);
            }

            for(GraphEdge e : oldEdges){
                GraphNode nodeB = null;
                for(VisualGraphNode node : nodeToRedo){
                    if(node.getNode().equals(e.nodeB)){
                        nodeB = node.getNode();
                        break;
                    }
                }
                GraphEdge newEdge = n.getNode().addEdge(nodeB, false);
                VisualGraphEdge.create(newEdge).addEvents();
                edgeCount++;
            }
        }
		return true;
	}

	public static void pushToUndoStack(){
		nodeRedoStack.clear();
		nodeUndoStack.add(copy(VisualGraphNode.getNodes()));
	}

    private static ArrayList<VisualGraphNode> copy(ArrayList<VisualGraphNode> toCopy){
        ArrayList<VisualGraphNode> nodes = new ArrayList<>();
        for(VisualGraphNode n : toCopy){
            nodes.add((VisualGraphNode) n.deepCopy());
        }
		return nodes;
    }
}
