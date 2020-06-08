import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewSchematicWindow extends Stage {
	Main main;
	public NewSchematicWindow(Main main) {
		this.main = main;
		this.setTitle("Create a New Schematic");
		Label nameLabel = new Label("Name Your Schematic");
		VBox container = new VBox(nameLabel);

		TextField nameField = new TextField();

		Label sizeLabel = new Label("Size of the Schematic");
		Label sizeLabelWidth = new Label("Width");
		TextField sizeFieldWidth = new TextField("2000");
		Label sizeLabelHeight = new Label("Height");
		TextField sizeFieldHeight = new TextField("2000");
		
		HBox buttons = new HBox();
		Button createButton = new Button("Create");
		createButton.setOnAction(e -> {
			submit(nameField.getText(), Integer.parseInt(sizeFieldWidth.getText()), Integer.parseInt(sizeFieldHeight.getText()));
		});
		Button cancelButton = new Button("Cancel");
		buttons.getChildren().add(createButton);
		buttons.getChildren().add(cancelButton);
		
		container.getChildren().add(nameField);
		container.getChildren().add(sizeLabel);
		container.getChildren().add(sizeLabelWidth);
		container.getChildren().add(sizeFieldWidth);
		container.getChildren().add(sizeLabelHeight);
		container.getChildren().add(sizeFieldHeight);
		container.getChildren().add(buttons);
		
		Scene scene = new Scene(container, 500, 500);
		this.setScene(scene);
		this.show();
		
		scene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				submit(nameField.getText(), Integer.parseInt(sizeFieldWidth.getText()), Integer.parseInt(sizeFieldHeight.getText()));
			}
		});
	}
	
	public void submit(String name, int width, int height) {
		main.createSchematic(name, width, height);
		this.close();
	}
}
