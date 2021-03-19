package data;

import maths.Functions;

import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.Paths;

/**
*    This class stores paths and other useful data that may be needed by
*    multiple different parts of GraphVisualiser, so that references only need to
*    be updated here
*    @author Harrison Boyle-Thomas
*    @date 05/02/21
**/
public class Data{

    private static int EXECUTION_SLEEP_DELAY = 500;

    public static String THEME_CONFIG_PATH = "/config/theme";

    private static long DELTA_TIME;

    public static final CameraControlData CAMERA_CONTROLS = new CameraControlData();

    public static final ColourCodeData COLOUR_CODE_DATA = new ColourCodeData();

    public static void updateSleepDelay(int newValue){
        EXECUTION_SLEEP_DELAY = (int) Functions.clamp(newValue, 0, 1000);
    }

    public static int getExecutionSleepDelay(){
        return EXECUTION_SLEEP_DELAY;
    }

    public static void setDeltaTime(long timeIn){
        DELTA_TIME = timeIn;
    }

    public static long getDeltaTime(){
        return DELTA_TIME;
    }

    public static int getFrameRate(){
        if(DELTA_TIME == 0){
            return 0;
        }
        return (int) (1000000000.0/DELTA_TIME) ;
    }

    public static String formatColourToRGBA(Color colour){
        if(colour == null){
            return null;
        }
        String output = "rgba(";
        output += ((int) (colour.getRed() * 255));
        output += ", ";
        output += ((int) (colour.getGreen() * 255));
        output += ", ";
        output += ((int) (colour.getBlue() * 255));
        output += ", ";
        output += colour.getOpacity();
        output += ")";
        return output;
    }
}
