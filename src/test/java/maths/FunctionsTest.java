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
		assertEquals(new Rotator(0, 180, 180), Functions.getRotationBetweenVectors(v0, v4));
		
		//to the right
		Vector v2 = new Vector(0, 5, 0);
		assertEquals(new Rotator(0, 0, 90), Functions.getRotationBetweenVectors(v0, v2));
		
		//to the left
		Vector left = new Vector(0, -5, 0);
		assertEquals(new Rotator(0, 0, 270), Functions.getRotationBetweenVectors(v0, left));
		
		//up
		Vector v3 = new Vector(0, 0, 5);
		assertEquals(new Rotator(0, 90, 0), Functions.getRotationBetweenVectors(v0, v3));
		
		//down
		Vector down = new Vector(0, 0, -5);
		assertEquals(new Rotator(0, 270, 0), Functions.getRotationBetweenVectors(v0, down));
		
		//diagonalRightXY
		Vector diagRightXY = new Vector(5, 5, 0);
		assertEquals(new Rotator(0, 0, 45), Functions.getRotationBetweenVectors(v0, diagRightXY));
		
		//diagRightAll
		Vector diagRight = new Vector(5, 5, 5);
		assertEquals(new Rotator(0, 45, 45), Functions.getRotationBetweenVectors(v0, diagRight));
		
	}
}