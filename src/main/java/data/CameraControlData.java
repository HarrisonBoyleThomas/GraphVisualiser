package data;

import javafx.scene.input.KeyCode;

import java.io.*;
import java.nio.file.Paths;

/**
*    The CCD class contains the control mappings for
*    the camera, and handles it's own loading and saving
*    @author Harrison Boyle-Thomas
**/
public class CameraControlData implements Serializable{

    public static final String CONFIG_FOLDER = "/config/";

    public static final String CONFIG_NAME = "cameraControls";

    private static final long serialVersionUID = 21L;

    public KeyCode MOVE_UP_KEY = KeyCode.Q;

    public KeyCode MOVE_DOWN_KEY = KeyCode.Z;

    public KeyCode MOVE_LEFT_KEY = KeyCode.A;

    public KeyCode MOVE_RIGHT_KEY = KeyCode.D;

    public KeyCode MOVE_FORWARD_KEY = KeyCode.W;

    public KeyCode MOVE_BACKWARD_KEY = KeyCode.S;

    public KeyCode PITCH_UP_KEY = KeyCode.UP;

    public KeyCode PITCH_DOWN_KEY = KeyCode.DOWN;

    public KeyCode YAW_LEFT_KEY = KeyCode.LEFT;

    public KeyCode YAW_RIGHT_KEY = KeyCode.RIGHT;

    public KeyCode ROLL_LEFT_KEY = KeyCode.COMMA;

    public KeyCode ROLL_RIGHT_KEY = KeyCode.PERIOD;

    public CameraControlData(){
        load();
    }

    private void copy(CameraControlData toCopy){
        MOVE_UP_KEY = toCopy.MOVE_UP_KEY;
        MOVE_DOWN_KEY = toCopy.MOVE_DOWN_KEY;
        MOVE_LEFT_KEY = toCopy.MOVE_LEFT_KEY;
        MOVE_RIGHT_KEY = toCopy.MOVE_RIGHT_KEY;
        MOVE_FORWARD_KEY = toCopy.MOVE_FORWARD_KEY;
        MOVE_BACKWARD_KEY = toCopy.MOVE_BACKWARD_KEY;
        PITCH_UP_KEY = toCopy.PITCH_UP_KEY;
        PITCH_DOWN_KEY = toCopy.PITCH_DOWN_KEY;
        YAW_LEFT_KEY = toCopy.YAW_LEFT_KEY;
        YAW_RIGHT_KEY = toCopy.YAW_RIGHT_KEY;
        ROLL_LEFT_KEY = toCopy.ROLL_LEFT_KEY;
        ROLL_RIGHT_KEY = toCopy.ROLL_RIGHT_KEY;
    }

    public boolean save(){
        boolean successful = false;
        try{
			File configFolder = new File(getClass().getResource(CONFIG_FOLDER).toURI());
            File config = new File(configFolder, CONFIG_NAME);
		    FileOutputStream fileOutputStream = new FileOutputStream(config);
    		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
    		objectOutputStream.close();
    		fileOutputStream.close();
            successful = true;
		}
		catch(Exception e){
            System.out.println("Unable to save custom controls- ensure config/cameraControls exists");
		}
        return successful;
    }

    public boolean load(){
        boolean successful = false;
        CameraControlData dataIn = null;
        try{
            File configFolder = new File(getClass().getResource(CONFIG_FOLDER).toURI());
            File config = new File(configFolder, CONFIG_NAME);
    		FileInputStream fileStream = new FileInputStream(config);
    		ObjectInputStream objectStream = new ObjectInputStream(fileStream);
    		dataIn = (CameraControlData) objectStream.readObject();
    		objectStream.close();
	    	fileStream.close();
			copy(dataIn);
            successful = true;
		}
		catch(Exception e){
            save();
		}
        return successful;
    }

    public void reset(){
        MOVE_UP_KEY = KeyCode.Q;

        MOVE_DOWN_KEY = KeyCode.Z;

        MOVE_LEFT_KEY = KeyCode.A;

        MOVE_RIGHT_KEY = KeyCode.D;

        MOVE_FORWARD_KEY = KeyCode.W;

        MOVE_BACKWARD_KEY = KeyCode.S;

        PITCH_UP_KEY = KeyCode.UP;

        PITCH_DOWN_KEY = KeyCode.DOWN;

        YAW_LEFT_KEY = KeyCode.LEFT;

        YAW_RIGHT_KEY = KeyCode.RIGHT;

        ROLL_LEFT_KEY = KeyCode.COMMA;

        ROLL_RIGHT_KEY = KeyCode.PERIOD;

        save();
    }
}
