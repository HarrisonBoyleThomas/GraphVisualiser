package data;

import maths.Functions;

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

    public static String THEME_CONFIG_PATH = "config/theme";

    public static void updateSleepDelay(int newValue){
        EXECUTION_SLEEP_DELAY = (int) Functions.clamp(newValue, 0, 1000);
    }

    public static int getExecutionSleepDelay(){
        return EXECUTION_SLEEP_DELAY;
    }

}
