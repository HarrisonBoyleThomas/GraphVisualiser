package menu;

import maths.Vector;
import viewport.Camera;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.geometry.Pos;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;

/**
*    The AlgorithmSetupPanel allows the user to set the initial parameters of Algorithms
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
**/
public class AlgorithmSetupPanel extends AlgorithmDetailsPanel{

	public AlgorithmSetupPanel(){
		update();
	}

	public void update(){
		getChildren().clear();
		Tooltip tooltip = new Tooltip("Use this panel to control running algorithms");
		Label title = new Label("ALGORITHM CONTROLS");
		Tooltip.install(title, tooltip);
		getChildren().add(title);

		if(MainWindow.get() != null){
			createStartNodeSection();
    		createRunButton();
		}
		else{
			getChildren().add(new Label("Algorithm setup dirty"));
		}
	}

    /**
	*    Create and setup the start node selection box
	*    The selection box allows the user to select a start node for shortest path algorithms,
	*    and asks the mainWindow to set the start node to the selected node when selected
	**/
    private void createStartNodeSection(){

        Tooltip tooltip = new Tooltip("Select a start node for the algorithm");
        HBox section = new HBox();
		section.setAlignment(Pos.BASELINE_CENTER);
        Tooltip.install(section, tooltip);

        Label label = new Label("Start node:");
        section.getChildren().add(label);
        Tooltip.install(label, tooltip);


        int startNodeIndex = -1;
        ComboBox selection = new ComboBox();
        for(VisualGraphNode n : VisualGraphNode.getNodes()){
            selection.getItems().add(n.getNode().getName());
			if(n.getNode().equals(MainWindow.get().getStartNode())){
				startNodeIndex = VisualGraphNode.getNodes().indexOf(n);
			}
        }
		if(startNodeIndex != -1){
			selection.getSelectionModel().select(startNodeIndex);
		}
        Tooltip.install(selection, tooltip);
        section.getChildren().add(selection);

        selection.setOnAction((event) -> {
            MainWindow.get().setStartNode(VisualGraphNode.getNodes().get(selection.getSelectionModel().getSelectedIndex()).getNode());
			MainWindow.get().updateAlgorithmDetails();
			//MainWindow.get().initialiseAlgorithms();
			if(MainWindow.get().canRunAlgorithms()){
				MainWindow.get().displayMessage("Algorithms initialised!", "You may now run the algorithms in each viewport");
			}
			else{
				MainWindow.get().displayWarningMessage("Algorithms incorrectly set up", "Please select an algorithm to run in all viewports");
			}
        });

        getChildren().add(section);
    }

    /**
	*    Create and set up the RUN button
	*    The run button asks the mainWindow to begin the execution of algorithms
	**/
	private void createRunButton(){
		Tooltip tooltip = new Tooltip("Run all algorithms on the current graph, step by step");
		Button button = new Button("RUN");
		Tooltip.install(button, tooltip);
        if(MainWindow.get() != null && MainWindow.get().canRunAlgorithms()){
            button.setOnAction(new EventHandler<ActionEvent>() {
	    		@Override public void handle(ActionEvent e){
	    			MainWindow.get().startAlgorithms();
	    		}
	    	});
		}
		else{
			button.setOnAction(new EventHandler<ActionEvent>() {
	    		@Override public void handle(ActionEvent e){
	    			MainWindow.get().displayErrorMessage("Unable to run algorithms", "You must set up the algorithms first!", null);
	    		}
	    	});
			tooltip = new Tooltip("Unable to run algorithms. You must initialise the algorithms in each viewport first");
			Tooltip.install(button, tooltip);
		}

		getChildren().add(button);
	}
}
