import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;

import menu.*;


public class GraphVisualiser extends Application{
	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("Graph visualiser");
		
		primaryStage.setScene(new Scene(makeMainWindow(), 1000, 700));
		primaryStage.show();
	}
	
	private MainWindow makeMainWindow(){
		return new MainWindow();
	}
	
}