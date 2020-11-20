package viewport;

import maths.Functions;
import maths.Vector;
import maths.Rotator;


public class Camera extends Actor{
	
	private Rotator rotation;
	private Rotator defaultRotation;
	
	private Vector defaultLocation;
	
	//Sensitivity in navigating 3D space
	private double xSensitivity = 1;
	//Sensitivity in rotating in 3D space
	private double rSensitivity = 10;
	
	private int fieldOfViewAngle = 74;
	//The relative position of the display to the camera
	private Vector displayPosition;
	
	//Nodes beyond this distance are not rendered
	private double maxDrawDistance = 1000;
	
	public Camera(Rotator initialRotation, Vector initialLocation){
		rotation = initialRotation;
		defaultRotation = initialRotation;
		location = initialLocation;
		defaultLocation = initialLocation;
		displayPosition = new Vector(1, 0, 0);
	}
	
	public Camera(){
		defaultRotation = new Rotator();
		resetRotation();
		defaultLocation = new Vector();
		resetLocation();
		displayPosition = new Vector(1, 0, 0);
	}
	
	public Rotator resetRotation(){
		rotation = defaultRotation;
		return rotation;
	}
	
	public Vector resetLocation(){
		location = defaultLocation;
		return location;
	}
	
	public void reset(){
		resetRotation();
		resetLocation();
	}
	
	public double setMaxDrawDistance(double distanceIn){
		maxDrawDistance = distanceIn;
		return maxDrawDistance;
	}
	
	public double getMaxDrawDistance(){
		return maxDrawDistance;
	}
	
	/**
	*    @Return the number of units the camera moves by when moving in 3D space
	**/
	public double getXSensitivity(){
		return xSensitivity;
	}
	
	/**
	*    @Return set the number of degrees the camera will move by when rotating in 3D space
	**/
	public double getRotationSensitivity(){
		return rSensitivity;
	}
	
	public int setfieldOfViewAngle(int fovIn){
		if(fovIn < 45){
			fieldOfViewAngle = 45;
		}
		else if(fovIn > 179){
			fieldOfViewAngle = 180;
		}
		else{
			fieldOfViewAngle = fovIn;
		}
		return fieldOfViewAngle;
	}
	
	public int getfieldOfViewAngle(){
		return fieldOfViewAngle;
	}
	
	/**
	*    set the number of units the camera will move by when
	*    moving in 3D space
	*    The default sensitivity is 1
	*    @Param the new sensitivity
	*    @Return the sensitivity
	**/
	public double setXInputSensitivity(double sensitivityIn){
		if(sensitivityIn < 1){
			xSensitivity = 1;
		}
		else if(sensitivityIn > 10){
			xSensitivity = 10;
		}
		else{
			xSensitivity = sensitivityIn;
		}
		return xSensitivity;
	}
	
	/**
	*    set the number of degrees the camera will move by when
	*    rotating in 3D space
	*    The default sensitivity is 10
	*    @Param the new sensitivity
	*    @Return the sensitivty
	**/
	public double setRInputSensitivity(double sensitivityIn){
		if(sensitivityIn < 1){
			rSensitivity = 1;
		}
		else if(sensitivityIn > 10){
			rSensitivity = 10;
		}
		else{
			rSensitivity = sensitivityIn;
		}
		return rSensitivity;
	}
	
	/**
	*    @Return the rotation of the camera
	**/
	public Rotator getRotation(){
		return rotation;
	}

	//Controls section
	
	/**
	*    Move the camera along it's relative x axis
	*    a positive input will move forward
	*    a negative input will move backward
	*    
	*    @Return the new location of the camera
	**/
	public Vector moveForward(double inputAxis){
		Vector magnitude = new Vector(xSensitivity, 0, 0);
		magnitude = magnitude.multiply(inputAxis);
		
		magnitude = Functions.rotateVector(new Vector(), magnitude, rotation);
		
		return addLocation(magnitude);
	}
	
	/**
	*    Move the camera along it's relative y axis
	*    A positive input will move to the left
	*    A negative input will move to the right
	*
	*    @Return the new location of the camera
	**/
	public Vector moveSideways(double inputAxis){
		Vector magnitude = new Vector(0, xSensitivity, 0);
		magnitude = magnitude.multiply(inputAxis);
		
		magnitude = Functions.rotateVector(new Vector(), magnitude, rotation);
		
		return addLocation(magnitude);
	}
	
	/**
	*    Move the camera along the world z axis
	*    A positive input will move upwards from the world origin (0, 0, 0)
	*    A negative input will move downwards from the world origin
	*
	*    @Return the new location of the camera
	**/
	public Vector moveUpwards(double inputAxis){
		Vector magnitude = new Vector(0, 0, xSensitivity);
		magnitude = magnitude.multiply(inputAxis);
		
		magnitude = Functions.rotateVector(new Vector(), magnitude, rotation);
		
		return addLocation(magnitude);
	}
	
	/**
	*    Rotate the camera about the x axis
	*    @Return the new rotation
	**/
	public Rotator roll(double inputAxis){
		//User's ability to roll undecided
		rotation = rotation.addRoll(rSensitivity * inputAxis);
		return rotation;
	}
	
	/**
	*    Rotate the camera about the y axis
	*    @Return the new rotation
	**/
	public Rotator pitch(double inputAxis){
		rotation = rotation.addPitch(rSensitivity * inputAxis);
		return rotation;
	}
	
	/**
	*    Rotate the camera about the z axis
	*    @Return the new rotation
	**/
	public Rotator yaw(double inputAxis){
		rotation = rotation.addYaw(rSensitivity * inputAxis);
		return rotation;
	}
	
	public Vector getRelativePosition(Vector target){
		Vector diff = Vector.subtract(target, location);
		return Functions.rotateVector(new Vector(), diff, rotation.antiClockwise());
	}
	
	/**
	*    Project the given 3D point
	*    to 2D screen-space by using an orthographic
	*    projection
	*    The scale is calculated by calculating the percentage of
	*    look at rotation by fov angle
	*    Points that lie along the same unit vector will share a screen position
	*
	*    @Return a Vector of (x, y, 0) of the position in 2D space, with origin (0,0) at the center of the screen
	*
	*    The screens bounds are the width and height, so a 3D coordinate is on screen
	*    If it is within -w/2 to 2/2 and -h/2 to h/2
	*    @Param target a location in 3D space
	*    @Param width and height of the screen
	*    The output must be translated to the renderer's screen space to be rendered correctly
	**/
	public Vector projectOrthographic(Vector target, int width, int height){
		Vector relativePosition = getRelativePosition(target);
		Rotator lookAtRotation = Functions.getRotationBetweenVectors(new Vector(0, 0, 0), relativePosition);
		
		double xScale = 0.0;
		double yScale = 0.0;
		if(lookAtRotation.yaw <= 180.0){	
		    xScale = lookAtRotation.yaw / fieldOfViewAngle;
		}
		else{
			xScale = -((360 - lookAtRotation.yaw) / fieldOfViewAngle);
		}
		
		if(lookAtRotation.pitch <= 180){
			yScale = lookAtRotation.pitch / fieldOfViewAngle;
		}
		else{
			yScale = -((360 - lookAtRotation.pitch) / fieldOfViewAngle);
		}
		int x = (int) (width * xScale / 2);
		int y = (int) (height * yScale / 2);
		
		return new Vector(x, y, 0);
	}
	
	
	public Vector projectPerspective(Vector target, int width, int height){
		double aspectRatio = width / height;
		int nearClip = 1;
		int farClip = 100000;
		return null;
	}
	
	/**
	*    Convert the given 2D vector to a JavaFX renderer coordinate
	*    @Param v a 2D screen space vector
	*    @Param width and height of the screen
	*    @Return a Vector with a JavaFX coordinate
	**/
	public Vector convert2DAxis(Vector v, int width, int height){
		int halfWidth = width/2;
		int halfHeight = height/2;
		
		int x = (int) (v.x + halfWidth);
		int y = (int) (-(v.y) + halfHeight);
		return new Vector(x, y, 0);
	}
	
	/**
	*    Projects a given coordinate to
	*    JavaFX screen space
	*    @Param target = a location in 3D space
	*    @Param width and height of the screen
	*    
	*    @Return a Vector representing the screen-space position of an orthographic projection
	**/
	public Vector project(Vector target, int width, int height){
		return convert2DAxis(projectOrthographic(target, width, height), width, height);
	}
	
	public String toString(){
		return "Camera: location= " + location + " rotation= " + rotation;
	}
}