package menu;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class EmptyDetails extends DetailsPanel{
	
	public EmptyDetails(){
		update();
	}
	
	public void update(){
		getChildren().clear();
		Label title = new Label("DETAILS PANEL");
		getChildren().add(title);
		
		addCreateNodeButton();
	}
	
	private void addCreateNodeButton(){
		Button createButton = new Button("CREATE NODE");
		Tooltip tooltip = new Tooltip("Create a node in front of the camera");
		Tooltip.install(createButton, tooltip);
		
		createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().createNode();
            }
        });
		getChildren().add(createButton);
	}
}