package maths;

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
*    @Author Harrison Boyle-Thomas
*    @Date 03/11/2020
**/
public class Rotator{
	
	final int roll;
	final int pitch;
	final int yaw;
	
	/**
	*    Create a rotator with the given roll pitch and yaw values
	**/
	public Rotator(int rollIn,int pitchIn, int yawIn){
		roll = rollIn % 360;
		pitch = pitchIn % 360;
		yaw = yawIn % 360;
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

	public Rotator addRoll(int amount){
		return new Rotator((roll + amount)%360, pitch, yaw);
	}
	

	public static Rotator addRoll(Rotator rot, int amount){
		return rot.addRoll(amount);
	}
	
	public Rotator addPitch(int amount){
		return new Rotator(roll, (pitch + amount)%360, yaw);
	}
	
	public static Rotator addPitch(Rotator rot, int amount){
		return rot.addPitch(amount);
	}
	
	public Rotator addYaw(int amount){
		return new Rotator(roll, pitch, (yaw + amount) %360);
	}
	
	public static Rotator addYaw(Rotator rot, int amount){
		return rot.addYaw(amount);
	}
	
	
	public Rotator subtractRoll(int amount){
		return new Rotator((roll - amount)%360, pitch, yaw);
	}
	
	public static Rotator subtractRoll(Rotator rot, int amount){
		return rot.subtractRoll(amount);
	}
	
	public Rotator subtractPitch(int amount){
		return new Rotator(roll, (pitch - amount)%360, yaw);
	}
	
	public static Rotator subtractPitch(Rotator rot, int amount){
		return rot.subtractPitch(amount);
	}
	
	public Rotator subtractYaw(int amount){
		return new Rotator(roll, pitch, (yaw - amount) %360);
	}
	
	public static Rotator subtractYaw(Rotator rot, int amount){
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
}
	
	
	
	