package viewport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import maths.Functions;
import maths.Vector;
import maths.Rotator;

public class CameraTest{
	
	@Test
	public void constructorTest(){
		Camera c = new Camera(new Rotator(0, 0, 90), new Vector(0, 0, 1));
		assertEquals(new Vector(0,0,1), c.getLocation());
		assertEquals(new Rotator(0,0,90), c.getRotation());
		
		c = new Camera();
		assertEquals(new Vector(), c.getLocation());
		assertEquals(new Rotator(), c.getRotation());
	}
	
	@Test
	public void getRelativePositionTest(){
		Camera c = new Camera();
		Vector v = new Vector();
		
		assertEquals(new Vector(), c.getRelativePosition(v));
		
		v = new Vector(1, 0, 0);
		assertEquals(new Vector(1, 0, 0), c.getRelativePosition(v));
		
		v = new Vector(0, 1, 0);
		assertEquals(new Vector(0, 1, 0), c.getRelativePosition(v));
		
		c.yaw(9);
		assertEquals(new Vector(-1, 0, 0), c.getRelativePosition(v));
		
		c.yaw(-9);
		c.pitch(9);
		assertEquals(new Vector(0, 1, 0), c.getRelativePosition(v));
		c.pitch(-9);
	}
	
	@Test
	public void projectOrthographicTest(){
		Camera c = new Camera();
		
		int width = 1920;
		int height = 1080;
		Vector front = new Vector(1, 0, 0);
		assertEquals(new Vector(0, 0, 0), c.projectOrthographic(front, width, height));
		
		//extra distance should not affect position
		front = new Vector(100, 0, 0);
		assertEquals(new Vector(0, 0, 0), c.projectOrthographic(front, width, height));

		Vector diagRight = new Vector(5, 5, 0);
		assertEquals(new Vector(-583, 0, 0), c.projectOrthographic(diagRight, width, height));
		//extra distance should not affect position
		diagRight = diagRight.multiply(100);
		assertEquals(new Vector(-583, 0, 0), c.projectOrthographic(diagRight, width, height));
		
		Vector right = new Vector(0, 5, 0);
		assertEquals(new Vector(-1167, 0, 0), c.projectOrthographic(right, width, height));
		
		
		Vector diagUp = new Vector(5, 0, 5);
		assertEquals(new Vector(0, -328, 0), c.projectOrthographic(diagUp, width, height));
		
		Vector up = new Vector(0, 0, 5);
		assertEquals(new Vector(0, -656, 0), c.projectOrthographic(up, width, height));
		
	}
	
	@Test
	public void projectTest(){
		Camera c = new Camera();
		
		int width = 1920;
		int height = 1080;
		Vector front = new Vector(1, 0, 0);
		assertEquals(new Vector(960, 540, 0), c.project(front, width, height));
		
		//extra distance should not affect position
		front = new Vector(100, 0, 0);
		assertEquals(new Vector(960, 540, 0), c.project(front, width, height));
		
		Vector diagRight = new Vector(5, 5, 0);
		assertEquals(new Vector(377, 540, 0), c.project(diagRight, width, height));
		//extra distance should not affect position
		diagRight = diagRight.multiply(100);
		assertEquals(new Vector(377, 540, 0), c.project(diagRight, width, height));
		
		Vector right = new Vector(0, 5, 0);
		assertEquals(new Vector(-207, 540, 0), c.project(right, width, height));
		
		
		Vector diagUp = new Vector(5, 0, 5);
		assertEquals(new Vector(960, 868, 0), c.project(diagUp, width, height));
		
		Vector up = new Vector(0, 0, 5);
		assertEquals(new Vector(960, 1196, 0), c.project(up, width, height));
	}
}