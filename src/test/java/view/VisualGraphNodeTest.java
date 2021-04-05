package viewport;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import maths.Functions;
import maths.Vector;
import maths.Rotator;

import model.GraphNode;

public class VisualGraphNodeTest{
    @BeforeEach
    public void setup(){
        while(VisualGraphNode.getNodes().size() > 0){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @AfterEach
    public void teardown(){
        while(VisualGraphNode.getNodes().size() > 1){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

	@Test
    public void createTest(){
        int size = VisualGraphNode.getNodes().size();
        GraphNode n = new GraphNode();
        VisualGraphNode node = VisualGraphNode.create(new Vector(), n);
        assertNotNull(node, "VGN should be created after calling create((0,0,0), node)");
        assertEquals(size + 1, VisualGraphNode.getNodes().size(), "Nodes list should be longer");
        assertTrue(VisualGraphNode.getNodes().contains(node), "Node list should contain a newly created node");
    }

    @Test
    public void deleteTest(){
        VisualGraphNode node = VisualGraphNode.create(new Vector(), new GraphNode());
        int size = VisualGraphNode.getNodes().size();
        boolean successful = VisualGraphNode.delete(node);
        assertEquals(size-1, VisualGraphNode.getNodes().size(), "Node list should be shorter after delete");
        assertFalse(VisualGraphNode.getNodes().contains(node), "Node list should not contain a deleted node");
        assertTrue(successful, "Delete should be successful if node was deleted");

        GraphNode n = new GraphNode();
        node = VisualGraphNode.create(new Vector(), n);
        size = VisualGraphNode.getNodes().size();
        successful = VisualGraphNode.delete(n);
        assertEquals(size-1, VisualGraphNode.getNodes().size(), "Node list should be shorter after delete");
        assertFalse(VisualGraphNode.getNodes().contains(node), "Node list should not contain a deleted node");
        assertTrue(successful, "Delete should be successful if node was deleted");

        successful = VisualGraphNode.delete(n);
        assertFalse(successful, "Delete should not be successful if node was not deleted");
    }

    @Test
    public void getNodeTest(){
        GraphNode n = new GraphNode();
        VisualGraphNode node = VisualGraphNode.create(new Vector(), n);
        VisualGraphNode foundNode = VisualGraphNode.getNode(n);
        assertEquals(node, foundNode, "Found node should be equal to created node");
    }

    @Test
    public void copyNodesTest(){
        GraphNode n = new GraphNode();
        VisualGraphNode node = VisualGraphNode.create(new Vector(), n);

        ArrayList<VisualGraphNode> copies = VisualGraphNode.copyNodes();
        System.out.println(copies.size());
        assertEquals(n, copies.get(0).getNode(), "Copy should hold a reference to the same node");

    }
}
