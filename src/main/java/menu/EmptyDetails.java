package menu;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
*    The EmptyDetails panel contains a menu that should always be accessible to
*    the user. This may be displayed when the user has selected no graph
*    components, or may even be added to other details panels
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
**/
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

    /**
	*    Create and set up the create node button
	*    the create button asks the mainWindow to create a node in front of the camera
	**/
	private void addCreateNodeButton(){
		Button createButton = new Button("CREATE NODE");
		createButton.setId("createButton");
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
