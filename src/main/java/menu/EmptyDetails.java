package menu;

import javafx.scene.control.Label;

public class EmptyDetails extends DetailsPanel{
	
	public EmptyDetails(){
		update();
	}
	
	public void update(){
		getChildren().clear();
		Label title = new Label("DETAILS PANEL");
		getChildren().add(title);
	}
}