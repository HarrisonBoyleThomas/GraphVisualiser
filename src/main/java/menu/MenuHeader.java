package menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.CustomMenuItem;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
*    The MenuHeader represents the top panel menu bar of the GraphVisualiser Main Window
**/
public class MenuHeader extends MenuBar{
	public MenuHeader(){
		Menu file = new Menu("File");
		
		initialiseFileMenu();
		initialiseEditMenu();
	    initialiseAlgorithmMenu();
		initialiseViewMenu();
		
	}
	
	private void initialiseFileMenu(){
		Menu file = new Menu("File");
		
		
		String loadInstructions = "Load a graph into the viewport";
		Tooltip loadTooltip = new Tooltip(loadInstructions);
		Label loadLabel = new Label("Load");
		Tooltip.install(loadLabel, loadTooltip);
		CustomMenuItem load = new CustomMenuItem(loadLabel);
		
		file.getItems().add(load);
		
		
		getMenus().add(file);
	}
	
	private void initialiseEditMenu(){
		Menu edit = new Menu("Edit");
		
		
		
		
		getMenus().add(edit);
	}
	
	private void initialiseAlgorithmMenu(){
		Menu algorithm = new Menu("Algorithm");
		
		String runInstructions = "Run all algorithms. The graph will not be editable while algorithms are running";
		Tooltip runTooltip = new Tooltip(runInstructions);
		Label r = new Label("Run");
		Tooltip.install(r, runTooltip);
		CustomMenuItem run = new CustomMenuItem(r);
		run.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().createViewport();
            }
        });
		
		algorithm.getItems().add(run);
		
		
		getMenus().add(algorithm);
	}
	
	private void initialiseViewMenu(){
		Menu view = new Menu("View");
		
		String viewportInstructions = "Add a new viewport to the main window. Each viewport can render a different algorithm";
		viewportInstructions += ", so that you can compare algorithms. A maximum of 4 viewports can be added";
		Tooltip addViewportTooltip = new Tooltip(viewportInstructions);
		Label av = new Label("Add viewport");
		Tooltip.install(av, addViewportTooltip);
		CustomMenuItem addViewport = new CustomMenuItem(av);
		
		addViewport.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().createViewport();
            }
        });
		
		view.getItems().add(addViewport);
		
		
		getMenus().add(view);
	}
}