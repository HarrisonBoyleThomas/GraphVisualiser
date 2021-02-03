package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import java.io.*;

/**
*    An Actor is an object that exists in 3D space within the Viewport
*    @author Harrison Boyle-Thomas
*    @date 03/02/21
**/
public abstract class Actor implements Serializable{
    protected Vector location;

    /**
    *    @param locationIn the location the Actor will be created at
    **/
	public Actor(Vector locationIn){
		location = locationIn;
	}
    /**
    *    @param x x coordinate of the actor's location
    *    @param y y coordinate of the actor's location
    *    @param z z coordinate of the actor's location
    **/
	public Actor(double x, double y, double z){
		location = new Vector(x, y, z);
	}

	public Actor(){
		location = new Vector();
	}
    /**
    *    @return the actor's 3D location
    **/
	public Vector getLocation(){
		return location;
	}
    /**
    *    @Set the actor's location to the new location
    *    @param locationIn the new location
    *    @return the location that the actor stores after the operation
    **/
	public Vector setLocation(Vector locationIn){
		location = locationIn;
		return location;
	}
    /**
    *    Increment the actor's location by the given amount
    *    @param shift the amount to add to the current location
    *    @return the new location
    **/
	public Vector addLocation(Vector shift){
		location = location.add(shift);
		return location;
	}
    /**
    *    Decrement the actor's location by the given amount
    *    @param shift the amount to subtract from the current location
    *    @return the new location
    **/
	public Vector subtractLocation(Vector shift){
		location = location.subtract(shift);
		return location;
	}
    /**
    *    @param other the actor to find the relative location from
    *    @return a vector that represents the translation needed to move from other to the actor's location
    **/
	public Vector getRelativeLocation(Actor other){
		return other.getLocation().subtract(location);
	}
    /**
    *    @param the vector to find the relative location from
    *    @return a vector that represents the translation needed to move from loc to the actor's location
    **/
	public Vector getRelativeLocation(Vector loc){
		return loc.subtract(location);
	}
    /**
    *    @param other the actor to find rotation to
    *    @return the clockwise rotation from the current location to the other's location
    **/
	public Rotator getClockwiseRotationTo(Actor other){
		return Functions.getRotationBetweenVectors(location, other.getLocation());
	}
    /**
    *    @param loc the vector to find the rotation to
    *    @return the clockwise rotation from the current location to loc
    **/
	public Rotator getClockwiseRotationTo(Vector loc){
		return Functions.getRotationBetweenVectors(location, loc);
	}
}
