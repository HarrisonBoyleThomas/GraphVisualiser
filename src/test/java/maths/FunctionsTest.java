package maths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FunctionsTest{
	
	@Test
	public void findLookAtRotationTest(){
		Vector v0 = new Vector();
		//forward
		Vector v1 = new Vector(5, 0, 0);
		assertEquals(new Rotator(0, 0, 0), Functions.getRotationBetweenVectors(v0, v1));
		
		//negative x
		Vector v4 = new Vector(-5, 0, 0);
		assertEquals(new Rotator(0, 0, 180), Functions.getRotationBetweenVectors(v0, v4));
		
		//to the right
		Vector v2 = new Vector(0, 5, 0);
		assertEquals(new Rotator(0, 0, 270), Functions.getRotationBetweenVectors(v0, v2));
		
		//to the left
		Vector left = new Vector(0, -5, 0);
		assertEquals(new Rotator(0, 0, 90), Functions.getRotationBetweenVectors(v0, left));
		
		//up
		Vector v3 = new Vector(0, 0, 5);
		assertEquals(new Rotator(0, 270, 0), Functions.getRotationBetweenVectors(v0, v3));
		
		//down
		Vector down = new Vector(0, 0, -5);
		assertEquals(new Rotator(0, 90, 0), Functions.getRotationBetweenVectors(v0, down));
		
		//diagonalRightXY
		Vector diagRightXY = new Vector(5, 5, 0);
		assertEquals(new Rotator(0, 0, 315), Functions.getRotationBetweenVectors(v0, diagRightXY));
		
		//diagRightAll
		Vector diagRight = new Vector(5, 5, 5);
		assertEquals(new Rotator(0, 324.73556975373657, 315), Functions.getRotationBetweenVectors(v0, diagRight));
		
	}

    @Test
	public void roundTest(){
		assertEquals(3.0, Functions.round(3.1, 0));
		assertEquals(4.0, Functions.round(3.5, 0));
		assertEquals(3.0, Functions.round(3.4, 0));
		assertEquals(-5.0, Functions.round((5 * Math.cos(Math.PI)) - (5 * Math.sin(Math.PI)), 1));
		assertEquals(-9.0, Functions.round(-9.0, 0));
	}
	
	@Test
	public void rotateVectorTest(){
		Vector v = new Vector(5, 0, 0);
		assertEquals(new Vector(5, 0, 0), Functions.rotateVector(new Vector(), v, new Rotator()));
		
		//yaw
		assertEquals(new Vector(0, -5, 0), Functions.rotateVector(new Vector(), v, new Rotator(0, 0, 90)));
		assertEquals(new Vector(-5, 0, 0), Functions.rotateVector(new Vector(), v, new Rotator(0, 0, 180)));
		assertEquals(new Vector(0, 5, 0), Functions.rotateVector(new Vector(), v, new Rotator(0, 0, 270)));
		
		//pitch
		assertEquals(new Vector(0, 0, 5), Functions.rotateVector(new Vector(), v, new Rotator(0, 90, 0)));
		assertEquals(new Vector(-5, 0, 0), Functions.rotateVector(new Vector(), v, new Rotator(0, 180, 0)));
		assertEquals(new Vector(0, 0, -5), Functions.rotateVector(new Vector(), v, new Rotator(0, 270, 0)));
		
		//roll
		v = new Vector(0, 5, 0);
		assertEquals(new Vector(0, 0, -5), Functions.rotateVector(new Vector(), v, new Rotator(90, 0, 0)));
		assertEquals(new Vector(0, -5, 0), Functions.rotateVector(new Vector(), v, new Rotator(180, 0, 0)));
		assertEquals(new Vector(0, 0, 5), Functions.rotateVector(new Vector(), v, new Rotator(270, 0, 0)));
		
		v = new Vector(5, 5, 5);
		assertEquals(new Vector(5, -5, 5), Functions.rotateVector(new Vector(), v, new Rotator(0, 0, 90)));
	}
}