import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;

public class Main extends Application {

	public Stage primaryStage;
	public MenuBar menuBar;
	public TabPane tabPane;
	public Scene scene;
	public String selectedItem = "NONE";
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
					splitPane.setDividerPositions(0.15);
					splitPane.setPrefHeight(primaryStage.getHeight());
					observable.removeListener(this);
				}
			}

		});

		// adding welcome text tab
		Tab welcomeTab = createWelcomeTab();
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
				splitPane.setDividerPositions(0.15);
				splitPane.setPrefWidth(primaryStage.getWidth());
				tabPane.setPrefWidth(primaryStage.getWidth());
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
	
	public Tab createWelcomeTab() {
		TextArea welcomeText = new TextArea();
		welcomeText.appendText("Welcome to the Digital Design CAD Tool!");
		welcomeText.appendText("\nChoose [File] -> [New] -> [Schematic] to make a new schematic.");
		welcomeText.appendText("\n\nPress [MIDDLE MOUSE] to drag the schematic. \nThat is a necessary feature!");
		welcomeText.appendText("\n\nDrop components by selecting them from the [Components] tab. \nYour cursor will become a crosshair.");
		welcomeText.appendText("\n\nIn order to move components that have already been dropped into \nthe schematic, select the [Select] tool which is located in the [Tools] tab");
		welcomeText.setEditable(false);
		
		Tab welcomeTab = new Tab("Welcome", welcomeText);
		
		return welcomeTab;
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
		MenuItem undoItem = new MenuItem("Undo");
		MenuItem redoItem = new MenuItem("Redo");
		MenuItem zoomInItem = new MenuItem("Zoom In");
		MenuItem zoomOutItem = new MenuItem("Zoom Out");
		MenuItem clearSchematicItem = new MenuItem("Clear Schematic");
		editMenu.getItems().add(undoItem);
		undoItem.setOnAction(e -> {
			for(Schematic sch : schematics) {
				sch.undo();
			}
		});
		
		editMenu.getItems().add(redoItem);
		redoItem.setOnAction(e -> {
			for(Schematic sch : schematics) {
				sch.redo();
			}
		});
		undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
		
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
		clearSchematicItem.setOnAction(e -> {
			//TODO: Make it only clear selected schematic
			for(Schematic sch : schematics) {
				sch.clear(true);
			}
		});
		editMenu.getItems().add(clearSchematicItem);

		// Project Menu
		Menu projectMenu = new Menu("Project");
		MenuItem openProjectItem = new MenuItem("Open Project...");
		MenuItem closeProjectItem = new MenuItem("Close Project");
		MenuItem propertiesItem = new MenuItem("Properties");
		projectMenu.getItems().add(openProjectItem);
		projectMenu.getItems().add(closeProjectItem);
		projectMenu.getItems().add(propertiesItem);
		
		// Window Menu
		Menu windowMenu = new Menu("Window");
		MenuItem toolTabItem = new MenuItem("Tools");
		MenuItem componentTabItem = new MenuItem("Components");
		windowMenu.getItems().add(toolTabItem);
		windowMenu.getItems().add(componentTabItem);

		// Help Menu
		Menu helpMenu = new Menu("Help");
		MenuItem aboutItem = new MenuItem("About");
		helpMenu.getItems().add(aboutItem);

		// Create menu bar and add menus to it
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(editMenu);
		menuBar.getMenus().add(projectMenu);
		menuBar.getMenus().add(windowMenu);
		menuBar.getMenus().add(helpMenu);

		return menuBar;
	}

	public void setupComponentTab(GridPane grid) {
		Image andImage = new Image("Images/AND/AND_image.png", 60, 40, false, false);
		Button andButton = new Button();
		andButton.setOnAction(e -> {
			selectedItem = "AND";
			scene.setCursor(Cursor.CROSSHAIR);
		});
		andButton.setGraphic(new ImageView(andImage));
		grid.add(andButton, 0, 0, 1, 1);

		Image orImage = new Image("Images/OR/OR_image.png", 60, 40, false, false);
		Button orButton = new Button();
		orButton.setOnAction(e -> {
			selectedItem = "OR";
			scene.setCursor(Cursor.CROSSHAIR);
		});
		orButton.setGraphic(new ImageView(orImage));
		grid.add(orButton, 1, 0, 1, 1);
		
		Image nandImage = new Image("Images/NAND/NAND_image.png", 60, 40, false, false);
		Button nandButton = new Button();
		nandButton.setOnAction(e -> {
			selectedItem = "NAND";
			scene.setCursor(Cursor.CROSSHAIR);
		});
		nandButton.setGraphic(new ImageView(nandImage));
		grid.add(nandButton, 0, 1, 1, 1);

		Image norImage = new Image("Images/NOR/NOR_image.png", 60, 40, false, false);
		Button norButton = new Button();
		norButton.setOnAction(e -> {
			selectedItem = "NOR";
			scene.setCursor(Cursor.CROSSHAIR);
		});
		norButton.setGraphic(new ImageView(norImage));
		grid.add(norButton, 1, 1, 1, 1);
		
		Image notImage = new Image("Images/NOT/NOT_image.png", 60, 40, false, false);
		Button notButton = new Button();
		notButton.setOnAction(e -> {
			selectedItem = "NOT";
			scene.setCursor(Cursor.CROSSHAIR);
		});
		notButton.setGraphic(new ImageView(notImage));
		grid.add(notButton, 0, 2, 1, 1);
		
		ScrollPane componentScrollPane = new ScrollPane(grid);
		Tab componentTab = new Tab("Components", componentScrollPane);
		controlTabPane.getTabs().add(componentTab);
	}
	
	public void setupToolTab(GridPane grid) {
		//TODO: Finish this method
		Image selectImage = new Image("Images/Select.png", 40, 40, false, false);
		Button selectButton = new Button();
		selectButton.setOnAction(e -> {
			selectedItem = "~SELECT";
			scene.setCursor(Cursor.DEFAULT);
		});
		selectButton.setGraphic(new ImageView(selectImage));
		grid.add(selectButton, 0, 0, 1, 1);
		
		Image textImage = new Image("Images/Text.png", 40, 40, false, false);
		Button textButton = new Button();
		textButton.setOnAction(e -> {
			selectedItem = "~TEXT";
			scene.setCursor(Cursor.TEXT);
		});
		textButton.setGraphic(new ImageView(textImage));
		grid.add(textButton, 1, 0, 1, 1);
		
		Image wireImage = new Image("Images/Wire.png", 40, 40, false, false);
		Button wireButton = new Button();
		wireButton.setOnAction(e -> {
			selectedItem = "~WIRE";
			scene.setCursor(Cursor.DEFAULT);
		});
		wireButton.setGraphic(new ImageView(wireImage));
		grid.add(wireButton, 0, 1, 1, 1);
		
		ScrollPane toolScrollPane = new ScrollPane(grid);
		Tab toolTab = new Tab("Tools", toolScrollPane);
		controlTabPane.getTabs().add(toolTab);
	}

	/*
	 * This method creates a new schematic and adds it to the tab pane.
	 */
	public void createSchematic(String name, int width, int height) {
		Schematic schematic = new Schematic(this, tabPane, width, height);
		Tab newSchematic = new Tab(name, schematic);
		tabPane.getTabs().add(newSchematic);
		tabPane.getSelectionModel().select(newSchematic);
		
		schematics.add(schematic);

	}
	
	public void openNewSchematicWindow() {
		new NewSchematicWindow(this);
	}
}
