import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimulationWindow extends Stage{
	Main main;
	
	public SimulationWindow(Main m) {
		this.main = m;
		this.setTitle("Simulate Schematic");
		ArrayList<CheckBox> boxes;
		if(main.currSchematic.inputPorts.size() >= 1) {
			boxes = new ArrayList<CheckBox>();
			
			for(int i = 0; i < main.currSchematic.inputPorts.size(); i++) {
				boxes.add(new CheckBox("input "+main.currSchematic.inputPorts.get(i).ID));
			}
			
			VBox vbox = new VBox(boxes.get(0));
			
			for(int i = 1; i < boxes.size(); i++) {
				vbox.getChildren().add(boxes.get(i));
			}
			TextArea outputArea = new TextArea();
			outputArea.appendText("Output: \n");
			vbox.getChildren().add(outputArea);
			Button startSimulationButton = new Button("Start Simulation");
			startSimulationButton.setOnAction(e -> {
				simulate(boxes, outputArea);
			});
			
			vbox.getChildren().add(startSimulationButton);
			Scene scene = new Scene(vbox, 1000, 800);
			this.setScene(scene);
			this.show();
			
		}else {
			System.err.println("There are no input ports!");
		}
		
	}
	
	public void simulate(ArrayList<CheckBox> boxes, TextArea outputArea) {
		outputArea.clear();
		outputArea.appendText("Output: \n");
		for(int i = 0; i < boxes.size(); i++) {
			main.currSchematic.inputPorts.get(i).output = boxes.get(i).isSelected();
		}
		main.currSchematic.simulateLogic();
		for(int i = 0; i < main.currSchematic.outputPorts.size(); i++) {
			outputArea.appendText("output "+main.currSchematic.outputPorts.get(i).ID+": "+main.currSchematic.outputPorts.get(i).output+"\n");
		}
	}
}
