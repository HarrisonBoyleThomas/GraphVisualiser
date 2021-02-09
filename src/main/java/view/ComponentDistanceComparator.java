package viewport;

import maths.Vector;

import java.util.*;

import javafx.scene.Node;

/**
*    The CDC is used to sort the child list of a viewport such that nodes that are closer
*    to the screen are at the end of the list. This comparator would produce a reversed sort
*    order. Only intended for use in Viewport, to boost efficiency
*    @author Harrison Boyle-Thomas
*    @date 09/02/21
**/
public class ComponentDistanceComparator implements Comparator<Node>{
    private Camera camera;
    private HashMap<Node, VisualGraphComponent> iconMap;

    public ComponentDistanceComparator(Camera cameraIn, HashMap<Node, VisualGraphComponent> iconMapIn){
        camera = cameraIn;
        iconMap = iconMapIn;
    }

    /**
    *    If a Node does not map to a VGC, then treat it's corresponding distance as
    *    the minimum possible value
    *    @return 1 if distanceA < distanceB
    *    @return -1 if distanceB < distanceA
    *    @return 0 if equal
    }
    **/
    public int compare(Node a, Node b){
        VisualGraphComponent compA = iconMap.get(a);
        VisualGraphComponent compB = iconMap.get(b);
        if(compA == null && compB == null){
            return 0;
        }
        if(compA == null){
            return 1;
        }
        if(compB == null){
            return -1;
        }

        double distanceA;
        double distanceB;
        if(compA instanceof VisualGraphNode && compB instanceof VisualGraphNode){
            //both are nodes
            distanceA = Vector.distance(compA.getLocation(), camera.getLocation());
            distanceB = Vector.distance(compB.getLocation(), camera.getLocation());
        }
        else if(compA instanceof VisualGraphNode){
            //a is a node and b is an edge
            distanceA = Vector.distance(compA.getLocation(), camera.getLocation());
            Vector bMidpoint =  ((VisualGraphEdge) compB).getWorldMidpoint();
            if(bMidpoint == null){
                return 1;
            }
            distanceB = Vector.distance(bMidpoint, camera.getLocation());

        }
        else if(compB instanceof VisualGraphNode){
            //b is a node and a is an edge
            distanceB = Vector.distance(compB.getLocation(), camera.getLocation());
            Vector aMidpoint = ((VisualGraphEdge) compA).getWorldMidpoint();
            if(aMidpoint == null){
                return -1;
            }
            distanceA = Vector.distance(aMidpoint, camera.getLocation());
        }
        else{
            //both are edges
            Vector aMidpoint = ((VisualGraphEdge) compA).getWorldMidpoint();
            Vector bMidpoint =  ((VisualGraphEdge) compB).getWorldMidpoint();
            if(aMidpoint == null && bMidpoint == null){
                return 0;
            }
            if(aMidpoint == null){
                return 1;
            }
            if(bMidpoint == null){
                return -1;
            }
            distanceA = Vector.distance(aMidpoint, camera.getLocation());
            distanceB = Vector.distance(bMidpoint, camera.getLocation());
        }
        if(distanceA == distanceB){
            return 0;
        }
        if(distanceA < distanceB){
            return 1;
        }
        return -1;
    }
}
