package menu;

import maths.Vector;
import viewport.Camera;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.lang.NumberFormatException;


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
			if(!MainWindow.get().areAlgorithmsFinished()){
    		    createRunButton();
    		    createExecuteButton();
		    }
			else{
				createResetButton();
			}
		}
		else{
			getChildren().add(new Label("Algorithm setup dirty"));
		}
	}


    private void createStartNodeSection(){

        Tooltip tooltip = new Tooltip("Select a start node for the algorithm");
        HBox section = new HBox();
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
        });

        getChildren().add(section);
    }

	private void createRunButton(){
		Tooltip tooltip = new Tooltip("Run all algorithms on the current graph, step by step");
		Button button = new Button("Run");
		Tooltip.install(button, tooltip);
        if(MainWindow.get() != null && MainWindow.get().canRunAlgorithms()){
            button.setOnAction(new EventHandler<ActionEvent>() {
	    		@Override public void handle(ActionEvent e){
	    			MainWindow.get().startAlgorithms();
	    		}
	    	});
		}
		else{
			tooltip = new Tooltip("Unable to run algorithms. You must initialise the algorithms in each viewport first");
			Tooltip.install(button, tooltip);
		}

		getChildren().add(button);
	}

	private void createExecuteButton(){
		Tooltip tooltip = new Tooltip("Run all algorithms on the current graph, to quickly obtain their output without stepping");
		Button button = new Button("Execute");
		Tooltip.install(button, tooltip);

        if(MainWindow.get() != null && MainWindow.get().canRunAlgorithms()){
            button.setOnAction(new EventHandler<ActionEvent>() {
	    		@Override public void handle(ActionEvent e){
	    			MainWindow.get().executeAlgorithms();
	    		}
	    	});
		}
		else{
			tooltip = new Tooltip("Unable to execute algorithms. You must initialise the algorithms in each viewport first");
			Tooltip.install(button, tooltip);
		}

		getChildren().add(button);
	}

	private void createResetButton(){
		Tooltip tooltip = new Tooltip("Reset all algorithms");
		Button button = new Button("Reset");
		Tooltip.install(button, tooltip);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e){
				MainWindow.get().initialiseAlgorithms();
			}
		});

		getChildren().add(button);
	}
}
