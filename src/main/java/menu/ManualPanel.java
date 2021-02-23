package menu;

import data.Data;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Arrays;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

import java.lang.reflect.Field;

import javafx.util.Duration;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.Observable;

import java.nio.file.Paths;
import java.io.InputStream;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class ManualPanel extends VBox{
    ArrayList<MediaPlayer> players = new ArrayList<>();
    public ManualPanel(){
        super(20);
        setMinHeight(500);
        setMinWidth(500);
        setMaxHeight(500);
        setMaxWidth(500);
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

        Label title = new Label("Welcome to GraphVisualiser!");
        getChildren().add(title);

        Tooltip informationTooltip = new Tooltip("View general information about GraphVisualiser");
        Button informationButton = new Button("About");
        Tooltip.install(informationButton, informationTooltip);
        informationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createInformationPage();
            }
        });
        content.getChildren().add(informationButton);

        Tooltip controlsTooltip = new Tooltip("View the controls for GraphVisualiser");
        Button controlsButton = new Button("Controls");
        Tooltip.install(controlsButton, controlsTooltip);
        controlsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createControlsPage();
            }
        });
        content.getChildren().add(controlsButton);

        Tooltip performanceTooltip = new Tooltip("View tips about improving the performance of GraphVisualiser");
        Button performanceConsiderations = new Button("Performance guide");
        Tooltip.install(performanceConsiderations, performanceTooltip);
        performanceConsiderations.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createPerformancePage();
            }
        });
        content.getChildren().add(performanceConsiderations);

        Tooltip tutorialTooltip = new Tooltip("View a tutorial video explaining how to use GraphVisualiser");
        Button tutorial = new Button("Tutorial");
        Tooltip.install(tutorial, tutorialTooltip);
        tutorial.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createTutorialPage();
            }
        });
        content.getChildren().add(tutorial);

        getChildren().add(content);

    }

    private void createInformationPage(){
        updateStyle();
        getChildren().clear();
        ScrollPane sp = new ScrollPane();
        VBox content = createPage("/manual/manual_about.txt");
        sp.prefViewportHeightProperty().bind(content.heightProperty());
        sp.setContent(content);
        getChildren().add(sp);

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createContentsPage();
            }
        });
        getChildren().add(back);
    }

    private void createControlsPage(){
        updateStyle();
        getChildren().clear();
        ScrollPane sp = new ScrollPane();
        ArrayList<String> data = readFile("/manual/manual_controls.txt");

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        sp.prefViewportHeightProperty().bind(content.heightProperty());

        content.prefWidthProperty().bind(this.widthProperty());
        if(data.size() > 0){
            try{
                Insets keyInset = new Insets(10.0, 10.0, 10.0, 10.0);
                Insets nameInset = new Insets(10.0, 2.0, 10.0, 2.0);
                Insets descriptionInset = new Insets(10.0, 2.0, 10.0, 2.0);
                GridPane grid = new GridPane();
                int controlNumber = 0;
                //List of all possible keys
                Object[] keys = KeyCode.values();
                for(String line : data){
                    String[] split = line.split(",");
                    if(split.length == 2){
                        Tooltip keyTooltip = new Tooltip(split[0]);
                        //If a control name is specified on this line, then the control binding
                        //can be changed - add a combobox to change the controls instead of a label
                        if(split[1].split(":").length > 2){
                            Field field = Data.CAMERA_CONTROLS.getClass().getField(split[1].split(":")[2]);
                            keyTooltip = new Tooltip("Key: " + field.get(Data.CAMERA_CONTROLS));
                            ComboBox keySelection = new ComboBox();
                            for(Object key : keys){
                                keySelection.getItems().add(key.toString());
                            }
                            keySelection.getSelectionModel().select(new ArrayList<Object>(Arrays.asList(keys)).indexOf(field.get(Data.CAMERA_CONTROLS)));
                            grid.add(keySelection, 0, controlNumber);

                            keySelection.setOnAction((event) -> {
                                try{
                                    KeyCode selectedKey = ((KeyCode) new ArrayList<Object>(Arrays.asList(keys)).get(keySelection.getSelectionModel().getSelectedIndex()));
                                    field.set(Data.CAMERA_CONTROLS, selectedKey);
                                    if(Data.CAMERA_CONTROLS.save()){
                                        MainWindow.get().displayMessage("Control binding changed!", "The control binding has been successfully changed for " + split[1].split(":")[0]);
                                    }
                                    Tooltip.install(keySelection, new Tooltip("Key: " + selectedKey));
                                }
                                catch(Exception illegalAccessException){
                                    MainWindow.get().displayErrorMessage("Unable to edit controls", "There was an error when trying to change the control binding", null);
                                }
                            });
                            Tooltip.install(keySelection, keyTooltip);
                        }
                        else{
                            Label key = new Label(split[0]);
                            Tooltip.install(key, keyTooltip);
                            key.setId("manualLine");
                            key.setMinWidth(55);
                            key.setWrapText(true);
                            grid.add(key, 0, controlNumber);
                            grid.setMargin(key, keyInset);
                        }

                        Tooltip nameTooltip = new Tooltip(split[1].split(":")[0]);
                        Label controlName = new Label(split[1].split(":")[0]);
                        Tooltip.install(controlName, nameTooltip);
                        controlName.setId("manualLine");
                        controlName.setMinWidth(100);
                        controlName.setWrapText(true);
                        grid.setMargin(controlName, nameInset);
                        grid.add(controlName, 1, controlNumber);

                        Tooltip descriptionTooltip = new Tooltip(split[1].split(":")[1]);
                        Label controlDescription = new Label(split[1].split(":")[1]);
                        Tooltip.install(controlDescription, descriptionTooltip);
                        controlDescription.setId("manualLine");
                        controlDescription.setWrapText(true);
                        grid.setMargin(controlDescription, descriptionInset);
                        grid.add(controlDescription, 2, controlNumber);
                        controlNumber++;
                    }
                    else if(split.length == 1){
                        Label label = new Label(split[0]);
                        if(split[0].toUpperCase().equals(split[0])){
                            label.setId("manualHeader");
                        }
                        else{
                            label.setId("manualLine");
                        }
                        content.getChildren().add(label);
                    }
                }
                grid.setGridLinesVisible(true);

                content.getChildren().add(grid);
            }
            catch(Exception e){
                e.printStackTrace();
                content.getChildren().add(new Label("Bad format for manual_controls.txt. Ensure the format of each line is <keyCode, controlName:description>"));
            }
        }
        else{
            content.getChildren().add(new Label("Error loading manual_controls.txt"));
        }
        //Add controls reset button
        Button resetControls = new Button("Reset controls");
        Tooltip resetTooltip = new Tooltip("Reset all controls to their default values");
        Tooltip.install(resetControls, resetTooltip);
        resetControls.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Data.CAMERA_CONTROLS.reset();
                MainWindow.get().displayMessage("Controls reset", "Successfully restored controls to their default values");
                createControlsPage();
            }
        });
        content.getChildren().add(resetControls);

        sp.setContent(content);
        getChildren().add(sp);

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createContentsPage();
            }
        });
        getChildren().add(back);
    }

    private void createPerformancePage(){
        updateStyle();
        getChildren().clear();
        ScrollPane sp = new ScrollPane();
        VBox content = createPage("/manual/manual_performanceGuide.txt");
        sp.prefViewportHeightProperty().bind(content.heightProperty());
        sp.setContent(content);
        getChildren().add(sp);

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createContentsPage();
            }
        });
        getChildren().add(back);
    }

    private void createTutorialPage(){
        updateStyle();
        getChildren().clear();
        if(players.size() > 0){
            players.get(0).stop();
        }
        try{
            VBox content = new VBox();

            Media media = new Media(getClass().getResource("/tutorialVideos/gvt.mp4").toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            players.add(player);
            player.setAutoPlay(true);
            MediaView mediaView = new MediaView (player);
            mediaView.fitWidthProperty().bind(this.widthProperty().multiply(0.95));
            content.getChildren().add(mediaView);

            HBox controlBar = new HBox();

            Button pauseButton = new Button("Pause");
            pauseButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    if(player.getStatus().equals(Status.PLAYING)){
                        pauseButton.setText("Play");
                        player.pause();
                    }
                    else if(player.getStatus().equals(Status.PAUSED)){
                        pauseButton.setText("Pause");
                        player.play();
                    }
                }
            });
            controlBar.getChildren().add(pauseButton);

            Slider timeSlider = new Slider(0.0, 9.36, 0.0);
            timeSlider.setMinorTickCount(5);
            timeSlider.setShowTickLabels(true);
            timeSlider.setShowTickMarks(true);
            timeSlider.prefWidthProperty().bind(this.widthProperty().multiply(0.6));
            timeSlider.valueProperty().addListener(new InvalidationListener(){
                public void invalidated(Observable observable){
    				player.seek(player.getTotalDuration().multiply(timeSlider.getValue() / 100.0));
                }
            });
            controlBar.getChildren().add(timeSlider);

            player.currentTimeProperty().addListener(new InvalidationListener(){
                public void invalidated(Observable observable){
                    timeSlider.setValue(player.getCurrentTime().divide(player.getTotalDuration()).toMillis() * 100.0);
                }
            });


            content.getChildren().add(controlBar);

            getChildren().add(content);
        }
        catch(Exception e){
            e.printStackTrace();
            Label error = new Label("Unable to load video, check that tutorialVideos/gvt.mp4 exists");
            getChildren().add(error);
        }

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(players.size() > 0){
                    players.get(0).stop();
                }
                createContentsPage();
            }
        });
        getChildren().add(back);
    }


    private ArrayList<String> readFile(String fileName){
        ArrayList<String> output = new ArrayList<>();
        try{
            Scanner reader = new Scanner(getClass().getResourceAsStream(fileName));
            while(reader.hasNextLine()){
                output.add(reader.nextLine());
            }
            reader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return output;

    }

    private VBox createPage(String fileName){
        ArrayList<String> data = readFile(fileName);

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.prefWidthProperty().bind(this.widthProperty());



        Label title = new Label("About");
        content.getChildren().add(title);


        TextFlow textSection = new TextFlow();
        for(String s : data){
            Label line = new Label(s);
            if(s.toUpperCase().equals(s)){
                line.setId("manualHeader");
            }
            else{
                line.setId("manualLine");
            }
            line.setWrapText(true);
            line.prefWidthProperty().bind(content.widthProperty());
            textSection.getChildren().add(line);
        }
        content.getChildren().add(textSection);
        return content;
    }
}
