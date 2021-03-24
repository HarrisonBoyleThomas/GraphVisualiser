package model.dataStructures;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.lang.IndexOutOfBoundsException;

public class BucketQueue<T> extends AbstractCollection{
    Bucket<T>[] data;
    HashMap<T, Integer> indexMap = new HashMap<>();
    int numElems = 0;
    int minIndex = -1;
    public BucketQueue(){
        data = new Bucket[1];
    }

    public BucketQueue(int initialSize){
        if(initialSize < 1){
            initialSize = 1;
        }
        data = new Bucket[initialSize];
    }

    public void add(T toAdd, int index){
        if(index < 0){
            throw new IndexOutOfBoundsException("Invalid index. Must be more than 0");
        }
        Bucket<T>[] newData = null;
        if(index > data.length){
            newData = new Bucket[index+1];
        }
        else{
            newData = new Bucket[data.length*2];
        }
        for(int i = 0; i < data.length; i++){
            newData[i] = data[i];
        }
        data = newData;
        if(data[index] == null){
            data[index] = new Bucket<T>();
        }
        data[index].add(toAdd);
        indexMap.put(toAdd, index);
        numElems++;
        if(index < minIndex){
            minIndex = index;
        }
    }

    public void updateIndex(T item, int newIndex){
        if(!indexMap.keySet().contains(item)){
            return;
        }
        int index = indexMap.remove(item);
        data[index].remove(item);
        if(newIndex < minIndex && index == minIndex){
            minIndex = newIndex;
        }
        numElems--;
        add(item, newIndex);

    }

    public T remove(int index){
        if(index < 0 || index >= data.length){
            throw new IndexOutOfBoundsException("Invalid index(" + index + "). Must be more than 0 and below " + data.length);
        }
        if(data[index] == null){
            return null;
        }
        numElems--;
        if(data[index].size() <= 1 && index == minIndex){
            minIndex = -1;
        }
        return data[index].removeMin();
    }

    public T get(int index){
        if(index < 0 || index >= data.length){
            return null;
        }
        if(data[index] == null){
            return null;
        }
        return data[index].getNext();
    }

    public int getMinIndex(){
        if(minIndex != -1){
            return minIndex;
        }
        for(int i = 0; i < data.length; i++){
            if(data[i] != null){
                if(data[i].size() > 0){
                    minIndex = i;
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int size(){
        return numElems;
    }

    @Override
    public Iterator<T> iterator(){
        ArrayList<T> elements = new ArrayList<>();
        for(int i = 0; i < data.length; i++){
            if(data[i] != null){
                elements.addAll(data[i].getElements());
            }
        }
        return elements.iterator();
    }


    private class Bucket<T>{
        private ArrayList<T> elements = null;

        public Bucket(){
            elements = new ArrayList<>();
        }

        public void add(T toAdd){
            elements.add(toAdd);
        }
        public T removeMin(){
            if(elements.size() == 0){
                return null;
            }
            return (T) elements.remove(0);
        }

        public ArrayList<T> getElements(){
            return new ArrayList<T>(elements);
        }

        public boolean remove(T toRemove){
            return elements.remove(toRemove);
        }

        public T getNext(){
            if(size() == 0){
                return null;
            }
            return elements.get(0);
        }

        public int size(){
            return elements.size();
        }
    }
}
