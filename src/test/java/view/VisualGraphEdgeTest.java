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
import model.GraphEdge;

public class VisualGraphEdgeTest{
    @BeforeEach
    public void setup(){
        //just delete the nodes, because this will also delete all edges
        while(VisualGraphNode.getNodes().size() > 1){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @AfterEach
    public void teardown(){
        //just delete the nodes, because this will also delete all edges
        while(VisualGraphNode.getNodes().size() > 1){
            VisualGraphNode.delete(VisualGraphNode.getNodes().get(0));
        }
    }

    @Test
    public void createTest(){
        int size = VisualGraphEdge.getEdges().size();
        GraphNode a = new GraphNode();
        VisualGraphNode.create(new Vector(), a);
        GraphNode b = new GraphNode();
        VisualGraphNode.create(new Vector(), b);
        GraphEdge e = a.addEdge(b, false);
        VisualGraphEdge edge = VisualGraphEdge.create(e);
        assertEquals(size + 1, VisualGraphEdge.getEdges().size(), "Edge list should be longer");
        assertTrue(VisualGraphEdge.getEdges().contains(edge), "Edge list should contain the new edge");
    }

    @Test
    public void delete(){
        GraphNode a = new GraphNode();
        VisualGraphNode.create(new Vector(), a);
        GraphNode b = new GraphNode();
        VisualGraphNode.create(new Vector(), b);
        GraphEdge e = a.addEdge(b, false);

        //VGE as parameter
        VisualGraphEdge edge = VisualGraphEdge.create(e);
        int size = VisualGraphEdge.getEdges().size();
        boolean successful = VisualGraphEdge.delete(edge);
        assertTrue(successful, "delete() should return true if successful");
        assertEquals(size - 1, VisualGraphEdge.getEdges().size(), "Edge list should be shorter");
        assertFalse(VisualGraphEdge.getEdges().contains(edge));

        //GraphEdge as parameter
        edge = VisualGraphEdge.create(e);
        size = VisualGraphEdge.getEdges().size();
        successful = VisualGraphEdge.delete(e);
        assertTrue(successful, "delete() should return true if successful");
        assertEquals(size - 1, VisualGraphEdge.getEdges().size(), "Edge list should be shorter");
        assertFalse(VisualGraphEdge.getEdges().contains(edge));

        //GraphNode with nodeA as parameter
        edge = VisualGraphEdge.create(e);
        size = VisualGraphEdge.getEdges().size();
        VisualGraphEdge.delete(a);
        assertEquals(size - 1, VisualGraphEdge.getEdges().size(), "Edge list should be shorter");
        assertFalse(VisualGraphEdge.getEdges().contains(edge));

        //GraphNode with nodeB as parameter
        edge = VisualGraphEdge.create(e);
        size = VisualGraphEdge.getEdges().size();
        VisualGraphEdge.delete(b);
        assertEquals(size - 1, VisualGraphEdge.getEdges().size(), "Edge list should be shorter");
        assertFalse(VisualGraphEdge.getEdges().contains(edge));


        //failed delete test
        successful = VisualGraphEdge.delete(e);
        assertFalse(successful, "delete() should return false if nothing was deleted");
    }

    @Test
    public void getEdgeTest(){
        GraphNode a = new GraphNode();
        VisualGraphNode.create(new Vector(), a);
        GraphNode b = new GraphNode();
        VisualGraphNode.create(new Vector(), b);
        GraphEdge e = a.addEdge(b, false);

        VisualGraphEdge edge = VisualGraphEdge.create(e);

        assertEquals(edge, VisualGraphEdge.getEdge(e), "Edge should be found for GraphEdge");
        assertEquals(edge, VisualGraphEdge.getEdge(a), "Edge should be found for node A");
        assertEquals(edge, VisualGraphEdge.getEdge(b), "Edge should be found for node B");
    }

    @Test
    public void copyEdgesTest(){
        GraphNode a = new GraphNode();
        VisualGraphNode.create(new Vector(), a);
        GraphNode b = new GraphNode();
        VisualGraphNode.create(new Vector(), b);
        GraphEdge e = a.addEdge(b, false);

        VisualGraphEdge edge = VisualGraphEdge.create(e);
        ArrayList<VisualGraphEdge> copies = VisualGraphEdge.copyEdges();
        assertEquals(e, copies.get(0).getEdge(), "Copied edge should reference the same GraphEdge");
    }
}
