import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SimulationWindow extends Stage{
	Main main;
	
	public SimulationWindow(Main m) {
		this.main = m;
		this.setTitle("Simulate Schematic");
		simulationSetupPage();
	}
	
	public void simulationSetupPage() {
		// number of intervals
		Label numInterval = new Label("Number of intervals:          ");
		TextField numIntervalField = new TextField();
		numIntervalField.appendText("1");
		HBox numIntervalHBox = new HBox(numInterval);
		numIntervalHBox.getChildren().add(numIntervalField);
		
		// interval length
		Label intervalLength = new Label("Length of intervals(ms):     ");
		TextField intervalLengthField = new TextField();
		intervalLengthField.appendText("50");
		HBox intervalLengthHBox = new HBox(intervalLength);
		intervalLengthHBox.getChildren().add(intervalLengthField);
		
		// submit button
		Button submit = new Button("Submit");
		submit.setOnAction(e -> {
			simulationRunPage(Integer.parseInt(numIntervalField.getText()), Integer.parseInt(intervalLengthField.getText()));
		});
		
		// main box
		Label mainLabel = new Label("Set up simulation");
		VBox mainBox = new VBox(mainLabel);
		mainBox.getChildren().add(numIntervalHBox);
		mainBox.getChildren().add(intervalLengthHBox);
		mainBox.getChildren().add(submit);
		
		Scene scene = new Scene(mainBox, 1000, 800);
		this.setScene(scene);
		this.show();
	}
	
	public void simulationRunPage(int numIntervals, int intervalLength) {
		int inputPortsSize = main.currSchematic.inputPorts.size();
		CheckBox[][] boxes = new CheckBox[numIntervals][inputPortsSize];
		if(main.currSchematic.inputPorts.size() >= 1) {
			// input check boxes
			Label[] inputLabels = new Label[main.currSchematic.inputPorts.size()];
			VBox inputLabelsVBox = new VBox();
			for(int i = 0; i < inputPortsSize; i++) {
				inputLabels[i] = new Label(main.currSchematic.inputPorts.get(i).name + ":  ");
				inputLabelsVBox.getChildren().add(inputLabels[i]);
			}
			HBox inputBoxesHorizontal = new HBox(inputLabelsVBox);
			for(int i = 0; i < numIntervals; i++) {
				VBox inputBoxesVertical = new VBox();
				for(int j = 0; j < inputPortsSize; j++) {
					boxes[i][j] = new CheckBox();
					inputBoxesVertical.getChildren().add(boxes[i][j]);
				}
				inputBoxesHorizontal.getChildren().add(inputBoxesVertical);
			}
			
			VBox main = new VBox(inputBoxesHorizontal);
			
			TextArea outputArea = new TextArea();
			outputArea.setEditable(false);
			outputArea.appendText("Output: \n");
			main.getChildren().add(outputArea);
			
			ScrollPane timingScrollPane = new ScrollPane();
			Canvas timingDiagram = createTimingDiagram(boxes, intervalLength);
			timingScrollPane.setContent(timingDiagram);
			timingScrollPane.setPrefHeight(this.getHeight());
			main.getChildren().add(timingScrollPane);
			
			Button startSimulationButton = new Button("Start Simulation");
			startSimulationButton.setOnAction(e -> {
				simulate(boxes, outputArea, intervalLength, timingDiagram);
			});
			
			main.getChildren().add(startSimulationButton);
			Scene scene = new Scene(main, 1000, 800);
			this.setScene(scene);
		}else {
			System.err.println("There are no input ports!");
		}
	}
	
	public void simulate(CheckBox[][] boxes, TextArea outputArea, int intervalLength, Canvas timingDiagram) {
		GraphicsContext gc = timingDiagram.getGraphicsContext2D();
		gc.clearRect(0, 0, timingDiagram.getWidth(), timingDiagram.getHeight());
		gc.setFill(Color.rgb(200,200,200));
		gc.fillRect(0, 0, timingDiagram.getWidth(), timingDiagram.getHeight());
		createGridLines(gc, timingDiagram, intervalLength);
		
		gc.setStroke(Color.BLACK);
		
		boolean[] lastOutputs = new boolean[main.currSchematic.outputPorts.size()];
		for(int i = 0, m = 100; i < boxes.length; i++, m+=50) {
			for(int j = 0; j < boxes[i].length; j++) {
				main.currSchematic.inputPorts.get(j).output = boxes[i][j].isSelected();
			}
			main.currSchematic.simulateLogic();
			for(int j = 0, k = 110; j < boxes[i].length; j++, k+=50) {
				if(boxes[i][j].isSelected() == true) {
					gc.strokeLine(m, k, m+50, k);
				}else {
					gc.strokeLine(m, k+30, m+50, k+30);
				}
				if((i > 0) && boxes[i-1][j].isSelected() != boxes[i][j].isSelected()) {
					gc.strokeLine(m, k, m, k+30);
				}
			}
			
			for(int j = 0, k = (main.currSchematic.inputPorts.size()*50)+110; j < main.currSchematic.outputPorts.size(); j++, k+=50) {
				if((i>0) && lastOutputs[j] != main.currSchematic.outputPorts.get(j).output) {
					gc.strokeLine(m, k, m, k+30);
				}
				if(main.currSchematic.outputPorts.get(j).output) {
					gc.strokeLine(m, k, m+50, k);
					lastOutputs[j] = true;
				}else {
					gc.strokeLine(m, k+30, m+50, k+30);
					lastOutputs[j] = false;
				}
			}
		}
		
		
	}
	
	public Canvas createTimingDiagram(CheckBox[][] boxes, int intervalLength) {
		Canvas timingDiagram = new Canvas((boxes.length * 50)+100, (boxes[0].length*50)+(main.currSchematic.outputPorts.size() * 50)+100);
		GraphicsContext gc = timingDiagram.getGraphicsContext2D();
		gc.setFill(Color.rgb(200,200,200));
		gc.fillRect(0, 0, timingDiagram.getWidth(), timingDiagram.getHeight());
		createGridLines(gc, timingDiagram, intervalLength);
		
		return timingDiagram;
	}
	
	public void createGridLines(GraphicsContext gc, Canvas timingDiagram, int intervalLength) {
		gc.setStroke(Color.rgb(20, 20, 20, 0.5));
		gc.setFill(Color.RED);
		gc.setLineWidth(1);
		for (int i = 100; i < timingDiagram.getHeight(); i += 50) {
			gc.strokeLine(0, i, timingDiagram.getWidth(), i);
		}
		for(int i = 0, j = 125; i < main.currSchematic.inputPorts.size(); i++, j += 50) {
			gc.fillText(main.currSchematic.inputPorts.get(i).name+" (input)", 10, j);
		}
		for(int i = 0, j = (main.currSchematic.inputPorts.size()*50)+125; i < main.currSchematic.outputPorts.size(); i++, j += 50) {
			gc.fillText(main.currSchematic.outputPorts.get(i).name+" (output)", 10, j);
		}
		for (int i = 100, j = 0; i < timingDiagram.getWidth(); i += 50, j++) {
			gc.fillText(Integer.toString(intervalLength * j) + "ms", i-10, 40);
			gc.strokeLine(i, 50, i, timingDiagram.getHeight());
		}
	}
}
