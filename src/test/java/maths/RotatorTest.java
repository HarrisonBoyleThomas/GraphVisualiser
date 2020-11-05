package maths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class RotatorTest{
	
	@Test
	public void constructorTest(){
		Rotator r1 = new Rotator();
		assertEquals(0, r1.roll);
		assertEquals(0, r1.pitch);
		assertEquals(0, r1.yaw);
		
		Rotator r2 = new Rotator(4, 6, 2);
		assertEquals(4, r2.roll);
		assertEquals(6, r2.pitch);
		assertEquals(2, r2.yaw);
		
		Rotator doABarrel = new Rotator(5, 5, 5);
		assertEquals(5, doABarrel.roll);
		
		Rotator boundsTest = new Rotator(360, 361, 362);
		assertEquals(0, boundsTest.roll);
		assertEquals(1, boundsTest.pitch);
		assertEquals(2, boundsTest.yaw);
		
		Rotator negativeTest = new Rotator(-1, -2, -3);
		assertEquals(359, negativeTest.roll);
		assertEquals(358, negativeTest.pitch);
		assertEquals(357, negativeTest.yaw);
	}
	
	@Test
	public void addTest(){
		Rotator r1 = new Rotator(1, 2, 3);
		Rotator r2 = new Rotator(4, 5, 6);
		Rotator r3 = new Rotator(-1, -2, -3);
		
		assertEquals(new Rotator(2, 4, 6), r1.add(r1));
		assertEquals(new Rotator(2, 4, 6), Rotator.add(r1, r1));
		
		assertEquals(new Rotator(5, 7, 9), r1.add(r2));
		assertEquals(new Rotator(5, 7, 9), Rotator.add(r1, r2));
		assertEquals(new Rotator(5, 7, 9), r2.add(r1));
		assertEquals(new Rotator(5, 7, 9), Rotator.add(r2, r1));
		
		assertEquals(0, r1.add(r3).roll);
		assertEquals(new Rotator(), r1.add(r3));
		assertEquals(new Rotator(), Rotator.add(r1, r3));
		assertEquals(new Rotator(), r3.add(r1));
		assertEquals(new Rotator(), Rotator.add(r3, r1));
		
		assertEquals(new Rotator(3, 3, 3), r2.add(r3));
		assertEquals(new Rotator(3, 3, 3), Rotator.add(r2, r3));
		assertEquals(new Rotator(3, 3, 3), r3.add(r2));
		assertEquals(new Rotator(3, 3, 3), Rotator.add(r3, r2));
	}
	
	@Test
	public void subtractTest(){
		Rotator r1 = new Rotator(1, 2, 3);
		Rotator r2 = new Rotator(4, 5, 6);
		Rotator r3 = new Rotator(-1, -2, -3);
		
		assertEquals(new Rotator(), r1.subtract(r1));
		assertEquals(new Rotator(), Rotator.subtract(r1, r1));
		
		assertEquals(new Rotator(-3, -3, -3), r1.subtract(r2));
		assertEquals(new Rotator(-3, -3, -3), Rotator.subtract(r1, r2));
		assertEquals(new Rotator(3, 3, 3), r2.subtract(r1));
		assertEquals(new Rotator(3, 3, 3), Rotator.subtract(r2, r1));
		
		assertEquals(new Rotator(2, 4, 6), r1.subtract(r3));
		assertEquals(new Rotator(2, 4, 6), Rotator.subtract(r1, r3));
		assertEquals(new Rotator(-2, -4, -6), r3.subtract(r1));
		assertEquals(new Rotator(-2, -4, -6), Rotator.subtract(r3, r1));
		
		assertEquals(new Rotator(5, 7, 9), r2.subtract(r3));
		assertEquals(new Rotator(5, 7, 9), Rotator.subtract(r2, r3));
		assertEquals(new Rotator(-5, -7, -9), r3.subtract(r2));
		assertEquals(new Rotator(-5, -7, -9), Rotator.subtract(r3, r2));
	}
	
	@Test
	public void incrementAddTest(){
		Rotator r = new Rotator();
		int counter = 0;
		
		for(int i = 0; i < 720; i++){
			counter = (counter + i) % 360;
			assertEquals(new Rotator(counter, 0, 0), r.addRoll(i));
			assertEquals(new Rotator(counter, 0, 0), Rotator.addRoll(r, i));
			r = r.addRoll(i);
		}
		
		r = new Rotator();
		counter = 0;
		for(int i = 0; i < 720; i++){
			counter = (counter + i) % 360;
			assertEquals(new Rotator(0, counter, 0), r.addPitch(i));
			assertEquals(new Rotator(0, counter, 0), Rotator.addPitch(r, i));
			r = r.addPitch(i);
		}
		
		r = new Rotator();
		counter = 0;
		for(int i = 0; i < 720; i++){
			counter = (counter + i) % 360;
			assertEquals(new Rotator(0, 0, counter), r.addYaw(i));
			assertEquals(new Rotator(0, 0, counter), Rotator.addYaw(r, i));
			r = r.addYaw(i);
		}
	}
	
	@Test
	public void incrementSubtractTest(){
		Rotator r = new Rotator();
		int counter = 0;
		
		for(int i = 0; i < 720; i++){
			counter = (counter - i) % 360;
			assertEquals(new Rotator(counter, 0, 0), r.subtractRoll(i));
			assertEquals(new Rotator(counter, 0, 0), Rotator.subtractRoll(r, i));
			r = r.subtractRoll(i);
		}
		
		counter = 0;
		for(int i = 0; i < 720; i++){
			counter = (counter - i) % 360;
			assertEquals(new Rotator(0, counter, 0), r.subtractPitch(i));
			assertEquals(new Rotator(0, counter, 0), Rotator.subtractPitch(r, i));
			r = r.subtractPitch(i);
		}
		
		counter = 0;
		for(int i = 0; i < 720; i++){
			counter = (counter - i) % 360;
			assertEquals(new Rotator(0, 0, counter), r.subtractYaw(i));
			assertEquals(new Rotator(0, 0, counter), Rotator.subtractYaw(r, i));
			r = r.subtractYaw(i);
		}
	}
	
	@Test
	public void deltaTest(){
		Rotator r1 = new Rotator(1, 2, 3);
		assertEquals(new Rotator(-1, -2, -3), r1.delta(new Rotator()));
		assertEquals(new Rotator(-1, -2, -3), Rotator.delta(r1, new Rotator()));
		
		assertEquals(new Rotator(1, 2, 3), (new Rotator()).delta(r1));
		assertEquals(new Rotator(1, 2, 3), Rotator.delta(new Rotator(), r1));
		
		Rotator r2 = new Rotator(2, 4, 6);
		assertEquals(new Rotator(1, 2, 3), r1.delta(r2));
		assertEquals(new Rotator(1, 2, 3), Rotator.delta(r1, r2));
		assertEquals(new Rotator(-1, -2, -3), r2.delta(r1));
		assertEquals(new Rotator(-1, -2, -3), Rotator.delta(r2, r1));
		
		assertEquals(new Rotator(), r1.delta(r1));
		assertEquals(new Rotator(), Rotator.delta(r1, r1));
		
		Rotator r3 = new Rotator(180, 180, 180);
		Rotator r4 = new Rotator(360, 360, 360);
		assertEquals(180, r3.delta(r4).pitch);
		assertEquals(new Rotator(180, 180, 180), r3.delta(r4));
		assertEquals(new Rotator(180, 180, 180), Rotator.delta(r3, r4));
		assertEquals(new Rotator(-180, -180, -180), r4.delta(r3));
		assertEquals(new Rotator(-180, -180, -180), Rotator.delta(r4, r3));
	}
	
	
	@Test
	public void oppositeTest(){
		Rotator r1 = new Rotator(0, 0, 0);
		assertEquals(new Rotator(180, 180, 180), r1.opposite());
		assertEquals(new Rotator(180, 180, 180), Rotator.opposite(r1));
	}
	
	@Test
	public void antiRotatorTest(){
		Rotator r1 = new Rotator(90, 0, 0);
		assertEquals(new Rotator(270, 0, 0), r1.antiRotator());
		assertEquals(new Rotator(270, 0, 0), Rotator.antiRotator(r1));
		
		Rotator r2 = new Rotator(0, 90, 0);
		assertEquals(new Rotator(0, 270, 0), r2.antiRotator());
		assertEquals(new Rotator(0, 270, 0), Rotator.antiRotator(r2));
		
		Rotator r3 = new Rotator(0, 0, 90);
		assertEquals(new Rotator(0, 0, 270), r3.antiRotator());
		assertEquals(new Rotator(0, 0, 270), Rotator.antiRotator(r3));
	}
}