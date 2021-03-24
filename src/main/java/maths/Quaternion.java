package maths;
import java.lang.Math;

public class Quaternion{
    public final double w;
    public final Vector vector;

    public Quaternion(double wIn, Vector vectorIn){
        w = wIn;
        vector = vectorIn;
    }

    public Quaternion add(Quaternion other){
        double newW = w + other.w;
        Vector newV = vector.add(other.vector);
        return new Quaternion(newW, newV);
    }

    public static Quaternion add(Quaternion a, Quaternion b){
        return a.add(b);
    }

    public Quaternion subtract(Quaternion other){
        double newW = w- other.w;
        Vector newV = vector.subtract(other.vector);
        return new Quaternion(newW, newV);
    }

    public static Quaternion subtract(Quaternion a, Quaternion b){
        return a.subtract(b);
    }

    public Quaternion multiply(Quaternion other){
        double realPart = w*other.w + vector.dot(other.vector);
        Vector vPart = other.vector.multiply(w).add(vector.multiply(other.w)).add(vector.cross(other.vector));
        return new Quaternion(realPart, vPart);
    }

    public static Quaternion multiply(Quaternion a, Quaternion b){
        return a.multiply(b);
    }

    public double magnitude(){
        return Math.sqrt(Math.pow(w,2) + Math.pow(vector.x,2) + Math.pow(vector.y,2) + Math.pow(vector.z,2));
    }

    public static double magnitude(Quaternion q){
        return q.magnitude();
    }

    public Quaternion inverse(){
        double mag = Math.pow(magnitude(), 2);
        double realPart = w / mag;
        Vector v = new Vector(0 - vector.x, 0 - vector.y, 0 - vector.z);
        v = v.divide(mag);
        return new Quaternion(realPart, v);
    }

    public static Quaternion inverse(Quaternion q){
        return q.inverse();
    }
}
