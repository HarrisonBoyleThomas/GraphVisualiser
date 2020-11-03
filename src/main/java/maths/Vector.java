package maths;

import java.lang.ArithmeticException;
import java.lang.NullPointerException;
import java.lang.Math;

/**
*    A vector represents a point in 3D space
*    A 2D vector is conventionally represented by z = 0
*    
*    @Author Harrison Boyle-Thomas
*    @Date 03/11/2020
**/
public class Vector{
	public final int x;
	public final int y;
	public final int z;
	
	/**
	*    create a 3D vector
	**/
	public Vector(int xIn, int yIn, int zIn){
		x = xIn;
		y = yIn;
		z = zIn;
	}
	
	/**
	*    create a 2D vector
	**/
	public Vector(int xIn,int yIn){
		x = xIn;
		y = yIn;
		z = 0;
	}
	
	/**
	*     Create a default vector at the world origin (0,0,0)
	**/
	public Vector(){
		x = 0;
		y = 0;
		z = 0;
	}
	
	/**
	*    Add the given vector to the current vector
	*    @Return a new Vector of the result
	**/
	public Vector add(Vector other){
		return new Vector(x + other.x, y + other.y, z + other.z);
	}
	
	/**
	*    Add the two supplied vectors together
	*    @Return a new Vector of the result
	**/
	public static Vector add(Vector v1, Vector v2){
		if(v1 == null){
			throw new NullPointerException("Vector 1 is null");
		}
		if(v2 == null){
			throw new NullPointerException("Vector 2 is null");
		}
		return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	/**
	*    Subtract the given vector from the current vector
	*    @Return a new Vector of the result
	**/
	public Vector subtract(Vector other){
		return new Vector(x - other.x, y - other.y, z - other.z);
	}
	
	/**
	*    Subtract vector v2 from vector v1
	*    @Return a new Vector of the result
	**/
	public static Vector subtract(Vector v1, Vector v2){
		if(v1 == null){
			throw new NullPointerException("Vector 1 is null");
		}
		if(v2 == null){
			throw new NullPointerException("Vector 2 is null");
		}
		return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	/**
	*    @Return the dot product between the current vector and the supplied vector
	**/
	public int dot(Vector other){
		return (x * other.x) + (y * other.y) + (z * other.z);
	}

    /**
	*    @Return the dot product of the two given vectors
	**/
    public static int dot(Vector v1, Vector v2){
		if(v1 == null){
			throw new NullPointerException("Vector 1 is null");
		}
		if(v2 == null){
			throw new NullPointerException("Vector 2 is null");
		}
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}
	
	/**
	*    Scales the current vector by the given multiplier
	*    @Return a Vector of the result
	**/
	public Vector multiply(int multiplier){
		return new Vector(x * multiplier, y * multiplier, z * multiplier);
	}
	
	/**
	*    Scales the given vector by the given multiplier
	*    @Return a Vector of the result
	**/
	public static Vector multiply(Vector v, int multiplier){
		return v.multiply(multiplier);
	}
	
	/**
	*    Divide the current vector by the given dividend
	*    @Return a Vector of the result
	**/
	public Vector divide(int dividend) throws ArithmeticException{
		if(dividend == 0){
			throw new ArithmeticException("Attempt to divide vector by zero");
		}
		return new Vector(x / dividend, y / dividend, z / dividend);
	}
	
	/**
	*    Divide the given vector by the given dividend
	*    @Return a Vector of the result
	**/
	public static Vector divide(Vector v, int dividend){
		return v.divide(dividend);
	}
	
	/**
	*    @Return the euclidian distance between two given vectors
	**/
	public static float distance(Vector v1, Vector v2){
		Vector diff = subtract(v1, v2);
		return length(diff);
	}
	
	/**
	*    @Return the length of the given vector
	*    If the given vector is the difference between two vectors, this will return the
	*    distance between them. If the given vector is a vector in world space, 
	*    the return length will be equivalent to it's distance from the origin
	**/
	public static float length(Vector v){
		return (float) Math.sqrt(Math.pow(v.x,2) + Math.pow(v.y,2) + Math.pow(v.z,2));
	}
	
	public float length(){
		return (float) Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
	}
	
	
	public Vector normalise(){
		return new Vector((int) (x / length()), (int) (y / length()), (int) (z / length()));
	}
	
	public static Vector normalise(Vector v){
		return v.normalise();
	}
	
	@Override
	public boolean equals(Object otherObject){
		if(otherObject instanceof Vector){
		    Vector vector = (Vector) otherObject;
			return (x == vector.x && y == vector.y && z == vector.z);
		}
		return false;
	}
	
	@Override
    public String toString() { 
        return "(" + x + "," + y + "," + z + ")"; 
    } 
	
	
		
}