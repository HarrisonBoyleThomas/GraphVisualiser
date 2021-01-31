package menu;

import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

/**
*    A DetailsComponent is a specialised VBox intended to be used
*    to display information about GraphVisualiser's state
*    @author Harrison Boyle-Thomas
*    @date 31/01/21
**/
public abstract class DetailsComponent extends VBox{
	public DetailsComponent(){
		super(10);
		//getStyleClass().clear();
		setAlignment(Pos.TOP_CENTER);
	}
}
