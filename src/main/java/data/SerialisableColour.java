package data;

import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.Paths;

/**
*    The SC class takes the attributes of a colour class and stores them so that
*    the colour can be saved. Allows simple conversion to help ease of access.
*    @Auther Harrison Boyle-Thomas
*    @date 19/03/21
**/
public class SerialisableColour implements Serializable{
    private double red;
    private double green;
    private double blue;
    private double alpha;
    public SerialisableColour(Color colourIn){
        red = colourIn.getRed();
        green = colourIn.getGreen();
        blue = colourIn.getBlue();
        alpha = colourIn.getOpacity();
    }
    public Color getColour(){
        return new Color(red, green, blue, alpha);
    }
}
