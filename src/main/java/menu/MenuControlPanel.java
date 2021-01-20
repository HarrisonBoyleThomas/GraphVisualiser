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


public class MenuControlPanel extends DetailsPanel{
	
	public MenuControlPanel(){
		update();
	}
	
	public void update(){
		getChildren().clear();
		Tooltip tooltip = new Tooltip("Use this panel to control running algorithms");
		Label title = new Label("ALGORITHM CONTROLS");
		Tooltip.install(title, tooltip);
		getChildren().add(title);
		
		createStepButton();
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
	}
}