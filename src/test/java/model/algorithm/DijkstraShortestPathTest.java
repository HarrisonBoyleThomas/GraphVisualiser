package model.algorithm;

import model.GraphEdge;
import model.GraphNode;
import model.GraphComponentState;

import java.lang.ReflectiveOperationException;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.fail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
*    Test the DSP algorithm on each variant
*    @Author Harrison Boyle-Thomas
*    @Date: 02.11.2020
**/
public class DijkstraShortestPathTest{

	private static Class[] algorithms = {
		ArrayBasedDijkstra.class,
		HeapBasedDijkstra.class
	};
	protected DijkstraShortestPath dsp;


	/*
	@Test
	public void twoNodeGraph(){
		for(Class algorithm : algorithms){
			try{
				dsp = (DijkstraShortestPath) algorithm.getConstructor(GraphNode.class).newInstance((Object) null);
				//A <--> B
				//test that DSP returns just the length of the edges for a single-edge route
				GraphNode nodeA = new GraphNode();
				GraphNode nodeB = new GraphNode();
				GraphEdge edge = nodeA.addEdge(nodeB, false);
				edge.setLength(12);


				ArrayList<GraphNode> nodes = new ArrayList<>();
				nodes.add(nodeA);
				nodes.add(nodeB);

				dsp.setStartNode(nodeA);
				dsp.initialise(nodes);
				dsp.run();

				assertEquals(dsp.getDistance(nodeB), 12);
				assertEquals(2, dsp.getShortestPath(nodeB).size());
				assertEquals(1, dsp.getShortestPath(nodeA).size());


				edge = nodeB.addEdge(nodeA, false);
				edge.setLength(1);

				dsp.setStartNode(nodeB);
				dsp.initialise(nodes);
				dsp.run();

				assertEquals(dsp.getDistance(nodeA), 1);
				assertEquals(2, dsp.getShortestPath(nodeA).size());
			}
			catch(Exception e){
				System.out.println("Failed to run " + algorithm);
			}
		}
	}*/

	@Test
	public void fourNodeGraph(){
		for(Class algorithm : algorithms){
			try{
				dsp = (DijkstraShortestPath) algorithm.getConstructor(GraphNode.class).newInstance((Object) null);
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

				dsp.setStartNode(nodeA);
				dsp.initialise(nodes);
				dsp.run();

				assertEquals(14, dsp.getDistance(nodeD), algorithm + " expect 14, got: " + dsp.getDistance(nodeD));
				assertEquals(dsp.getPredecessor(nodeD), nodeC, algorithm + " predecessor should be nodeC");
				assertEquals(dsp.getDistance(nodeB), 5, algorithm + " distance should be 5");
				assertEquals(dsp.getDistance(nodeC), 2, algorithm + " distance should be 2");
				assertEquals(3, dsp.getShortestPath(nodeD).size(), algorithm + " shortest path should have 3 nodes");


				//now add a short edge from A to D in the original graph
				//the new edge should be the shortest path from A to D

				edge = nodeA.addEdge(nodeD, false);
				edge.setLength(1);

				dsp.setStartNode(nodeA);
				dsp.initialise(nodes);
				dsp.run();

				assertEquals(1, dsp.getDistance(nodeD), algorithm + " Distance should be 1");
				assertEquals(nodeA, dsp.getPredecessor(nodeD), algorithm + " predecessor should be nodeA");
				assertEquals(2, dsp.getShortestPath(nodeD).size(), algorithm + " Distance should be 2");
			}
			catch(ReflectiveOperationException e){
				fail("Failed to instantiate " + algorithm);
			}
		}
	}

	@Test
	public void largeGraphTest(){
		for(Class algorithm : algorithms){
			try{
				dsp = (DijkstraShortestPath) algorithm.getConstructor(GraphNode.class).newInstance((Object) null);
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

				dsp.setStartNode(root);
				dsp.initialise(nodes);
				dsp.run();


				//Test that the correct distances/predecessors are generated for the expensive route
				int indexOfTarget = nodes.indexOf(target);
				prev = root;
				int cumulativeDistance = 0;
				for(int i=1; i< indexOfTarget; i++){
					GraphNode next = nodes.get(i);
					cumulativeDistance += (i*2);
					assertEquals(prev, dsp.getPredecessor(next), algorithm + " Expected " + prev.getName() + ", got: " + dsp.getPredecessor(next).getName());
					assertEquals(cumulativeDistance, dsp.getDistance(next), algorithm + " Expected " + cumulativeDistance + ", got: " + dsp.getDistance(next));
					prev = next;
				}

				//Test that the correct distances/predecessors are generated for the cheap route
				cumulativeDistance = 0;
				prev = root;
				assertEquals(2, root.getEdges().size(), algorithm + " expect size to be 2, got: " + root.getEdges().size());
				for(int i = indexOfTarget + 1; i < nodes.size(); i++){
					GraphNode next = nodes.get(i);
					cumulativeDistance += prev.getEdge(next).getLength();
					assertEquals(prev, dsp.getPredecessor(next), algorithm + " Expected predecessor of" + prev.getName() + " for node: " + next.getName() + ", got: " + dsp.getPredecessor(next).getName());
					assertEquals(cumulativeDistance, dsp.getDistance(next), algorithm + " Expected " + cumulativeDistance + ", got: " + dsp.getDistance(next));
					prev = next;
				}

				//Test that the shortest path found is through the cheap route, and
				//the correct distance is calculated
				assertEquals(prev, dsp.getPredecessor(target), "" + algorithm);
				assertEquals(cumulativeDistance + prev.getEdge(target).getLength(), dsp.getDistance(target), "" + algorithm);
				assertEquals(20, dsp.getShortestPath(target).size(), "" + algorithm);
			}
			catch(ReflectiveOperationException e){
				fail("Failed to instantiate: " + algorithm);
			}
		}
	}
}
