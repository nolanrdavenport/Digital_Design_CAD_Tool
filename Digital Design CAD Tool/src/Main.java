import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
		// General Setup
		primaryStage.setTitle("Digital Design CAD Tool");
		MenuBar menuBar = setupMenuBar();
		SplitPane splitPane = new SplitPane();
		TabPane tabPane = new TabPane();
		HBox tabContainer = new HBox(tabPane);
		ScrollPane toolScrollPane = new ScrollPane();
		VBox toolContainer_vert1 = new VBox();
		setupToolBarLeft(toolContainer_vert1);
		VBox toolContainer_vert2 = new VBox();
		setupToolBarRight(toolContainer_vert2);
		HBox toolContainer = new HBox();
		toolContainer.getChildren().add(toolContainer_vert1);
		toolContainer.getChildren().add(new Separator());
		toolContainer.getChildren().add(toolContainer_vert2);
		toolScrollPane.setContent(toolContainer);
		splitPane.getItems().addAll(toolScrollPane, tabContainer);
		this.primaryStage = primaryStage;
		this.menuBar = menuBar;
		this.tabPane = tabPane;
		
		//For setting the divider position at the start of the program
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
		
		//adding welcome text tab
		TextArea welcomeText = new TextArea();
		welcomeText.appendText("Welcome to the Digital Design CAD Tool!");
		welcomeText.appendText("\nChoose [File] -> [New] -> [Schematic] to make a new schematic.");
		welcomeText.setEditable(false);
		Tab welcomeTab = new Tab("Welcome", welcomeText);
		tabPane.getTabs().add(welcomeTab);
		
		// Add menu bar and split pane to the scene
		VBox vBox = new VBox(menuBar);
		vBox.getChildren().add(splitPane);
		Scene scene = new Scene(vBox, 500, 500);
		
		// Adjust divider position whenever the frame is changed in width
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	splitPane.setDividerPositions(0.1);
		    }
		});
		
		// Adjust the split pane height whenever the frame is changed in height
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	splitPane.setPrefHeight(primaryStage.getHeight());
		    }
		});
		
		// Finalize stage initialization and show stage
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
	
	/*
	 * These are methods that set up the tool bar.
	 * TODO: MAKE A BETTER VERSION OF THIS SHIT. This is temp
	 */
	public void setupToolBarLeft(VBox container) {
		Image andImage = new Image("Images/AND_gate.png", 60, 60, false, false);
		Button andButton = new Button();
		andButton.setGraphic(new ImageView(andImage));
		container.getChildren().add(andButton);
		
		Image orImage = new Image("Images/OR_gate.png", 60, 60, false, false);
		Button orButton = new Button();
		orButton.setGraphic(new ImageView(orImage));
		container.getChildren().add(orButton);
	}
	public void setupToolBarRight(VBox container) {
		Image nandImage = new Image("Images/NAND_gate.png", 60, 60, false, false);
		Button nandButton = new Button();
		nandButton.setGraphic(new ImageView(nandImage));
		container.getChildren().add(nandButton);
		
		Image norImage = new Image("Images/NOR_gate.png", 60, 60, false, false);
		Button norButton = new Button();
		norButton.setGraphic(new ImageView(norImage));
		container.getChildren().add(norButton);
	}
	
	/*
	 * This method creates a new schematic and adds it to the tab pane. 
	 */
	public void createSchematic() {
		ScrollPane sPane = new ScrollPane();
		Schematic schematic = new Schematic(2000,2000);
		sPane.setContent(schematic);
		Tab newSchematic = new Tab("schematic", sPane);
		tabPane.getTabs().add(newSchematic);
	}
}
