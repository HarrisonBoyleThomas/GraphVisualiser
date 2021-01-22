package maths;

import java.lang.Math;

import java.io.*;

/**
*    Rotators represent 3D rotation
*
*    z
*    |    /y
*    |   /
*    |  /
*    | /
*    |_________x
*
*    Roll rotates around the x axis
*    Pitch rotates around the y axis
*    Yaw rotates around the z axis
*
*    Angles are conventionally clockwise
*    For example, (0, 0, 270) means rotate 270 degrees clockwise about the Z axis
*    Many maths functions take anti-clockwise inputs, so
*    must be converted before use
*
*    @Author Harrison Boyle-Thomas
*    @Date 03/11/2020
**/
public class Rotator implements Serializable{

	public final double roll;
	public final double pitch;
	public final double yaw;

	/**
	*    Create a rotator with the given roll pitch and yaw values
	**/
	public Rotator(double rollIn,double pitchIn, double yawIn){
		double newRoll = rollIn % 360;
		if(newRoll < 0){
			newRoll = 360 + newRoll;
		}
		roll = newRoll;
		double newPitch = pitchIn % 360;
		if(newPitch < 0){
			newPitch = 360 + newPitch;
		}
		pitch = newPitch;
		double newYaw = yawIn % 360;
		if(newYaw < 0){
			newYaw = 360 + newYaw;
		}
		yaw = newYaw;
	}

	/**
	*    Create a default rotator with an orientation of 0
	**/
	public Rotator(){
		roll = 0;
		pitch = 0;
		yaw = 0;
	}

	public Rotator add(Rotator other){
		return new Rotator(roll + other.roll, pitch + other.pitch, yaw + other.yaw);
	}

	public static Rotator add(Rotator r1, Rotator r2){
		return r1.add(r2);
	}

	public Rotator subtract(Rotator other){
		return new Rotator(roll - other.roll, pitch - other.pitch, yaw - other.yaw);
	}

	public static Rotator subtract(Rotator r1, Rotator r2){
		return r1.subtract(r2);
	}

	public Rotator addRoll(double amount){
		return new Rotator(roll + amount, pitch, yaw);
	}


	public static Rotator addRoll(Rotator rot, double amount){
		return rot.addRoll(amount);
	}

	public Rotator addPitch(double amount){
		return new Rotator(roll, pitch + amount, yaw);
	}

	public static Rotator addPitch(Rotator rot, double amount){
		return rot.addPitch(amount);
	}

	public Rotator addYaw(double amount){
		return new Rotator(roll, pitch, yaw + amount);
	}

	public static Rotator addYaw(Rotator rot, double amount){
		return rot.addYaw(amount);
	}


	public Rotator subtractRoll(double amount){
		return new Rotator(roll - amount, pitch, yaw);
	}

	public static Rotator subtractRoll(Rotator rot, double amount){
		return rot.subtractRoll(amount);
	}

	public Rotator subtractPitch(double amount){
		return new Rotator(roll, pitch - amount, yaw);
	}

	public static Rotator subtractPitch(Rotator rot, double amount){
		return rot.subtractPitch(amount);
	}

	public Rotator subtractYaw(double amount){
		return new Rotator(roll, pitch, yaw - amount);
	}

	public static Rotator subtractYaw(Rotator rot, double amount){
		return rot.subtractYaw(amount);
	}


	/**
	*    @Return a new Rotator that represents the difference in
	*    rotation from the current vector and the given vector
	*
	*
	*    For example, (50,0,0).delta((90,0,0)) will return (40,0,0),
	*    because the current vector must be rotated 40 units to become the other rotator
	**/
	public Rotator delta(Rotator other){
		return new Rotator(other.roll - roll, other.pitch - pitch, other.yaw - yaw);
	}

	/**
	*    @Return a Rotator representing the difference between the given rotators
	**/
	public static Rotator delta (Rotator r1, Rotator r2){
		return r1.delta(r2);
	}

	/**
	*    Return a Rotator that is in the opposite direction
	**/
	public Rotator opposite(){
		return new Rotator(roll + 180, pitch + 180, yaw + 180);
	}

	/**
	*    Return the opposite rotation to the given Rotator
	**/
	public static Rotator opposite(Rotator r1){
		return r1.opposite();
	}

	/**
	*    Return the Rotator that is rotated in opposite direction
	*    For example, given a rotator (90, 0, 0) clockwise
	*    calling antiClockwise will return (270, 0, 0)
	*    90 clockwise -> 90 anti clockwise = 270 clockwise
	**/
	public Rotator antiClockwise(){
		return new Rotator(360-roll, 360-pitch, 360-yaw);
	}

	/**
	*    @Return the anti rotator for the given Rotator
	**/
	public static Rotator antiClockwise(Rotator r){
		return r.antiClockwise();
	}

	public static Rotator reverseOrder(Rotator r){
		return new Rotator(r.yaw, r.pitch, r.roll);
	}


	@Override
	public boolean equals(Object otherObject){
		if(otherObject instanceof Rotator){
		    Rotator rotator = (Rotator) otherObject;
			return (roll == rotator.roll && pitch == rotator.pitch && yaw == rotator.yaw);
		}
		return false;
	}

	@Override
    public String toString() {
        return "(" + roll + "," + pitch + "," + yaw + ")";
    }

	public String toStringNeat(){
		String strR = String.format("%1$5s", "" + Functions.round(roll, 1)).substring(0, 4);
		String strP = "" + String.format("%1$5s", Functions.round(pitch, 1)).substring(0, 4);
		String strY = "" + String.format("%1$5s", Functions.round(yaw, 1)).substring(0, 4);
		return "(" + strR + "," + strP + "," + strY + ")";
	}
}
