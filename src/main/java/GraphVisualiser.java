import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.Parent;

import menu.*;
import data.Data;
import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;

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

	private int totalCount = 0;

	private int frames = 0;

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
				totalCount += (int) (1000000000.0/(currentNanoTime - lastFrameTime));
				handleMovementInput();
				MainWindow.get().updateAppDetails();
				lastFrameTime = currentNanoTime;
				frames++;
				if(frames > 1000){
					int average = 0;
					if(frames > 0){
						average = totalCount / frames;
					}
					System.out.println("(N:" + VisualGraphNode.getNodes().size() + ", E:" + VisualGraphEdge.getEdges().size() + "), " + average);
					frames = 0;
					totalCount = 0;
				}

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

	public Parent getRoot(){
		return MainWindow.get();
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
				if(e.getCode() == KeyCode.F){
					totalCount = 0;
					frames = 0;
					System.out.println("\n\n\n");
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
