package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
*    Test that Graph Edges work
*
*    @Author Harrison Boyle-Thomas
*    @Date 30/10/2020
**/
public class GraphEdgeTest{
	
	GraphNode nodeA;
	GraphNode nodeB;
	
	@BeforeEach
	public void setUp(){
		//Create two nodes before each test
		nodeA = new GraphNode();
		nodeB = new GraphNode();
	}
	
	@Test
	public void constructorTest(){
		GraphEdge edge = new GraphEdge(nodeA, nodeB, 0);
		assertNotEquals(null, edge);
		
		assertEquals(nodeA, edge.nodeA);
		assertEquals(nodeB, edge.nodeB);
		
		assertEquals(0, edge.getLength());
		assertEquals("", edge.getName());
		
		assertEquals(GraphComponentState.UNVISITED, edge.getState());
	}
	
	/**
	*    Test that the edge length can be obtained and altered
	**/
	@Test
	public void lengthTest(){
		GraphEdge edge = new GraphEdge(nodeA, nodeB, 0);
		
		assertEquals(0, edge.getLength());
		
		for(int i = 0; i < 100000; i++){
			assertEquals(i, edge.setLength(i));
			assertEquals(i, edge.getLength());
		}
		
		assertEquals(0, edge.setLength(-1));
		assertEquals(0, edge.getLength());
		
		assertEquals(100000, edge.setLength(100001));
		assertEquals(100000, edge.getLength());
	}
	
	/**
	*    Test that the name of the edge can be obtained/altered
	**/
	@Test
	public void nameTest(){
		GraphEdge edge = new GraphEdge(nodeA, nodeB, 0);
		assertEquals("General Kenobi", edge.setName("General Kenobi"));
		assertEquals("General Kenobi", edge.getName());
	}
	
	/**
	*    Test that a list of length 2 is obtained containing the endpoints
	*    of an edge
	**/
	public void getNodesTest(){
		GraphEdge edge = new GraphEdge(nodeA, nodeB, 0);
		
		assertEquals(2, edge.getNodes().length);
		
		assertEquals(nodeA, edge.getNodes()[0]);
		assertEquals(nodeB, edge.getNodes()[1]);
	}
	
	/**
	*    Test that the overridden equals method works
	**/
	@Test
	public void equalsTest(){
		GraphEdge edgeA = new GraphEdge(nodeA, nodeB, 0);
		GraphEdge edgeB = new GraphEdge(nodeA, nodeB, 5);
		GraphEdge edgeC = new GraphEdge(nodeB, nodeA, 0);
		
		assertEquals(true, edgeA.equals(edgeA));
		assertEquals(true, edgeA.equals(edgeB));
		assertEquals(true, edgeB.equals(edgeA));
		assertEquals(false, edgeA.equals(edgeC));
	}
	
	/**
	*    Test that the state of an edge can be altered/obtained
	**/
	@Test
	public void stateTest(){
		GraphEdge edge = new GraphEdge(nodeA, nodeB, 0);
		assertEquals(GraphComponentState.UNVISITED, edge.getState());
		assertEquals(GraphComponentState.VISITED, edge.setState(GraphComponentState.VISITED));
		assertEquals(GraphComponentState.VISITED, edge.getState());
	}
}