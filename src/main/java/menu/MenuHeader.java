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
*    The headers contain many useful oeprations that the user may need
*    @Author Harrison Boyle-Thomas
*    @Date 22/01/21
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
		load.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				MainWindow.get().loadGraph();
			}
		});

		file.getItems().add(load);


		String saveInstructios = "Save the currently selected nodes";
		Tooltip saveTooltip = new Tooltip(saveInstructios);
		Label saveLabel = new Label("Save");
		Tooltip.install(saveLabel, saveTooltip);
		CustomMenuItem save = new CustomMenuItem(saveLabel);
		save.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				MainWindow.get().saveGraph();
			}
		});

		file.getItems().add(save);


		getMenus().add(file);
	}

	private void initialiseEditMenu(){
		Menu edit = new Menu("Edit");

        Tooltip copyTooltip = new Tooltip("Copy the selected nodes to a buffer");
		Label copyLabel = new Label("Copy");
		Tooltip.install(copyLabel, copyTooltip);
		CustomMenuItem copy = new CustomMenuItem(copyLabel);
		copy.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				MainWindow.get().copySelected();
			}
		});
		edit.getItems().add(copy);


		Tooltip pasteTooltip = new Tooltip("Paste the nodes from the buffer");
		Label pasteLabel = new Label("Paste");
		Tooltip.install(pasteLabel, pasteTooltip);
		CustomMenuItem paste = new CustomMenuItem(pasteLabel);
		paste.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				MainWindow.get().pasteSelected();
			}
		});
        edit.getItems().add(paste);


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
