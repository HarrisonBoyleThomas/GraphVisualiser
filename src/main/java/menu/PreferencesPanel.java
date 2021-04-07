package menu;

import data.Data;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import java.util.stream.Collectors;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.lang.reflect.Field;

import javafx.util.Duration;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.Observable;


import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
*    The preferences panel can be displayed on a popup window to allow the user
*    to access menus to edit the preferences of particular parts of the app
**/
public class PreferencesPanel extends VBox{
    public PreferencesPanel(){
        super(20);
        setMinHeight(500);
        setMinWidth(500);
        setMaxHeight(500);
        setMaxWidth(500);
        createContentsPage();
    }

    public void reset(){
        createContentsPage();
    }

    private void updateStyle(){
		List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList());
        getStylesheets().clear();
        getStylesheets().add(((MainWindow)windows.get(0).getScene().getRoot()).getThemeStyleSheet());
	}

    private void createContentsPage(){
        updateStyle();
        getChildren().clear();

        VBox content = new VBox(5);
        setAlignment(Pos.TOP_CENTER);
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("PREFERENCES");
        content.getChildren().add(title);

        Button colourCodeButton = new Button("Colour codes");
        Tooltip colourCodeTooltip = new Tooltip("Edit the colour codes for graph components in alhorithms");
        Tooltip.install(colourCodeButton, colourCodeTooltip);
        colourCodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createColourCodePage();
            }
        });
        content.getChildren().add(colourCodeButton);
        getChildren().add(content);
    }

    private void createColourCodePage(){
        ColourCodePanel panel = new ColourCodePanel(this);
        getChildren().clear();

        getChildren().add(panel);
    }
}
