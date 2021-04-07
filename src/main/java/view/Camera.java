package viewport;

import maths.Functions;
import maths.Vector;
import maths.Rotator;

/**
*    The Camera is a core part of the viewport. It projects coordinates to screen-space,
*    relative to it's coordinates, which can be changed by the user
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
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
	private double maxDrawDistance = setMaxDrawDistance(100);

    /**
	*    @param initialRotation the rotation to give the camera when it is created
	*    @param initialLocation the location to give the camera when it is created
	**/
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

    /**
	*    Set the rotation to the default rotation
	*    @return the new rotation
	**/
	public Rotator resetRotation(){
		rotation = defaultRotation;
		return rotation;
	}
   /**
   *    Set the location to the default location
   *    @return the new location
   **/
	public Vector resetLocation(){
		location = defaultLocation;
		return location;
	}
    /**
	*    Reset rotation and location to their default values
	**/
	public void reset(){
		resetRotation();
		resetLocation();
	}
    /**
	*    @param distanceIn the new distance
	**/
	public double setMaxDrawDistance(double distanceIn){
		maxDrawDistance = Functions.clamp(distanceIn, 10, 200);
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
    /**
	*    Set the field of view of the camera
	*    The input value is clamped between 45 and 179 exclusive
	*    @return the new value
	**/
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
		Vector magnitude = getForwardVector();
		magnitude = magnitude.multiply(inputAxis * xSensitivity);

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
		Vector magnitude = getRightVector();
		magnitude = magnitude.multiply(inputAxis * xSensitivity);

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
		Vector magnitude = getUpVector();
		magnitude = magnitude.multiply((-inputAxis) * xSensitivity);

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
	*    Rotate the camera about the y axis of the world
	*    @Return the new rotation
	**/
	public Rotator pitch(double inputAxis){
		rotation = rotation.addPitch(rSensitivity * inputAxis);
		return rotation;
	}
	/**
	*    Rotate the camera about the z axis of the world
	*    @Return the new rotation
	**/
	public Rotator yaw(double inputAxis){
		rotation = rotation.addYaw(rSensitivity * inputAxis);
		return rotation;
	}

	/**
	*    Rotate the camera about the relative y axis of the camera
	*    @Return the new rotation
	**/
	public Rotator pitchRelative(double inputAxis){
		double mult = inputAxis * rSensitivity;
		Vector forward = getForwardVector();
		Rotator delta = new Rotator(0, mult * Math.cos(Math.toRadians(rotation.roll)), mult * Math.sin(Math.toRadians(rotation.roll)));
		Vector pitchVector = Functions.rotateVectorInverse(new Vector(), forward, delta).normalise();
		Rotator newRotation = Functions.getRotationBetweenVectors(new Vector(), pitchVector);
		rotation = new Rotator(rotation.roll, newRotation.pitch, newRotation.yaw);
		//rotation = rotation.addPitch(rSensitivity * inputAxis);
		return rotation;
	}

	/**
	*    Rotate the camera about the relative z axis of the camera
	*    @Return the new rotation
	**/
	public Rotator yawRelative(double inputAxis){
		double mult = inputAxis * rSensitivity;
		Vector forward = getForwardVector();
		Rotator delta = new Rotator(0, mult * Math.sin(Math.toRadians(rotation.roll)), mult * Math.cos(Math.toRadians(rotation.roll)));
		Vector yawVector = Functions.rotateVectorInverse(new Vector(), forward, delta).normalise();
		Rotator newRotation = Functions.getRotationBetweenVectors(new Vector(), yawVector);
		rotation = new Rotator(rotation.roll, newRotation.pitch, newRotation.yaw);
		//rotation = rotation.addYaw(rSensitivity * inputAxis);
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

    /**
	*    Perspective projection. This method does not work, and is not used
	**/
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
    /**
	*    @return true if the distance between the camera and the target is less than the maxdrawdistance
	**/
	public boolean isInViewRange(Vector target){
		return Vector.distance(location, target) < maxDrawDistance;
	}
    /**
	*    @return true if the distance between the camera and the target is less than the maxdrawdistance
	**/
	public boolean isInViewRange(Actor target){
		return Vector.distance(location, target.getLocation()) < maxDrawDistance;
	}

	/**
	*    @Return true if the given actor is in front of the camera
	**/
	public boolean isInFront(Actor target){
		Vector relative = getRelativePosition(target.getLocation());
		return relative.x > 0;
	}

	/**
	*    @Return a unit vector representing the direction of the camera
	**/
	public Vector getForwardVector(){
		Vector front = new Vector(1, 0, 0);
		return Functions.rotateVectorInverse(new Vector(), front, rotation).normalise();
	}

	/**
	*    @Return a unit vector representing the sideways direction of the camera(right side)
	**/
	public Vector getRightVector(){
		Vector right = new Vector(0, 1, 0);
		return Functions.rotateVectorInverse(new Vector(), right, rotation).normalise();
	}

	/**
	*    @Return a unit vector representing the upwards direction of the camera(above)
	**/
	public Vector getUpVector(){
		Vector up = new Vector(0, 0, 1);
		return Functions.rotateVectorInverse(new Vector(), up, rotation).normalise();
	}


	public String toString(){
		return "Camera: location= " + location + " rotation= " + rotation;
	}
}
