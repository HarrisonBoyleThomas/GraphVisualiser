package maths;

import java.lang.ArithmeticException;
import java.lang.NullPointerException;
import java.lang.Math;

import java.io.*;

/**
*    A vector represents a point in 3D space
*    A 2D vector is conventionally represented by z = 0
*
*    @Author Harrison Boyle-Thomas
*    @Date 03/11/2020
**/
public class Vector implements Serializable{
	public final double x;
	public final double y;
	public final double z;

	/**
	*    create a 3D vector
	*    @param xIn the x component of the vector
	*    @param yIn the y component of the vector
	*    @param zIn the z component of the vector
	**/
	public Vector(double xIn, double yIn, double zIn){
		x = xIn;
		y = yIn;
		z = zIn;
	}

	/**
	*    create a 2D vector, with z = 0
	*    @param xIn the x component of the vector
	*    @param yIn the y component of the vector
	**/
	public Vector(double xIn,double yIn){
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
	*    @param the vector to add to the current vector
	*    @return a new Vector of the result
	**/
	public Vector add(Vector other){
		return new Vector(x + other.x, y + other.y, z + other.z);
	}

	/**
	*    Add the two supplied vectors together
	*    @param v1 operand one of the operation
	*    @param v2 operand two of the operation
	*    @return a new Vector of the result
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
	*    @param other the vector to subtract
	*    @return a new Vector of the result
	**/
	public Vector subtract(Vector other){
		return new Vector(x - other.x, y - other.y, z - other.z);
	}

	/**
	*    Subtract vector v2 from vector v1
	*    @param v1 operand one of the operation
	*    @param v2 operand two of the operation
	*    @return a new Vector of the result
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
	*    @param other apply the dot product using the other vector
	*    @return the dot product between the current vector and the supplied vector
	**/
	public double dot(Vector other){
		return (x * other.x) + (y * other.y) + (z * other.z);
	}

    /**
	*    @param v1 operand one
	*    @param v2 operand two
	*    @return the dot product of the two given vectors
	**/
    public static double dot(Vector v1, Vector v2){
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
	*    @param multiplier the amount to scale by
	*    @return a Vector of the result
	**/
	public Vector multiply(double multiplier){
		return new Vector(x * multiplier, y * multiplier, z * multiplier);
	}

	/**
	*    Scales the given vector by the given multiplier
	*    @param v the vector to scale
	*    @param multiplier the amount to scale by
	*    @return a Vector of the result
	**/
	public static Vector multiply(Vector v, double multiplier){
		return v.multiply(multiplier);
	}

	/**
	*    Divide the current vector by the given dividend
	*    @param divident the amount to divide by
	*    @Return a Vector of the result
	**/
	public Vector divide(int dividend) throws ArithmeticException{
		if(dividend == 0){
			throw new ArithmeticException("Attempt to divide vector by zero");
		}
		return new Vector(x / dividend, y / dividend, z / dividend);
	}

	/**
	*    Divide the given vector by the given dividend]
	*    @param v the vector to divide
	*    @param divident the amount to divide by
	*    @Return a Vector of the result
	**/
	public static Vector divide(Vector v, int dividend){
		return v.divide(dividend);
	}

	/**
	*    @param v1 the origin vector
	*    @param v2 the target vector
	*    @return the euclidian distance between two given vectors
	**/
	public static double distance(Vector v1, Vector v2){
		Vector diff = subtract(v1, v2);
		return length(diff);
	}

	/**
	*    @param v the vector to find the length of
	*    @rturn the length of the given vector
	*    If the given vector is the difference between two vectors, this will return the
	*    distance between them. If the given vector is a vector in world space,
	*    the return length will be equivalent to it's distance from the origin
	**/
	public static double length(Vector v){
		return Math.sqrt(Math.pow(v.x,2) + Math.pow(v.y,2) + Math.pow(v.z,2));
	}

    /**
	*    @return the length of the vector
	**/
	public double length(){
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
	}


	/**
	*    @return a normalised vector
	*    @return null for (0,0,0)
	**/
	public Vector normalise(){
		if(x == 0 && y == 0 && z == 0){
			return null;
		}
		return new Vector((x / length()), (y / length()), (z / length()));
	}

    /**
	*    @param v the vector to normalise
	*    @return the result of normalisation
	**/
	public static Vector normalise(Vector v){
		return v.normalise();
	}

    /**
	*    @return true if the x y z components match
	**/
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

	/**
	*    @return a formatted string of constant length
	**/
	public String toStringNeat(){
		String strX = "" + Functions.round(x, 2);
		String strY = "" + Functions.round(y, 2);
		String strZ = "" + Functions.round(z, 2);

		return "(" + String.format("%1$10s", strX) + "," + String.format("%1$10s", strY) + "," + String.format("%1$10s", strZ) +")";
	}



}
