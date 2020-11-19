package viewport;

import java.util.concurrent.ThreadLocalRandom;

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
	public void resetLocationTest(){
		Camera c = new Camera();
		c.moveForward(1.0);
		
		//make sure that the location has changed
		assertNotEquals(new Vector(), c.getLocation());
		
		
		assertEquals(new Vector(), c.resetLocation());
		assertEquals(new Vector(), c.getLocation());
	}
	
	@Test
	public void resetRotationTest(){
		Camera c = new Camera();
		c.roll(1.0);
		c.pitch(1.0);
		c.yaw(1.0);
		
		//make sure that the rotation has changed
		assertNotEquals(new Rotator(), c.getRotation());
		
		assertEquals(new Rotator(), c.resetRotation());
		assertEquals(new Rotator(), c.getRotation());
	}
	
	@Test
	public void resetTest(){
		Camera c = new Camera();
		
		c.moveForward(1.0);
		c.roll(1.0);
		c.pitch(1.0);
		c.yaw(1.0);
		
		//make sure that the location has changed
		assertNotEquals(new Vector(), c.getLocation());
		//make sure that the rotation has changed
		assertNotEquals(new Rotator(), c.getRotation());
		
		c.reset();
		
		assertEquals(new Vector(), c.getLocation());
		assertEquals(new Rotator(), c.getRotation());
	}
	
	@Test
	public void xSensitivityTest(){
		Camera c = new Camera();
		
		int input = ThreadLocalRandom.current().nextInt(1, 11);
		
		assertEquals(input, c.setXInputSensitivity(input));
		assertEquals(input, c.getXSensitivity());
		
		//bounds tests
		assertEquals(1.0, c.setXInputSensitivity(0.0));
		assertEquals(1.0, c.getXSensitivity());
		
		assertEquals(10.0, c.setXInputSensitivity(11.0));
		assertEquals(10.0, c.getXSensitivity());
	}
	
	@Test
	public void rSensitivityTest(){
		Camera c = new Camera();
		int input = ThreadLocalRandom.current().nextInt(1, 11);
		
		assertEquals(input, c.setRInputSensitivity(input));
		assertEquals(input, c.getRotationSensitivity());
		
		//bounds tests
		assertEquals(1.0, c.setRInputSensitivity(0.0));
		assertEquals(1.0, c.getRotationSensitivity());
		
		assertEquals(10.0, c.setRInputSensitivity(11.0));
		assertEquals(10.0, c.getRotationSensitivity());
	}
	
	@Test
	public void moveForwardTest(){
		Camera c = new Camera();
		
		double totalX = 0.0;
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setXInputSensitivity((double) sensitivity);
			c.moveForward(1.0);
			totalX += sensitivity;
			
			assertEquals(new Vector(totalX, 0.0, 0.0), c.getLocation());
		}
		
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setXInputSensitivity((double) sensitivity);
			c.moveForward(-1.0);
			totalX -= sensitivity;
			
			assertEquals(new Vector(totalX, 0.0, 0.0), c.getLocation());
		}
	}
	
	@Test
	public void moveSidewaysTest(){
		Camera c = new Camera();
		
		double totalY = 0.0;
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setXInputSensitivity((double) sensitivity);
			c.moveSideways(1.0);
			totalY += sensitivity;
			
			assertEquals(new Vector(0.0, totalY, 0.0), c.getLocation());
		}
		
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setXInputSensitivity((double) sensitivity);
			c.moveSideways(-1.0);
			totalY -= sensitivity;
			
			assertEquals(new Vector(0.0, totalY, 0.0), c.getLocation());
		}
	}
	
	@Test
	public void moveUpwardsTest(){
		Camera c = new Camera();
		
		double totalZ = 0.0;
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setXInputSensitivity((double) sensitivity);
			c.moveUpwards(1.0);
			totalZ += sensitivity;
			
			assertEquals(new Vector(0.0, 0.0, totalZ), c.getLocation());
		}
		
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setXInputSensitivity((double) sensitivity);
			c.moveUpwards(-1.0);
			totalZ -= sensitivity;
			
			assertEquals(new Vector(0.0, 0.0, totalZ), c.getLocation());
		}
	}
	
	@Test
	public void rollTest(){
		Camera c = new Camera();
		
		double totalRoll = 0.0;
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setRInputSensitivity((double) sensitivity);
			c.roll(1.0);
			totalRoll += sensitivity;
			
			assertEquals(new Rotator(totalRoll, 0.0, 0.0), c.getRotation());
		}
		
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setRInputSensitivity((double) sensitivity);
			c.roll(-1.0);
			totalRoll -= sensitivity;
			
			assertEquals(new Rotator(totalRoll, 0.0, 0.0), c.getRotation());
		}
	}
	
	@Test
	public void pitchTest(){
		Camera c = new Camera();
		
		double totalPitch = 0.0;
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setRInputSensitivity((double) sensitivity);
			c.pitch(1.0);
			totalPitch += sensitivity;
			
			assertEquals(new Rotator(0.0, totalPitch, 0.0), c.getRotation());
		}
		
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setRInputSensitivity((double) sensitivity);
			c.pitch(-1.0);
			totalPitch -= sensitivity;
			
			assertEquals(new Rotator(0.0, totalPitch, 0.0), c.getRotation());
		}
	}
	
	@Test
	public void yawTest(){
		Camera c = new Camera();
		
		double totalYaw = 0.0;
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setRInputSensitivity((double) sensitivity);
			c.yaw(1.0);
			totalYaw += sensitivity;
			
			assertEquals(new Rotator(0.0, 0.0, totalYaw), c.getRotation());
		}
		
		for(int sensitivity = 1; sensitivity <= 10; sensitivity++){
			c.setRInputSensitivity((double) sensitivity);
			c.yaw(-1.0);
			totalYaw -= sensitivity;
			
			assertEquals(new Rotator(0.0, 0.0, totalYaw), c.getRotation());
		}
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