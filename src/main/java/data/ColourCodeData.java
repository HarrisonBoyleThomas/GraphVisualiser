package data;

import model.GraphComponentState;
import java.util.HashMap;

import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.Paths;

/**
*    The CCD class contains the colour mappings for VGC states
*    @author Harrison Boyle-Thomas
**/
public class ColourCodeData implements Serializable{
    private HashMap<GraphComponentState, SerialisableColour> colours = new HashMap<>();

    public static final String CONFIG_FOLDER = "config/";

    public static final String CONFIG_NAME = "colourCodes";

    public ColourCodeData(){
        load();
    }
    public void setColourForState(GraphComponentState state, Color colour){
        if(colour == null || state == null){
            return;
        }
        colours.put(state, new SerialisableColour(colour));
        save();
    }
    public Color getColourForState(GraphComponentState state){
        if(!colours.keySet().contains(state)){
            return null;
        }
        return colours.get(state).getColour();
    }

    protected HashMap<GraphComponentState, SerialisableColour> getColours(){
        return new HashMap<GraphComponentState, SerialisableColour>(colours);
    }

    public void reset(){
        colours.clear();
        setColourForState(GraphComponentState.UNVISITED, Color.BLACK);
        setColourForState(GraphComponentState.VISITED, Color.rgb(0,255,0,1.0));
        setColourForState(GraphComponentState.IN_OPEN_LIST, Color.rgb(255,0,0,1.0));
        setColourForState(GraphComponentState.CURRENT, Color.rgb(0,255,255,1.0));
        setColourForState(GraphComponentState.IN_TREE, Color.CORNFLOWERBLUE);
        setColourForState(GraphComponentState.INVALID, Color.rgb(255,0,0,1.0));
        setColourForState(GraphComponentState.SELECTED, Color.ORANGE);
    }

    public boolean save(){
        boolean successful = false;
        try{
			File configFolder = new File(CONFIG_FOLDER);
            File config = new File(configFolder, CONFIG_NAME);
		    FileOutputStream fileOutputStream = new FileOutputStream(config);
    		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
    		objectOutputStream.close();
    		fileOutputStream.close();
            successful = true;
		}
		catch(Exception e){
            //e.printStackTrace();
            System.out.println("Unable to save custom colours- ensure config/colourCodes exists");
		}
        return successful;
    }

    public boolean load(){
        boolean successful = false;
        ColourCodeData dataIn = null;
        try{
            File configFolder = new File(CONFIG_FOLDER);
            File config = new File(configFolder, CONFIG_NAME);
    		FileInputStream fileStream = new FileInputStream(config);
    		ObjectInputStream objectStream = new ObjectInputStream(fileStream);
    		dataIn = (ColourCodeData) objectStream.readObject();
    		objectStream.close();
	    	fileStream.close();
			copy(dataIn);
            successful = true;
		}
		catch(Exception e){
            reset();
            save();
		}
        return successful;
    }

    private void copy(ColourCodeData other){
        colours = other.getColours();
    }


}
