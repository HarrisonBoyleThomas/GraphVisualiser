package viewport;

import maths.*;

public class Camera{
	
	private Rotator rotation;
	private Rotator defaultRotation
	
	private Vector defaultLocation;
	
	//Sensitivity in navigating 3D space
	private double xSensitivity = 1;
	//Sensitivity in rotating in 3D space
	private double rSensitivity = 10;
	
	public Camera(Rotator initialRotation, Rotator initialLocation){
		rotation = initialRotation;
		defaultRotation = initialRotation;
		location = initialLocation;
		defaultLocation = initialLocation;
	}
	
	public Camera(){
		defaultRotation = new Rotator();
		resetRotation();
		reLocation = new Vector();
		setDefaultLocation();
	}
	
	public Rotator resetRotation(){
		rotation = defaultRotation;
		return rotation;
	}
	
	public Rotator resetLocation(){
		location = defaultLocation;
		return location;
	}
	
	public void reset(){
		resetRotation();
		resetLocation();
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
		magnitude.multiply(inputAxis);
		
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
}