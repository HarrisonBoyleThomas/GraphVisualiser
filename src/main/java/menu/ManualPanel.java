package menu;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
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

import java.nio.file.Paths;
import java.io.InputStream;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class ManualPanel extends VBox{
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
                for(String line : data){
                    String[] split = line.split(",");
                    if(split.length == 2){
                        Tooltip keyTooltip = new Tooltip(split[0]);
                        Label key = new Label(split[0]);
                        Tooltip.install(key, keyTooltip);
                        key.setId("manualLine");
                        key.setMinWidth(55);
                        key.setWrapText(true);
                        grid.add(key, 0, controlNumber);
                        grid.setMargin(key, keyInset);

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
                content.getChildren().add(new Label("Bad format for manual_controls.txt. Ensure the format of each line is <keyCode, controlName:description>"));
            }
        }
        else{
            content.getChildren().add(new Label("Error loading manual_controls.txt"));
        }
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


    private ArrayList<String> readFile(String fileName){
        ArrayList<String> output = new ArrayList<>();
        try{
            Scanner reader = new Scanner(new File(getClass().getResource(fileName).toURI()));
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
