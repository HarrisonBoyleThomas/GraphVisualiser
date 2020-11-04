package maths;

import java.lang.Math; 

/**
*    Given two vectors, find the roll, pitch, and yaw angles from v1 to v2
**/
public class Functions{
	/**
	*    Find the rotation needed to rotate from v1 to v2
	*    with respect to the default rotation
	**/
    public static Rotator getRotationBetweenVectors(Vector v1, Vector v2){
	    
	    //roll angle
    	//int rollDot = Vector.dot(new Vector(0, v1.y, v1.z), new Vector(0, v2.y, v2.z));
    	//float rollAngle = Math.acos(rollDot / ((new Vector(0, v1.y, v1.z)).distanceFromOrigin() * (new Vector(0, v2.y, v2.z)).distanceFromOrigin()));
    	
    	//pitch angle
    	//int pitchDot = Vector.dot(new Vector(v1.x, 0, v1.z), new Vector(v2.x, 0, v2.z));
    	//float pitchAngle = Math.acos(pitchDot / ((new Vector(v1.x, 0, v1.z)).distanceFromOrigin() * (new Vector(v2.x, 0, v2.z)).distanceFromOrigin()));
    	
    	//yaw angle
    	//int yawDot = Vector.dot(new Vector(v1.x, v1.y, 0), new Vector(v2.x, v2.y, v2.z));
    	//float yawAngle = Math.acos(yawDot / ((new Vector(v1.x, v1.y, 0)).distanceFromOrigin() * (new Vector(v2.x, v2.y, 0)).distanceFromOrigin()));
    	
    	
    	Vector diff = Vector.normalise(Vector.subtract(v2, v1));
    	
    	double rollAngle = Math.toDegrees(Math.atan2(diff.y, diff.z));
    	
    	double pitchAngle = Math.toDegrees(Math.atan2(diff.z, diff.x));
    	
    	double yawAngle = Math.toDegrees(Math.atan2(diff.y, diff.x));
    	
    	return new Rotator(0, pitchAngle, yawAngle);
    }
	
	/**
	*    Extracts the direction cosines, and returns the angle
	*    needed to rotate from v1 to v2
	**/
	public static Rotator findLookAtRotation(Vector v1, Vector v2){
		Vector diff = Vector.subtract(v2, v1);
		
		double length = diff.length();
		
		if(length == 0){
			return new Rotator();
		}
		double roll = Math.acos(diff.x / length)*180/Math.PI;
		
		double pitch = Math.acos(diff.y / length)*180/Math.PI;
		
		double yaw = Math.acos(diff.z / length)*180/Math.PI;
		
		return new Rotator(roll, pitch, yaw);
	}
	
	public static Vector getUnitVectorFromRotation(Rotator r){
		return new Vector(Math.cos(r.roll), Math.cos(r.pitch), Math.cos(r.yaw));
	}
}