package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.util.ArrayList; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
*    Test the DSP algorithm
*    @Author Harrison Boyle-Thomas
*    @Date: 02.11.2020
**/
public class DijkstraShortestPathTest{
	
	@Test
	public void twoNodeGraph(){
		//A <--> B
		//test that DSP returns just the length of the edges for a single-edge route
		GraphNode nodeA = new GraphNode();
		GraphNode nodeB = new GraphNode();
		GraphEdge edge = nodeA.addEdge(nodeB, false);
		edge.setLength(12);
		
		
		ArrayList<GraphNode> nodes = new ArrayList<>();
		nodes.add(nodeA);
		nodes.add(nodeB);
		
		DijkstraShortestPath dsp = new DijkstraShortestPath(nodeA, nodes);
		dsp.run();
		
		assertEquals(dsp.getDistance(nodeB), 12);
		assertEquals(2, dsp.getShortestPath(nodeB).size());
		assertEquals(1, dsp.getShortestPath(nodeA).size());
		
		
		edge = nodeB.addEdge(nodeA, false);
		edge.setLength(1);
		
		dsp = new DijkstraShortestPath(nodeB, nodes);
		dsp.run();
		
		assertEquals(dsp.getDistance(nodeA), 1);
		assertEquals(2, dsp.getShortestPath(nodeA).size());
	}
	
	@Test
	public void fourNodeGraph(){
		
		//    B
		//  5/ \10
		// A    D
		//  2\ /12
		//    C
		//shortest path from A->D is 14 from A->C->D
		GraphNode nodeA = new GraphNode();
		GraphNode nodeB = new GraphNode();
		GraphNode nodeC = new GraphNode();
		GraphNode nodeD = new GraphNode();
		
		ArrayList<GraphNode> nodes = new ArrayList<>();
		nodes.add(nodeA);
		nodes.add(nodeB);
		nodes.add(nodeC);
		nodes.add(nodeD);
		
		
		GraphEdge edge = nodeA.addEdge(nodeB, false);
		edge.setLength(5);
		
		edge = nodeB.addEdge(nodeD, false);
		edge.setLength(10);
		
		edge = nodeA.addEdge(nodeC, false);
		edge.setLength(2);
		
		edge = nodeC.addEdge(nodeD, false);
		edge.setLength(12);
		
		DijkstraShortestPath dsp = new DijkstraShortestPath(nodeA, nodes);
		dsp.run();
		
		System.out.println(dsp.getDistance(nodeD));
		assertEquals(14, dsp.getDistance(nodeD), "expect 14, got: " + dsp.getDistance(nodeD));
		assertEquals(dsp.getPredecessor(nodeD), nodeC);
		assertEquals(dsp.getDistance(nodeB), 5);
		assertEquals(dsp.getDistance(nodeC), 2);
		assertEquals(3, dsp.getShortestPath(nodeD).size());
		
		
		//now add a short edge from A to D in the original graph
		//the new edge should be the shortest path from A to D
		
		edge = nodeA.addEdge(nodeD, false);
		edge.setLength(1);
		
		dsp = new DijkstraShortestPath(nodeA, nodes);
		dsp.run();
		
		assertEquals(1, dsp.getDistance(nodeD));
		assertEquals(nodeA, dsp.getPredecessor(nodeD));
		assertEquals(2, dsp.getShortestPath(nodeD).size());
	}
	
	@Test
	public void largeGraphTest(){
		//Create a graph with two routes
		//an expensive, but "good looking" route,
		//and a cheap, but "bad looking" route.
		//The greedy nature of dsp should not affect choice of route
		//
		//
		//     expensiveRoute
		//    /              \
		//Root                target
		//    \              /
		//     cheapRoute----
		//
		//
		//cost(expensiveRoute) is approx 2*cost(cheapRoute)
		//But the cost of cheapRoute starts high and decreases towards the target node
		//With the opposite occurring for the expensive route
		
		GraphNode root = new GraphNode();
		
		GraphNode prev = root;
		
		root.setName("root");
		
		ArrayList<GraphNode> nodes = new ArrayList<>();
		nodes.add(root);
		for(int i = 1; i<20; i ++){
			GraphNode node = new GraphNode();
			//cost of cheap route doubles i
			prev.addEdge(node, false).setLength(i*2);
			prev = node;
			nodes.add(node);
			node.setName("" + (i));
		}
		
		GraphNode target = nodes.get(nodes.size()-1);
		
		
		ArrayList<GraphNode> shortestPath = new ArrayList<>();
		shortestPath.add(root);
		prev = root;
		//cost of expensive route start expensive, but becomes cheaper
		for(int i = 1; i < 19; i++){
			GraphNode node = new GraphNode();
			prev.addEdge(node, false).setLength(20-i);
			prev = node;
			nodes.add(node);
			node.setName("" + (i+20));
			shortestPath.add(node);
		}
		prev.addEdge(target, false).setLength(1);
		
		DijkstraShortestPath dsp = new DijkstraShortestPath(root, nodes);
		dsp.run();
		
		
		//Test that the correct distances/predecessors are generated for the expensive route
		int indexOfTarget = nodes.indexOf(target);
		prev = root;
		int cumulativeDistance = 0;
		for(int i=1; i< indexOfTarget; i++){
			GraphNode next = nodes.get(i);
			cumulativeDistance += (i*2);
			assertEquals(prev, dsp.getPredecessor(next), "Expected " + prev.getName() + ", got: " + dsp.getPredecessor(next).getName());
			assertEquals(cumulativeDistance, dsp.getDistance(next), "Expected " + cumulativeDistance + ", got: " + dsp.getDistance(next));
			prev = next;
		}
		
		//Test that the correct distances/predecessors are generated for the cheap route
		cumulativeDistance = 0;
		prev = root;
		assertEquals(2, root.getEdges().size(), "expect size to be 2, got: " + root.getEdges().size());
		for(int i = indexOfTarget + 1; i < nodes.size(); i++){
			GraphNode next = nodes.get(i);
			cumulativeDistance += prev.getEdge(next).getLength();
			assertEquals(prev, dsp.getPredecessor(next), "Expected predecessor of" + prev.getName() + " for node: " + next.getName() + ", got: " + dsp.getPredecessor(next).getName());
			assertEquals(cumulativeDistance, dsp.getDistance(next), "Expected " + cumulativeDistance + ", got: " + dsp.getDistance(next));
			prev = next;
		}
		
		//Test that the shortest path found is through the cheap route, and
		//the correct distance is calculated
		assertEquals(prev, dsp.getPredecessor(target));
		assertEquals(cumulativeDistance + prev.getEdge(target).getLength(), dsp.getDistance(target));
		assertEquals(20, dsp.getShortestPath(target).size());
	}		
}