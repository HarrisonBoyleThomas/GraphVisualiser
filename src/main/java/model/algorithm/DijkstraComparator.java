package model.algorithm;
import java.util.*;

import model.GraphNode;

/**
*    DijkstraComparator compares two graph node weights
*    Weights less than 0 are assumed to be Infinity,
*    so a the compare() function takes this into account
*    @Author Harrison Boyle-Thomas
*    @Date 01/02/21
**/
public class DijkstraComparator implements Comparator<GraphNode>{

    HashMap<GraphNode, Integer> weightMap;

    /**
    *    @Param mapIn = the distance map generated by DSP
    **/
    public DijkstraComparator(HashMap<GraphNode, Integer> mapIn){
        weightMap = mapIn;
    }

    /**
    *    @a operand one
    *    @b operand two
    *    @return -1 if the weight(a) < weight(b) from the
    *    supplied distance map
    **/
    public int compare(GraphNode a, GraphNode b){
        Integer weightOfA = weightMap.get(a);
        Integer weightOfB = weightMap.get(b);
        if(weightOfA == weightOfB){
            return 0;
        }
        if(weightOfA == -1){
            return 1;
        }
        if(weightOfB == -1){
            return -1;
        }
        if(weightOfA < weightOfB){
            return -1;
        }
        return 1;
    }
}