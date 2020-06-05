import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;

public class Main extends Application {

	public Stage primaryStage;
	public MenuBar menuBar;
	public TabPane tabPane;
	public Scene scene;
	public String selectedItem;
	public ArrayList<Schematic> schematics;
	public TabPane controlTabPane;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		schematics = new ArrayList<>();
		// General Setup
		primaryStage.setTitle("Digital Design CAD Tool");
		MenuBar menuBar = setupMenuBar();
		SplitPane splitPane = new SplitPane();
		TabPane tabPane = new TabPane();
		HBox tabContainer = new HBox(tabPane);
	
		/*
		ScrollPane componentScrollPane = new ScrollPane();
		VBox componentContainer_vert1 = new VBox();
		setupToolBarLeft(componentContainer_vert1);
		VBox componentContainer_vert2 = new VBox();
		setupToolBarRight(componentContainer_vert2);
		HBox componentContainer = new HBox();
		componentContainer.getChildren().add(componentContainer_vert1);
		componentContainer.getChildren().add(new Separator());
		componentContainer.getChildren().add(componentContainer_vert2);
		componentScrollPane.setContent(componentContainer);
		*/
		TabPane controlTabPane = new TabPane();
		this.controlTabPane = controlTabPane;
		
		GridPane toolGrid = new GridPane();
		setupToolTab(toolGrid);
		GridPane componentGrid = new GridPane();
		setupComponentTab(componentGrid);
		
		splitPane.getItems().addAll(controlTabPane, tabContainer);
		this.primaryStage = primaryStage;
		this.menuBar = menuBar;
		this.tabPane = tabPane;

		// For setting the divider position at the start of the program
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

		// adding welcome text tab
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
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				splitPane.setDividerPositions(0.1);
			}
		});

		// Adjust the split pane height whenever the frame is changed in height
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				splitPane.setPrefHeight(primaryStage.getHeight());
			}
		});

		// Finalize stage initialization and show stage
		primaryStage.setMaximized(true);
		scene.getStylesheets().add("Styles/primary_style.css");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		this.scene = scene;
		
		
	}

	/*
	 * This method creates and returns the menu bar that is used at the top of the
	 * program.
	 */
	public MenuBar setupMenuBar() {
		// File Menu
		Menu fileMenu = new Menu("File");
		Menu newMenu = new Menu("New");
		MenuItem schematicItem = new MenuItem("Schematic");
		schematicItem.setOnAction(e -> {
			openNewSchematicWindow();
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
		MenuItem zoomInItem = new MenuItem("Zoom In");
		MenuItem zoomOutItem = new MenuItem("Zoom Out");
		editMenu.getItems().add(zoomInItem);
		zoomInItem.setOnAction(e -> {
			for(Schematic sch : schematics) {
				sch.zoom(0);
			}
		});
		zoomInItem.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN));
		editMenu.getItems().add(zoomOutItem);
		zoomOutItem.setOnAction(e -> {
			for(Schematic sch : schematics) {
				sch.zoom(1);
			}
		});
		zoomOutItem.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN));

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
	 * These are methods that set up the tool bar. TODO: MAKE A BETTER VERSION OF
	 * THIS SHIT. This is temp
	 */
	public void setupToolBarLeft(VBox container) {
		Image andImage = new Image("Images/AND_image.png", 60, 40, false, false);
		Button andButton = new Button();
		andButton.setOnAction(e -> {
			selectedItem = "AND";
		});
		andButton.setGraphic(new ImageView(andImage));
		container.getChildren().add(andButton);

		Image orImage = new Image("Images/OR_image.png", 60, 40, false, false);
		Button orButton = new Button();
		orButton.setOnAction(e -> {
			selectedItem = "OR";
		});
		orButton.setGraphic(new ImageView(orImage));
		container.getChildren().add(orButton);
	}

	public void setupToolBarRight(VBox container) {
		Image nandImage = new Image("Images/NAND_image.png", 60, 40, false, false);
		Button nandButton = new Button();
		nandButton.setOnAction(e -> {
			selectedItem = "NAND";
		});
		nandButton.setGraphic(new ImageView(nandImage));
		container.getChildren().add(nandButton);

		Image norImage = new Image("Images/NOR_image.png", 60, 40, false, false);
		Button norButton = new Button();
		norButton.setOnAction(e -> {
			selectedItem = "NOR";
		});
		norButton.setGraphic(new ImageView(norImage));
		container.getChildren().add(norButton);
	}
	
	public void setupComponentTab(GridPane grid) {
		Image andImage = new Image("Images/AND_image.png", 60, 40, false, false);
		Button andButton = new Button();
		andButton.setOnAction(e -> {
			selectedItem = "AND";
		});
		andButton.setGraphic(new ImageView(andImage));
		grid.add(andButton, 0, 0, 1, 1);

		Image orImage = new Image("Images/OR_image.png", 60, 40, false, false);
		Button orButton = new Button();
		orButton.setOnAction(e -> {
			selectedItem = "OR";
		});
		orButton.setGraphic(new ImageView(orImage));
		grid.add(orButton, 1, 0, 1, 1);
		
		Image nandImage = new Image("Images/NAND_image.png", 60, 40, false, false);
		Button nandButton = new Button();
		nandButton.setOnAction(e -> {
			selectedItem = "NAND";
		});
		nandButton.setGraphic(new ImageView(nandImage));
		grid.add(nandButton, 0, 1, 1, 1);

		Image norImage = new Image("Images/NOR_image.png", 60, 40, false, false);
		Button norButton = new Button();
		norButton.setOnAction(e -> {
			selectedItem = "NOR";
		});
		norButton.setGraphic(new ImageView(norImage));
		grid.add(norButton, 1, 1, 1, 1);
		
		ScrollPane componentScrollPane = new ScrollPane(grid);
		Tab componentTab = new Tab("Components", componentScrollPane);
		controlTabPane.getTabs().add(componentTab);
	}
	public void setupToolTab(GridPane grid) {
		//TODO: Finish this method
		ScrollPane toolScrollPane = new ScrollPane(grid);
		Tab toolTab = new Tab("Tools", toolScrollPane);
		controlTabPane.getTabs().add(toolTab);
	}

	/*
	 * This method creates a new schematic and adds it to the tab pane.
	 */
	public void createSchematic(String name, int width, int height) {
		ScrollPane sPane = new ScrollPane();
		Schematic schematic = new Schematic(this, tabPane, width, height);
		sPane.setContent(schematic);
		Tab newSchematic = new Tab(name, sPane);
		tabPane.getTabs().add(newSchematic);
		tabPane.getSelectionModel().select(newSchematic);
		
		schematics.add(schematic);

	}
	
	public void openNewSchematicWindow() {
		new NewSchematicWindow(this);
	}
}
