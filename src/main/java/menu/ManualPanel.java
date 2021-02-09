package menu;

public class ManualPanel extends ScrollPane{
    public ManualPanel(){
        createContentsPage();
    }

    private void createContentsPage(){
        VBox content = new VBox();

        Label title = new Label("Welcome to GraphVisualiser!");

        Button informationButton = new Button("About");
        informationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createInformationPage();
            }
        });
        content.getChildren().add(informationButton);

        Button controlsButton = new Button("Controls");
        controlsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createControlsPage();
            }
        });
        content.getChildren().add(controlsButton);

        Button performanceConsiderations = new Button("Performance guide");
        performanceConsiderations.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createPerformancePage();
            }
        });
        content.getChildren().add(performanceConsiderations);

        setContent(content);
    }

    private void createInformationPage(){
        VBox content = new VBox();
        Label title = new Label("About");
        content.getChildren().add(title);

        Text text = new Text();
        setContent(content);
    }

    private void createControlsPage(){
        VBox content = new VBox();

        setContent(content);
    }

    private void createPerformancePage(){
        VBox content = new VBox();

        setContent(content);
    }
}
