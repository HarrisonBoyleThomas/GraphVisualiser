package maths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.Math;

public class VectorTest{
	
	@Test
	public void constructorTest(){
		Vector v = new Vector();
		assertEquals(0, v.x);
		assertEquals(0, v.y);
		assertEquals(0, v.z);
		
		v = new Vector(10, 5);
		assertEquals(10, v.x);
		assertEquals(5, v.y);
		assertEquals(0, v.z);
		
		v = new Vector(10, 5, 2);
		assertEquals(10, v.x);
		assertEquals(5, v.y);
		assertEquals(2, v.z);
		
		v = new Vector(-2, -4, -5);
		assertEquals(-2, v.x);
		assertEquals(-4, v.y);
		assertEquals(-5, v.z);
	}
	
	@Test
	public void addTest(){
		Vector v1 = new Vector();
		Vector v2 = new Vector(1, 2, 3);
		
		assertEquals(new Vector(1, 2, 3), v1.add(v2));
		assertEquals(new Vector(1, 2, 3), Vector.add(v1, v2));
		assertEquals(new Vector(1, 2, 3), v2.add(v1));
		assertEquals(new Vector(1, 2, 3), Vector.add(v2, v1));
		
		v1 = new Vector(5, 6, 7);
		
		assertEquals(new Vector(6, 8, 10), v1.add(v2));
		assertEquals(new Vector(6, 8, 10), Vector.add(v1, v2));
		assertEquals(new Vector(6, 8, 10), v2.add(v1));
		assertEquals(new Vector(6, 8, 10), Vector.add(v2, v1));
	}
	
	@Test
	public void subtractTest(){
		Vector v1 = new Vector();
		Vector v2 = new Vector(1, 2, 3);
		
		assertEquals(new Vector(-1, -2, -3), v1.subtract(v2));
		assertEquals(new Vector(-1, -2, -3), Vector.subtract(v1, v2));
		assertEquals(new Vector(1, 2, 3), v2.subtract(v1));
		assertEquals(new Vector(1, 2, 3), Vector.subtract(v2, v1));
		
		v1 = new Vector(5, 6, 7);
		
		assertEquals(new Vector(4, 4, 4), v1.subtract(v2));
		assertEquals(new Vector(4, 4, 4), Vector.subtract(v1, v2));
		assertEquals(new Vector(-4, -4, -4), v2.subtract(v1));
		assertEquals(new Vector(-4,-4, -4), Vector.subtract(v2, v1));
	}
	
	
	@Test
	public void dotTest(){
		Vector v1 = new Vector(1, 1, 1);
		Vector v2 = new Vector(2, 2, 2);
		Vector v3 = new Vector(1, 2, 3);
		Vector v4 = new Vector(-10, 0, 0);
		
		assertEquals(3, v1.dot(v1));
		assertEquals(3, Vector.dot(v1, v1));
		
		assertEquals(12, v2.dot(v2));
		assertEquals(12, Vector.dot(v2, v2));
		
		assertEquals(14, v3.dot(v3));
		assertEquals(14, Vector.dot(v3, v3));
		
		assertEquals(100, v4.dot(v4));
		assertEquals(100, Vector.dot(v4, v4));
		
		assertEquals(-10, v1.dot(v4));
		assertEquals(-10, v4.dot(v1));
		assertEquals(-10, Vector.dot(v1, v4));
		assertEquals(-10, Vector.dot(v4, v1));
		
		assertEquals(6, v1.dot(v3));
		assertEquals(6, v3.dot(v1));
		assertEquals(6, Vector.dot(v1, v3));
		assertEquals(6, Vector.dot(v3, v1));
		
		assertEquals(6, v1.dot(v2));
		assertEquals(6, v2.dot(v1));
		assertEquals(6, Vector.dot(v1, v2));
		assertEquals(6, Vector.dot(v2, v1));
		
		assertEquals(12, v2.dot(v3));
		assertEquals(12, v3.dot(v2));
		assertEquals(12, Vector.dot(v2, v3));
		assertEquals(12, Vector.dot(v3, v2));
	}
	
	@Test
	public void multiplyTest(){
		Vector v1 = new Vector(2, 4, 6);
		
		assertEquals(new Vector(), v1.multiply(0));
		assertEquals(new Vector(), Vector.multiply(v1, 0));
		
		assertEquals(new Vector(-2, -4, -6), v1.multiply(-1));
		assertEquals(new Vector(-2, -4, -6), Vector.multiply(v1, -1));
		
		assertEquals(new Vector(4, 8, 12), v1.multiply(2));
		assertEquals(new Vector(4, 8, 12), Vector.multiply(v1, 2));
	}
	
	@Test
	public void divideTest(){
		Vector v1 = new Vector(2, 4, 6);
		
		assertEquals(v1, v1.divide(1));
		assertEquals(v1, Vector.divide(v1, 1));
		
		assertEquals(new Vector(1, 2, 3), v1.divide(2));
		assertEquals(new Vector(1, 2, 3), Vector.divide(v1, 2));
		
		assertEquals(new Vector(-2, -4, -6), v1.divide(-1));
		assertEquals(new Vector(-2, -4, -6), Vector.divide(v1, -1));
	}
	
	@Test
	public void distanceTest(){
		Vector v1 = new Vector(1, 2, 3);
		
		assertEquals(Math.sqrt(1 + 4 + 9), Vector.distance(v1, new Vector()));
		assertEquals(Math.sqrt(1 + 4 + 9), Vector.distance(new Vector(), v1));
		
		
		Vector v2 = new Vector(5, 6, 7);
		assertEquals(Math.sqrt(16 + 16 + 16), Vector.distance(v1, v2));
		assertEquals(Math.sqrt(16 + 16 + 16), Vector.distance(v2, v1));
		
		
		Vector v3 = new Vector(-1, -2, -3);
		assertEquals(Math.sqrt(1 + 4 + 9) * 2, Vector.distance(v1, v3));
		assertEquals(Math.sqrt(1 + 4 + 9) * 2, Vector.distance(v3, v1));
		
		assertEquals(Math.sqrt(36 + 64 + 100), Vector.distance(v2, v3));
		assertEquals(Math.sqrt(36 + 64 + 100), Vector.distance(v3, v2));
	}
	
	@Test
	public void lengthTest(){
		Vector v1 = new Vector(1, 2, 3);
		
		assertEquals(Math.sqrt(1 + 4 + 9), v1.length());
		assertEquals(Math.sqrt(1 + 4 + 9), Vector.length(v1));
		
		Vector v2 = new Vector(-1, -2, -3);
		assertEquals(Math.sqrt(1 + 4 + 9), v2.length());
		assertEquals(Math.sqrt(1 + 4 + 9), Vector.length(v2));
		
		Vector v3 = new Vector(7, 3, 8);
		assertEquals(Math.sqrt(122), v3.length());
		assertEquals(Math.sqrt(122), Vector.length(v3));
	}
	
	@Test
	public void normaliseTest(){
		Vector v1 = new Vector(199, 0, 0);
		assertEquals(new Vector(1, 0, 0), v1.normalise());
		assertEquals(new Vector(1, 0, 0), Vector.normalise(v1));
		
		Vector v2 = new Vector(0, 45, 0);
		assertEquals(new Vector(0, 1, 0), v2.normalise());
		assertEquals(new Vector(0, 1, 0), Vector.normalise(v2));
		
		Vector v3 = new Vector(0, 0, 3);
		assertEquals(new Vector(0, 0, 1), v3.normalise());
		assertEquals(new Vector(0, 0, 1), Vector.normalise(v3));
		
		Vector v4 = new Vector(20, 20, 0);
		assertEquals(new Vector(20 / Math.sqrt(800), 20 / Math.sqrt(800), 0), v4.normalise());
		assertEquals(new Vector(20 / Math.sqrt(800), 20/Math.sqrt(800), 0), Vector.normalise(v4));
	}	
}
		