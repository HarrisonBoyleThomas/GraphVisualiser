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
        ArrayList<VisualGraphNode> oldState = nodeUndoStack.remove(nodeUndoStack.size()-1);
        nodeRedoStack.add(VisualGraphNode.getNodes());
        if(nodeUndoStack.size() == 0){
            output = false;
			nodeUndoStack.add(new ArrayList<VisualGraphNode>());
		}
        ArrayList<VisualGraphNode> nodeToUndo = nodeUndoStack.get(nodeUndoStack.size()-1);
        VisualGraphNode.setNodes(nodeToUndo);

        VisualGraphEdge.setEdges(new ArrayList<>());
        int edgeCount = 0;
        for(VisualGraphNode n : nodeToUndo){
            for(GraphEdge e : n.getNode().getEdges()){
                VisualGraphEdge.create(e);
                edgeCount++;
            }
        }
        return output;
	}

	public static boolean redo(){
		if(nodeRedoStack.size() == 0){
			return false;
		}
        System.out.println("redo size: " + nodeRedoStack.size());
		ArrayList<VisualGraphNode> nodeToRedo = nodeRedoStack.remove(nodeRedoStack.size()-1);
        VisualGraphNode.setNodes(nodeToRedo);

        VisualGraphEdge.setEdges(new ArrayList<>());
        for(VisualGraphNode n : nodeToRedo){
            for(GraphEdge e : n.getNode().getEdges()){
                VisualGraphEdge.create(e);
            }
        }
        nodeUndoStack.add(VisualGraphNode.getNodes());
		return true;
	}

	public static void pushToUndoStack(){
		nodeRedoStack.clear();

        ArrayList<VisualGraphNode> nodesCopy = VisualGraphNode.getNodes();
        ArrayList<VisualGraphNode> nodes = new ArrayList<>();
        for(VisualGraphNode n : nodesCopy){
            nodes.add((VisualGraphNode) n.deepCopy());
        }
		nodeUndoStack.add(nodes);
	}
}
