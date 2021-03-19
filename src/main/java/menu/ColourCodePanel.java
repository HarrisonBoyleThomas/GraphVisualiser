package menu;

import data.Data;
import model.GraphComponentState;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.control.Tooltip;
import java.util.stream.Collectors;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;

import javafx.event.*;

import java.lang.reflect.Field;

import javafx.util.Duration;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.Observable;


import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class ColourCodePanel extends VBox{
    private PreferencesPanel parent;
    public ColourCodePanel(PreferencesPanel parentIn){
        super(20);
        setMinHeight(500);
        setMinWidth(500);
        setMaxHeight(500);
        setMaxWidth(500);
        update();
        parent = parentIn;
    }

    private void updateStyle(){
		List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList());
        getStylesheets().clear();
        getStylesheets().add(((MainWindow)windows.get(0).getScene().getRoot()).getThemeStyleSheet());
	}

    private void update(){
        updateStyle();
        getChildren().clear();

        VBox content = new VBox(5);
        setAlignment(Pos.TOP_CENTER);
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Colour codes");

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        int index = 0;
        for(GraphComponentState state : GraphComponentState.values()){
            Color initialValue = Data.COLOUR_CODE_DATA.getColourForState(state);
            if(initialValue != null){
                grid.add(new Label(state.name()), 0, index);
                ColorPicker picker = new ColorPicker();
                picker.setOnAction(new EventHandler() {
                    public void handle(Event t) {
                        System.out.println("Colour set to " + Data.formatColourToRGBA(picker.getValue()) + " for " + state.name());
                        Data.COLOUR_CODE_DATA.setColourForState(state, picker.getValue());
                        update();
                    }
                });
                picker.setValue(initialValue);
                grid.add(picker, 1, index);
                index++;
            }
        }
        content.getChildren().add(grid);

        Button reset = new Button("Reset");
        Tooltip resetTooltip = new Tooltip("Reset colours to their default values");
        Tooltip.install(reset, resetTooltip);
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Data.COLOUR_CODE_DATA.reset();
                update();
                System.out.println("Colour codes reset");
            }
        });
        content.getChildren().add(reset);

        Button back = new Button("Back");
        Tooltip backTooltip = new Tooltip("Go back to the previous screen");
        Tooltip.install(back, backTooltip);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                parent.reset();
            }
        });
        content.getChildren().add(back);

        getChildren().add(content);

    }
}
