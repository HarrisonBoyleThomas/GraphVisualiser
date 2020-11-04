package maths;

import java.lang.Math; 

/**
*    Given two vectors, find the roll, pitch, and yaw angles from v1 to v2
**/
public class Functions{
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
    	
    	int rollAngle = (int) Math.toDegrees(Math.atan2(new Double(diff.y), new Double(diff.z)));
    	
    	int pitchAngle = (int) Math.toDegrees(Math.atan2(new Double(diff.z), new Double(diff.x)));
    	
    	int yawAngle = (int) Math.toDegrees(Math.atan2(new Double(diff.y), new Double(diff.x)));
    	
    	return new Rotator(rollAngle, pitchAngle, yawAngle);
    }
	
	public static Rotator findLookAtRotation(Vector v1, Vector v2){
		Vector diff = Vector.subtract(v2, v1);
		
		double length = diff.length();
		int alpha = (int) Math.acos(diff.x / length);
		
		int beta = (int) Math.acos(diff.y / length);
		
		int gamma = (int) Math.acos(diff.z / length);
		
		return new Rotator();
	}
}