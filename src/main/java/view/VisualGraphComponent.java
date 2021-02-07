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

	//where the VGC is meant to be rendered on-screen. A 2D vector
	protected transient Vector renderLocation;

	//The scale of the VGC due to distance from the camera
	protected transient double renderScale;

	protected transient EventHandler<MouseEvent> clickEvent;

    //true if the component has been clicked by the user
	protected transient boolean selected;

	private static ArrayList<VisualGraphComponent> components = new ArrayList<>();

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
                handleClick();
            }
        };
	}

    protected void handleClick(){
		MainWindow.get().addClickedComponent(this);
	}

	public void setSelected(boolean selectedIn){
		selected = selectedIn;
	}

	public static synchronized void updateComponents(Camera camera){
		ArrayList<VisualGraphComponent> comps = new ArrayList<>();
		comps.addAll(VisualGraphNode.getNodes());
		comps.addAll(VisualGraphEdge.getEdges());

		HashMap<VisualGraphComponent, Double> distances = new HashMap<>();
		//To improve performance, only add components that are within the max view distance
        for(VisualGraphComponent comp : comps){
			if(camera.isInViewRange(comp.getLocation()) || comp instanceof VisualGraphEdge){
                distances.put(comp, Vector.distance(comp.getLocation(), camera.getLocation()));
			}
		}
		ArrayList<Entry<VisualGraphComponent, Double>> list = new ArrayList<>(distances.entrySet());
		list.sort(Entry.comparingByValue());
		Collections.reverse(list);
		comps = new ArrayList<>();
		for(Entry<VisualGraphComponent, Double> e : list){
			comps.add(e.getKey());
		}
		components = comps;
	}

	public static ArrayList<VisualGraphComponent> getComponents(){
		return new ArrayList<VisualGraphComponent>(components);
	}

}
