package maths;

import java.lang.Math;

import java.io.*;

/**
*    Rotators represent 3D rotation
*
*    z
*    |       /y
*    |     /
*    |   /
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
	*    @param rollIn the roll of the Rotator
	*    @param pitchIn thew pitch of the Rotator
	*    @param yawIn the yaw of the Rotator
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

    /**
	*    Get the addition of the given rotator to the current rotator
	*    @param other the rotator to add
	*    @return a new rotator representing the effect of the addition
	**/
	public Rotator add(Rotator other){
		return new Rotator(roll + other.roll, pitch + other.pitch, yaw + other.yaw);
	}

    /**
	*    Get the addition of the two supplied rotators
	*    @param r1 operand one of the addition
	*    @param r2 operand two of the addition
	*    @return a rotator representing the result
	**/
	public static Rotator add(Rotator r1, Rotator r2){
		return r1.add(r2);
	}

    /**
	*    Get the subtraction of the given rotator from the current rotator
	*    @param other the rotator to subtract
	*    @return a new rotator representing the effect of the subtraction
	**/
	public Rotator subtract(Rotator other){
		return new Rotator(roll - other.roll, pitch - other.pitch, yaw - other.yaw);
	}

    /**
	*    Get the subtraction of r2 from r1
	*    @param r1 operand one of the subtraction
	*    @param r2 operand two of the subtraction
	*    @return a rotator representing the result
	**/
	public static Rotator subtract(Rotator r1, Rotator r2){
		return r1.subtract(r2);
	}

    /**
	*    Get a new rotator representing the increment of the roll component of the current rotator
	*    @param amount the amount to increment the roll by
	*    @return the effect of the roll operation
	**/
	public Rotator addRoll(double amount){
		return new Rotator(roll + amount, pitch, yaw);
	}

    /**
	*    Get a rotator representing the output of a roll operation
	*    @param rot the rotator to apply the operation on
	*    @param amount the amount to roll by
	*    @return a new rotator representing the output of the operation
	**/
	public static Rotator addRoll(Rotator rot, double amount){
		return rot.addRoll(amount);
	}

    /**
	*    Get a rotator representing the effect of pitching the current rotator by the given amount
	*    @param amount the amount to pitch by
	*    @return a rotator representing the output
	**/
	public Rotator addPitch(double amount){
		return new Rotator(roll, pitch + amount, yaw);
	}

    /**
	*    Get a rotator representing the output of a pitch on the given rotator
	*    @param rot the rotator to apply the pitch on
	*    @param amount the amount to pitch by
	**/
	public static Rotator addPitch(Rotator rot, double amount){
		return rot.addPitch(amount);
	}

	/**
	*    Get a rotator representing the output of incrementing the yaw component of the current
	*    rotator
	*    @param amount the amount to yaw by
	*    @return a new rotator representing the result of the operation
	**/
	public Rotator addYaw(double amount){
		return new Rotator(roll, pitch, yaw + amount);
	}

    /**
	*    Get a rotator representing the output of a yaw on the given rotator
	*    @param rot the rotator to apply the yaw on
	*    @param amount the amount to yaw by
	*    @return a new rotator representing the output of the yaw
	**/
	public static Rotator addYaw(Rotator rot, double amount){
		return rot.addYaw(amount);
	}

	/**
	*    @param amount the amount to decrease the roll by
	*    @return a rotator representing the output of the operation
	**/
	public Rotator subtractRoll(double amount){
		return new Rotator(roll - amount, pitch, yaw);
	}

    /**
	*    @param rot the rotator to apply the operation on
	*    @param amount the amount to decrement the roll by
	*    @return the a rotator representing the output of the operation
	**/
	public static Rotator subtractRoll(Rotator rot, double amount){
		return rot.subtractRoll(amount);
	}

    /**
	*    @param amount the amount to decrease the pitch by
	*    @return a new rotator representing the effect of the operation
	**/
	public Rotator subtractPitch(double amount){
		return new Rotator(roll, pitch - amount, yaw);
	}

    /**
	*    @param rot the rotator to apply the pitch on
	*    @param amount the amount to decrease the pitch by
	*    @return a new rotator representing the output of the operation
	**/
	public static Rotator subtractPitch(Rotator rot, double amount){
		return rot.subtractPitch(amount);
	}

    /**
	*    @param amount the amount to decrease the yaw by
	*    @return a new rotator representing the result of the operation
	**/
	public Rotator subtractYaw(double amount){
		return new Rotator(roll, pitch, yaw - amount);
	}

	/**
	*    @param rot the rotator to apply the operation on
	*    @param amount the amount to decrease the yaw by
	*    @return a nw rotator representing the result of the operation
	**/
	public static Rotator subtractYaw(Rotator rot, double amount){
		return rot.subtractYaw(amount);
	}


	/**
	*    @param other the rotator to find the difference in rotation from
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
	*    @param r1 origin Rotation
	*    @param r2 target rotation
	*    @Return a Rotator representing the difference between the given rotators
	**/
	public static Rotator delta (Rotator r1, Rotator r2){
		return r1.delta(r2);
	}

	/**
	*    @return a Rotator that is in the opposite direction
	**/
	public Rotator opposite(){
		return new Rotator(roll + 180, pitch + 180, yaw + 180);
	}

	/**
	*    @param r1 the rotator to apply the operation on
	*    @return the opposite rotation to the given Rotator
	**/
	public static Rotator opposite(Rotator r1){
		return r1.opposite();
	}

	/**
	*    @return the Rotator that is rotated in opposite direction
	*    For example, given a rotator (90, 0, 0) clockwise
	*    calling antiClockwise will return (270, 0, 0)
	*    90 clockwise -> 90 anti clockwise = 270 clockwise
	**/
	public Rotator antiClockwise(){
		return new Rotator(360-roll, 360-pitch, 360-yaw);
	}

	/**
	*    @param r the rotator to apply the operation on
	*    @return the anti rotator for the given Rotator
	**/
	public static Rotator antiClockwise(Rotator r){
		return r.antiClockwise();
	}

    /**
	*     @param r the rotator to apply the operation on
	*     @return a rotator that switches the roll and yaw components around
	**/
	public static Rotator reverseOrder(Rotator r){
		return new Rotator(r.yaw, r.pitch, r.roll);
	}

    /**
	*    @return true IFF the components of the two rotators match
	**/
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

    /**
	*    @return a formatted string with constant size
	**/
	public String toStringNeat(){
		String strR = String.format("%1$5s", "" + Functions.round(roll, 1)).substring(0, 4);
		String strP = "" + String.format("%1$5s", Functions.round(pitch, 1)).substring(0, 4);
		String strY = "" + String.format("%1$5s", Functions.round(yaw, 1)).substring(0, 4);
		return "(" + strR + "," + strP + "," + strY + ")";
	}
}
