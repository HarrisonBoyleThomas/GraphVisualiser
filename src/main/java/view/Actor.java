package viewport;

import maths.Vector;
import maths.Rotator;

public abstract class Actor{
    protected Vector location;
	
	public Actor(Vector locationIn){
		location = locationIn;
	}
	
	public Actor(double x, double y, double z){
		location = new Vector(x, y, z);
	}
	
	public Actor(){
		location = new Vector();
	}
	
	public Vector getLocation(){
		return location;
	}
	
	protected Vector setLocation(Vector locationIn){
		location = locationIn;
		return location;
	}
	
	public Vector addLocation(Vector shift){
		location = location.add(shift);
		return location;
	}
	
	public Vector subtractLocation(Vector shift){
		location = location.subtract(shift);
		return location;
	}
	
	public Vector getRelativeLocation(Actor other){
		return other.getLocation().subtract(location);
	}
	
	public Vector getRelativeLocation(Vector loc){
		return loc.subtract(location);
	}
	
	public Rotator getClockwiseRotationTo(Actor other){
		return Functions.getRotationBetweenVectors(location, other.getLocation());
	}
	
	public Rotator getClockwiseRotationTo(Vector loc){
		return Functions.getRotationBetweenVectors(location, loc);
	}
}