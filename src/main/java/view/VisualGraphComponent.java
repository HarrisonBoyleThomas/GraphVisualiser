package viewport;

import maths.Vector;
import maths.Rotator;
import maths.Functions;
import model.algorithm.GraphAlgorithm;

import menu.MainWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collections;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.geometry.Bounds;


import javafx.scene.Group;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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
	protected transient Group icon;
	static final long serialVersionUID = 21L;

	//where the VGC is meant to be rendered on-screen. A 2D vector
	protected transient Vector renderLocation;

	//The scale of the VGC due to distance from the camera
	protected transient double renderScale;

	protected transient EventHandler<MouseEvent> clickEvent;

    //true if the component has been clicked by the user
	protected transient boolean selected;

    //Due to the way the viewport renders, css styles are not applied to components
	//before rendering, which makes it impossible to detect when the viewport should
	//render new nodes. This variable stores the intended colour of the component
	//before it is added to the screen. I.e. the edge/node highlight colour
	protected transient Color setColour;

	public Vector getRenderLocation(){
		return renderLocation;
	}

	public Group getIcon(){
		return icon;
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

	public void updateRenderScale(double scaleIn){
		renderScale = Functions.clamp(scaleIn, 0.3, 3.0);
	}

	public double getRenderScale(){
		return renderScale;
	}

	/**
	*    Update the visual appearance of the icon
	**/
	public abstract void updateIcon(GraphAlgorithm algorithm);

	protected void addEvents(){
		clickEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                e.consume();
                handleClick(null, e.getClickCount() == 2);
            }
        };
	}


    protected void handleClick(VisualGraphComponent toAdd, boolean doubleClick){
		if(toAdd != null){
			if(doubleClick){
				MainWindow.get().addClickedComponentDoubleClick(toAdd);
			}
			else{
		        MainWindow.get().addClickedComponent(toAdd);
			}
		}
	}

	public void setSelected(boolean selectedIn){
		selected = selectedIn;
	}

    /**
	*    @return an array list of all VGNs and VGEs
	**/
	public static ArrayList<VisualGraphComponent> getComponents(){
		ArrayList<VisualGraphComponent> components = new ArrayList<VisualGraphComponent>();
		components.addAll(VisualGraphNode.getNodes());
		components.addAll(VisualGraphEdge.getEdges());
		return components;
	}

    /**
	*    Find if the given group is the identical to the VGC's icon
	*    @param other the group to compare with
	*    @return true if the two groups share common key features
	**/
	public abstract boolean iconsEqual(Group other);

	public Color getSetColour(){
		return setColour;
	}

}
