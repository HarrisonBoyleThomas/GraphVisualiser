package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
*    Test that GraphNodes work
*    @Author Harrison Boyle-Thomas
*    @Date 30/10/2020
**/
public class GraphNodeTest{
	
	GraphNode nodeA;
	
	
	@BeforeEach
	public void setUp(){
		//Create a node before each test
		nodeA = new GraphNode();
	}
	
	/**
	*    Test that the default constructor works
	**/
	@Test
	public void defaultConstructorTest(){
		assertNotEquals(null, nodeA);
		assertEquals(0, nodeA.getValue());
		assertEquals("", nodeA.getName());
		assertEquals(GraphComponentState.UNVISITED, nodeA.getState());
		
	}
	
	/**
	*    Test that the alternate constructor works
	**/
	@Test
	public void constructorTest(){
		GraphNode nodeA = new GraphNode(1);
		assertNotEquals(null, nodeA);
		assertEquals(1, nodeA.getValue());
		assertEquals("", nodeA.getName());
		assertEquals(GraphComponentState.UNVISITED, nodeA.getState());
	}
	
	/**
	*    Test that the name of the node can be obtained/altered
	**/
	@Test
	public void nameTest(){
		assertEquals("Hello there", nodeA.setName("Hello there"));
		assertEquals("Hello there", nodeA.getName());
	}
	
	/**
	*    Test that the value of a node can be obtained/altered
	**/
	@Test
	public void valueTest(){
		assertEquals(0, nodeA.getValue());
		assertEquals(1, nodeA.setValue(1));
		assertEquals(1, nodeA.getValue());
		
		assertEquals(-1, nodeA.setValue(-1));
		assertEquals(-1, nodeA.getValue());
		
		assertEquals(100000, nodeA.setValue(100000));
		assertEquals(100000, nodeA.getValue());
	}
	
	/**
	*    Test that edges can be added/obtained
	**/
    @Test
	public void addEdgeTest(){
		//test directed edges
		GraphNode nodeB = new GraphNode();
		
		//nodeA should have no edges initially
		assertEquals(0, nodeA.getEdges().size());
		
		//An edge should be created
		GraphEdge edge = nodeA.addEdge(nodeB, false);
		assertNotEquals(null, edge);
		
		//the endpoints of the edges should be A and B
		assertEquals(nodeA, edge.nodeA);
        assertEquals(nodeB, edge.nodeB);	

        //bidirected edge not created, so no edge should exist from B to A		
		assertEquals(0, nodeB.getEdges().size());
		
		
		//trying to add a duplicate edge should be rejected by the node
		assertEquals(null, nodeA.addEdge(nodeB, false));
		assertEquals(1, nodeA.getEdges().size());
    }
	
	/**
	*    Test that bi-directional edges can be added/obtained
	**/
	@Test
	public void addEdgeBiDirectedTest(){
		//test biDirected edges
		GraphNode nodeB = new GraphNode();
		
		//nodeA should have no edges initially
		assertEquals(0, nodeA.getEdges().size());
		
		//An edge should be created
		GraphEdge edge = nodeA.addEdge(nodeB, true);
		assertNotEquals(null, edge);
		
		//the endpoints of the edges should be A and B
		assertEquals(nodeA, edge.nodeA);
        assertEquals(nodeB, edge.nodeB);	

        //bidirected edge created, so an edge should exist from B to A		
		assertEquals(1, nodeB.getEdges().size());
		assertEquals(nodeA, nodeB.getEdges().get(0).nodeB);
		
		//trying to add a duplicate edge should be rejected by the node
		assertEquals(null, nodeA.addEdge(nodeB, true));
		assertEquals(1, nodeA.getEdges().size());
	}
	
	/**
	*    Test that edges can be removed from the edge list of nodes
	**/
	@Test
	public void removeEdgeTest(){
		GraphNode nodeB = new GraphNode();
		
		nodeA.addEdge(nodeB, false);
		
		//attempting to remove null should not remove anything and return false
		assertEquals(false, nodeA.removeEdge((GraphNode) null, false));
		assertEquals(1, nodeA.getEdges().size());
		
		//removing nodeB edge should be successful
		assertEquals(true, nodeA.removeEdge(nodeB, false));
		assertEquals(0, nodeA.getEdges().size());
		//attempting to remove the nodeB edge again should result in no change
		assertEquals(false, nodeA.removeEdge(nodeB, false));
		
		
		GraphEdge edge = nodeA.addEdge(nodeB, false);
		assertEquals(true, nodeA.removeEdge(edge, false));
	}
	
	/**
	*    Test that the edge list can be obtained
	**/
	@Test
	public void getEdgeTest(){
		GraphNode nodeB = new GraphNode();
		
		GraphEdge edge = nodeA.addEdge(nodeB, false);
		assertEquals(edge, nodeA.getEdge(nodeB));
		edge.setName("testing");
		
		assertEquals(edge, nodeA.getEdgeByName("testing"));
		
		
		assertEquals(null, nodeA.getEdge(null));
		assertEquals(null, nodeA.getEdgeByName(""));
	}
	
	/**
	*    Test that nodes can be deleted from the graph model
	*    by removing their references for the garbage collector
	**/
	@Test
	public void deleteTest(){
		GraphNode nodeB = new GraphNode();
		nodeA.addEdge(nodeB, true);
		
		nodeA.delete();
		
		assertEquals(0, nodeA.getEdges().size());
		assertEquals(0, nodeB.getEdges().size());
	}
	
	/**
	*    Test that the state of a node can be altered/obtained
	**/
	@Test
	public void stateTest(){
		assertEquals(GraphComponentState.UNVISITED, nodeA.getState());
		assertEquals(GraphComponentState.VISITED, nodeA.setState(GraphComponentState.VISITED));
		assertEquals(GraphComponentState.VISITED, nodeA.getState());
	}
}