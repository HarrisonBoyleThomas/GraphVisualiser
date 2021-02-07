package menu;

import data.Data;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;

/**
*    The AppDetails displays useful performance information about GraphVisualiser,
*    such as frame rate
*    @author Harrison Boyle-Thomas
*    @date 07/02/21
**/
public class AppDetails extends DetailsPanel{
    public AppDetails(){
        update();
    }

    public void update(){
        getChildren().clear();
        createFPSSection();
    }

    private void createFPSSection(){
        Tooltip tooltip = new Tooltip("The current frame rate of GraphVisualiser");
        HBox section = new HBox();
        Tooltip.install(section, tooltip);
        Label title = new Label("Frame rate:");
        Label count = new Label("" + Data.getFrameRate());
        section.getChildren().add(title);
        section.getChildren().add(count);
        section.setAlignment(Pos.TOP_CENTER);
        getChildren().add(section);
    }
}
