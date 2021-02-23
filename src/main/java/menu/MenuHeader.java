package menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.CustomMenuItem;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import javafx.scene.Scene;
import javafx.stage.Stage;

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
		initialiseHelpMenu();

	}

    /**
	*    Create and set up the FILE tab in the menu
	**/
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


		String saveInstructions = "Save the currently selected nodes";
		Tooltip saveTooltip = new Tooltip(saveInstructions);
		Label saveLabel = new Label("Save");
		Tooltip.install(saveLabel, saveTooltip);
		CustomMenuItem save = new CustomMenuItem(saveLabel);
		save.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				MainWindow.get().saveGraph();
			}
		});

		file.getItems().add(save);


		String themeInstructions = "Set the theme of GraphVisualiser";
		Tooltip themeTooltip = new Tooltip(themeInstructions);
		Label themeLabel = new Label("Theme");
		Tooltip.install(themeLabel, themeTooltip);
		Menu theme = new Menu("Theme");
		for(Object value : ThemeState.values()){
			CustomMenuItem item = new CustomMenuItem(new Label(("" + value).toLowerCase()));
			theme.getItems().add(item);
			item.setOnAction(new EventHandler<ActionEvent>(){
				@Override public void handle(ActionEvent e){
                    MainWindow.get().setTheme((ThemeState) value);
				}
			});
		}

		file.getItems().add(theme);



		getMenus().add(file);
	}

    /**
	*    Create and set up the EDIT tab in the menu
	**/
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

    /**
	*    Create and set up the ALGORITHM tab in the menu
	**/
	private void initialiseAlgorithmMenu(){
		Menu algorithm = new Menu("Algorithm");

		String runInstructions = "Run all algorithms. The graph will not be editable while algorithms are running";
		Tooltip runTooltip = new Tooltip(runInstructions);
		Label r = new Label("Run");
		Tooltip.install(r, runTooltip);
		CustomMenuItem run = new CustomMenuItem(r);
		run.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                MainWindow.get().startAlgorithms();
            }
        });

		algorithm.getItems().add(run);


		getMenus().add(algorithm);
	}

	/**
	*    Create and set up the VIEW tab in the menu
	**/
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

	private void initialiseHelpMenu(){
		Menu help = new Menu("Help");
		Tooltip manualTooltip = new Tooltip("Copy the selected nodes to a buffer");
		Label manualLabel = new Label("Manual");
		Tooltip.install(manualLabel, manualTooltip);
		CustomMenuItem manual = new CustomMenuItem(manualLabel);
		manual.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				Stage popup = new Stage();
				popup.initOwner(MenuHeader.this.getScene().getWindow());
				popup.setResizable(false);
				popup.setTitle("Manual");
				Scene scene = new Scene(new ManualPanel(), 600, 450);
				popup.setScene(scene);
				popup.show();
			}
		});
		help.getItems().add(manual);
		getMenus().add(help);
	}
}
