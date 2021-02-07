package menu;

import data.Data;

import maths.Vector;
import viewport.Camera;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;

import java.io.*;
import java.nio.file.Paths;

/**
*    The AlgorithmControlPanel allows users to handle the execution of running AlgorithmSetupPanel
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
**/
public class AlgorithmControlPanel extends AlgorithmDetailsPanel{

	public AlgorithmControlPanel(){
		update();
	}

	public void update(){
		getChildren().clear();
		Tooltip tooltip = new Tooltip("Use this panel to control running algorithms");
		Label title = new Label("ALGORITHM CONTROLS");
		Tooltip.install(title, tooltip);
		getChildren().add(title);

		createStepButton();
		createExecuteButton();
		createSpeedDial();
		createPauseButton();
		createTerminateButton();
	}

    /**
	*    Create and setup events for the STEP button in the control panel
	*    The step button asks the mainWindow to execute one step of algorithms
	**/
	private void createStepButton(){
		Tooltip tooltip = new Tooltip("Advance all algorithms one step");
		Button step = new Button("Step");
		Tooltip.install(step, tooltip);

		step.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().stepAlgorithms();
            }
        });

		getChildren().add(step);
	}

    /**
	*    Create and setup the EXECUTE button in the control panel
	*    The execute button asks the mainWindow to execute all steps of the algorithms
	**/
	private void createExecuteButton(){
		if(MainWindow.get().areAlgorithmsExecuting()){
			return;
		}
		Tooltip tooltip = new Tooltip("Run all algorithms on the current graph, to quickly obtain their output without stepping");
		Button button = new Button("Execute");
		Tooltip.install(button, tooltip);

        button.setOnAction(new EventHandler<ActionEvent>() {
	    	@Override public void handle(ActionEvent e){
	    		MainWindow.get().executeAlgorithms();
	    	}
	    });

		getChildren().add(button);
	}
    private void createPauseButton(){
		if(!MainWindow.get().areAlgorithmsExecuting()){
			return;
		}
		Tooltip tooltip = new Tooltip("Toggle pause the execution of algorithms");
		Button button = new Button("Pause");
		if(MainWindow.get().areAlgorithmsPaused()){
			button = new Button("Unpause");
		}
		Tooltip.install(button, tooltip);

		button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().pauseAlgorithms();
            }
        });

		getChildren().add(button);
	}
    /**
	*    Create and setup the TERMINATE button in the control panel
	*    The terminate button asks the mainWIndow to stop the execution of the algorithms
	**/
	private void createTerminateButton(){
		Tooltip tooltip = new Tooltip("Cancel the execution of all algorithms so you can make edits to the graph");
		Button button = new Button("Terminate");
		Tooltip.install(button, tooltip);

		button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().terminateAlgorithms();
            }
        });

		getChildren().add(button);
	}
    /**
	*    Create a dial that controls the speed of algorithms
	*    And saves it's value to a config file for later use
	**/
	private void createSpeedDial(){
		Tooltip tooltip = new Tooltip("Set the execution speed of algorithms(milliseconds)");
		Slider slider = new Slider(0, 1000, Data.getExecutionSleepDelay());
		Tooltip.install(slider, tooltip);

		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> value, Number oldValue, Number newValue) {
				Data.updateSleepDelay((int) ((double) newValue));
                MainWindow.get().updateAlgorithmSpeed();
            }
        });
		slider.setMinorTickCount(5);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);

		getChildren().add(slider);
	}
}
