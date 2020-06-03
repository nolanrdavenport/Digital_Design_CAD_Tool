import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
public class Main extends Application{
	
	public Stage primaryStage;
	public MenuBar menuBar;
	public TabPane tabPane;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		primaryStage.setTitle("Digital Design CAD Tool");
		MenuBar menuBar = setupMenuBar();
		SplitPane splitPane = new SplitPane();
		TabPane tabPane = new TabPane();
		ToolBar toolBar = setupToolBar();
		
		HBox tabContainer = new HBox(tabPane);
		HBox toolContainer = new HBox(toolBar);
		
		splitPane.getItems().addAll(toolContainer, tabContainer);
		primaryStage.showingProperty().addListener(new ChangeListener<Boolean>() {

		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if (newValue) {
		            splitPane.setDividerPositions(0.1);
		            splitPane.setPrefHeight(primaryStage.getHeight());
		            observable.removeListener(this);
		        }
		    }

		});
		this.primaryStage = primaryStage;
		this.menuBar = menuBar;
		this.tabPane = tabPane;
		
		//adding welcome text tab
		TextArea welcomeText = new TextArea();
		welcomeText.appendText("Welcome to the Digital Design CAD Tool!");
		welcomeText.appendText("\nChoose [File] -> [New] -> [Schematic] to make a new schematic.");
		welcomeText.setEditable(false);
		Tab welcomeTab = new Tab("Welcome", welcomeText);
		tabPane.getTabs().add(welcomeTab);
		
		
		VBox vBox = new VBox(menuBar);
		vBox.getChildren().add(splitPane);
		Scene scene = new Scene(vBox, 500, 500);
		
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	splitPane.setDividerPositions(0.1);
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	splitPane.setPrefHeight(primaryStage.getHeight());
		    }
		});
		primaryStage.setMaximized(true);
		
		scene.getStylesheets().add("Styles/primary_style.css");

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/*
	 * This method creates and returns the menu bar that is used at the top of the program.
	 */
	public MenuBar setupMenuBar() {
		// File Menu
		Menu fileMenu = new Menu("File");
		Menu newMenu = new Menu("New");
		MenuItem schematicItem = new MenuItem("Schematic");
		schematicItem.setOnAction(e -> {
			createSchematic();
		});
		MenuItem truthTableItem = new MenuItem("Truth Table");
		newMenu.getItems().add(schematicItem);
		newMenu.getItems().add(truthTableItem);
		MenuItem openFileItem = new MenuItem("Open File...");
		MenuItem closeItem = new MenuItem("Close");
		MenuItem saveItem = new MenuItem("Save");
		MenuItem saveAsItem = new MenuItem("Save As...");
		fileMenu.getItems().add(newMenu);
		fileMenu.getItems().add(openFileItem);
		fileMenu.getItems().add(closeItem);
		fileMenu.getItems().add(saveItem);
		fileMenu.getItems().add(saveAsItem);
		
		// Edit Menu
		Menu editMenu = new Menu("Edit");
		
		// Project Menu
		Menu projectMenu = new Menu("Project");
		MenuItem openProjectItem = new MenuItem("Open Project...");
		MenuItem closeProjectItem = new MenuItem("Close Project");
		MenuItem propertiesItem = new MenuItem("Properties");
		projectMenu.getItems().add(openProjectItem);
		projectMenu.getItems().add(closeProjectItem);
		projectMenu.getItems().add(propertiesItem);
		
		// Help Menu
		Menu helpMenu = new Menu("Help");
		MenuItem aboutItem = new MenuItem("About");
		helpMenu.getItems().add(aboutItem);
		
		// Create menu bar and add menus to it
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(editMenu);
		menuBar.getMenus().add(projectMenu);
		menuBar.getMenus().add(helpMenu);
		
		return menuBar;
	}
	
	public ToolBar setupToolBar() {
		ToolBar toolBar = new ToolBar();
		Button temp1 = new Button("[temp1]");
		Button temp2 = new Button("[temp2]");
		toolBar.setOrientation(Orientation.VERTICAL);
		
		toolBar.getItems().add(temp1);
		toolBar.getItems().add(temp2);
		
		return toolBar;
	}
	
	/*
	 * This method creates a new schematic and adds it to the tab pane. 
	 */
	public void createSchematic() {
		Tab newSchematic = new Tab("schematic", new Label("[TODO: make schematic system]"));
		tabPane.getTabs().add(newSchematic);
	}
}
