import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import components.*;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Schematic extends Canvas {
	public int width, height;
	public int scale = 1;
	public GraphicsContext gc;
	public TabPane tabPane;
	public Main main;

	// For dragging the viewport around
	private double mouseX;
	private double mouseY;

	// For dragging components around
	private double compMouseX;
	private double compMouseY;

	// For making wires
	private double wireStartX;
	private double wireStartY;
	private double wireEndX;
	private double wireEndY;
	private boolean wireDirectionFound;
	int wireDirection = 0; // "0" for left/right, and "1" for up/down

	// For storing and manipulating schematic states
	public ArrayList<Component> components;
	public ArrayList<Wire> wires;
	public Deque<SchematicState> pastStates;
	public Deque<SchematicState> futureStates;
	public boolean justDragged = false;

	Component selectedComponent = null;

	@SuppressWarnings("unchecked")
	public Schematic(Main main, TabPane tabPane, int width, int height) {
		super(width, height);
		pastStates = new LinkedList<SchematicState>();
		futureStates = new LinkedList<SchematicState>();
		components = new ArrayList<Component>();
		wires = new ArrayList<Wire>();
		pastStates.addFirst(new SchematicState((ArrayList<Component>) components.clone(), (ArrayList<Wire>) wires.clone()));
		this.tabPane = tabPane;
		this.height = height;
		this.width = width;
		this.main = main;
		GraphicsContext gc = this.getGraphicsContext2D();
		this.gc = gc;
		gc.setImageSmoothing(false);
		wireDirectionFound = false;

		gc.fillRect(0, 0, width, height);

		// This event handler handles left clicks
		this.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY && main.selectedItem != "NONE"
					&& !main.selectedItem.contains("~")) {
				placeItem(main.selectedItem, event);
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~SELECT") {
				boolean selectedAnItem = false;
				for (Component comp : components) {
					comp.selected = false;
				}
				for (Component comp : components) {
					if (!comp.selected && comp.insideBounds(event.getX(), event.getY())) {
						selectComponent(comp);
						selectedAnItem = true;
						break;
					}
				}
				// If no item is selected, unselect the previously selected item
				if (!selectedAnItem) {
					for (Component comp : components) {
						comp.square();
						comp.selected = false;
					}
					selectedComponent = null;
				}
				refresh(false);
			}
		});

		this.setOnMousePressed(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				mouseX = event.getScreenX();
				mouseY = event.getScreenY();
				main.scene.setCursor(Cursor.MOVE);
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~SELECT") {
				compMouseX = event.getX();
				compMouseY = event.getY();
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~WIRE") {
				wireStartX = event.getX();
				wireStartY = event.getY();
			}
		});

		this.setOnMouseDragged(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				middleMouseDragged(event);
			} else if (event.getButton() == MouseButton.PRIMARY && selectedComponent != null
					&& main.selectedItem == "~SELECT") {
				double deltaX = event.getX() - compMouseX;
				double deltaY = event.getY() - compMouseY;
				selectedComponent.setX(selectedComponent.getX() + deltaX);
				selectedComponent.setY(selectedComponent.getY() + deltaY);

				compMouseX = event.getX();
				compMouseY = event.getY();
				
				justDragged = true;
				refresh(false);
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~WIRE") {
				wireEndX = event.getX();
				wireEndY = event.getY();
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
		});

		this.setOnMouseReleased(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				if (!main.selectedItem.contains("~") && main.selectedItem != "NONE") {
					main.scene.setCursor(Cursor.CROSSHAIR);
				} else {
					main.scene.setCursor(Cursor.DEFAULT);
				}
			} else if (event.getButton() == MouseButton.PRIMARY && selectedComponent != null
					&& main.selectedItem == "~SELECT") {
				if(justDragged) {
					refresh(true);
					justDragged = false;
				}
			} else if (event.getButton() == MouseButton.PRIMARY && main.selectedItem == "~WIRE" && wireDirectionFound) {
				createWire();
				wireDirectionFound = false;
			}
		});

		main.scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.R && selectedComponent != null) {
				selectedComponent.rotate();
				refresh(true);
			}else if(event.getCode() == KeyCode.DELETE && selectedComponent != null) {
				components.remove(selectedComponent);
				refresh(true);
			}
		});
		createGridLines();
		
		
		// Give scroll functionality to scrollwheel - because why not?
		this.setOnScroll(event -> {
			if(event.getDeltaY() < 0) {
				zoom(1);
			}else {
				zoom(0);
			}
		});

	}

	public void middleMouseDragged(MouseEvent event) {
		double deltaX = event.getScreenX() - mouseX;
		double deltaY = event.getScreenY() - mouseY;
		this.setTranslateX(this.getTranslateX() + deltaX);
		this.setTranslateY(this.getTranslateY() + deltaY);
		mouseX = event.getScreenX();
		mouseY = event.getScreenY();
	}

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
	
	public void clear() {
		wires = new ArrayList<Wire>();
		components = new ArrayList<Component>();
		refresh(true);
	}

	@SuppressWarnings("unchecked")
	public void refresh(boolean saveSchematicState) {
		if(saveSchematicState) {
			pastStates.addFirst(new SchematicState((ArrayList<Component>) components.clone(), (ArrayList<Wire>) wires.clone()));
			System.out.println("Past state added " + pastStates.size());
		}
		
		gc.clearRect(0, 0, width, height);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
		createGridLines();
		
		
		for (Component comp : components) {
			comp.drawComponent(gc);

			if (comp.selected == true) {
				selectComponent(comp);
			}
		}

		for (Wire wire : wires) {
			wire.drawWire(gc);
		}
		
		
	}

	public void placeItem(String selectedItem, MouseEvent event) {
		switch (selectedItem) {
		case "AND":
			components.add(new AndGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), 0,
					this, 2));
			break;
		case "OR":
			components.add(
					new OrGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), 0, this, 2));
			break;
		case "NAND":
			components.add(new NandGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), 0,
					this, 2));
			break;
		case "NOR":
			components.add(new NorGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), 0,
					this, 2));
			break;
		case "NOT":
			components.add(new NotGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), 0,
					this, 2));
			break;
		default:
			System.err.println("This is not a valid component ID: " + selectedItem);
		}

		refresh(true);
	}

	public void selectComponent(Component comp) {

		if (comp.rotation == 0 || comp.rotation == 2) {
			selectedComponent = comp;
		} else {
			selectedComponent = comp;
		}

	}

	public void createWire() {
		// TODO: improve this to make it easier to use
		Wire tempWire = new Wire();
		
		/*
		double tempStartX = wireStartX - (wireStartX % 10);
		double tempStartY = wireStartY - (wireStartY % 10);

		double tempEndX = wireEndX - (wireEndX % 10);
		double tempEndY = wireEndY - (wireEndY % 10);
		*/
		
		double tempStartX = Math.round((float)wireStartX / 10) * 10;
		double tempStartY = Math.round((float)wireStartY / 10) * 10;
		double tempEndX = Math.round((float)wireEndX / 10) * 10;
		double tempEndY = Math.round((float)wireEndY / 10) * 10;
		
		if(wireDirection == 0) {
			Line line1 = new Line(tempStartX, tempStartY, tempEndX, tempStartY);
			Line line2 = new Line(tempEndX, tempStartY, tempEndX, tempEndY);

			tempWire.addLine(line1);
			tempWire.addLine(line2);
		}else {
			Line line1 = new Line(tempStartX, tempStartY, tempStartX, tempEndY);
			Line line2 = new Line(tempStartX, tempEndY, tempEndX, tempEndY);

			tempWire.addLine(line1);
			tempWire.addLine(line2);
		}

		wires.add(tempWire);
		refresh(true);
	}

	public void renderCurrentWire() {
		refresh(false);

		gc.setStroke(Color.RED);
		gc.setLineWidth(2);
		
		double tempStartX = Math.round((float)wireStartX / 10) * 10;
		double tempStartY = (Math.round((float)wireStartY / 10) * 10) - 1;
		double tempEndX = Math.round((float)wireEndX / 10) * 10;
		double tempEndY = (Math.round((float)wireEndY / 10) * 10) - 1;

		if (wireDirection == 0) {
			gc.strokeLine(tempStartX, tempStartY, tempEndX, tempStartY);
			gc.strokeLine(tempEndX, tempStartY, tempEndX, tempEndY);
		}else {
			gc.strokeLine(tempStartX, tempStartY, tempStartX, tempEndY);
			gc.strokeLine(tempStartX, tempEndY, tempEndX, tempEndY);
		}
	}
	
	//TODO: make these methods
	@SuppressWarnings("unchecked")
	public void undo() {
		if(pastStates.size() > 1) {
			futureStates.addFirst(pastStates.pop());
			System.out.println("undo called and the new pastStates size is " + pastStates.size());
			System.out.println("the new futureStates size is " + futureStates.size());
			if(futureStates.size() > 20) {
				futureStates.removeLast();
			}
			components = new ArrayList<Component>();
			wires = new ArrayList<Wire>();
			for(Component comp : pastStates.peek().getComponents()) {
				components.add(comp);
			}
			for(Wire wire : pastStates.peek().getWires()) {
				wires.add(wire);
			}
		}else {
			System.err.println("There are no past states! " + pastStates.size());
		}
		refresh(false);
	}
	public void redo() {
		if(futureStates.size() > 0) {
			pastStates.addFirst(futureStates.pop());
			if(pastStates.size() > 20) {
				pastStates.removeLast();
			}
			components = pastStates.getFirst().getComponents();
			wires = pastStates.getFirst().getWires();
		}else {
			System.err.println("There are no future states!");
		}
		refresh(false);
	}
	
	public void cleanUpWires() {
		/*
		 * TODO: Figure this shit out
		 * specifications:
		 * 1. 2 "Wire" instances that could be simplified into 1, should be.
		 * 2. Any connected wires should be merged. 
		 * 3. idk how to do this but we shall figure it out soon enough lol.
		 */
	}
}
