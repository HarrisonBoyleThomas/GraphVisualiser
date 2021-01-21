package menu;

import maths.Vector;
import viewport.Camera;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;


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
		createTerminateButton();
	}

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

	private void createExecuteButton(){
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
}
