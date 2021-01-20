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

		createStartNodeSection();
	}


    private void createStartNodeSection(){

        Tooltip tooltip = new Tooltip("Select a start node for the algorithm");
        HBox section = new HBox();
        Tooltip.install(section, tooltip);

        Label label = new Label("Start node:");
        section.getChildren().add(label);
        Tooltip.install(label, tooltip);

        ComboBox selection = new ComboBox();
        for(VisualGraphNode n : VisualGraphNode.getNodes()){
            selection.getItems().add(n.getNode().getName());
        }
        Tooltip.install(selection, tooltip);
        section.getChildren().add(selection);

        selection.setOnAction((event) -> {
            MainWindow.get().setStartNode(VisualGraphNode.getNodes().get(selection.getSelectionModel().getSelectedIndex()).getNode());
        });

        getChildren().add(section);
    }
}
