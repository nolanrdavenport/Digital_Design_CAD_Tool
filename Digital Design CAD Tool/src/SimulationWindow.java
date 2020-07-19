import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimulationWindow extends Stage{
	Main main;
	
	public SimulationWindow(Main m) {
		this.main = m;
		ArrayList<CheckBox> boxes;
		if(main.currSchematic.inputPorts.size() > 1) {
			boxes = new ArrayList<CheckBox>();
			
			for(int i = 0; i < main.currSchematic.inputPorts.size(); i++) {
				boxes.add(new CheckBox("input "+i));
			}
			
			VBox vbox = new VBox(boxes.get(0));
			
			for(int i = 1; i < boxes.size(); i++) {
				vbox.getChildren().add(boxes.get(i));
			}
			
			Scene scene = new Scene(vbox, 500, 500);
			this.setScene(scene);
			this.show();
			
		}else {
			System.err.println("There are no input ports!");
		}
	}
}
