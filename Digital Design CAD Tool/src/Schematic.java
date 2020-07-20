/*
 * Schematic class. Runs the logic behind the displaying and functionality of the schematic feature. Displayed onto the screen using a Canvas.
 *  	
 *  	This is the Digital Design CAD Tool. This tool is used to design digital circuits.
 *      Copyright (C) 2020  Nolan Davenport
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import com.sun.javafx.geom.Vec2d;

import components.*;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Schematic extends Canvas implements Serializable{
	private static final long serialVersionUID = -1671532202546307968L;
	// General variables
	public int width, height;
	public int scale = 1;
	public GraphicsContext gc;
	public TabPane tabPane;
	public Main main;
	public String name;
	public ArrayList<Integer> IDs;

	// For dragging the viewport around
	private double mouseX;
	private double mouseY;
	private double translateX = 0;
	private double translateY = 0;

	// For dragging components around
	private double compMouseX;
	private double compMouseY;

	// For creating wires
	private double wireStartX;
	private double wireStartY;
	private double wireEndX;
	private double wireEndY;
	private boolean wireDirectionFound;
	private boolean wireStarted;
	int wireDirection = 0; // "0" for left/right, and "1" for up/down
	int wireDetectionThreshold = 4;
	private Wire wireToBeAddedTo = null;
	private Component valueDeterminingComponent = null;
	enum WireStartLocation {
		COMPONENT, WIRE;
	}

	// For storing and manipulating schematic states
	public SchematicState currState;
	public boolean justDragged = false;
	public Deque<SchematicState> pastStates;
	public int maxPastStatesSize = 100;
	public Deque<SchematicState> futureStates;
	public SchematicState lastState;

	// For selecting components and wires
	Component selectedComponent = null;
	Wire selectedWire = null;
	
	// For the functional simulation of this schematic.
	ArrayList<IOPort> inputPorts; // holds all input ports. 
	ArrayList<IOPort> outputPorts;

	/*
	 * Schematic constructor. Creates and initializes instances of the schematic class.
	 */
	public Schematic(Main main, TabPane tabPane, int width, int height, String name) {
		// initialization
		super(width, height);
		currState = new SchematicState();
		pastStates = new LinkedList<SchematicState>();
		futureStates = new LinkedList<SchematicState>();
		lastState = new SchematicState(currState);
		IDs = new ArrayList<Integer>();
		inputPorts = new ArrayList<IOPort>();
		outputPorts = new ArrayList<IOPort>();
		this.name = name;
		this.tabPane = tabPane;
		this.height = height;
		this.width = width;
		this.main = main;
		GraphicsContext gc = this.getGraphicsContext2D();
		this.gc = gc;
		gc.setImageSmoothing(false);
		wireDirectionFound = false;
		wireStarted = false;

		// makes the background of the schematic black. 
		gc.fillRect(0, 0, width, height);

		// This event handler handles left clicks
		this.setOnMouseClicked(event -> {
			double mouseEventX = event.getX();
			double mouseEventY = event.getY();
			
			if (event.getButton() == MouseButton.PRIMARY && main.selectedItem != "NONE"
					&& !main.selectedItem.contains("~")) {
				placeComponent(main.selectedItem, event);
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~SELECT") {
				// For selecting components or wires with the select tool.
				boolean selectedAnItem = false;
				deselectAllComponents();
				for (Component comp : currState.components) {
					if (!comp.selected && comp.insideBounds(mouseEventX, mouseEventY)) {
						selectComponent(comp);
						selectedAnItem = true;
						break;
					}
				}
				if(!selectedAnItem) {
					for (Wire wire : currState.wires) {
						if (!wire.selected && wire.clickedOnALine(mouseEventX, mouseEventY)) {
							selectWire(wire);
							selectedAnItem = true;
							break;
						}
					}
				}
				refresh(false);
			}
		});
		
		// For starting features that require dragging.
		this.setOnMousePressed(event -> {
			double mouseEventX = event.getX();
			double mouseEventY = event.getY();
			
			if (event.getButton() == MouseButton.MIDDLE) {
				// For moving the viewport around
				mouseX = event.getScreenX();
				mouseY = event.getScreenY();
				main.scene.setCursor(Cursor.MOVE);
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~SELECT") {
				// For moving objects around.
				compMouseX = mouseEventX;
				compMouseY = mouseEventY;
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~WIRE") {
				// For creating new wires. 
				deselectAllComponents();
				
				for(Component comp : currState.components) {
					boolean foundComponent = false;
					for(Vec2d inputLocation : comp.inputLocations) {
						if(Math.abs(mouseEventX - inputLocation.x) <= wireDetectionThreshold && Math.abs(mouseEventY - inputLocation.y) <= wireDetectionThreshold) {
							startLine(event, WireStartLocation.COMPONENT);
							foundComponent = true;
							break;
						}
					}
					if(foundComponent) break;
					if(Math.abs(mouseEventX - comp.outputLocation.x) <= wireDetectionThreshold && Math.abs(mouseEventY - comp.outputLocation.y) <= wireDetectionThreshold) {
						startLine(event, WireStartLocation.COMPONENT);
						valueDeterminingComponent = comp;
						foundComponent = true;
						break;
					}
					if(foundComponent) break;
				}

				// For starting a wire from another wire. 
				if (!wireStarted) {
					for (Wire wire : currState.wires) {
						for (Line line : wire.lines) {
							if (Math.abs(mouseEventX - line.x1) <= wireDetectionThreshold
									&& Math.abs(mouseEventY - line.y1) <= wireDetectionThreshold) {
								startLine(event, WireStartLocation.WIRE);
								wireToBeAddedTo = wire;
								valueDeterminingComponent = wireToBeAddedTo.valueDeterminingComponent;
								break;
							} else if (Math.abs(mouseEventX - line.x2) <= wireDetectionThreshold
									&& Math.abs(mouseEventY - line.y2) <= wireDetectionThreshold) {
								startLine(event, WireStartLocation.WIRE);
								wireToBeAddedTo = wire;
								valueDeterminingComponent = wireToBeAddedTo.valueDeterminingComponent;
								break;
							} else {
								wireToBeAddedTo = null;
							}
						}
					}
				}
			}
		});
		
		// Handles features that require dragging. 
		this.setOnMouseDragged(event -> {
			double mouseEventX = event.getX();
			double mouseEventY = event.getY();
			
			if (event.getButton() == MouseButton.MIDDLE) {
				// For handling the viewport dragging. 
				middleMouseDragged(event);
			} else if (event.getButton() == MouseButton.PRIMARY && selectedComponent != null
					&& main.selectedItem == "~SELECT") {
				// For moving objects.
				double deltaX = mouseEventX - compMouseX;
				double deltaY = mouseEventY - compMouseY;
				selectedComponent.setX(selectedComponent.getX() + deltaX);
				selectedComponent.setY(selectedComponent.getY() + deltaY);

				compMouseX = mouseEventX;
				compMouseY = mouseEventY;

				justDragged = true;
				refresh(false);
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~WIRE") {
				// For creating wires. 
				if (wireStarted) {
					wireEndX = mouseEventX;
					wireEndY = mouseEventY;
					if (!wireDirectionFound) {
						wireDirectionFound = true;
						double diffX = Math.abs(wireEndX - wireStartX);
						double diffY = Math.abs(wireEndY - wireStartY);

						wireDirection = (diffX > diffY) ? 0 : 1;
						renderCurrentWire();
					} else {
						renderCurrentWire();
					}
				}

			}
		});
		
		// For ending actions that required dragging.
		this.setOnMouseReleased(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				// For changing mouse symbol after dragging.
				if (!main.selectedItem.contains("~") && main.selectedItem != "NONE") {
					main.scene.setCursor(Cursor.CROSSHAIR);
				} else {
					main.scene.setCursor(Cursor.DEFAULT);
				}
			} else if (event.getButton() == MouseButton.PRIMARY && selectedComponent != null
					&& main.selectedItem == "~SELECT") {
				// For ending a component drag. 
				if (justDragged) {
					refresh(true);
					justDragged = false;
					selectedComponent.square();
				}
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~WIRE" && wireDirectionFound) {
				// For ending the wire creation process.
				boolean endingPointClosed = false;
				boolean avoidCreatingWire = false;
				float temp1 = Math.round((float) wireEndX / 10) * 10 - 1;
				float temp2 = Math.round((float) wireEndY / 10) * 10;
				for (Wire wire : currState.wires) {
					for (Line line : wire.lines) {
						// for going into the middle of a line vertically
						if (line.x1 == line.x2 && line.x1 == temp1
								&& ((line.y1 < temp2 && line.y2 > temp2) || (line.y1 > temp2 && line.y2 < temp2))) {
							endingPointClosed = true;
							if(wireToBeAddedTo == null) {
								wireToBeAddedTo = wire;
							}else if(wireToBeAddedTo.valueDeterminingComponent != null && wire.valueDeterminingComponent != null && (wireToBeAddedTo.valueDeterminingComponent != wire.valueDeterminingComponent)) {
								wireError(0);
								avoidCreatingWire = true;
							}else if(wireToBeAddedTo.valueDeterminingComponent != null) {
								combineWires(wireToBeAddedTo, wire);
							}else if(wire.valueDeterminingComponent != null) {
								combineWires(wire, wireToBeAddedTo);
								wireToBeAddedTo = wire;
							}else {
								combineWires(wireToBeAddedTo, wire);
							}
							
							break;
						// for going into the middle of a line horizontally
						}else if(line.y1 == line.y2 && line.y1 == temp2 && ((line.x1 < temp1 && line.x2 > temp1) || (line.x1 > temp1 && line.x2 < temp1))) {
							endingPointClosed = true;
							if(wireToBeAddedTo == null) {
								wireToBeAddedTo = wire;
							}else if(wireToBeAddedTo.valueDeterminingComponent != null && wire.valueDeterminingComponent != null  && (wireToBeAddedTo.valueDeterminingComponent != wire.valueDeterminingComponent)) {
								wireError(0);
								avoidCreatingWire = true;
							}else if(wireToBeAddedTo.valueDeterminingComponent != null) {
								combineWires(wireToBeAddedTo, wire);
							}else if(wire.valueDeterminingComponent != null) {
								combineWires(wire, wireToBeAddedTo);
								wireToBeAddedTo = wire;
							}else {
								combineWires(wireToBeAddedTo, wire);
							}
							
							break;
						}else if(((temp1 == line.x1 && temp2 == line.y1) || (temp1 == line.x2 && temp2 == line.y2))) {
							wireToBeAddedTo = wire;
						}
					}
					if(endingPointClosed) {
						break;
					}
				}
				
				createWire(avoidCreatingWire);
				
				valueDeterminingComponent = null;
				wireToBeAddedTo = null;
				wireDirectionFound = false;
				wireStarted = false;
			}
		});
		
		// Handles key presses
		main.scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.R && selectedComponent != null) {
				// For rotating objects. 
				selectedComponent.rotate();
				refresh(true);
			} else if (event.getCode() == KeyCode.DELETE) {
				// For deleting objects. 
				if(selectedComponent != null) {
					currState.components.remove(selectedComponent);
					deselectAllComponents();
					refresh(true);
				}else if(selectedWire != null) {
					currState.wires.remove(selectedWire);
					deselectAllComponents();
					refresh(true);
				}
				
			} else if (event.getCode() == KeyCode.ENTER) {
				deselectAllComponents();
				refresh(false);
			}
		});
		
		
		createGridLines();

		// Give scroll functionality to the scroll wheel.
		this.setOnScroll(event -> {
			if (event.getDeltaY() < 0) {
				zoom(1);
			} else {
				zoom(0);
			}
		});

		refresh(true);

	}
	
	/*
	 * Called when the middle mouse is dragged. For dragging the viewport.
	 */
	public void middleMouseDragged(MouseEvent event) {
		double deltaX = event.getScreenX() - mouseX;
		double deltaY = event.getScreenY() - mouseY;
		translateX = translateX + deltaX;
		translateY = translateY + deltaY;
		this.setTranslateX(translateX - (translateX % 10));
		this.setTranslateY(translateY - (translateY % 10));
		mouseX = event.getScreenX();
		mouseY = event.getScreenY();
	}
	
	/*
	 * Creates grid lines used on the schematic to assist users in placing objects and wires.
	 */
	public void createGridLines() {
		gc.setStroke(Color.rgb(50, 50, 50, 0.5));
		gc.setLineWidth(1);
		for (int i = 10; i < height; i += 10) {
			gc.strokeLine(0, i, width, i);
		}
		for (int i = 10; i < width; i += 10) {
			gc.strokeLine(i, 0, i, height);
		}
	}
	
	/*
	 * Zooms in the schematic. 
	 * @param direction The direction of zoom. 0 is for zooming in and 1 is for zooming out. 
	 */
	public void zoom(int direction) {
		double zoomFactor = 1.1;
		if (direction == 1) {
			zoomFactor = 2.0 - zoomFactor;
		}
		if ((zoomFactor > 1 && this.getScaleY() < 2.5) || (zoomFactor < 1 && this.getScaleY() > 0.5)) {
			this.setScaleX(this.getScaleX() * zoomFactor);
			this.setScaleY(this.getScaleY() * zoomFactor);
		}

	}
	
	/*
	 * Clears the schematic.
	 * @param saveSchematicState Boolean that determines if the schematic state should be saved for use in the undo feature.
	 */
	public void clear(boolean saveSchematicState) {
		currState.wires = new ArrayList<Wire>();
		currState.components = new ArrayList<Component>();
		deselectAllComponents();
		refresh(saveSchematicState);
	}
	
	/*
	 * Refreshes the screen by clearing everything and redrawing the components based on the current state.
	 * @param saveSchematicState Boolean that determines if the schematic state should be saved for use in the undo feature.
	 */
	public void refresh(boolean saveSchematicState) {
		if (saveSchematicState) {
			pastStates.addFirst(new SchematicState(lastState));
			if (pastStates.size() > maxPastStatesSize) {
				pastStates.removeLast();
			}
			lastState = new SchematicState(currState);
		}

		gc.clearRect(0, 0, width, height);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
		createGridLines();

		for (Component comp : currState.components) {
			comp.drawComponent(gc);

			if (comp.selected == true) {
				selectComponent(comp);
			}
		}

		for (Wire wire : currState.wires) {
			wire.drawWire(gc);
		}

	}
	
	/*
	 * Places a component into the schematic.
	 * @param selectedItem The string for the selected item. 
	 * @param event The mouse event that initiated the method call.
	 */
	public void placeComponent(String selectedItem, MouseEvent event) {
		double mouseEventX = event.getX();
		double mouseEventY = event.getY();
		
		// TODO: This just sets the ID to 0, but I need to make a custom ID generator that makes unique IDs.
		switch (selectedItem) {
		case "AND":
			currState.components.add(new AndGate(0, 0, (mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, 2, generateUniqueID()));
			break;
		case "OR":
			currState.components.add(
					new OrGate(0, 0, (mouseEventX - (mouseEventX % 10)), (mouseEventY - (mouseEventY % 10)), 0, 2, generateUniqueID()));
			break;
		case "NAND":
			currState.components.add(new NandGate(0, 0, (mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, 2, generateUniqueID()));
			break;
		case "NOR":
			currState.components.add(new NorGate(0, 0, (mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, 2, generateUniqueID()));
			break;
		case "XOR":
			currState.components.add(new XorGate(0, 0, (mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, 2, generateUniqueID()));
			break;
		case "NOT":
			currState.components.add(new NotGate(0, 0, (mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, 1, generateUniqueID()));
			break;
		case "IO_IN":
			//TODO: CHANGE THIS TO ALLOW USER TO CHANGE NAME
			currState.components.add(new IOPort((mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, generateUniqueID(), "in", "temp"));
			break;
		case "IO_OUT":
			//TODO: CHANGE THIS TO ALLOW USER TO CHANGE NAME
			currState.components.add(new IOPort((mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, generateUniqueID(), "out", "temp"));
			break;
		case "IO_BI":
			//TODO: CHANGE THIS TO ALLOW USER TO CHANGE NAME
			currState.components.add(new IOPort((mouseEventX - (mouseEventX % 10)),
					(mouseEventY - (mouseEventY % 10)), 0, generateUniqueID(), "bi", "temp"));
			break;
		default:
			System.err.println("This is not a valid component ID: " + selectedItem);
		}

		refresh(true);
	}

	/*
	 * Selects the component.
	 * @param comp The component that is to be selected.
	 */
	public void selectComponent(Component comp) {
		if (comp.rotation == 0 || comp.rotation == 2) {
			selectedComponent = comp;
		} else {
			selectedComponent = comp;
		}
		comp.selected = true;
	}
	
	/*
	 * Selects the wire.
	 * @param wire The wire that is to be selected.
	 */
	public void selectWire(Wire wire) {
		wire.selected = true;
		selectedWire = wire;
	}

	/*
	 * Loops through all components and wire and ensures that they are all deselected. Sets the selectedComponent and selectedWire variables to null.
	 */
	public void deselectAllComponents() {
		for(Component comp : currState.components) {
			comp.selected = false;
			comp.square();
		}
		
		for(Wire wire : currState.wires) {
			wire.selected = false;
		}
		
		selectedComponent = null;
		selectedWire = null;
	}
	
	/*
	 * Creates a wire.
	 * @param endingPointClosed Boolean for making the ending point closed.
	 * @param endingPointOpen Boolean for making the ending point open.
	 */
	public void createWire(boolean avoidCreatingWire) {
		Wire tempWire = new Wire();

		double tempStartX = Math.round((float) wireStartX / 10) * 10;
		double tempStartY = Math.round((float) wireStartY / 10) * 10;
		double tempEndX = Math.round((float) wireEndX / 10) * 10;
		double tempEndY = Math.round((float) wireEndY / 10) * 10;

		Line line1;
		Line line2;
		if (wireDirection == 0) {
			line1 = new Line(tempStartX, tempStartY, tempEndX, tempStartY);
			line2 = new Line(tempEndX, tempStartY, tempEndX, tempEndY);

			tempWire.addLine(line1);
			tempWire.addLine(line2);
		} else {
			line1 = new Line(tempStartX, tempStartY, tempStartX, tempEndY);
			line2 = new Line(tempStartX, tempEndY, tempEndX, tempEndY);

			tempWire.addLine(line1);
			tempWire.addLine(line2);

		}

		if (wireToBeAddedTo != null) {
			if(wireToBeAddedTo.valueDeterminingComponent != null && valueDeterminingComponent != null && (wireToBeAddedTo.valueDeterminingComponent != valueDeterminingComponent)) {
				wireError(0);
			}else if(wireToBeAddedTo.valueDeterminingComponent == null && !avoidCreatingWire) {
				wireToBeAddedTo.addLine(line1);
				wireToBeAddedTo.addLine(line2);
				wireToBeAddedTo.valueDeterminingComponent = valueDeterminingComponent;
				wireToBeAddedTo = null;
			}else if(!avoidCreatingWire){
				wireToBeAddedTo.addLine(line1);
				wireToBeAddedTo.addLine(line2);
				wireToBeAddedTo = null;
			}
		} else {
			if(valueDeterminingComponent != null) {
				tempWire.valueDeterminingComponent = valueDeterminingComponent;
			}
			currState.wires.add(tempWire);
		}
		tempWire.cleanUp();
		valueDeterminingComponent = null;
		wireToBeAddedTo = null;

		debugWires(); // TODO: REMOVE THIS SHIT
		
		refresh(true);
	}
	
	/*
	 * Combines wires into one.
	 * @param one The first wire. This instance doesn't get removed. The other parameter's wires are added to this wire.
	 * @param two The second wire. This instance's wires gets copied to the first wire and this instance is removed.
	 */
	public void combineWires(Wire one, Wire two) {
		if(one != two) {
			for(Line line : two.lines) {
			one.addLine(line);
			}
			currState.wires.remove(two);
		}
	}
	
	/*
	 * Displays wire errors.
	 * @param err The code that represents the error.
	 */
	public void wireError(int err) {
		switch(err) {
			case 0:
				System.err.println("There are more than one value determining components on that wire node!");
				break;
		}
	}
	
	/*
	 * Renders the current wire before actually creating a wire object. Used for displaying the possible wire location and properties to the user.
	 */
	public void renderCurrentWire() {
		refresh(false);

		gc.setStroke(Color.RED);
		gc.setLineWidth(2);

		double tempStartX = Math.round((float) wireStartX / 10) * 10 - 1;
		double tempStartY = (Math.round((float) wireStartY / 10) * 10) - 1;
		double tempEndX = Math.round((float) wireEndX / 10) * 10 - 1;
		double tempEndY = (Math.round((float) wireEndY / 10) * 10) - 1;

		if (wireDirection == 0) {
			// left/right direction for the beginning line
			gc.strokeLine(tempStartX, tempStartY, tempEndX, tempStartY);
			gc.strokeLine(tempEndX, tempStartY, tempEndX, tempEndY);
			if(tempStartX == tempEndX && tempStartY != tempEndY) {
				wireDirection = 1;
			}
		} else {
			// up/down direction  for the beginning line
			gc.strokeLine(tempStartX, tempStartY, tempStartX, tempEndY);
			gc.strokeLine(tempStartX, tempEndY, tempEndX, tempEndY);
			if(tempStartY == tempEndY && tempStartX != tempEndX) {
				wireDirection = 0;
			}
		}
	}
	
	/*
	 * Starts a line.
	 * @param e the event that initiated the start of the line.
	 */
	public void startLine(MouseEvent e, WireStartLocation wStartLoc ) {
		if(wStartLoc == WireStartLocation.COMPONENT) {
			for(Wire wire : currState.wires) {
				if(wire.valueDeterminingComponent == valueDeterminingComponent) {
					wireToBeAddedTo = wire;
				}
			}
		}
		wireStartX = e.getX();
		wireStartY = e.getY();
		wireStarted = true;
	}

	/*
	 * Undoes a previous action. Goes back to a previous schematic state.
	 */
	public void undo() {
		// TODO: Fix this to allow undoing wire changes... Because that doesn't really work all too well.
		if (pastStates.size() > 0) {
			clear(false);
			currState = new SchematicState(pastStates.removeFirst());
		} else {
			pastStates.clear();
			lastState = new SchematicState(currState);
		}
		deselectAllComponents();
		refresh(false);
	}
	
	/*
	 * Redoes an action that was undone. Goes forward to a previously undone state.
	 */
	public void redo() {
		// TODO: Implement this in the future, but not now. It's not that important
		// right now to create a functional program.
	}
	
	/*
	 * Generates a unique ID that is between 100000 and 999999.
	 * @return the unique ID.
	 */
	public int generateUniqueID() {
		boolean foundUniqueID = false;
		int ID = 0;
		Random rand = new Random();
		while(!foundUniqueID) {
			ID = rand.nextInt(899999) + 100000; // generates a random number between 100000 and 999999
			if(!IDs.contains(ID)) {
				foundUniqueID = true;
				IDs.add(ID);
			}
		}
		return ID;
	}
	
	/*
	 * Synthesizes the schematic so that logic simulations can be done. 
	 * @return Whether or not the schematic successfully synthesized. If false, then there is an issue with the schematic. 
	 */
	public boolean synthesizeSchematic() {
		inputPorts.clear();
		outputPorts.clear();
		// Connects wires to component inputs
		for(Component comp : currState.components) {
			// for inputs
			if(comp.id.equals("IO_in")) {
				inputPorts.add((IOPort)comp);
			}else{
				if(comp.id.equals("IO_out")) outputPorts.add((IOPort)comp);
				for(int i = 0; i < comp.inputLocations.length; i++) {
					for(Wire wire : currState.wires) {
						for(Line line : wire.lines) {
							if((line.x1 == comp.inputLocations[i].x && line.y1 == comp.inputLocations[i].y) || (line.x2 == comp.inputLocations[i].x && line.y2 == comp.inputLocations[i].y)) {
								comp.inputs[i] = wire;
							}
						}
					}
				}
			}
		}
		debugComponents();
		new SimulationWindow(main);
		return true;
	}
	
	/*
	 * Sets the output IOPorts to the proper value depending on the input IOPorts. 
	 */
	public void simulateLogic() {
		for(int i = 0; i < currState.components.size() * 30; i++) {
			setOutputs();
		}
	}
	
	/*
	 * Loops through and sets the outputs of the components.
	 * @return Whether or not an output was changed.
	 */
	public boolean setOutputs() {
		boolean somethingChanged = false;
		for(Component comp : currState.components) {
			boolean oldOut = comp.output;
			comp.calculateOutput();
			if(oldOut != comp.output) somethingChanged = true;
		}
		return somethingChanged;
	}
	
	/*
	 * Displays useful information for debugging the wire system.
	 */
	public void debugWires() {
		System.out.println("NUM WIRES: " + currState.wires.size());
		for (int i = 0; i < currState.wires.size(); i++) {
			System.out.println("Wire " + i + "  |  " + currState.wires.get(i).lines.size() + " lines  |  " + "valueDeterminingComponent: " + currState.wires.get(i).valueDeterminingComponent);
		}
		System.out.println("_____________________________________________________________________________________________________________\n");
	}
	
	/*
	 * Displays useful information for debugging components.
	 */
	public void debugComponents() {
		for(Component comp : currState.components) {
			System.out.println(comp+":");
			for(int i = 0; i < comp.inputs.length; i++) {
				System.out.println("input "+i+": "+comp.inputs[i]);
			}
		}
		System.out.println("___________________________________________________________________________________________________________________\n");
	}
}
