package maths;

import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Functions{
	/**
	*    Find the rotation needed to rotate clockwise from v1 to v2
	*    with respect to the default rotation
	*    The rotation is applied in stages: yaw, pitch
	*    For example,
	*    To rotate to (5, 5, 5) from (0,0,0):
	*    Yaw 315 degrees clockwise,
	*    then from there, pitch approx. 325 degrees clockwise
	*    
	*    All directions can be achieved without rolling, so roll
	*    is omitted in the output Rotator
	*    To get anti-clockwise rotation, call antiRotation on the output Rotator
	**/
    public static Rotator getRotationBetweenVectors(Vector v1, Vector v2){
	    
    	Vector diff = Vector.normalise(Vector.subtract(v2, v1));
		
		if(diff == null){
			return new Rotator(0, 0, 180);
		}
    	
		//Roll cannot be extracted
    	//double rollAngle = Math.toDegrees(Math.atan2(diff.y, diff.z));
    	
    	//double pitchAngle = Math.toDegrees(Math.atan2(diff.z, diff.x));
    	
    	double yawAngle = Math.toDegrees(Math.atan2(diff.y, diff.x));
		
		
		Vector yawedVector = rotateVector(new Vector(), diff, new Rotator(0, 0, yawAngle));
		
		double pitchAngle = Math.toDegrees(Math.atan2(yawedVector.z, yawedVector.x));
    	
    	return new Rotator(0, pitchAngle, yawAngle).antiClockwise();
    }
	
	/**
	*    Rotate a given vector about the given origin clockwise by the given rotator
	**/
	public static Vector rotateVector(Vector origin, Vector v, Rotator r){
		Rotator opposite = r.antiClockwise();
		
		Vector diff = v.subtract(origin);
		double yaw = Math.toRadians(opposite.yaw);
		double x = round((diff.x * Math.cos(yaw)) - (diff.y * Math.sin(yaw)), 3);
		double y = round((diff.x * Math.sin(yaw)) + (diff.y * Math.cos(yaw)), 3);
	    double z = v.z;
		Vector zVector = new Vector(x, y, z);
		
        
        double pitch = Math.toRadians(opposite.pitch);
        x = round((zVector.x * Math.cos(pitch)) + (zVector.z * Math.sin(pitch)), 3);
		y = zVector.y;
		z = round((-zVector.x * Math.sin(pitch)) + (zVector.z * Math.cos(pitch)), 3);
		Vector yzVector = new Vector(x, y, z);
		
		
		double roll = Math.toRadians(opposite.roll);
		x = yzVector.x;
		y = round((yzVector.y * Math.cos(roll)) - (yzVector.z * Math.sin(roll)), 3);
		z = round((yzVector.y * Math.sin(roll)) + (yzVector.z * Math.cos(roll)), 3);
		Vector xyzVector = new Vector(x, y, z);
		return origin.add(xyzVector);
		
	}
	
	
	/**
	*    Rounds the given value the given number of decimal places
	**/
	public static double round(double value, int dp){
		if(dp < 0){
			return value;
		}
		BigDecimal decimal = new BigDecimal(value).setScale(dp, RoundingMode.HALF_UP);
		return decimal.doubleValue();
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
		double roll = Math.toRadians(r.roll);
		double pitch = Math.toRadians(r.pitch);
		double yaw = Math.toRadians(r.yaw);
		return new Vector(Math.cos(yaw) * Math.cos(pitch), Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch));
	}
	
	public static Rotator getRotatorFromUnitVector(Vector v){
		return new Rotator(Math.acos(v.x) * 180 / Math.PI, Math.acos(v.y) * 180 / Math.PI, Math.acos(v.z) );
	}
	
	public static double clamp(double value, double min, double max){
		if(value < min){
			return min;
		}
		if(value > max){
			return max;
		}
		return value;
	}
}