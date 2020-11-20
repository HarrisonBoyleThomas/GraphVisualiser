package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;

import javafx.scene.Group;

/**
*    A VGC stores the common features of a graph component that can be drawn on a JavaFX canvas
*    Due to the complexity of calculating renderLocation, the render location of a VGC is stored
*    after calculation
*    
*    Author: Harrison Boyle-Thomas
*    Date: 20/11/2020
**/
public abstract class VisualGraphComponent extends Actor{
	//The icon for the VGC that will be rendered at the renderLocation
	protected Group icon;
	
	//where the VGC is meant to be rendered on-screen. A 2D vector
	protected Vector renderLocation;
	
	public Vector getRenderLocation(){
		return renderLocation;
	}
	
	/**
	*    @Param screen bounds
	*    @Return true if the renderLocation of the component
	*    is within the given screen bounds
	**/
	public boolean isOnScreen(int width, int height){
		if(renderLocation == null){
			return false;
		}
		return (renderLocation.x >= 0 && renderLocation.x <= width && renderLocation.y >= 0 && renderLocation.y <= height);
	}
	
	/**
	*    Set the renderLocation to the new location
	**/
	public void updateRenderLocation(Vector newLocation){
		renderLocation = newLocation;
	}
	
	/**
	*    Update the visual appearance of the icon
	**/
	public abstract void updateIcon();
}