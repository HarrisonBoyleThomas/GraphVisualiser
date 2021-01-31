package maths;

import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
*    The Functions class contains many useful operations on Vectors and
*    Rotators
*    @Author Harrison Boyle-Thomas
*    @Date 31/01/21
**/
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
    *    @Param v1 the origin vector
    *    @Param v2 the target vector
    *    @Return the clockwise rotation (yaw-pitch-roll) from v1 to v2
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
    *    Get the anti-clockwise rotation between two vectors (yaw-pitch-roll)
    *    e.g: 30deg anticlockwise = 330deg clockwise
    *    @Return a Rotator repreenting a clock-wise representation equal to an anti-clockwise rotation
    **/
	public static Rotator getRotationBetweenVectorsInverse(Vector v1, Vector v2){
		Vector diff = Vector.normalise(Vector.subtract(v2, v1));

		if(diff == null){
			return new Rotator(0, 0, 180);
		}

		double pitchAngle = Math.toDegrees(Math.atan2(diff.z, diff.x));

		Vector pitchedVector = rotateVector(new Vector(), diff, new Rotator(0, pitchAngle, 0));

		double yawAngle = Math.toDegrees(Math.atan2(pitchedVector.y, pitchedVector.x));

		return new Rotator(0, pitchAngle, yawAngle).antiClockwise();
	}

	/**
	*    Rotate a given vector about the given origin clockwise by the given rotator
    *    in the order (yaw-pitch-roll)
    *    @Param origin the origin of the rotation
    *    @Param v the vector to rotate
    *    @Param r the rotation to rotate the vector by
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
    *    Rotate a vector in opposite component order (roll-pitch-yaw),
    *    according to Davenport rotation
    *    @Param origin the origin of rotation
    *    @Param v the vector to rotate
    *    @Param r rotation to rotate by
    *    @Return a Vector of the new position
    **/
	public static Vector rotateVectorInverse(Vector origin, Vector v, Rotator r){
		Rotator opposite = r.antiClockwise();

		Vector diff = v.subtract(origin);

		double roll = Math.toRadians(opposite.roll);
		double x = v.x;
		double y = round((diff.y * Math.cos(roll)) - (diff.z * Math.sin(roll)), 3);
		double z = round((diff.y * Math.sin(roll)) - (diff.z * Math.cos(roll)), 3);
		Vector xVector = new Vector(x, y, z);

		double pitch = Math.toRadians(opposite.pitch);
        x = round((xVector.x * Math.cos(pitch)) + (xVector.z * Math.sin(pitch)), 3);
		y = xVector.y;
		z = round((-xVector.x * Math.sin(pitch)) + (xVector.z * Math.cos(pitch)), 3);
		Vector yxVector = new Vector(x, y, z);

		double yaw = Math.toRadians(opposite.yaw);
		x = round((yxVector.x * Math.cos(yaw)) - (yxVector.y * Math.sin(yaw)), 3);
		y = round((yxVector.x * Math.sin(yaw)) + (yxVector.y * Math.cos(yaw)), 3);
	    z = yxVector.z;
		Vector zyxVector = new Vector(x, y, z);
		return origin.add(zyxVector);

	}


	/**
	*    Rounds the given value the given number of decimal places
    *    @Param value the value to round
    *    @Param dp the number of decomal places to round to
    *    @Return the rounded value to dp decimal places, or the value, if dp < 0
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
    *    @Param v1 the origin vector
    *    @Param v2 the vector to find the direction to
    *    @Return the rotation cosine from v1 to v2
	**/
	public static Rotator findLookAtRotation(Vector v1, Vector v2){
		Vector diff = Vector.subtract(v2, v1);

		double length = diff.length();

		if(length == 0){
			return new Rotator();
		}


		double yaw = Math.toDegrees(Math.atan2(diff.z, diff.x));

		double adj = Math.sqrt(Math.pow(diff.x, 2) + Math.pow(diff.z, 2));

		double pitch = Math.atan2(diff.y, adj);

		return new Rotator(0, pitch, yaw);
	}


    /**
    *    Get the Vector representation of a Rotator
    *    @Param r the rotator to convert
    *    @Return A unit vector of representing the rotation
    **/
	public static Vector findVectorFromRotation(Rotator r){
		double z = Math.sin(Math.toRadians(r.yaw));
		double x = Math.cos(Math.toRadians(r.yaw));
		double y = Math.sin(Math.toRadians(r.pitch));
		return new Vector(x, y, z);
	}

    /**
    *    Get a unit vector representation of a Rotator
    *    @Param r the rotator to convert
    *    @Return a vector representation of the given rotator
    **/
	public static Vector getUnitVectorFromRotation(Rotator r){
		double roll = Math.toRadians(r.roll);
		double pitch = Math.toRadians(r.pitch);
		double yaw = Math.toRadians(r.yaw);
		return new Vector(Math.cos(yaw) * Math.cos(pitch), Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch));
	}

    /**
    *    Convert a vector to a rotator
    *    @Param v the vector to convert
    *    @Return a Rotator in the yaw-pitch-roll order of the given vector
    **/
	public static Rotator getRotatorFromUnitVector(Vector v){
		return new Rotator(Math.acos(v.x) * 180 / Math.PI, Math.acos(v.y) * 180 / Math.PI, Math.acos(v.z) );
	}

    /**
    *    Return a double, such that min <= value <= max
    *    @Param value the value to clamp
    *    @Param min the minimum value to return
    *    @Param max the maximum value to return
    *    @Return the clamped value
    **/
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
