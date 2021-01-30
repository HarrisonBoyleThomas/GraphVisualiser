package menu;

import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public abstract class DetailsComponent extends VBox{
	public DetailsComponent(){
		super(10);
		//getStyleClass().clear();
		setAlignment(Pos.TOP_CENTER);
	}
}
