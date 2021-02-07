import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.stage.Stage;

import menu.*;
import data.Data;

import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;


public class GraphVisualiser extends Application{
	private ArrayList<KeyCode> heldDownKeys = new ArrayList<>();

	private long lastFrameTime;

	public static void main(String[] args){
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("Graph visualiser");

		Scene scene = new Scene(MainWindow.get(), 1000, 700);
		addEvents(scene);
		primaryStage.setScene(scene);
		primaryStage.show();


		//Mainloop
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				Data.setDeltaTime(currentNanoTime - lastFrameTime);
				handleMovementInput();
				MainWindow.get().updateAppDetails();
				lastFrameTime = currentNanoTime;

			    //try{
				//    Thread.sleep(1);
			    //}
			    //catch(InterruptedException  error){
                //
			    //}
			}
		}.start();

        //kill all threads in the program when closing
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
            public void handle(WindowEvent e) {
				Platform.exit();
				System.exit(0);
            }
		});


	}



	private void handleMovementInput(){
		MainWindow.get().handleMovementInput(heldDownKeys);
	}


	private void addEvents(Scene scene){
		//Pitch sensor
		scene.setOnKeyPressed(new EventHandler<KeyEvent> (){
			public void handle(KeyEvent e){
				if(e.getCode() == KeyCode.ENTER){
					MainWindow.get().stepAlgorithms();
				}
				if(!heldDownKeys.contains(e.getCode())){
					heldDownKeys.add(e.getCode());
				}
				//Prevent focus traversal
				e.consume();
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent> () {
			public void handle(KeyEvent e){
				heldDownKeys.remove(e.getCode());
				MainWindow.get().handleSingleInput(e.getCode());
			}
		});



	}

}
